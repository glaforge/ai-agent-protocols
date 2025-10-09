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
import com.google.adk.agents.ParallelAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.adk.tools.GoogleSearchTool;
import com.google.adk.web.AdkWebServer;

public class _34_CompanyDetective_Parallel implements AgentProvider {
    @Override
    public BaseAgent getAgent() {
        var companyProfiler = LlmAgent.builder()
            .name("company-profiler")
            .description("Provides a general overview of a company.")
            .instruction("""
                Your role is to provide a brief overview of the given company.
                Include its mission, headquarters, and current CEO.
                Use the Google Search Tool to find this information.
                """)
            .model("gemini-2.5-flash")
            .tools(new GoogleSearchTool())
            .outputKey("profile")
            .build();

        var newsFinder = LlmAgent.builder()
            .name("news-finder")
            .description("Finds the latest news about a company.")
            .instruction("""
                Your role is to find the top 3-4 recent news headlines for the given company.
                Use the Google Search Tool.
                Present the results as a simple bulleted list.
                """)
            .model("gemini-2.5-flash")
            .tools(new GoogleSearchTool())
            .outputKey("news")
            .build();

        var financialAnalyst = LlmAgent.builder()
            .name("financial-analyst")
            .description("Analyzes the financial performance of a company.")
            .instruction("""
                Your role is to provide a snapshot of the given company's recent financial performance.
                Focus on stock trends or recent earnings reports.
                Use the Google Search Tool.
                """)
            .model("gemini-2.5-flash")
            .tools(new GoogleSearchTool())
            .outputKey("financials")
            .build();

        var marketResearcher = ParallelAgent.builder()
            .name("market-researcher")
            .description("Performs comprehensive market research on a company.")
            .subAgents(
                companyProfiler,
                newsFinder,
                financialAnalyst
            )
            .build();

        var reportCompiler = LlmAgent.builder()
            .name("report-compiler")
            .description("Compiles a final market research report.")
            .instruction("""
                Your role is to synthesize the provided information into a coherent market research report.
                Combine the company profile, latest news, and financial analysis into a single, well-formatted report.
                
                ## Company Profile
                {profile}
                
                ## Latest News
                {news}
                
                ## Financial Snapshot
                {financials}
                """)
            .model("gemini-2.0-flash")
            .build();

        return SequentialAgent.builder()
            .name("34-company-detective")
            .description("Collects various market information about a company.")
            .subAgents(
                marketResearcher,
                reportCompiler
            ).build();
    }

    public static void main(String[] args) {
        AdkWebServer.start(new _34_CompanyDetective_Parallel().getAgent());
//        AgentRunner.run(new _34_CompanyDetective_Parallel().getAgent());
    }
}
