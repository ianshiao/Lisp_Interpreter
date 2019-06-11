import java.util.*;

public class lisp{
   
    static class SExp{
        int IntAtom;
        String SymAtom;
        SExp left;
        SExp right;
        boolean isIntAtom;
        boolean isSymAtom;

        SExp(){
            isIntAtom = false;
            isSymAtom = false;
        }

        SExp( int i ){
            IntAtom = i;
            left = null;
            right = null;
            isIntAtom = true;
            isSymAtom = false;
        }
        
        SExp( String s ){
            SymAtom = s;
            left = null;
            right = null;
            isIntAtom = false;
            isSymAtom = true;
        }
        
        SExp( SExp l, SExp r ){ // CONS
            left = l;
            right = r;
            isIntAtom = false;
            isSymAtom = false;
        }
    }
    
    static StringTokenizer st;
    static String token;
    static int nextToken;
    static boolean errFlag;

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
            else if( token.chars().allMatch( Character::isDigit ) )
                tokenCode = 4; // integer
            else if( token.chars().allMatch( Character::isLetter ) )
                tokenCode = 5; // symbolic
            else if( token.equals(" ") )
                tokenCode = 6; // space
            else
                tokenCode = -1; // invalid atom name, cannot mix char and digit
        }

        return tokenCode;
    }

    static SExp inputRoutine(){
        nextToken = ckNextToken(); // move to next token
        SExp s1;
        SExp s2;
        SExp output = new SExp();

        if( nextToken != 2 && nextToken != 3 ){ 
            switch( nextToken ){
                case 1:
                    s1 = inputRoutine();
                    output = s1;
                    if( nextToken == 3 ){
                        s2 = inputRoutine();
                        output = new SExp( s1, s2 );
                        if( nextToken == 2 ) // ')'
                            nextToken = ckNextToken(); // move to next token
                        else if( nextToken != 2 ){ // expect '(' s-exp ')'
                            errFlag = true;
                        }
                    }
                    else if( nextToken == 6 ){ // ' ' -> list exp
                        s2 = inputRoutine2(); // ')' = Atom("NIL")
                        output = new SExp( s1, s2 );
                        nextToken = ckNextToken(); // move to next token
                    }
                    else if( nextToken != 2 ){ // only allow '.' or ' ' between two s-exp
                        errFlag = true;
                        System.out.printf("** error: unexpected symbol \"%s\" **\n", token);
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
                    errFlag = true;
                    System.out.printf("** error: invalid atom name \"%s\" **\n", token);
                    nextToken = ckNextToken();
                    break;
            }
        }
        else{
            errFlag = true;
            System.out.printf("** error: missing left parenthesis, unexpected symbol \"%s\" **\n", token);
        }

        return output;
    }

    static SExp inputRoutine2(){
        SExp s1;
        SExp s2;
        SExp output = new SExp();

        if( nextToken == 0 ){ // no more token but the routine is still running
            errFlag = true;
            System.out.println("** error: missing right parenthesis **");
        }
        else if( nextToken == 2 )
            output = new SExp( "NIL" ); // create Atom("NIL")
        else{
            s1 = inputRoutine();
            s2 = inputRoutine2();
            output = new SExp( s1, s2 );
        }

        return output;
    }

    static void print( SExp output ){
        if( output.isIntAtom )
            System.out.print( output.IntAtom );
        else if( output.isSymAtom )
            System.out.print( output.SymAtom );
        else{
            System.out.print( "(" );
            print( output.left );
            System.out.print( " . " );
            print( output.right );
            System.out.print( ")" );
        }
    }

    static String stringTrim( String str ){
        str = str.replaceAll( " +", " " ); // replce all multiple spaces to single space
        str = str.replaceAll( "\\( ", "\\(" ); // "( 1" --> "(1"
        str = str.replaceAll( " \\. | \\.", "\\." ); // "(1 . 2)" --> "(1.2)"
        str = str.replaceAll( " \\)", "\\)" ); // "1 )" --> "1)"
        return str;
    }

    public static void main( String args[] ){
        String L = new String();
        String readLine = new String();

        while(true){
            L += readLine; // combine each input line
            Scanner s = new Scanner( System.in );
            readLine = s.nextLine();
            
            if( readLine.equals("$") || readLine.equals("$$") ){
                L = stringTrim( L );
                st = new StringTokenizer( L, "(). ", true );
                errFlag = false;

                SExp output;
                System.out.print("> ");
                output = inputRoutine();

                if( L.equals("") ){
                    errFlag = true;
                    System.out.println("** error: empty input **");
                }

                if( st.hasMoreTokens() ){ // still some tokens aren't used
                    errFlag = true;
                    System.out.println("** error: missing left or right parentheses **");
                }
                
                if( !errFlag ){
                    print(output);
                    System.out.println("");
                }
                else if( errFlag ){ // kind of delete this SExp
                    output = null;
                }

                if( readLine.equals("$$") ){
                    System.out.println("> Bye!");
                    break;
                }

                L = new String();
                readLine = new String();
            }
        }
    }
}
