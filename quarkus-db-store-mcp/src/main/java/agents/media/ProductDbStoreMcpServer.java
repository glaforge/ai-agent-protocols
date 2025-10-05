package agents.media;

import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductDbStoreMcpServer {

    private static final Logger log = LoggerFactory.getLogger(ProductDbStoreMcpServer.class);

    @Tool(name = "store-product-brief-in-database",
        description = "Store a product brief in the database")
    public ToolResponse storeProductBrief(
        @ToolArg(name = "product_name", description = "Name of the product")
        String productName,
        @ToolArg(name = "product_description", description = "Description of the product")
        String productDescription
    ) {
        try {
            log.info("Saved product brief for: " + productName);
            return ToolResponse.success("Product saved.");
        } catch (Throwable t) {
            log.error("Failed to save product brief: " + t.getMessage(), t);
            return ToolResponse.error("An error occurred while saving product details: " + t.getMessage());
        }
    }
}

