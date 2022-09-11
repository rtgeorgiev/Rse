package lang.rse;

import lang.ParseException;
import lang.rse.RseTokens;
import lex.Lexer;

import java.io.IOException;
import java.sql.Statement;

public class RseParser {

    private Lexer lex;

    public RseParser() {
    }

    public void parse(String filePath) throws IOException {
        lex = new Lexer(RseTokens.DEFS);
        lex.readFile(filePath);
        lex.next();
        // call your start-symbol parsing method here
        program();
        System.out.println("Parse succeeded.");
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
        }



    public void statement() {
        //System.out.println("Statement: " + lex.tok().type);
        switch (lex.tok().type) {
            //statement -> PRINTINT exp SEMIC
            case "PRINTINT":
                lex.next();
                exp();
                break;
            //statement -> PRINTCHAR exp SEMIC
            case "PRINTCHAR":
                lex.next();
                exp();
                break;
            //statement -> IF LBR exp RBR THEN
            case "IF":
                lex.next();
                eat("LBR");
                exp();
            //    lex.next();
                statement();
                eat("ELSE");
                statement();
                break;

            //statement -> DO LCBR Statement* RCBR UNTIL LBR Exp RBR
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
        //System.out.println("BasicExp: " + lex.tok().type);
        switch (lex.tok().type) {
            //BasicExp -> INT
            case "INT":
                eat("INT");
                break;
            //BasicExp -> ID
            case "ID":
                eat("ID");
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

            default:
                throw new ParseException(lex.tok(), "INT", "ID", "LBR","EQ");
        }
    }

    public void ExpRest() {
        //System.out.println("ExpRest: " + lex.tok().type);
        switch (lex.tok().type) {
            //ExpRest  -> ADD Exp
            case "ADD":
                eat("ADD");
                exp();
                break;
            //ExpRest  -> SUB Exp
            case "SUB":
                eat("SUB");
                exp();
                break;
            //ExpRest  -> MUL Exp
            case "MUL":
                eat("MUL");
                BasicExp();
                ExpRest();
                break;
            //ExpRest  -> DIV Exp
            case "DIV":
                eat("DIV");
                BasicExp();
                ExpRest();
                break;
            //ExpRest  -> RBR
            case "RBR":
                eat("RBR");
                break;
            case "EQUALS":
                eat("EQUALS");
                exp();
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

            //ExpRest -> RBR

            default:
                throw new ParseException(lex.tok(), "ADD", "SUB", "MUL", "DIV","RBR","EQUALS","EQ","LEQ","LT", "SEMIC");
        }
    }

    private void eof() {
        if (lex.tok().type != "EOF") {
            throw new ParseException(lex.tok(), "EOF");
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

    public static void main(String[] args) throws IOException {
        RseParser parser = new RseParser();
        parser.parse(args[0]);
    }
}
