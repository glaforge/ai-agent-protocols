package agents.media;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.adk.tools.Annotations.Schema;
import org.a2aproject.sdk.A2A;

import org.a2aproject.sdk.client.Client;
import org.a2aproject.sdk.client.ClientEvent;
import org.a2aproject.sdk.client.MessageEvent;
import org.a2aproject.sdk.client.http.A2ACardResolver;
import org.a2aproject.sdk.client.http.A2AHttpClientFactory;
import org.a2aproject.sdk.client.transport.jsonrpc.JSONRPCTransport;
import org.a2aproject.sdk.client.transport.jsonrpc.JSONRPCTransportConfig;
import org.a2aproject.sdk.spec.A2AClientError;
import org.a2aproject.sdk.spec.A2AClientException;
import org.a2aproject.sdk.spec.AgentCard;
import org.a2aproject.sdk.spec.Message;
import org.a2aproject.sdk.spec.Part;
import org.a2aproject.sdk.spec.TextPart;

import static io.github.glaforge.ansiren.Ansi.bold;
import static io.github.glaforge.ansiren.Ansi.green;
import io.github.glaforge.ansiren.Ansi;

public class SocialMediaClient {

    private static final String SERVER_URL = "http://localhost:9999";
    private static final String MESSAGE_TEXT = "Send this message to LinkedIn: Hello everyone!";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Schema(name = "post_to_social_media", description = "Post messages to social media networks")
    public static Map<String, String> postSocialMedia(
        @Schema(name = "social-post-message", description = "The content of the social media post to publish")
        String textContent) {
        try {

            AgentCard publicAgentCard = A2ACardResolver.builder()
                .httpClient(A2AHttpClientFactory.create())
                .baseUrl(SERVER_URL)
                .build()
                .getAgentCard();

            System.out.println(bold("\nAgent card:"));
            System.out.println(Ansi.ready().brightBlack().append(OBJECT_MAPPER.writeValueAsString(publicAgentCard)).reset().toString());

            final CompletableFuture<String> messageResponse = new CompletableFuture<>();

            // Create the consumer list for handling client events
            List<BiConsumer<ClientEvent, AgentCard>> consumers = new ArrayList<>();
            consumers.add((event, agentCard) -> {
                if (event instanceof MessageEvent messageEvent) {
                    Message responseMessage = messageEvent.getMessage();
                    StringBuilder textBuilder = new StringBuilder();
                    if (responseMessage.parts() != null) {
                        for (Part<?> part : responseMessage.parts()) {
                            if (part instanceof TextPart textPart) {
                                textBuilder.append(textPart.text());
                            }
                        }
                    }
                    messageResponse.complete(textBuilder.toString());
                } else {
                    System.out.println("Received client event: " + event.getClass().getSimpleName());
                }
            });

            // Create error handler for streaming errors
            Consumer<Throwable> streamingErrorHandler = (error) -> {
                System.err.println("Streaming error occurred: " + error.getMessage());
                error.printStackTrace();
                messageResponse.completeExceptionally(error);
            };

            Client client = Client
                .builder(publicAgentCard)
                .addConsumers(consumers)
                .streamingErrorHandler(streamingErrorHandler)
                .withTransport(JSONRPCTransport.class, new JSONRPCTransportConfig())
                .build();

            Message message = A2A.toUserMessage(textContent); // the message ID will be automatically generated for you

            System.out.println(bold("\nSending social post:"));
            System.out.println(bold(green(textContent)));
            client.sendMessage(message);

            System.out.println("Message sent successfully. Responses will be handled by the configured consumers.");

            String responseText = messageResponse.get();
            System.out.println(bold("Response:\n") + green(responseText));

            return Map.of("success", responseText);
        } catch (Throwable throwable) {
            throwable.printStackTrace();

            return Map.of("error", throwable.getMessage());
        }
    }

    public static void main(String[] args) throws A2AClientException, ExecutionException, InterruptedException, JsonProcessingException, A2AClientError {
        postSocialMedia(MESSAGE_TEXT);
    }
}
