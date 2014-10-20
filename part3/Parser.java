/* *** This file is given as part of the programming assignment. *** */

public class Parser {


    // tok is global to all these parsing methods;
    // scan just calls the scanner's scan method and saves the result in tok.
    private Token tok; // the current token
    private SymTable symTable; //Symbol Table Class for managine semantics. 
    private void scan() {
        tok = scanner.scan();
    }

    private Scan scanner;
    Parser(Scan scanner) {
        this.scanner = scanner;
        this.symTable = new SymTable();
        scan();
        program();
        if( tok.kind != TK.EOF )
            parse_error("junk after logical end of program");
    }

    private void program() {
        block();
    }

    private void block() {
        // you'll need to add some code here
        symTable.startOfBlock();
        if( is(TK.VAR) || is(TK.none) ) 
        {
            declarations();
        }
        statement_list();
        symTable.endOfBlock();
    }

    private void declarations() {
        mustbe(TK.VAR);
        while( is(TK.ID) ) {
            symTable.newDeclaration(tok);
            scan();
        }
        mustbe(TK.RAV);
    }

    // you'll need to add a bunch of methods here
    private void statement_list() {
        statement();
        while( is(TK.ID) || is(TK.ASSIGN) || is(TK.PRINT) || is(TK.IF) || is(TK.DO) || is(TK.FA) || is(TK.none) )
        {
            statement();
        }
    }

    private void statement() {
        if( is(TK.ID) || is(TK.ASSIGN) || is(TK.none) )
            assignment();
        else if( is(TK.PRINT) || is(TK.none) )
          print();
        else if( is(TK.IF) || is(TK.none) )
          pif();
        else if( is(TK.DO) || is(TK.none) )
          pdo();
        else if( is(TK.FA) || is(TK.none) )
          fa();
    }

    private void assignment() {
        if(is(TK.ID))
        {
            symTable.isDeclared(tok);
            scan();
        }
        else
            parse_error("assignment ERROR");
        if(is(TK.ASSIGN))
            scan();
        else
            parse_error("assignment ERROR");
        expression();
    }

    private void print() {
        mustbe(TK.PRINT);
        expression();
    }

    private void pif() {
        mustbe(TK.IF);
        guarded_commands();
        mustbe(TK.FI);
    }

    private void pdo() {
        mustbe(TK.DO);
        guarded_commands();
        mustbe(TK.OD);
    }

    private void fa() {
        mustbe(TK.FA);
        if(is(TK.ID))
        {
            symTable.isDeclared(tok);
            scan();
        }
        else 
            parse_error("fa ERROR");
        if(is(TK.ASSIGN))
            scan();
        else 
            parse_error("fa ERROR");
        expression();
        mustbe(TK.TO);
        expression();
        if(is(TK.ST))
        {
            mustbe(TK.ST);
            expression();
        }
        commands();
        mustbe(TK.AF);
    }

    private void guarded_commands() {
        guarded_command();
        while( is(TK.BOX) )
        {
            scan();
            guarded_command();
        }
        if( is(TK.ELSE) || is(TK.none) )
        {
            mustbe(TK.ELSE);
            commands();
        }
    }

    private void guarded_command() {
        expression();
        commands();
    }

    private void commands() {
        mustbe(TK.ARROW);
        block();
    }

    private void expression() {
        simple();
        if( is(TK.EQ) || is(TK.LT) || is(TK.GT) || is(TK.NE) || is(TK.LE) || is(TK.GE) || is(TK.none) )
        {
            relop();
            simple();
        }

    }

    private void simple() {
        term();
        while( is(TK.PLUS) || is(TK.MINUS) )
        {
            addop();
            term();
        }
    }

    private void term() {
        factor();
        while( is(TK.TIMES) || is(TK.DIVIDE) )
        {
            multop();
            factor();
        }
    }

    private void factor() {
        if(is(TK.ID))
        {
            symTable.isDeclared(tok);
            scan();
        }
        else if (is(TK.NUM))
          scan();
        else if(is(TK.LPAREN)) 
        {
            mustbe(TK.LPAREN);
            expression();
            mustbe(TK.RPAREN);
        }
        else
            parse_error("factor");
    }

    private void relop() {
        if(is(TK.EQ))
            scan();
        else if(is(TK.LT))
            scan();
        else if(is(TK.GT))
            scan();
        else if(is(TK.NE))
            scan();
        else if(is(TK.LE))
            scan();
        else if(is(TK.GE))
            scan();
        else
            parse_error("relop ERROR");
    }

    private void addop() {
        if(is(TK.PLUS))
            scan();
        else if(is(TK.MINUS))
            scan();
        else
            parse_error("addop ERROR");
    }

    private void multop() {
        if(is(TK.TIMES))
            scan();
        else if (is(TK.DIVIDE))
            scan();
        else
            parse_error("multop ERROR");
    }

    // is current token what we want?
    private boolean is(TK tk) {
        return tk == tok.kind;
    }

    // ensure current token is tk and skip over it.
    private void mustbe(TK tk) {
        if( ! is(tk) ) {
            System.err.println( "mustbe: want " + tk + ", got " +
                                    tok);
            parse_error( "missing token (mustbe)" );
        }
        scan();
    }

    private void parse_error(String msg) {
        System.err.println( "can't parse: line "
                            + tok.lineNumber + " " + msg );
        System.exit(1);
    }
}
