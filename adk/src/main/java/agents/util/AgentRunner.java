package agents.util;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.RunConfig;
import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Flowable;
import org.reactivestreams.Subscriber;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static agents.util.AnsiMarkdown.*;

public class AgentRunner {

    public static void run(BaseAgent agent) {
        runWithState(agent, null);
    }

    private static void printlnEvent(Event event) {
//        System.out.println("\n" + gray(event.toString()));
        System.out.println();
        System.out.println(yellow("[" + bold(event.author() + "]")));
        event.branch().ifPresent(branch -> System.out.println(yellow("Branch: " + bold(branch))));
        event.turnComplete().ifPresent(complete -> System.out.println(yellow("Turn complete: " + bold(complete.toString()))));
        event.content().ifPresent(content -> {
            content.parts().ifPresent(parts -> {
                parts.forEach(part -> {
                    if (part.functionCall().isPresent()) {
                        System.out.println(yellow(bold("Function call: ")));
                        System.out.println(yellow(" - " + part.functionCall().get().name().get() + "(" + part.functionCall().get().args().get() + ")"));
                    }
                    if (part.functionResponse().isPresent()) {
                        System.out.println(yellow(bold("Function response: ")) + yellow(" - " + part.functionResponse().get().response().get()));
                    }
                    if (part.text().isPresent()) {
                        String text = part.text().get();
                        System.out.println(
                            yellow(bold("Text: ")) +
                                yellow(text.substring(0, Math.min(text.length(), 100)) +
                                    (text.length() >= 100 ? "..." : "")));
                    }
                });
            });
        });
        event.groundingMetadata().ifPresent(groundingMetadata -> {
            System.out.println(yellow(bold("Grounding Metadata:")));
            System.out.println(groundingMetadata);
        });
    }

    public static void runWithState(BaseAgent agent, Map<String, Object> state) {
        InMemoryRunner runner = new InMemoryRunner(agent);

        final String appName = runner.appName();
        final String userId = UUID.randomUUID().toString();

        Session session = runner
            .sessionService()
            .createSession(appName, userId,
                state == null ? null : new ConcurrentHashMap<>(state), null)
            .blockingGet();

        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
            while (true) {
                System.out.print(bold(red("\n\uD83D\uDC64 You > ")));
                System.out.print("\u001B[1m");
                String userInput = scanner.nextLine();
                System.out.print("\u001B[0m");

                if ("quit".equalsIgnoreCase(userInput)) {
                    break;
                }

                Content userMsg = Content.fromParts(Part.fromText(userInput));

                Flowable<Event> events = runner.runAsync(session.userId(), session.id(), userMsg,
                    RunConfig.builder()
                        .setSaveInputBlobsAsArtifacts(true)
                        .setStreamingMode(RunConfig.StreamingMode.SSE)
                        .build());

                System.out.print(bold(green("\n\uD83E\uDD16 Agent > \n")));
                events//.filter(Event::finalResponse)
                    .filter(event -> event.partial().isEmpty() || (event.partial().isPresent() && !event.partial().get()))
                    .blockingForEach(event -> {
                        printlnEvent(event);
                        if (event.finalResponse()) {
                            event.content().flatMap(Content::parts).ifPresent(parts -> {
                                System.out.println();
                                parts.forEach(part -> {
                                    System.out.print(
                                        markdown(part.text().orElseGet(() -> ""))
                                    );
                                });
                            });
                        }

/*
                        if (event.actions().stateDelta() != null && !event.actions().stateDelta().isEmpty()) {
                            System.out.println("\nSTATE DELTA:");
                            event.actions().stateDelta().forEach((key, value) -> {
                                System.out.println(" - " + blue(key) + ": " + cyan(value.toString()));
                            });
                        }

                        if (event.actions().artifactDelta() != null && !event.actions().artifactDelta().isEmpty()) {
                            System.out.println("\nARTIFACT DELTA:");
                            event.actions().artifactDelta().forEach((key, value) -> {
                                System.out.println(" - " + green(key) + ": " + gray(value.toString()));
                            });
                        }
*/
                    });
            }
        }
    }

    private static void subscribe(Subscriber<? super Event> subscriber) {
        Event.builder().build();
    }
}
