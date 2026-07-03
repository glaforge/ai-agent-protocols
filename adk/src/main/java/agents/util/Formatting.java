package agents.util;

import com.google.genai.types.Content;

import java.util.Base64;
import java.util.Map;

import io.github.glaforge.ansiren.Ansi;
import io.github.glaforge.ansiren.MarkdownRenderer;
import static io.github.glaforge.ansiren.Ansi.bold;
import static io.github.glaforge.ansiren.Ansi.green;
import static io.github.glaforge.ansiren.Ansi.yellow;

public class Formatting {
    private static final MarkdownRenderer markdownRenderer = new MarkdownRenderer();
    public static void logContent(Content content) {
        System.out.println(Ansi.ready().bold().brightBlack().append("--- CONTENT ---").reset().toString());
        StringBuilder sb = new StringBuilder();
        content.parts().ifPresent(parts -> {
            parts.forEach(part -> {
                part.text().ifPresent(text -> sb.append("- text: " + markdownRenderer.render(text) + "\n"));
                part.inlineData().ifPresent(inline -> sb.append("- inline data: " + green(inline.mimeType().orElse("")) + "\n"));
                part.functionCall().ifPresent(functionCall -> sb.append("- function call: " + green(functionCall.name().get()) + "(" + yellow(functionCall.args().orElse(Map.of()).toString()) + ")\n"));
                part.functionResponse().ifPresent(functionResponse -> sb.append("- output: " + yellow(functionResponse.response().orElse(Map.of()).toString()) + "\n"));
                part.executableCode().ifPresent(executableCode -> sb.append("- executable code: " + yellow(executableCode.code().orElse("")) + "\n"));
                part.codeExecutionResult().ifPresent(codeExecutionResult -> sb.append("- code execution result: " + yellow(codeExecutionResult.output().orElse("")) + "\n"));
                part.videoMetadata().ifPresent(videoMetadata -> sb.append("- video metadata: " + yellow(videoMetadata.toString()) + "\n"));
                part.fileData().ifPresent(fileData -> sb.append("- file data: " + yellow(fileData.mimeType().orElse("")) + "\n"));
                part.thought().ifPresent(thought -> sb.append("- thought: " + yellow(thought.toString()) + "\n"));
                part.thoughtSignature().ifPresent(thought -> sb.append("- thought signature: " + Ansi.ready().brightBlack().append(Base64.getEncoder().encodeToString(thought)).reset().toString() + "\n"));
            });
        });
        System.out.print(sb.toString()
//            .replaceAll("(\\w+=Optional\\.empty),?", "$1")
//            .replaceAll("Optional\\[(.*)]", "$1")
        );
    }
}
