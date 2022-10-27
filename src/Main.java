import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;

public class Main {
    private static RegexTokenizer readRegexTokenizer(Path filePath) throws FileNotFoundException, JSONException
    {
        final RegexTokenizer tokenizer;
        FileReader fr = new FileReader(filePath.toString());
        JSONTokener tokener = new JSONTokener(fr);
        JSONObject root = new JSONObject(tokener);

        final var keyWords = new HashSet<String>();
        ((JSONArray)root.get("key_words")).forEach(ob -> keyWords.add((String)ob) );

        tokenizer = new RegexTokenizer(keyWords);

        ((JSONArray)root.get("lexemes")).forEach(ob -> {
            final var lexemeInfo = (JSONObject)ob;
            final String name = lexemeInfo.getString("name");
            final String regex = lexemeInfo.getString("regex");
            tokenizer.addPattern(new TokenPattern(name, regex));
        });

        return tokenizer;
    }

    public static void main(String[] args) throws FileNotFoundException
    {
        PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
        System.setOut(out);

        final Path lexemesPath;
        final Path filePath;
        try {
            lexemesPath = FileSystems.getDefault().getPath("lexemes.json").toAbsolutePath();
            filePath = FileSystems.getDefault().getPath("test.c").toAbsolutePath();
        } catch (IllegalArgumentException ex) {
            System.err.println(ex.getLocalizedMessage());
            System.exit(1);
            return;
        }

        try {
            final RegexTokenizer tokenizer = readRegexTokenizer(lexemesPath);
            final String code = Files.readString(Paths.get(filePath.toString()));
            for (var token : tokenizer.getTokens(code)) {
                Console.TokenPrinter.print(token);
            }
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
        }
    }
}
