import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class Variable {
    public Token token;
    public Deque<Occurrence> assignments;
    public Deque<Occurrence> usages;

    Variable(Token tok) {
        this.token = tok;
        this.assignments = new ArrayDeque<Occurrence>();
        this.usages = new ArrayDeque<Occurrence>();
    }



    // public String toString() { // make it printable for output
    //     /* Variable name */
    //     String varName = token.string;

    //     /* Declared on: */
    //     String declaredOnAtNestingDepth = "\n  declared on line " + token.lineNumber + " at nesting depth " + token.nestingDepth;

    //     /* Assigned on: */
    //     String assignedToOn = "\n  assigned to on:";
    //     for (Occurrence ass : assignments)
    //     {
    //         assignedToOn += " " + ass.lineNumber;
    //         if (ass.lineCount >= 2) 
    //             assignedToOn += "(" + ass.lineCount + ")";
    //     }

    //     /* Used on: */
    //     String usedOn = "\n  used on:";
    //     for (Occurrence used : usages)
    //     {
    //         usedOn += " " + used.lineNumber;
    //         if (used.lineCount >= 2) 
    //             usedOn += "(" + used.lineCount + ")";
    //     }

    //     return varName + declaredOnAtNestingDepth + assignedToOn + usedOn;
    // }
}
