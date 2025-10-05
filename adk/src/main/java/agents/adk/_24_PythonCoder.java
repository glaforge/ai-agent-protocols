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
import com.google.adk.tools.BuiltInCodeExecutionTool;
import com.google.adk.web.AdkWebServer;

public class _24_PythonCoder implements AgentProvider {
    @Override
    public BaseAgent getAgent() {
        return LlmAgent.builder()
            .name("python-code-agent")
            .description("Agent to do execute some programming logic with Python.")
            .instruction("""
                You are a helpful Python developer that can implement algorithms in Python.
                Use your built-in Python code execution capabilities to implement and execute Python code.
                """)
            .model("gemini-2.5-flash")
            .tools(new BuiltInCodeExecutionTool())
            .build();
    }

    public static void main(String[] args) {
        AdkWebServer.start(new _24_PythonCoder().getAgent());
    }
}
