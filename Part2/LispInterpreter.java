/* 
    6341 Project part 2
    LispInterpreter.java
    Author: Yi Hsiao
*/

import java.util.*;

public class LispInterpreter{
    public static InputRoutine ir;
    public static boolean errFlag;

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
                String output = "";
                L = stringTrim( L );
                ir.st = new StringTokenizer( L, "(). ", true );
                errFlag = false;

                SExp S = new SExp(); // output of inputroutine
                SExp R = new SExp(); // output of evaluation
                 
                System.out.print("> ");
                S = ir.input1(); // S = input();

                if( L.equals("") ){
                    errFlag = true;
                    System.out.println("** error: empty input **");
                }

                if( ir.st.hasMoreTokens() || !ir.token.equals("end of token") ){ // still some tokens aren't used
                    errFlag = true;
                    System.out.println("** error: unexpected parentheses **");
                }
                
                if( !errFlag ){
                    if( S.left.SymAtom.equals("DEFUN") ){
                        print( Evaluation.add_dList( S ) );
                    }
                    else{
                        try{
                            R = Evaluation.eval( S ); // R = eval[ S, aList ];
                            print( R );
                        } catch( NullPointerException e ){
                            System.out.print( Evaluation.errMsg );
                        }
                    }
                    System.out.print("\ndot notation:\n");
                    print(S);
                    System.out.print("\n\n");
                }
                else if( errFlag ){ // kind of delete this SExp
                    S = null;
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
