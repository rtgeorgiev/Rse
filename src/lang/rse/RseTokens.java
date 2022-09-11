package lang.rse;

public final class RseTokens {

    public static final String[][] DEFS = {
         //   {"OPERATOR", "\\*|/|\\+|-|==|<=|<"},
            // define your remaining Rse tokens here
            {"MAIN", "main"},
            {"LBR", "\\("},
            {"RBR", "\\)"},
            {"LCBR", "\\{"},
            {"RCBR", "\\}"},
            {"SEMIC", ";"},
            {"PRINTINT", "printint"},
            {"PRINTCHAR", "printchar"},
            {"IF", "if"},
            {"ELSE", "else"},
            {"INT", "-?[0-9]+"},
            {"DO", "do"},
            {"ADD", "\\+"},
            {"SUB", "\\-"},
            {"MUL", "\\*"},
            {"DIV", "\\/"},
            {"EQ", "=="},
            {"LT", "<"},
            {"LEQ", "<="},
            {"ENDIF", "endif"},
            {"THEN", "then"},
            {"UNTIL", "until"},
            {"EQUALS", "="},
            {"ID", "_?+[a-zA-Z_$0-9]+"},
            {"COMMA", ","},
    };
}
