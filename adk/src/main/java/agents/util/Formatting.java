package agents.util;

import com.google.genai.types.Content;

import java.util.Base64;
import java.util.Map;

import static agents.util.AnsiMarkdown.*;

public class Formatting {
    public static void logContent(Content content) {
        System.out.println(bold(gray("--- CONTENT ---")));
        StringBuilder sb = new StringBuilder();
        content.parts().ifPresent(parts -> {
            parts.forEach(part -> {
                part.text().ifPresent(text -> sb.append("- text: " + markdown(text) + "\n"));
                part.inlineData().ifPresent(inline -> sb.append("- inline data: " + green(inline.mimeType().orElse("")) + "\n"));
                part.functionCall().ifPresent(functionCall -> sb.append("- function call: " + green(functionCall.name().get()) + "(" + yellow(functionCall.args().orElse(Map.of()).toString()) + ")\n"));
                part.functionResponse().ifPresent(functionResponse -> sb.append("- output: " + yellow(functionResponse.response().orElse(Map.of()).toString()) + "\n"));
                part.executableCode().ifPresent(executableCode -> sb.append("- executable code: " + yellow(executableCode.code().orElse("")) + "\n"));
                part.codeExecutionResult().ifPresent(codeExecutionResult -> sb.append("- code execution result: " + yellow(codeExecutionResult.output().orElse("")) + "\n"));
                part.videoMetadata().ifPresent(videoMetadata -> sb.append("- video metadata: " + yellow(videoMetadata.toString()) + "\n"));
                part.fileData().ifPresent(fileData -> sb.append("- file data: " + yellow(fileData.mimeType().orElse("")) + "\n"));
                part.thought().ifPresent(thought -> sb.append("- thought: " + yellow(thought.toString()) + "\n"));
                part.thoughtSignature().ifPresent(thought -> sb.append("- thought signature: " + gray(Base64.getEncoder().encodeToString(thought)) + "\n"));
            });
        });
        System.out.print(sb.toString()
//            .replaceAll("(\\w+=Optional\\.empty),?", "$1")
//            .replaceAll("Optional\\[(.*)]", "$1")
        );
    }
}
