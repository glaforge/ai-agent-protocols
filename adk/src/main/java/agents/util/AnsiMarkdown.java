package agents.util;

public class AnsiMarkdown {

    public static String red(String msg) {
        return "\u001B[31m" + msg + "\u001B[0m";
    }

    public static String green(String msg) {
        return "\u001B[32m" + msg + "\u001B[0m";
    }

    public static String gray(String msg) {
        return "\u001B[90m" + msg + "\u001B[0m";
    }

    public static String blue(String msg) {
        return "\u001B[34m" + msg + "\u001B[0m";
    }

    public static String yellow(String msg) {
        return "\u001B[33m" + msg + "\u001B[0m";
    }

    public static String cyan(String msg) {
        return "\u001B[36m" + msg + "\u001B[0m";
    }

    public static String underline(String msg) {
        return "\u001B[4m" + msg + "\u001B[0m";
    }

    public static String bold(String msg) {
        return "\u001B[1m" + msg + "\u001B[0m";
    }

    public static String strikethrough(String msg) {
        return "\u001B[9m" + msg + "\u001B[0m";
    }

    public static String italic(String msg) {
        return "\u001B[3m" + msg + "\u001B[0m";
    }

    public static String markdown(String msg) {
        if (msg == null) return "";

        return msg
            // Bold
            .replaceAll("\\*\\*(.*?)\\*\\*", "\u001B[1m$1\u001B[0m")
            // Italic
            .replaceAll("\\*(.*?)\\*", "\u001B[3m$1\u001B[0m")
            // Underline
            .replaceAll("__(.*?)__", "\u001B[4m$1\u001B[0m")
            // Strikethrough
            .replaceAll("~~(.*?)~~", "\u001B[9m$1\u001B[0m")
            // Blockquote
            .replaceAll("(> ?.*)",
                "\u001B[3m\u001B[34m\u001B[1m$1\u001B[22m\u001B[0m")
            // Lists (bold magenta number and bullet)
            .replaceAll("([\\d]+\\.|-|\\*) (.*)",
                "\u001B[35m\u001B[1m$1\u001B[22m\u001B[0m $2")
            // Block code (black on gray)
            .replaceAll("(?s)```(\\w+)?\\n(.*?)\\n```",
                "\u001B[3m\u001B[1m$1\u001B[22m\u001B[0m\n\u001B[57;107m$2\u001B[0m\n")
            // Inline code (black on gray)
            .replaceAll("`(.*?)`", "\u001B[57;107m$1\u001B[0m")
            // Headers (cyan bold)
            .replaceAll("(#{1,6}) (.*?)\n",
                "\u001B[36m\u001B[1m$1 $2\u001B[22m\u001B[0m\n")
            // Headers with a single line of text followed by 2 or more equal signs
            .replaceAll("(.*?\n={2,}\n)",
                "\u001B[36m\u001B[1m$1\u001B[22m\u001B[0m\n")
            // Headers with a single line of text followed by 2 or more dashes
            .replaceAll("(.*?\n-{2,}\n)",
                "\u001B[36m\u001B[1m$1\u001B[22m\u001B[0m\n")
            // Images (blue underlined)
            .replaceAll("!\\[(.*?)]\\((.*?)\\)",
                "\u001B[34m$1\u001B[0m (\u001B[34m\u001B[4m$2\u001B[0m)")
            // Links (blue underlined)
            .replaceAll("!?\\[(.*?)]\\((.*?)\\)",
                "\u001B[34m$1\u001B[0m (\u001B[34m\u001B[4m$2\u001B[0m)");
    }
}
