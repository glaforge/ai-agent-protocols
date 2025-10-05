package agents.media;

import agents.AgentProvider;
import agents.util.AgentRunner;
import agents.util.TerminalImageRenderer;
import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.web.AdkWebServer;
import com.google.genai.types.Blob;
import com.google.genai.types.Content;
import io.reactivex.rxjava3.core.Maybe;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MediaCreative implements AgentProvider {
    @Override
    public BaseAgent getAgent() {
        return LlmAgent.builder()
            .name("media_creative")
            .model("gemini-2.5-flash-image-preview") // Nano Banana 🍌
            .description("media creative agent")
            .instruction("""
                Generate a detailed and high-quality photography, in 3:4 aspect ratio, described in the brief below.
                Be sure to put the name of the product on the product itself (carved, engraved, printed...)
                
                {product_brief?}
                """)
            .afterModelCallback((callbackContext, llmResponse) -> {
                llmResponse.content() // 1. Let's find the image part!
                    .flatMap(Content::parts)
                    .stream()
                    .flatMap(List::stream)
                    // Filter parts containing image content
                    .filter(part -> part.inlineData().isPresent())
                    .forEach(part -> {
                        // 2. Save the image as an artifact for the pipeline
                        callbackContext.saveArtifact("rendered-image", part);

                        // 3. Potentially save the image as a file elsewhere
                        Blob blob = part.inlineData().get();
                        byte[] imageBytes = blob.data().get();
                        String mimeType = blob.mimeType().get();

                        try {
                            Files.write(Path.of("rendered-image." + mimeType.split("/")[1]), imageBytes);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        BufferedImage image = TerminalImageRenderer.partToImage(part);
                        if (image != null) {
                            TerminalImageRenderer.printImage(image);
                        }
                    });
                // Returning empty means not altering the agent's response
                return Maybe.empty();
            })
            .build();
    }

    public static void main(String[] args) {
//        AdkWebServer.start(new MediaCreative().getAgent());
        AgentRunner.run(new MediaCreative().getAgent());
    }
}
