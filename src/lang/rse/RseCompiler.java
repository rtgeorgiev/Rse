package lang.rse;

import lang.ParseException;
import lang.rse.RseTokens;
import lex.Lexer;
import stackmachine.machine.SysCall;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

/*
The language is untyped.

Some remarks on the semantics:

    all variables are implicitly global

    global variables are implicitly initialised to 0

    if and do treat 0 as false and everything else as true

    the comparison operators evaluate to either 0 (false) or 1 (true)

    there are no logical operators (but they can be simulated with arithmetic)
 */

public class RseCompiler {

    private Lexer lex;
    private int freshNameCounter;
    private PrintStream out;
    private ArrayList<String> vars = new ArrayList<String>();

    public RseCompiler(PrintStream out) {
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
        program();
    }

    public void program() {
        //Program -> MAIN LCBR Statement* RCBR
    /*    switch (lex.tok().type) {
            case "MAIN":
                lex.next(); */
        eat("MAIN");
        eat("LCBR");
        while (this.lex.tok().type != "RCBR") {
            statement();
        }
        eat("RCBR");
        emit("halt");
        emit(".data");
        for (String s : vars) {
            emit(s + ": 0");
        }
        eof();
    }



    public void statement() {
        //System.out.println("Statement: " + lex.tok().type);
        switch (lex.tok().type) {
            //statement -> PRINTINT exp SEMIC
            case "PRINTINT":
                lex.next();
                exp();
                emit("push " + SysCall.OUT_DEC);
                emit("sysc");
                //eat("SEMIC");
                break;
            //statement -> PRINTCHAR exp SEMIC
            case "PRINTCHAR":
                lex.next();
                exp();
                emit("push " + SysCall.OUT_CHAR);
                emit("sysc");
                // eat("SEMIC");
                break;
            //statement -> IF LBR exp RBR THEN
            case "IF":
                lex.next();
                eat("LBR");
                exp();
              // lex.next();
               // statement();
                while (!lex.tok().type.equals("ELSE")) {
                   statement();
                }
                lex.next();
                while (!lex.tok().type.equals("ENDIF")) {
                   statement();
                }
              //  statement();
               lex.next();
                break;

            /*case "IF":
                lex.next();
                eat("LBR");
                exp();
                eat("RBR");
                statement();
                eat("ELSE");
                statement();
                break; */


      /*      case "DO":
                // Statement -> DO LCBR Statement* RCBR UNTIL LBR Exp RBR
                lex.next();
                eat("LCBR");
                while(this.lex.tok().type != "RCBR") {
                    statement();
                }
                eat("RCBR");
                eat("UNTIL");
                eat("LBR");
                exp();
             //   eat("RCBR"); */

            case "DO":
                lex.next();
                if (lex.tok().type == "LCBR") {
                    lex.next();
                    while (lex.tok().type != "RCBR"){
                        statement();
                    }
                    eat("RCBR");
                } else {
                    //   lex.next();
                    //      exp();
                    statement();
                }
                eat("UNTIL");
                eat("LBR");
                exp();
                //   lex.next();
                break;

            // statement -> ID EQUALS exp SEMIC
            case "ID":
                exp();
                break;

            case "LCBR":
                lex.next();
                while(this.lex.tok().type != "RCBR") {
                    statement();
                }
                lex.next();
                break;

            default:
                throw new ParseException(lex.tok(), "PRINTINT", "PRINTCHAR", "IF", "THEN", "ELSE", "WHILE", "DO", "ID");
        }
    }


    public void exp() {
        //System.out.println("Exp: " + lex.tok().type);
        BasicExp();
        ExpRest();
    }

    public void BasicExp() {

        String t;
        int i;

        //System.out.println("BasicExp: " + lex.tok().type);
        switch (lex.tok().type) {
            //BasicExp -> INT
            case "INT":
                i = Integer.parseInt(eat("INT"));
                emit("push " + i);
                break;
            //BasicExp -> ID
            case "ID":
                t = eat("ID");
                if (!vars.contains(t)){
                    vars.add(t);
                } else {
                    emit("push " + t);
                    emit("load");
                }
                break;
            //BasicExp -> LBR exp RBR
            case "LBR":
                eat("LBR");
                exp();
                break;
            //BasicExp -> EQ exp ExpRest
            case "EQ":
                eat("EQ");
                exp();
                break;
            //BasicExp -> LCBR exp RCBR

            default:
                throw new ParseException(lex.tok(), "INT", "ID", "LBR","EQ");
        }
    }

    public void ExpRest() {

        int i;

        //System.out.println("ExpRest: " + lex.tok().type);
        switch (lex.tok().type) {
            //ExpRest  -> ADD Exp
            case "ADD":
                eat("ADD");
                if (lex.tok().type.equals("INT")) {
                    i = Integer.parseInt(eat("INT"));
                    emit("push " + i);
                    emit("add");
                    ExpRest();
                }
                if (lex.tok().type.equals("ID")) {
                    String t = eat("ID");
                    emit("push " + t);
                    emit("load");
                    emit("add");
                    ExpRest();
                }
                break;
            //ExpRest  -> SUB Exp
            case "SUB":
                eat("SUB");
                if (lex.tok().type.equals("INT")) {
                    i = Integer.parseInt(eat("INT"));
                    emit("push " + i);
                    emit("sub");
                    ExpRest();
                }
                if (lex.tok().type.equals("ID")) {
                    String t = eat("ID");
                    emit("push " + t);
                    emit("load");
                    emit("sub");
                    ExpRest();
                }
                if (lex.tok().type.equals("LBR")) {
                    eat("LBR");
                    exp();
                }
                break;
            //ExpRest  -> MUL Exp
            case "MUL":
                eat("MUL");
                if (lex.tok().type.equals("INT")) {
                    i = Integer.parseInt(eat("INT"));
                    emit("push " + i);
                    emit("mul");
                    ExpRest();
                }
                if (lex.tok().type.equals("ID")) {
                    String t = eat("ID");
                    emit("push " + t);
                    emit("load");
                    emit("mul");
                    if (lex.tok().type.equals("RBR")) {
                        eat("RBR");
                    }
                    ExpRest();
                }
                break;
            //ExpRest  -> DIV Exp
            case "DIV":
                eat("DIV");
                if (lex.tok().type.equals("INT")) {
                    i = Integer.parseInt(eat("INT"));
                    emit("push " + i);
                    emit("div");
                    ExpRest();
                }
                if (lex.tok().type.equals("ID")) {
                    String t = eat("ID");
                    emit("push " + t);
                    emit("load");
                    emit("div");
                    ExpRest();
                }
                break;
            //ExpRest  -> RBR
            case "RBR":
                eat("RBR");
                break;

            case "EQUALS":
                eat("EQUALS");
                exp();
                emit("push "+vars.get(vars.size()-1));
                emit("store");
                break;
            //ExpRest  -> EQ exp
            case "EQ":
                eat("EQ");
                exp();
                break;
            //ExpRest  -> LEQ exp
            case "LEQ":
                eat("LEQ");
                exp();
                break;
            //ExpRest -> LT exp
            case "LT":
                eat("LT");
                exp();
                break;
            //ExpRest -> SEMIC
            case "SEMIC":
                eat("SEMIC");
                break;

            default:
                throw new ParseException(lex.tok(), "ADD", "SUB", "MUL", "DIV","RBR","EQUALS","EQ","LEQ","LT", "SEMIC");
        }
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

    private void eof() {
        if (lex.tok().type != "EOF") {
            throw new ParseException(lex.tok(), "EOF");
        }
    }

    public static void main(String[] args) throws IOException {
        String inFilePath = args[0];
        if (args.length > 1) {
            try (PrintStream out = new PrintStream(new FileOutputStream(args[1]))) {
                RseCompiler compiler = new RseCompiler(out);
                compiler.compile(inFilePath);
            }
        } else {
            RseCompiler compiler = new RseCompiler(System.out);
            compiler.compile(inFilePath);
        }
    }
}
