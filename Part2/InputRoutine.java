/* 
    6341 Project part 2
    InputRoutine.java
    Author: Yi Hsiao
*/

import java.util.*;

class InputRoutine{
    public static StringTokenizer st;
    public static String token;
    public static int nextToken;
    
    public static SExp T = new SExp( "T" );
    public static SExp NIL = new SExp( "NIL" );
	
    static int ckNextToken(){
        int tokenCode = 0;

        if( st.hasMoreTokens() ){
            token = st.nextToken();
            
            if( token.equals("(") )
                tokenCode = 1; // (
            else if( token.equals(")") )
                tokenCode = 2; // )
            else if( token.equals(".") )
                tokenCode = 3; // .
            else if( token.matches("[+-]?\\d+") )
                tokenCode = 4; // integer
            else if( token.matches("[a-zA-Z]+\\d*") )
                tokenCode = 5; // symbolic
            else if( token.equals(" ") )
                tokenCode = 6; // space
            else
                tokenCode = -1; // invalid atom name, cannot mix char and digit
        }
        else{
            token = "end of token";
        }

        return tokenCode;
    }
	
	static SExp input1(){
        nextToken = ckNextToken(); // move to next token
        SExp s1;
        SExp s2;
        SExp output = new SExp();

        if( nextToken != 2 && nextToken != 3 ){ 
            switch( nextToken ){
                case 1:
                    s1 = input1();
                    output = s1;
                    if( nextToken == 3 ){
                        s2 = input1();
                        output = new SExp( s1, s2 );
                        if( nextToken == 2 ) // ')'
                            nextToken = ckNextToken(); // move to next token
                        else if( nextToken != 2 ){ // expect '(' s-exp ')'
                            LispInterpreter.errFlag = true;
                            System.out.println("** error: missing right parentheses **");
                        }
                    }
                    else if( nextToken == 6 ){ // ' ' -> list exp
                        s2 = input2(); // ')' = Atom("NIL")
                        output = new SExp( s1, s2 );
                        nextToken = ckNextToken(); // move to next token
                    }
                    else if( nextToken != 2 && !token.equals("end of token") ){ // only allow '.' or ' ' between two s-exp
                        LispInterpreter.errFlag = true;
                        System.out.println("** error: missing right parentheses **");
                    }
                    break;
                
                case 4:
                    output = new SExp( Integer.parseInt(token) ); // create int atom
                    nextToken = ckNextToken(); // move to next token
                    break;

                case 5:
                    output = new SExp( token ); // create symbolic atom
                    nextToken = ckNextToken(); // move to next token
                    break;

                case -1: // invalid atom name
                    LispInterpreter.errFlag = true;
                    System.out.printf("** error: invalid atom name \"%s\" **\n", token);
                    nextToken = ckNextToken();
                    break;
            }
        }
        else{
            if( nextToken == 2 ){
                output = NIL;
                nextToken = ckNextToken();
            }
            else{
                LispInterpreter.errFlag = true;
                System.out.println("** error: missing left parentheses **");
            }
        }

        return output;
    }

    static SExp input2(){
        SExp s1;
        SExp s2;
        SExp output = new SExp();

        if( nextToken == 0 ){ // no more token but the routine is still running
            LispInterpreter.errFlag = true;
            System.out.println("** error: missing right parenthesis **");
        }
        else if( nextToken == 2 )
            output = NIL; // return Atom("NIL")
        else if( nextToken == 6 ){
            s1 = input1();
            s2 = input2();
            output = new SExp( s1, s2 );
        }
        else{
            LispInterpreter.errFlag = true;
            System.out.println("** error: unexpected symbol **");
        }

        return output;
    }
}
