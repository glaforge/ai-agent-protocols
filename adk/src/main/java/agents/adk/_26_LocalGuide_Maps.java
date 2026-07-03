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
import com.google.adk.tools.GoogleMapsTool;
import com.google.adk.web.AdkWebServer;

/**
 * This touristic local guide uses the Google Maps tool
 * to ground answers with results from Google Maps.
 */
public class _26_LocalGuide_Maps implements AgentProvider {

    @Override
    public BaseAgent getAgent() {
        return LlmAgent.builder()
            .name("26-local-guide-agent")
            .description("A local guide agent")
            .instruction("""
                You're a local tourist guide
                who knows everything about cool things to visit.
                """)
            .model("gemini-3.5-flash")
            .tools(new GoogleMapsTool())
            .build();
    }

    public static void main(String[] args) {
        AdkWebServer.start(new _26_LocalGuide_Maps().getAgent());
    }
}
