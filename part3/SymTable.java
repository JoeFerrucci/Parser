/*SymTable.java Joe Ferrucci*/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class SymTable {

    // Stack (Deque) of Lists to hold each <block>
    /* because Java Docs say a more complete and consistent set of LIFO stack operations is provided by the Deque interface*/
    private Deque<ArrayList<Token>> stack;

    SymTable() 
    {
        stack = new ArrayDeque<ArrayList<Token>>();
    }

    // Start of new block; push.
    public void startOfBlock() 
    {
        stack.push(new ArrayList<Token>());
    }

    // End of block; pop.
    public void endOfBlock() 
    {
        stack.pop();
    }

    /* A new variable declaration has been discovered in parsing */
    public void newDeclaration(Token tok) 
    {
        boolean alreadyDeclared = false; // redeclaring is false

        // Iterate through current block's stackframe (ArrayList)
        for(Token t : stack.peek()) 
        {
            if(t.string.equals(tok.string)) 
            {
                alreadyDeclared = true; // redeclaring is true
            }
        }

        // Print Error message If: alreadyDeclared
        // Else: add into block's symboltale (ArrayList)
        if (alreadyDeclared) 
        {
            System.err.println("variable " + tok.string + 
                " is redeclared on line " + tok.lineNumber);
        } 
        else 
        {
            // pop last ArrayList and add to it, 
            // then place it back on stack.
            stack.peek().add(tok);
        }
    }

    /* Called by isDeclared() hence, its private */
    private boolean isDeclaredImplementation(Token tok) 
    {
        // Iterate through entire stack of ArrayLists 
        for(ArrayList<Token> list : stack) 
        {
            // For each token inside stack-frame (ArrayList)
            for(Token t : list) 
            {
                if(tok.string.equals(t.string)) 
                {
                    return true;
                }
            }
        }

        return false;
    }

    /* Just an Interface Function for isDeclaredImplementation() */
    public void isDeclared(Token tok) 
    {
        if (isDeclaredImplementation(tok) == false) 
        {
            System.err.println("undeclared variable " + tok.string + 
                                " on line " + tok.lineNumber);
            System.exit(1);
        }
    }
}

