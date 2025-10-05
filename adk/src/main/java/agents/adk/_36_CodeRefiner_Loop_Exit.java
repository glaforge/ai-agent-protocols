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
import agents.util.AgentRunner;
import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.LoopAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.adk.tools.ExitLoopTool;
import com.google.adk.web.AdkWebServer;

public class _36_CodeRefiner_Loop_Exit implements AgentProvider {
    @Override
    public BaseAgent getAgent() {
        var codeGenerator = LlmAgent.builder()
            .name("code-generator")
            .description("Writes and refines code based on a request and feedback.")
            .instruction("""
                Your role is to write a Python function based on the user's request.
                In the first turn, write the initial version of the code.
                In subsequent turns, you will receive feedback on your code.
                Your task is to refine the code based on this feedback.
                
                Previous feedback (if any):
                {feedback?}
                """)
            .model("gemini-2.5-flash")
            .outputKey("generated_code")
            .build();

        var codeReviewer = LlmAgent.builder()
            .name("code-reviewer")
            .description("Reviews code and decides if it's complete or needs more work.")
            .instruction("""
                Your role is to act as a senior code reviewer.
                Analyze the provided Python code for correctness, style, and potential bugs.
                
                Code to review:
                {generated_code}
                
                If the code is perfect and meets the user's request,
                you MUST call the `exit_loop` tool.
                
                Otherwise, provide constructive feedback for the `code-generator to improve the code.
                """)
            .model("gemini-2.5-flash")
            .outputKey("feedback")
            .tools(ExitLoopTool.INSTANCE)
            .build();


        var codeRefinerLoop = LoopAgent.builder()
            .name("code-refiner-loop")
            .description("Iteratively generates and reviews code until it is correct.")
            .subAgents(
                codeGenerator,
                codeReviewer
            )
            .maxIterations(3) // Safety net to prevent infinite loops
            .build();

        var finalPresenter = LlmAgent.builder()
            .name("final-presenter")
            .description("Presents the final, accepted code to the user.")
            .instruction("""
                The code has been successfully generated and reviewed.
                Present the final version of the code to the user in a clear format.
                
                Final Code:
                {generated_code}
                """)
            .model("gemini-2.5-flash")
            .build();

        return SequentialAgent.builder()
            .name("code-refiner-assistant")
            .description("Manages the full code generation and refinement process.")
            .subAgents(
                codeRefinerLoop,
                finalPresenter)
            .build();
    }

    public static void main(String[] args) {
        AdkWebServer.start(new _36_CodeRefiner_Loop_Exit().getAgent());
//        AgentRunner.run(new _36_CodeRefiner_Loop_Exit().getAgent());
    }
}
