package agents.adk.one;

import agents.AgentProvider;
import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.events.ToolConfirmation;
import com.google.adk.models.Gemini;
import com.google.adk.tools.Annotations.Schema;
import com.google.adk.tools.FunctionTool;
import com.google.adk.tools.GoogleSearchAgentTool;
import com.google.adk.tools.ToolContext;
import com.google.adk.web.AdkWebServer;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.HttpOptions;

public class _38_HITL implements AgentProvider {

    @Schema(name = "request_confirmation")
    public String requestConfirmation(
            @Schema(
                    name = "request_action",
                    description = "Description of the action to be confirmed or denied")
            String actionRequest,
            @Schema(name = "toolContext")
            ToolContext toolContext) {

        boolean isConfirmed = toolContext.toolConfirmation()
                .map(ToolConfirmation::confirmed)
                .orElse(false);

        if (!isConfirmed) {
            toolContext.requestConfirmation(
                    "Should I execute the following action? " + actionRequest, null);
            return "Confirmation requested.";
        }

        return "The following action has been confirmed: " + actionRequest;
    }

    @Override
    public BaseAgent getAgent() {

        Client client = Client.builder()
                .httpOptions(HttpOptions.builder()
                        .timeout(60 * 60 * 1000)
                        .build())
                .build();

        GenerateContentConfig config = GenerateContentConfig.builder()
                .httpOptions(HttpOptions.builder()
                        .timeout(60 * 60 * 1000)
                        .build())
                .build();

        LlmAgent assistant = LlmAgent.builder()
                .name("38-hitl")
                .description("A report assistant")
                .instruction("""
                        You are a helpful and friendly report assistant.
                        You can use the `google_search_agent` tool to search the internet
                        in order to create detailed reports about user's requested topics.
                        Before taking any action, ask the user for confirmation first,
                        using the `request_confirmation` tool.
                        
                        """)
                .model("gemini-3.5-flash")
                .generateContentConfig(config)
                .tools(
                        GoogleSearchAgentTool.create(
                                Gemini.builder()
                                        .modelName("gemini-3.5-flash")
                                        .apiClient(client)
                                        .build()),
                        FunctionTool.create(this, "requestConfirmation", true)
                )
                .build();


        return assistant;
    }

    public static void main(String[] args) {
        AdkWebServer.start(new _38_HITL().getAgent());
    }
}
