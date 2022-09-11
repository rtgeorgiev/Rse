package lang.rfun;

public final class RfunTokens {

    public static final String[][] DEFS = {
        //    {"OPERATOR", "\\*|/|\\+|-|==|<=|<"},
            // define your remaining Rfun tokens here

            {"MAIN", "main"},
            {"LBR", "\\("},
            {"RBR", "\\)"},
            {"LCBR", "\\{"},
            {"RCBR", "\\}"},
            {"SEMIC", ";"},
            {"FUN", "fun"},
            {"RETURN", "return"},
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
            {"ID", "_?+[a-zA-Z_$0-9]+"},
            {"EQUALS", "="},
            {"COMMA", ","},
    };
}
