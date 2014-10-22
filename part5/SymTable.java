/*SymTable.java Joe Ferrucci*/

import java.util.*;

public class SymTable {

    // Stack (Deque) of Lists to hold each <block>
    /* because Java Docs say a more complete and consistent set of LIFO stack operations is provided by the Deque interface*/
    Stack<LinkedList<Token>> block;
    Vector<Variable> listOfDeclarations;
    Vector<String> findIndexByName;
    Vector<LinkedList<Token>> listOfAssignedToVars;
    Vector<LinkedList<Token>> listOfUsedVars;


    SymTable() 
    {
        block = new Stack<LinkedList<Token>>();
        listOfDeclarations = new Vector<Variable>();
        findIndexByName = new Vector<String>();
        listOfAssignedToVars = new Vector<LinkedList<Token>>();
        listOfUsedVars = new Vector<LinkedList<Token>>();
    }


    /* Prints The Table for Part 4 */
    public void outputTable()
    {
    // System.err.println("\nCurrent Var Table Size = " + varTable.size() + "\n");
    // for (Variable var : varTable)
    // {
    //     /* Var Name: */
    //     String varName = var.token.string;
    //     System.err.println(varName + " ");

    //     /* Declared On: */
    //     String declaredOnAtNestingDepth = " declared on line " + var.token.lineNumber + " at nesting depth " + var.token.nestingDepth;
    //     System.err.println(declaredOnAtNestingDepth);

    //     /* Assigned To On: */
    //     String assignedToOnString = " assigned to on:";
    //     System.err.println(assignedToOnString);

    //     /* Used On: */
    //     String usedOnString = " used on:";
    //     for(Occurrence occ : var.usages)
    //     {
    //         usedOnString += (" " + occ.lineNumber);
    //     }
    //     System.err.println(usedOnString);
    // }
        for(int i = 0; i < findIndexByName.size(); i++)
        {
            System.err.println(findIndexByName.get(i));
            System.err.println("  declared on line " + listOfDeclarations.get(i).lineNumber + " at nesting depth " + (listOfDeclarations.get(i).nestDepth - 1));
            ListIterator<Token> itr = listOfAssignedToVars.get(i).listIterator();
            Token test;
            Token test2;
            Token compare;
            int count = 1;
            if(itr.hasNext())
            {
                System.err.print("  assigned to on:");
                test2 = test = itr.next();
                while(itr.hasNext())
                {
                    System.err.print(" " + test.lineNumber);
                    compare = itr.next();
                    count = 1;
                    while(compare.lineNumber == test.lineNumber)
                    {
                        count++;
                        if(itr.hasNext())
                        {
                            compare = itr.next();
                        }
                        else
                        {
                            break;
                        }
                    }

                    if(count > 1)
                    {
                        System.err.print("(" + count + ")");
                    }
                    test2 = test;
                    test = compare;
                }

                if(count > 1 && test2.lineNumber == test.lineNumber)
                {
                    System.err.println("");
                }
                else
                {
                    System.err.println(" " + test.lineNumber);
                }

                }
            else
            {
                System.err.println("  never assigned");
            }

            ListIterator<Token> itr2 = listOfUsedVars.get(i).listIterator();
            if(itr2.hasNext())
            {
                System.err.print("  used on:");
                test = test2 = itr2.next();
                count = 1;

                while(itr2.hasNext())
                {
                    System.err.print(" " + test2.lineNumber);
                    compare = itr2.next();
                    count = 1;

                    while(compare.lineNumber == test2.lineNumber)
                    {
                        count++;
                        if(itr2.hasNext())
                        {
                            compare = itr2.next();
                        }
                        else
                        {
                            break;
                        }
                    }

                    if(count > 1)
                    {
                        System.err.print("(" + count + ")");
                    }

                    test = test2;
                    test2 = compare;
                }

                if(count > 1 && test2.lineNumber == test.lineNumber)
                {
                    System.err.println("");
                }
                else
                {
                    System.err.println(" " + test2.lineNumber);
                }
            }
            else
            {
                System.err.println("  never used");
            }
          }
    }


    // Start of new block; push.
    public void startOfBlock() {
        block.push(new LinkedList<Token>());
    }

    // End of block; pop.
    public void endOfBlock() {
        block.pop();
    }

    /* A new variable declaration has been discovered in parsing */
    public int newDeclaration(Token tok) {
        int alreadyDeclared = 0; // redeclaring is false
        int index = 0;
        Token test;
        // Iterate through current block's stackframe (ArrayList)
        // Searches all Tokens in current block. 
        LinkedList<Token> ptr = block.peek();
        ListIterator<Token> itr = ptr.listIterator();
        while(itr.hasNext())
        {
            index = itr.nextIndex();
            test = itr.next();
            // If alreadyDeclared: Print Error message 
            // Else: add into current block's symbol table (an ArrayList)
            if(test.string.equals(tok.string))
            {
                System.err.println("variable " +tok.string+ " is redeclared on line " +tok.lineNumber);
                alreadyDeclared = 1; // redeclaring is true
                break;
            }
        }

        // New variable declared for first time!!
        // pop last ArrayList, then add(tok) to it, 
        // then the language auto places it back on stack.
        if( alreadyDeclared == 0)
        {
            ptr.add(tok);
            findIndexByName.add(tok.string);
            listOfAssignedToVars.add(new LinkedList<Token>());
            listOfUsedVars.add(new LinkedList<Token>());
            listOfDeclarations.add(new Variable(tok.lineNumber, block.size()));
        }
        return alreadyDeclared;
    }

    /* isDeclared() Function */
    public int isDeclared(Token tok) {
        Stack<LinkedList<Token>> tempblock = new Stack<LinkedList<Token>>();
        int alreadyDeclared = 0;
        int index = 0;
        Token test;
        int blockcheck;

        // These loops combined will: SEARCH THE ENTIRE STACK
        // Iterate through entire stack of ArrayLists
        while(!block.empty())
        {
            blockcheck = block.size();
            LinkedList<Token> ptr = block.peek();
            ListIterator<Token> itr = ptr.listIterator();
            while(itr.hasNext())
            {
                index = itr.nextIndex();
                test = itr.next();
                // If declared already:
                if(test.string.equals(tok.string))
                {
                    alreadyDeclared = 1; // yes, it's been declared before.
                    int indexptr = findIndexByName.lastIndexOf(tok.string);

                    while(true) 
                    {
                        if(indexptr < 0)
                        {
                            break;
                        }

                        if(blockcheck >= listOfDeclarations.get(indexptr).nestDepth)
                        {
                            listOfAssignedToVars.get(indexptr).add(tok);
                            break;
                        }
                        else
                        {
                            indexptr = findIndexByName.lastIndexOf(tok.string, indexptr - 1);
                        }
                    }
                    break;
                }
            }

            if(alreadyDeclared == 1)
            {
                break;
            }
            tempblock.push(block.pop());
        }
        // it has not been declared yet, therefore say undeclared.
        if( alreadyDeclared == 0)
        {
            System.err.println("undeclared variable " + tok.string + " on line " + tok.lineNumber);
            System.exit(1);
        }
        while(!tempblock.empty())
        {
            block.push(tempblock.pop());
        }
        return index;
    }

    /* isUsed() Function */
    public int isUsed(Token tok) 
    {
        Stack<LinkedList<Token>> tempblock = new Stack<LinkedList<Token>>();
        int alreadyDeclared = 0;
        int index = 0;
        Token test;
        int blockcheck;

        // These loops combined will: SEARCH THE ENTIRE STACK
        // Iterate through entire stack of ArrayLists
        while(!block.empty())
        {
            blockcheck = block.size();
            LinkedList<Token> ptr = block.peek();
            ListIterator<Token> itr = ptr.listIterator();
            while(itr.hasNext())
            {
                index = itr.nextIndex();
                test = itr.next();
                // If being used again:
                if(test.string.equals(tok.string))
                {
                    int indexptr = findIndexByName.lastIndexOf(tok.string);
                    while(true) 
                    {
                        if(indexptr < 0)
                        {
                            break;
                        }
                        if(blockcheck >= listOfDeclarations.get(indexptr).nestDepth)
                        {
                            listOfUsedVars.get(indexptr).add(tok);
                            break;
                        }
                        else
                        {
                            indexptr = findIndexByName.lastIndexOf(tok.string, indexptr - 1);
                        }
                    }
                    alreadyDeclared = 1;
                    break;
                }
            }

            if(alreadyDeclared == 1)
            {
                break;
            }
            tempblock.push(block.pop());
        }

        // it has not been declared yet, therefore say undeclared.
        if( alreadyDeclared == 0)
        {
            System.err.println("undeclared variable " + tok.string + " on line " + tok.lineNumber);
            System.exit(1);
        }
        while(!tempblock.empty())
        {
            block.push(tempblock.pop());
        }
        return index; // for use back in Parser.java
    }

}//eoc