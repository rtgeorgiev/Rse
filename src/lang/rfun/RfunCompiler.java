package lang.rfun;

import lang.ParseException;
import lang.rse.RseTokens;
import lex.Lexer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/*
The language is untyped.

Some remarks on the semantics:

    all variables are implicitly global

    global variables are implicitly initialised to 0

    if and do treat 0 as false and everything else as true

    the comparison operators evaluate to either 0 (false) or 1 (true)

    there are no logical operators (but they can be simulated with arithmetic)

    behaviour of calling a function with the wrong number of parameters is undefined (of course, this should be caught by static analysis, but the crude structure of this compiler does not facilitate that)

    if a function execution reaches the end of the function body without executing a return statement, the function will return normally, with a return value of 0

    return from within the main { ... } block exits the program (the return value is ignored in this case)
 */

public class RfunCompiler {

    private Lexer lex;
    private int freshNameCounter;
    private PrintStream out;

    public RfunCompiler(PrintStream out) {
        this.out = out;
    }

    private String freshName(String prefix) {
        return "$" + prefix + "_" + (freshNameCounter++);
    }

    private void emit(String s) {
        out.println(s);
    }

    public void compile(String filePath) throws IOException {
        freshNameCounter = 0;
        lex = new Lexer(RseTokens.DEFS);
        lex.readFile(filePath);
        lex.next();
        // to be completed
    }

    /**
     * Check the head token and, if it matches, advance to the next token.
     * @param type the token type that we expect
     * @return the text of the head token that was matched
     * @throws ParseException if the head token does not match.
     */
    public String eat(String type) {
        if (type.equals(lex.tok().type)) {
            String image = lex.tok().image;
            lex.next();
            return image;
        } else {
            throw new ParseException(lex.tok(), type);
        }
    }

    public static void main(String[] args) throws IOException {
        String inFilePath = args[0];
        if (args.length > 1) {
            try (PrintStream out = new PrintStream(new FileOutputStream(args[1]))) {
                RfunCompiler compiler = new RfunCompiler(out);
                compiler.compile(inFilePath);
            }
        } else {
            RfunCompiler compiler = new RfunCompiler(System.out);
            compiler.compile(inFilePath);
        }
    }
}
