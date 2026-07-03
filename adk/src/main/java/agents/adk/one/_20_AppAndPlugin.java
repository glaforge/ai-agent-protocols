package agents.adk.one;

import agents.AgentProvider;
import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.UrlContextTool;
import com.google.adk.web.AdkWebServer;

public class _11_UrlContextGrounding implements AgentProvider {
    @Override
    public BaseAgent getAgent() {
        return LlmAgent.builder()
                .name("url-summarizer")
                .description("Summarize the content of a URL")
                .instruction("""
                        A precise word smith
                        who uses the `url_context` tool
                        to summarize the content of a URL
                        given by the user.
                        """)
                .model("gemini-2.5-flash")
                .tools(new UrlContextTool())
                .build();
    }

    public static void main(String[] args) {
        AdkWebServer.start(new _11_UrlContextGrounding().getAgent());
    }
}
