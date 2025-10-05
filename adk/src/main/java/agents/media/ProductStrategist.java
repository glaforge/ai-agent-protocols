package agents.media;

import agents.AgentProvider;
import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.AgentTool;
import com.google.adk.tools.BaseTool;
import com.google.adk.tools.mcp.McpToolset;
import com.google.adk.tools.mcp.SseServerParameters;
import com.google.adk.web.AdkWebServer;

public class ProductStrategist implements AgentProvider {
    @Override
    public BaseAgent getAgent() {
        SseServerParameters connectionParams = SseServerParameters.builder()
            .url("http://localhost:8080/mcp/sse")
            .build();

        try (McpToolset toolset = new McpToolset(connectionParams)) {
            BaseTool databaseTool = toolset.getTools(null).blockingFirst();

            return LlmAgent.builder()
                .name("product_strategist")
                .model("gemini-2.5-flash")
                .description("product strategist")
                .instruction("""
                    As a world-class Marketing Product Strategist, your mission is to conceptualize innovative products
                    that deeply resonate with specific audiences.
                    
                    You will be given:
                    * a **product category** and
                    * a **target demographic**.
                    
                    Based on this input, conduct a brief internal analysis of the demographic's likely values, desires, and pain points.
                    Then, develop a compelling product brief that includes the following two sections:
                    
                    **1. Product Name:**
                       - A creative and fitting name for the product.
                    
                    **2. Product Description:**
                       - A detailed description of the product. What is it? What are its key features, look, and feel? How does it work?
                    
                    If the user hasn't provided the information, be sure to ask users
                    about the product they want to create and the demographics they want to target.
                    
                    With that information (product type and target demographics) you craft a product brief with:
                    
                    * the name of the product
                    * the detailed description of the product
                    
                    Call the `store-product-brief-in-database` tool to save the product name and description in the database.
                    
                    Lastly, call the `media_creative` tool to create a beautiful photography of the product.
                    """)
                .outputKey("product_brief")
                .tools(
                    databaseTool,
                    AgentTool.create(new MediaCreative().getAgent())
                )
                .build();
        }
    }

    public static void main(String[] args) {
        AdkWebServer.start(new ProductStrategist().getAgent());
    }
}
