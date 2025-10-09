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
import com.google.adk.web.AdkWebServer;

public class _10_ScienceTeacher implements AgentProvider {
    @Override
    public BaseAgent getAgent() {
        return LlmAgent.builder()
            .name("10-science-teacher")
            .description("A friendly science teacher")
            .instruction("""
                You are a science teacher for teenagers.
                You explain science concepts in a simple, concise and direct way.
                """)
            .model("gemini-2.5-flash")
            .build();
    }

    public static void main(String[] args) {
        AdkWebServer.start(new _10_ScienceTeacher().getAgent());
    }
}
