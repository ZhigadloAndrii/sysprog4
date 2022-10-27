public class Token {
    public final String lexeme;
    public final TokenPattern pattern;
    public final String forwardInput;

    Token(String aLexeme, TokenPattern aPatter, String aForwardInput)
    {
        lexeme = aLexeme;
        pattern = aPatter;
        forwardInput = aForwardInput;
    }
}
