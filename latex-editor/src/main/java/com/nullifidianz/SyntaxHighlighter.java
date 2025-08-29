package com.nullifidianz;

import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyntaxHighlighter {
    private static final Pattern LATEX_PATTERN = Pattern.compile(
            "(?<COMMAND>\\\\([a-zA-Z]+|\\{|\\}|\\$|\\^|\\_|\\&|\\#|\\%|\\~))" +
                    "|(?<COMMENT>%.*)" +
                    "|(?<MATH>\\$[^$]*\\$)");

    public static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = LATEX_PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass = matcher.group("COMMAND") != null ? "command"
                    : matcher.group("COMMENT") != null ? "comment" : matcher.group("MATH") != null ? "math" : null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
}