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
import com.google.adk.tools.BaseTool;
import com.google.adk.tools.mcp.McpToolset;
import com.google.adk.tools.mcp.SseServerParameters;
import com.google.adk.tools.mcp.StreamableHttpServerParameters;
import com.google.adk.web.AdkWebServer;

import java.time.LocalDate;
import java.util.List;

/**
 * This agent calls an MCP server.
 * STDIO, Server-Sent Event, and Streamable HTTP MCP servers are supported.
 */
public class _28_MoonExpert_MCP implements AgentProvider {
    @Override
    public BaseAgent getAgent() {
        // Server-Sent Events MCP server
//        SseServerParameters params = SseServerParameters.builder()
//            .url("https://moonphases-1029513523185.europe-west1.run.app/mcp/sse")
//            .build();

        // Streamable HTTP MCP server
        StreamableHttpServerParameters params = StreamableHttpServerParameters.builder()
            .url("https://mn-mcp-server-1029513523185.europe-west1.run.app/mcp")
            .build();

        try (McpToolset mcpToolset = new McpToolset(params)) {
            List<BaseTool> moonPhasesTools =
                mcpToolset.getTools(null).toList().blockingGet();

            return LlmAgent.builder()
                .name("28-moon-expert")
                .model("gemini-2.5-flash")
                .description("a moon expert")
                .instruction("""
                    You are a knowledgeable astronomy expert
                    focusing on everything about the moon.
                    Today's date is: \
                    """ + LocalDate.now())
                .tools(moonPhasesTools)
                .build();
        }
    }

    public static void main(String[] args) {
        AdkWebServer.start(new _28_MoonExpert_MCP().getAgent());
    }
}
