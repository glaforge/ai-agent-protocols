/*
 * Copyright 2025 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package agents.adk;

import agents.AgentProvider;
import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.AgentTool;
import com.google.adk.tools.GoogleSearchTool;
import com.google.adk.web.AdkWebServer;
import io.reactivex.rxjava3.core.Maybe;

import java.time.LocalDate;
/**
 * A more complex agent system for content creation and social media management.
 * It demonstrates how to configure and use subagents.
 * <br>
 * The system consists of multiple specialized subagents working together:
 * <ul>
 *   <li>Google Search Agent - Performs web searches to gather information</li>
 *   <li>Topic Search Agent - Explores specific topics using the search agent</li>
 *   <li>Social Media Agent - Crafts engaging social media posts</li>
 *   <li>Content Companion - Orchestrates the workflow between all sub-agents</li>
 * </ul>
 *
 * The main agent ("content-companion") features two subagents:
 * "topic-search-agent" and "social-media-agent".
 * And the "google-search-agent" is an "agent-as-tool" for the "topic-search-agent".
 * <br>
 * The two subagents use a callback to always transfer back the
 * control to the main agent when they have finished their task.
 */
public class _30_SearchAndTweet_SubAgents implements AgentProvider {
    @Override
    public BaseAgent getAgent() {
        LlmAgent searchAgent = LlmAgent.builder()
            .name("google-search-agent")
            .description("An agent that searches on Google Search")
            .instruction("""
                Your role is to search on Google Search.
                Use the Google Search Tool to search up-to-date and relevant information about the topic.
                Today is \
                """ + LocalDate.now())
            .model("gemini-2.5-flash")
            .tools(new GoogleSearchTool())
            .build();

        LlmAgent topicSearchAgent = LlmAgent.builder()
            .name("topic-search-agent")
            .description("An agent that searches and dives in particular topics")
            .instruction("""
                Your role is to help explore a particular topic.
                Use the `google-search-agent` tool to search up-to-date and relevant information about the topic.
                Be sure to display the result of the search to inform the user.
                Today is \
                """ + LocalDate.now())
            .model("gemini-2.5-flash")
            .tools(AgentTool.create(searchAgent))
            .afterAgentCallback(callbackContext -> {
                callbackContext.eventActions().setTransferToAgent("content-companion");
                return Maybe.empty();
            })
            .build();

        LlmAgent socialMediaAgent = LlmAgent.builder()
            .name("social-media-agent")
            .description("An agent that crafts social media posts about a topic")
            .instruction("""
                Given the content about a topic, your role is to craft an attractive social media post about it.
                Don't hesitate to use meaningful emojis when it helps convey the message.
                Today is \
                """ + LocalDate.now())
            .model("gemini-2.5-flash")
            .afterAgentCallback(callbackContext -> {
                callbackContext.eventActions().setTransferToAgent("content-companion");
                return Maybe.empty();
            })
            .build();

        return LlmAgent.builder()
            .name("30-content-companion")
            .description("A content companion that searches topics and crafts compelling social media stories")
            .instruction("""
                Your role is to help bloggers and influencers come up with interesting topic ideas,
                to search for information about the topic to write about,
                and potentially to craft a compelling social media post.
                
                Don't search yourself:
                Use the `topic-search-agent` to find information about a topic.
                
                Don't write social media posts yourself:
                Use the `social-media-agent` to craft a social media post about the topic.
                """)
            .model("gemini-2.5-flash")
            .subAgents(socialMediaAgent, topicSearchAgent)
            .build();
    }

    public static void main(String[] args) {
        AdkWebServer.start(new _30_SearchAndTweet_SubAgents().getAgent());
    }
}
