/*SymTable.java Joe Ferrucci*/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.ListIterator;

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
        // Searches all Tokens in current block.
        // ArrayList<Token> currBlockList = stack.peek();
        // ListIterator<Token> iter = currBlockList.listIterator(); 
        // while(iter.hasNext())
        // {
        //     if(iter.next().string.equals(tok.string))
        //     {
        //         alreadyDeclared = true; // redeclaring is true
        //     }
        // }
        
        for(Token t : stack.peek())
        {
            if(t.string.equals(tok.string))
            {
                alreadyDeclared = true; // redeclaring is true
            }
        }
        
        // If alreadyDeclared: Print Error message
        // Else: add into current block's symbol table (an ArrayList)
        if (alreadyDeclared)
        {
            System.err.println("variable " + tok.string +
                               " is redeclared on line " + tok.lineNumber);
        }
        else
        {
            // pop last ArrayList, then add(tok) to it,
            // then the language auto places it back on stack.
            stack.peek().add(tok);
        }
    }
    
    /* isDeclared() Function */
    public void isDeclared(Token tok) 
    {
        boolean isDeclared = false;
        // These 2 for-loops combined will: SEARCH THE ENTIRE STACK
        // Iterate through entire stack of ArrayLists 
        for(ArrayList<Token> list : stack) 
        {
            // For each token inside stack-frame (ArrayList)
            for(Token t : list) 
            {
                // If declared already:
                if(tok.string.equals(t.string))
                {
                    // Added for Part 4
                    // End of Part 4
                    isDeclared = true; // yes, it's been declared before.
                }
            }
        }


        // it has not been declared, therefore say undeclared.
        if (isDeclared == false) 
        {
            System.err.println("undeclared variable " + tok.string + 
                                " on line " + tok.lineNumber);
            System.exit(1);
        } 
        // it HAS BEEN declared, therefore update assignments/usages.
        else 
        {
            // start here.
        }
    }
}

