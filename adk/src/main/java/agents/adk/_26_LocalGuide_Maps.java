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
import com.google.adk.models.Gemini;
import com.google.adk.models.LlmRequest;
import com.google.adk.models.LlmResponse;
import com.google.adk.web.AdkWebServer;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GoogleMaps;
import com.google.genai.types.LatLng;
import com.google.genai.types.RetrievalConfig;
import com.google.genai.types.Tool;
import com.google.genai.types.ToolConfig;
import io.reactivex.rxjava3.core.Flowable;

/**
 * This touristic local guide implements a custom Gemini model
 * to automatically configure the LlmRequest sent to Gemini
 * to ground answers with results from Google Maps.
 *
 * Note: Works only when using Gemini via Vertex AI.
 */
public class _26_LocalGuide_Maps implements AgentProvider {

    static class CustomGemini extends Gemini {
        public CustomGemini(String modelName, Client apiClient) {
            super(modelName, apiClient);
        }

        @Override
        public Flowable<LlmResponse> generateContent(LlmRequest llmRequest, boolean stream) {
            LlmRequest requestWithMapsTool = llmRequest.toBuilder()
                .config(GenerateContentConfig.builder()
                    .tools(
                        Tool.builder().googleMaps(GoogleMaps.builder()
//                            .authConfig(AuthConfig.builder()
//                                .apiKeyConfig(ApiKeyConfig.builder()
//                                    .apiKeyString(System.getenv("GOOGLE_MAPS_API_KEY"))
//                                    .build())
//                                .build())
                            .build())
                    )
                    .toolConfig(ToolConfig.builder()
                        .retrievalConfig(RetrievalConfig.builder()
                            .latLng(LatLng.builder()
                                // Antwerp coordinates
                                .latitude(51.217468)
                                .longitude(4.421192)
                                .build())
                            .build())
                        .build())
                    .build())
                .build();
            return super.generateContent(requestWithMapsTool, stream);
        }
    }

    @Override
    public BaseAgent getAgent() {
        Client client = Client.builder()
            .project("genai-java-demos")
            .location("europe-west1")
            .vertexAI(true)
            .build();

        return LlmAgent.builder()
            .name("26-local-guide-agent")
            .description("A local guide agent")
            .instruction("""
                You're a local tourist guide
                who knows everything about cool things to visit.
                """)
            .model(new CustomGemini("gemini-2.5-flash", client))
            .build();
    }

    public static void main(String[] args) {
        AdkWebServer.start(new _26_LocalGuide_Maps().getAgent());
    }
}
