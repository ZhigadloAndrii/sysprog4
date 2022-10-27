import java.util.regex.Pattern;

public class TokenPattern {
    final String name;
    final Pattern pattern;

    final public static TokenPattern keyWord = new TokenPattern("Key word", "");
    final public static TokenPattern unrecognizedLexeme = new TokenPattern("Unrecognized lexeme", "");

    TokenPattern(String aName, String aPatternString)
    {
        name = aName;
        pattern = Pattern.compile(aPatternString);
    }
}
