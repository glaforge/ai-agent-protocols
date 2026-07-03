package agents.adk;

import agents.AgentProvider;
import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.apps.App;
import com.google.adk.models.Gemini;
import com.google.adk.plugins.GlobalInstructionPlugin;
import com.google.adk.plugins.LoggingPlugin;
import com.google.adk.plugins.Plugin;
import com.google.adk.runner.Runner;
import com.google.adk.summarizer.EventsCompactionConfig;
import com.google.adk.summarizer.LlmEventSummarizer;
import com.google.adk.web.AdkWebServer;

import java.util.List;

public class _80_AppAndPlugin implements AgentProvider {
    @Override
    public BaseAgent getAgent() {

        LlmAgent assistant = LlmAgent.builder()
                .name("80-helpful-assistant")
                .description("A helpful assistant")
                .instruction("You are a helpful and friendly assistant")
                .model("gemini-3.5-flash")
                .build();

        // Define plugins
        List<Plugin> plugins = List.of(
                new LoggingPlugin(),
                new GlobalInstructionPlugin("YOU ALWAYS WRITE IN ALL CAPS")
        );

        String apiKey = System.getenv("GEMINI_API_KEY");
        Gemini llm = new Gemini("gemini-3.5-flash", apiKey);

        // Build the App
        App myApp = App.builder()
                .name("customer_support_app")
                .rootAgent(assistant)
                .plugins(plugins)
                .eventsCompactionConfig(
                        new EventsCompactionConfig(
                                2, // compaction interval
                                5, // overlab size
                                new LlmEventSummarizer(llm),
                                4000, // token threshold
                                100 // event retention size
                        ))
                .build();

        // Run the application
        Runner runner = Runner.builder()
                .app(myApp)
//                .artifactService(artifactService)
//                .sessionService(sessionService)
//                .memoryService(memoryService)
                .build();

        return assistant;
    }

    public static void main(String[] args) {
        AdkWebServer.start(new _80_AppAndPlugin().getAgent());
    }
}
