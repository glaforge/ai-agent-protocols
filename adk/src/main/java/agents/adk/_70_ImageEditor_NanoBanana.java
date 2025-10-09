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
import agents.util.TerminalImageRenderer;
import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Maybe;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static agents.util.AnsiMarkdown.*;

public class _70_ImageEditor_NanoBanana implements AgentProvider {
    @Override
    public BaseAgent getAgent() {
        return LlmAgent.builder()
            .name("70-image-editor")
            .description("an image editor using the nano banana image model")
            .model("gemini-2.5-flash-image")
            .instruction("""
                You are a creative designer able to create and edit images.
                
                If you're given an image or already created one,
                modify the image according to the request.
                
                Existing image: {image?}
                """)
            .beforeModelCallback((callbackContext, llmRequestBuilder) -> {
                Part image = (Part) callbackContext.state().get("image");
                System.out.println(blue("Found image in state: ") + image);
                llmRequestBuilder.contents(List.of(
                    callbackContext.userContent().get(),
                    Content.fromParts(image))
                );
                return Maybe.empty();
            })
            .afterModelCallback((callbackContext, llmResponse) -> {
                llmResponse.content()
                    .flatMap(Content::parts)
                    .stream()
                    .flatMap(List::stream)
                    .filter(part -> part.inlineData().isPresent())
                    .forEach(part -> {
                        callbackContext.state().put("image", part);

                        part.inlineData().ifPresent(blob -> {
                            // Potentially save the file as a file elsewhere
                            // byte[] imageBytes = blob.data().get();
                            // String mimeType = blob.mimeType().get();

                            BufferedImage image = TerminalImageRenderer.partToImage(part);
                            if (image != null) {
                                TerminalImageRenderer.printImage(image);
                                TerminalImageRenderer.saveImageToFile(image);
                            }
                        });
                    });
                return Maybe.empty();
            })
            .build();
    }

    public static void main(String[] args) throws IOException {
//        AdkWebServer.start(new _70_ImageEditor_NanoBanana().getAgent());

        if (args.length > 0) {
            Path path = Path.of(args[0]);
            System.out.println(bold("Loading: ") + blue(path.toString()));
            String mimeType = "image/" + args[0].substring(args[0].lastIndexOf('.') + 1);
            byte[] bytes = Files.readAllBytes(path);
            Part part = Part.fromBytes(bytes, mimeType);

            AgentRunner.runWith(
                new _70_ImageEditor_NanoBanana().getAgent(),
                Map.of("image", part),
                Map.of());
        } else {
            AgentRunner.run(new _70_ImageEditor_NanoBanana().getAgent());
        }
    }
}
