import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTokenizer {
    private final Set<String> keyWords;
    private final List<TokenPattern> patterns;
    private final StringBuffer mainRegexStr;

    {
        patterns = new ArrayList<>();
        mainRegexStr = new StringBuffer();
    }

    RegexTokenizer(Set<String> aKeyWords)
    {
        keyWords = aKeyWords;
    }

    public void addPattern(TokenPattern aPattern)
    {
        final String patterStr = aPattern.pattern.pattern();
        mainRegexStr.append(String.format(mainRegexStr.isEmpty() ? "(%s)" : "|(%s)", patterStr));
        patterns.add(aPattern);
    }

    public List<Token> getTokens(String code)
    {
        final List<Token> tokens = new ArrayList<>();
        final Pattern pattern = Pattern.compile(mainRegexStr.toString());
        final Matcher matcher = pattern.matcher(code);
        int curPos = 0;
        while (matcher.find(curPos)) {
            final String lexeme = code.substring(matcher.start(), matcher.end());
            final TokenPattern tokenPattern = keyWords.contains(lexeme) ? TokenPattern.keyWord : pattern(lexeme);
            tokens.add(new Token(lexeme, tokenPattern, forwardInput(curPos, matcher, code)));
            curPos = matcher.end();
        }
        if (curPos < code.length()) {
            tokens.add(new Token(code.substring(curPos), TokenPattern.unrecognizedLexeme, ""));
        }
        return tokens;
    }

    private TokenPattern pattern(String lexeme) {
        return patterns.stream().filter(tp -> tp.pattern.matcher(lexeme).find()).findFirst().get();
    }

    private static String forwardInput(int curPos, Matcher matcher, String code)
    {
        if (curPos != matcher.start()) {
            return code.substring(curPos, matcher.start());
        }
        return "";
    }

}
