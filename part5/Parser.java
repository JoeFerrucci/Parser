/* *** This file is given as part of the programming assignment. *** */

public class Parser {




    // tok is global to all these parsing methods;
    // scan just calls the scanner's scan method and saves the result in tok.
    private Token tok; // the current token
    private SymTable symTable = new SymTable();
    private void scan() {
        tok = scanner.scan();
    }

    private Scan scanner;
    Parser(Scan scanner) {
        this.scanner = scanner;
        scan();
        program();
        if( tok.kind != TK.EOF )
            parse_error("junk after logical end of program");
        symTable.outputTable();
    }

    private void program() {
        System.out.println("#include <stdio.h>");
        System.out.println("int main(){");
        block();
        System.out.println("}");
    }

    private void block() {
        // you'll need to add some code here
        symTable.startOfBlock();
        if(is(TK.VAR) || is(TK.none))
        {
          declarations();
        }
        statement_list();

    }

    private void declarations() {
        int reDeclared = 0;
        mustbe(TK.VAR);
        while( is(TK.ID) ) {
            reDeclared = symTable.newDeclaration(tok);
            if(reDeclared == 0)
            {
              System.out.println("int x_" + tok.string + " = -12345;");
            }
            scan();
        }
        mustbe(TK.RAV);
    }

    private void statement_list() {
        while(is(TK.ID)||is(TK.ASSIGN)||is(TK.PRINT)||is(TK.IF)||is(TK.DO)||is(TK.FA)||is(TK.none))
        {
          statement();
        }
        symTable.endOfBlock();
    }

    private void statement() {
      if(is(TK.ID)||is(TK.ASSIGN)||is(TK.none))
        assignment();
      else if(is(TK.PRINT)||is(TK.none))
        print();
      else if(is(TK.IF)||is(TK.none))
        pif();
      else if(is(TK.DO)||is(TK.none))
        pdo();
      else if(is(TK.FA)||is(TK.none))
        fa();
      System.out.println(";");
    }

    private void assignment() {
      if(is(TK.ID))
      {
        symTable.isDeclared(tok);
        System.out.print("x_" + tok.string);
        scan();
      }
      else parse_error("assignment ERROR");

      if(is(TK.ASSIGN))
      {
        System.out.print(" = ");
        scan();
      }
      else parse_error("assignment ERROR");
      expression();
    }

    private void print() {
      mustbe(TK.PRINT);
      System.out.print("printf(\"%d\\n\", ");
      expression();
      System.out.println(")");
    }

    private void pif() {
      mustbe(TK.IF);
      System.out.print("if");
      guarded_commands();
      mustbe(TK.FI);
    }

    private void pdo() {
      int tempDo = 1;
      mustbe(TK.DO);
      System.out.print("while(1){ if");
      guarded_commands();
      System.out.print("else break;}");
      mustbe(TK.OD);
      tempDo = 0;
    }

    private void fa() {
      Token tempTok = tok; // hang on to the current token!
      int tempSuchThat = 0;
      int tempFa = 0;
      
      mustbe(TK.FA);
      if(is(TK.ID))
      {
        symTable.isDeclared(tok);
        System.out.print("for(x_"+tok.string);
        tempTok = tok;
        scan();
      }
      else parse_error("fa ERROR");

      if (is(TK.ASSIGN))
      {
        System.out.print("=");
        scan();
      }
      else parse_error("fa ERROR");

      expression();
      mustbe(TK.TO);
      System.out.print(";x_" + tempTok.string);
      System.out.print("<=");
      expression();
      System.out.print(";");
      System.out.print("x_" + tempTok.string);
      System.out.print("++");
      System.out.print(")");

      if(is(TK.ST))
      {
        System.out.print("{ if(!(");
        mustbe(TK.ST);
        expression();
        System.out.print(")) continue;");
        tempSuchThat = 1;
      }
      commands();
      if(tempSuchThat == 1)
      {
        System.out.print("}");
        tempSuchThat = 0;
      }
      mustbe(TK.AF);
    }

    private void guarded_commands() {
      guarded_command();
      while(is(TK.BOX))
      {
        scan();
        System.out.print("else if");
        guarded_command();
      }
      if(is(TK.ELSE)||is(TK.none)){
        mustbe(TK.ELSE);
        System.out.print("else");
        commands();
      }
    }
    
    private void guarded_command() {
      System.out.print("(");
      expression();
      System.out.print(")");
      commands();
    }

    private void commands() {
      mustbe(TK.ARROW);
      System.out.println("{");
      block();
      System.out.println("}");
    }

    private void expression() {
      simple();
      if(is(TK.EQ)||is(TK.LT)||is(TK.GT)||is(TK.NE)||is(TK.LE)||is(TK.GE)||is(TK.none))
      {
        relop();
        simple();
      }
    }

    private void simple() {
      term();
      while(is(TK.PLUS)||is(TK.MINUS))
      {
        addop();
        term();
      }
    }

    private void term() {
      factor();
      while(is(TK.TIMES)||is(TK.DIVIDE))
      {
        multop();
        factor();
      }
    }
  
    private void factor() {
      if(is(TK.ID))
      {
        System.out.print(" x_" + tok.string + " ");
        symTable.isUsed(tok);
        scan();
      }
      else if(is(TK.NUM))
      {
        System.out.print(" " + tok.string + " ");
        scan();
      }
      else if(is(TK.LPAREN))
      {
        mustbe(TK.LPAREN);
        System.out.print("(");
        expression();
        mustbe(TK.RPAREN);
        System.out.print(")");
      }
      else
        parse_error("factor");
    }

    private void relop() {
      if(is(TK.EQ))
      {
        System.out.print("==");
        scan();
      }
      else if(is(TK.LT))
      {
        System.out.print("<");
        scan();
      }
      else if(is(TK.GT))
      {
        System.out.print(">");
        scan();
      }
      else if(is(TK.NE))
      {
        System.out.print("!=");
        scan();
      }
      else if(is(TK.LE))
      {
        System.out.print("<=");
        scan();
      }
      else if(is(TK.GE))
      {
        System.out.print(">=");
        scan();
      }
      else
        parse_error("relop ERROR");
    }

    private void addop() {
      if(is(TK.PLUS))
      {
        System.out.print("+");
        scan();
      }
      else if(is(TK.MINUS))
      {
        System.out.print("-");
        scan();
      }
      else
        parse_error("addop ERROR");
    }

    private void multop() {
      if(is(TK.TIMES))
      {
        System.out.print("*");
        scan();
      }
      else if(is(TK.DIVIDE))
      {
        System.out.print("/");
        scan();
      }
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
