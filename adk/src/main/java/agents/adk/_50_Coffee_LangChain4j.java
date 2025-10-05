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
import com.google.adk.models.langchain4j.LangChain4j;
import com.google.adk.web.AdkWebServer;
import dev.langchain4j.model.ollama.OllamaChatModel;

public class _50_Coffee_LangChain4j implements AgentProvider {
    @Override
    public BaseAgent getAgent() {
        OllamaChatModel ollamaChatModel = OllamaChatModel.builder()
            .modelName("gemma3n:e2b")
            .baseUrl("http://127.0.0.1:11434")
            .returnThinking(false)
            .build();

        return LlmAgent.builder()
            .name("barista-specialist")
            .description("A friendly barista")
            .instruction("""
                You are a humorous and friendly barista.
                Always answer with humor and enthusiasm.
                """)
            .model(new LangChain4j(ollamaChatModel))
            .build();
    }

    public static void main(String[] args) {
        AdkWebServer.start(new _50_Coffee_LangChain4j().getAgent());
    }
}
