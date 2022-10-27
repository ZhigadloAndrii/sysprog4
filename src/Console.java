public class Console {
    static public class TokenPrinter {
        static void print(Token token)  {
            System.out.print("< " + token.lexeme + " > - ");
            System.out.println("< " +token.pattern.name +" >");
        }
    }
}

