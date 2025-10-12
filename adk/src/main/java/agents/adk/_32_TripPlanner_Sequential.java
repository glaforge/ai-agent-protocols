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
import com.google.adk.agents.SequentialAgent;
import com.google.adk.tools.GoogleSearchTool;
import com.google.adk.web.AdkWebServer;

/**
 * A sequential workflow agent organizing the work of three agents in a row.
 * Some of the agents use the Google Search built-in too.
 * The agents define an output key, to store the result of their work into the shared state.
 */
public class _32_TripPlanner_Sequential implements AgentProvider {
    @Override
    public BaseAgent getAgent() {
        var destinationResearcher = LlmAgent.builder()
            .name("destination-researcher")
            .description("Finds points of interest for a given destination and travel style.")
            .instruction("""
                Your role is to find points of interest for a given city and travel style,
                according to the duration of the trip, and potentially budget.
                Use the Google Search Tool to find relevant information.
                Present the results as a simple bullet point list of key attractions.
                """)
            .model("gemini-2.5-flash")
            .tools(new GoogleSearchTool())
            .outputKey("destination-research")
            .build();

        var itineraryCreator = LlmAgent.builder()
            .name("itinerary-creator")
            .description("Creates an itinerary from a list of attractions.")
            .instruction("""
                Your role is to create a logical itinerary from the provided list of attractions:
                
                {destination-research}
                
                Group the attractions by proximity or theme for each day.
                Present the itinerary clearly, with each day's plan laid out.
                """)
            .model("gemini-2.5-flash")
            .outputKey("itinerary")
            .build();

        var restaurantSuggester = LlmAgent.builder()
            .name("restaurant-suggester")
            .description("Suggests restaurants for each day of the itinerary.")
            .instruction("""
                Your role is to suggest one lunch and one dinner restaurant for each day of the itinerary:
                
                {itinerary}
                
                Use the Google Search Tool to find restaurants that are near the day's activities
                and match the overall travel style.
                Add the restaurant suggestions to the itinerary.
                """)
            .model("gemini-2.5-flash")
            .tools(new GoogleSearchTool())
            .build();

        return SequentialAgent.builder()
            .name("32-trip-planner")
            .description("""
                Helps you plan a trip by finding attractions,
                creating an itinerary, and suggesting restaurants.
                """)
            .subAgents(
                destinationResearcher,
                itineraryCreator,
                restaurantSuggester
            )
            .build();
    }


    public static void main(String[] args) {
        System.setProperty(
            "org.apache.tomcat.websocket.DEFAULT_BUFFER_SIZE",
            String.valueOf(10 * 1024 * 1024)
        );
        System.setProperty(
            "spring.mvc.async.request-timeout",
            String.valueOf(60 * 60 * 1000)
        );

        AdkWebServer.start(new _32_TripPlanner_Sequential().getAgent());
//        AgentRunner.run(new _32_TripPlanner_Sequential().getAgent());
    }
}
