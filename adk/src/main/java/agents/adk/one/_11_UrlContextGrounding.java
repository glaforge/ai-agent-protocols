package agents.adk.one;

import agents.AgentProvider;
import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.GoogleMapsTool;
import com.google.adk.web.AdkWebServer;

public class _10_MapsGrounding implements AgentProvider {
    @Override
    public BaseAgent getAgent() {
        return LlmAgent.builder()
                .name("restaurant-guide")
                .description("A restaurant guide for the traveler")
                .instruction("""
                        You are a restaurant guide gourmet travelers.
                        Use the `google_maps` tool
                        when asked to search for restaurants
                        near a certain location.
                        """)
                .model("gemini-2.5-flash")
                .tools(new GoogleMapsTool())
                .build();
    }

    public static void main(String[] args) {
        AdkWebServer.start(new _10_MapsGrounding().getAgent());
    }
}
