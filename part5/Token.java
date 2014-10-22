public class Token {
    public TK kind;
    public String string;
    public int lineNumber;
    public int nestingDepth; // Added by Joe Ferrucci for Part 4
    public Token(TK kind, String string, int lineNumber) {
        this.kind = kind;
        this.string = string;
        this.lineNumber = lineNumber;
        this.nestingDepth = 0; // Added by Joe Ferrucci for Part 4
    }
    public String toString() { // make it printable for debugging
        return "Token("+kind.toString()+" "+string+" "+lineNumber+")";
    }
}
