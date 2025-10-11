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
import agents.util.Formatting;
import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.Annotations.Schema;
import com.google.adk.tools.FunctionTool;
import com.google.adk.web.AdkWebServer;
import io.reactivex.rxjava3.core.Maybe;

import java.time.LocalDate;
import java.util.Map;

import static agents.util.AnsiMarkdown.*;

/**
 * The weather forecast agent exemplifies how to define the various possible callbacks.
 */
public class _40_WeatherForecast_Callback implements AgentProvider {
    @Override
    public BaseAgent getAgent() {

        return LlmAgent.builder()
            .name("40-weather-assistant")
            .description("weather forecast assistant")
            .instruction("""
                You are an expert meteorologist.
                When asked about the weather, call the `get_weather` tool.
                """)
            .model("gemini-2.5-flash")
            .tools(FunctionTool.create(_40_WeatherForecast_Callback.class, "getWeather"))

            .beforeAgentCallback(callbackContext -> {
                System.out.println(blue(bold("beforeAgentCallback")));
                return Maybe.empty();
            })
            .afterAgentCallback(callbackContext -> {
                System.out.println(blue(bold("afterAgentCallback")));
                return Maybe.empty();
            })
            .beforeModelCallback((callbackContext, llmRequestBuilder) -> {
                System.out.println(blue(bold("beforeModelCallback")));
                llmRequestBuilder.build().contents().forEach(Formatting::logContent);
                return Maybe.empty();
            })
            .afterModelCallback((callbackContext, llmResponse) -> {
                System.out.println(blue(bold("afterModelCallback")));
                llmResponse.content().ifPresent(Formatting::logContent);
                return Maybe.empty();
            })
            .beforeToolCallback((invocationContext, baseTool, input, toolContext) -> {
                System.out.println(blue(bold("beforeToolCallback")));
                System.out.println("- function call: " + green(baseTool.name()) + "(" + yellow(input.toString()) + ")");
                return Maybe.empty();
            })
            .afterToolCallback((invocationContext, baseTool, input, toolContext, response) -> {
                System.out.println(blue(bold("afterToolCallback")));
                System.out.println("- function call: " + green(baseTool.name()) + "(" + yellow(input.toString()) + ")");
                System.out.println("- output: " + yellow(response.toString()));
//                return Maybe.empty();
                return Maybe.just(Map.of("weather", "rainy"));
            })
            .build();
    }

    @Schema(name = "getWeather", description = "Get the weather for a given location")
    public static Map<String, String> getWeather(
        @Schema(name = "location", description = "The location to get the weather for")
        String location
    ) {
        return Map.of(
            "weather", "sunny",
            "location", location,
            "date", LocalDate.now().toString()
        );
    }

    public static void main(String[] args) {
        AdkWebServer.start(new _40_WeatherForecast_Callback().getAgent());
    }
}
