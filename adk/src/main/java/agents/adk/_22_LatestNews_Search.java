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
import com.google.adk.tools.GoogleSearchTool;
import com.google.adk.web.AdkWebServer;

import java.time.LocalDate;

/**
 * This news search agent uses a built-in tool: the Google Search tool.
 * It allows answers to be grounded in the latest news, as found on Google Search.
 */
public class _22_LatestNews_Search implements AgentProvider {
    @Override
    public BaseAgent getAgent() {
        return LlmAgent.builder()
            .name("22-news-search-agent")
            .description("A news search agent")
            .instruction("""
                You are a news search agent.
                Use the `google_search` tool
                when asked to search for recent events and information.
                Today is \
                """ + LocalDate.now())
            .model("gemini-2.5-flash")
            .tools(new GoogleSearchTool())
            .build();
    }

    public static void main(String[] args) {
        AdkWebServer.start(new _22_LatestNews_Search().getAgent());
    }
}
