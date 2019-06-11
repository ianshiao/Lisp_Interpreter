/* 
    6341 Project part 2
    Evaluation.java
    Author: Yi Hsiao
*/

class Evaluation{
    public static InputRoutine ir;
    public static LispInterpreter li;
    
    public static SExp aList = ir.NIL;
    public static SExp dList = ir.NIL;
    public static String errMsg;

    public static SExp add_dList( SExp exp ){ // ( DEFUN ( F ( X Y ) ) Fb )
        SExp tmp = new SExp();

        tmp = new SExp( exp.right.left.right.left, exp.right.right.left );
        tmp = new SExp( exp.right.left.left, tmp ); // cons[ F, cons[ (X Y), Fb ] ]

        dList = new SExp( tmp, dList ); // add to dList

        return tmp.left;
    }

    public static SExp add_aList( SExp par, SExp val ){
        SExp tmp = new SExp();
        
        if( par.isSymAtom && par.SymAtom.equals("NIL") ){ // null[par] -> aList
            tmp = aList;
        }
        else{ // T -> cons[ cons[ car[par], car[val] ], add_aList[ cdr[par], cdr[val] ] ]
            SExp tmp2 = new SExp( par.left, val.left );
            tmp = new SExp( tmp2, add_aList( par.right, val.right ) );
        }

        return tmp;
    }
    
    public static SExp in( SExp a, SExp List ){
        SExp result = new SExp();
        
        if( List.isSymAtom && List.SymAtom.equals("NIL") ){ // null[List] -> NIL
            result = ir.NIL;
        }
        else if( List.left.left.SymAtom.equals( a.SymAtom ) ){ // eq[ caar[List], a ] -> T
            result = ir.T;
        }
        else{ // T -> in[ a, cdr[List] ]
            result = in( a, List.right );
        }

        return result;
    }

    public static SExp getval( SExp a, SExp List ){
        SExp output = new SExp();
        
        if( List.left.left.SymAtom.equals( a.SymAtom ) ){ // eq[ caar[List], a ] -> cdar[aList]
            output = List.left.right;
        }
        else{ // T -> getval[ a, cdr[List] ]
            output = getval( a, List.right );
        }

        return output;
    }

    public static SExp eval( SExp exp ){
        SExp output = new SExp();
        
        if( exp.isIntAtom || exp.isSymAtom ){ // atom[exp] ->
            if( exp.isIntAtom ){ // int[exp] -> exp
                output = exp;
            }
            else if( exp.SymAtom.equals("T") ){ // eq[ exp, T ] -> T
                output = ir.T;
            }
            else if( exp.SymAtom.equals("NIL") ){ // eq[ exq, NIL] -> NIL
                output = ir.NIL;
            }
            else if( in( exp, aList ).SymAtom.equals("T") ){ // in[ exp, aList ] -> getVal[ exp, aList ]
                output = getval( exp, aList );
            }
            else{ // T -> "unbound"
                errMsg = "** error: unbound **";
                output = null;
            }
        }
        else if( ( exp.left.isIntAtom || exp.left.isSymAtom ) && ( exp.right.isIntAtom || exp.right.isSymAtom ) ){
            errMsg = "** error: invalid argument **";
            output = null;
        }
        else if( exp.left.isSymAtom ){ // atom[ car[exp] ] ->
            if( exp.left.SymAtom.equals("QUOTE")  ){ // eq[ car[exp], QUOTE ] -> cadr[exp]
                output = exp.right.left;
            }
            else if( exp.left.SymAtom.equals("COND") ){ // eq[ car[exp], COND ] -> evcon[ cdr[exp], aList ]
                output = evcon( exp.right );
            }
            else{ // T -> apply[ car[exp], evlis[ cdr[exp], aList ], aList ]
                output = apply( exp.left, evlis( exp.right ) );
            }
        }
        else{ // T -> "not a Lisp expression"
            errMsg = "** error: not a Lisp expression **";
            output = null;
        }

        return output;
    }

    public static SExp evlis( SExp list ){
        SExp output = new SExp();
        
        if( list.isSymAtom && list.SymAtom.equals("NIL") ){ // null[list] -> NIL
            output = ir.NIL;
        }
        else{ // T->cons[ eval[ car[list] ], evlist[ cdr[list] ] ]
            output = new SExp( eval( list.left ), evlis( list.right ) );
        }

        return output;
    }

    public static SExp evcon( SExp be ){
        SExp output = new SExp();

        if( be.isSymAtom && be.SymAtom.equals("NIL") ){ // null[be] -> "exception"
            errMsg = "** error: exception **";
            output = null;
        }
        else{
            SExp tmp = eval( be.left.left );
            
            if( tmp.isSymAtom && tmp.SymAtom.equals("T") ){ // eval[ caar[be], aList ] -> eval[ cadar[be], aList ]
                output = eval( be.left.right.left );
            }
            else{ // T -> evcon[ cdr[be], aList ]
                output = evcon( be.right );
            }
        }

        return output;
    }

    public static SExp apply( SExp f, SExp x ){
        SExp output = new SExp();
        int numsArgu = count( x );

        if( f.isSymAtom ){
            if( f.SymAtom.equals("CAR") ){ // eq[ f, CAR ] -> caar[x]
                output = x.left.left;
            }
            else if( f.SymAtom.equals("CDR") ){ // eq[ f, CDR ] -> cdar[x]
                output = x.left.right;
            }
            else if( f.SymAtom.equals("CONS") ){ // eq[ f, CONS ] -> cons[ car[x], cadr[x] ]
                output = new SExp( x.left, x.right.left );
            }
            else if( f.SymAtom.equals("ATOM") ){ // eq[ f, ATOM ] -> atom[ car[x] ]
                if( x.left.isIntAtom || x.left.isSymAtom )
                    output = ir.T;
                else
                    output = ir.NIL;
            }
            else if( f.SymAtom.equals("INT") ){ // eq[ f, INT ] -> int[ car[x] ]
                if( x.left.isIntAtom )
                    output = ir.T;
                else
                    output = ir.NIL;
            }
            else if( f.SymAtom.equals("NULL") ){ // eq[ f, NULL ] -> null[ car[x] ]
                if( x.left.isSymAtom && x.left.SymAtom.equals("NIL") )
                    output = ir.T;
                else
                    output = ir.NIL;
            }
            else if( f.SymAtom.equals("EQ") ){ // eq[ f, EQ ] -> eq[ car[x], cadr[x] ]
                output = eq( x.left, x.right.left );
            }
            else if( f.SymAtom.equals("PLUS") ){ // eq[ f, PLUS ] -> +[ car[x], cadr[x] ]
                if( numsArgu == 2 ){
                    if( x.left.isIntAtom && x.right.left.isIntAtom ){
                        output = new SExp( x.left.IntAtom + x.right.left.IntAtom );
                    }
                    else{
                        errMsg = "** error: wrong argument type **";
                        output = null;
                    }
                }
                else if( numsArgu < 2 ){
                    errMsg = "** error: too few arguments **";
                    output = null;
                }
                else{
                    errMsg = "** error: too many arguments **";
                    output = null;
                }
            }
            else if( f.SymAtom.equals("MINUS") ){ // eq[ f, MINUS ] -> -[ car[x], cadr[x] ]
                if( numsArgu == 2 ){
                    if( x.left.isIntAtom && x.right.left.isIntAtom ){
                        output = new SExp( x.left.IntAtom - x.right.left.IntAtom );
                    }
                    else{
                        errMsg = "** error: wrong argument type **";
                        output = null;
                    }
                }
                else if( numsArgu < 2 ){
                    errMsg = "** error: too few arguments **";
                    output = null;
                }
                else{
                    errMsg = "** error: too many arguments **";
                    output = null;
                }
            }
            else if( f.SymAtom.equals("TIMES") ){ // eq[ f, TIMES ] -> *[ car[x], cadr[x] ]
                if( numsArgu == 2 ){
                    if( x.left.isIntAtom && x.right.left.isIntAtom ){
                        output = new SExp( x.left.IntAtom * x.right.left.IntAtom );
                    }
                    else{
                        errMsg = "** error: wrong argument type **";
                        output = null;
                    }
                }
                else if( numsArgu < 2 ){
                    errMsg = "** error: too few arguments **";
                    output = null;
                }
                else{
                    errMsg = "** error: too many arguments **";
                    output = null;
                }
            }
            else if( f.SymAtom.equals("QUOTIENT") ){ // eq[ f, QUOTIENT ] -> /[ car[x], cadr[x] ]
                if( numsArgu == 2 ){
                    if( x.left.isIntAtom && x.right.left.isIntAtom ){
                        output = new SExp( x.left.IntAtom / x.right.left.IntAtom );
                    }
                    else{
                        errMsg = "** error: wrong argument type **";
                        output = null;
                    }
                }
                else if( numsArgu < 2 ){
                    errMsg = "** error: too few arguments **";
                    output = null;
                }
                else{
                    errMsg = "** error: too many arguments **";
                    output = null;
                }
            }
            else if( f.SymAtom.equals("REMAINDER") ){ // eq[ f, REMAINDER ] -> %[ car[x], cadr[x] ]
                if( numsArgu == 2 ){
                    if( x.left.isIntAtom && x.right.left.isIntAtom ){
                        output = new SExp( x.left.IntAtom % x.right.left.IntAtom );
                    }
                    else{
                        errMsg = "** error: wrong argument type **";
                        output = null;
                    }
                }
                else if( numsArgu < 2 ){
                    errMsg = "** error: too few arguments **";
                    output = null;
                }
                else{
                    errMsg = "** error: too many arguments **";
                    output = null;
                }
            }
            else if( f.SymAtom.equals("GREATER") ){ // eq[ f, GREATER ] -> >[ car[x], cadr[x] ]
                if( numsArgu == 2 ){
                    if( x.left.IntAtom > x.right.left.IntAtom )
                        output = ir.T;
                    else
                        output = ir.NIL;
                }
                else if( numsArgu < 2 ){
                    errMsg = "** error: too few arguments **";
                    output = null;
                }
                else{
                    errMsg = "** error: too many arguments **";
                    output = null;
                }
            }
            else if( f.SymAtom.equals("LESS") ){ // eq[ f, LESS ] -> <[ car[x], cadr[x] ]
                if( numsArgu == 2 ){
                    if( x.left.IntAtom < x.right.left.IntAtom )
                        output = ir.T;
                    else
                        output = ir.NIL;
                }
                else if( numsArgu < 2 ){
                    errMsg = "** error: too few arguments **";
                    output = null;
                }
                else{
                    errMsg = "** error: too many arguments **";
                    output = null;
                }
            }
            else{ // T -> eval[ cdr[ getval[ f, dList ] ], add_aList[ car[ getval[ f, dList ] ], x, aList ] ]
                if( in( f, dList ).SymAtom.equals("T") ){
                    if( numsArgu == count( getval( f, dList ).left ) ){
                        aList = add_aList( getval( f, dList ).left, x );
                        //System.out.print("a-List: ");li.print(aList);System.out.println("");
                        output = eval( getval( f, dList ).right );
                    }
                    else if( numsArgu < count( getval( f, dList ).left ) ){
                        errMsg = "** error: too few arguments **";
                        output = null;
                    }
                    else{
                        errMsg = "** error: too many arguments **";
                        output = null;
                    }
                }
                else{
                    errMsg = "** error: undefined function **";
                    output = null;
                }
            }
        }
        else{ // T -> "Not a Lisp expression"
            errMsg = "** error: Not a Lisp expression **";
            output = null;
        }

        return output;
    }

    public static SExp eq( SExp s1, SExp s2 ){
        SExp output = new SExp();
        
        if( s1.isIntAtom || s1.isSymAtom ){ // atom[s1] ->
            if( s1.isIntAtom && s2.isIntAtom ){ // atom[s2] -> eq[ s1, s2 ]
                if( s1.IntAtom == s2.IntAtom ) // int
                    output = ir.T;
                else
                    output = ir.NIL;
            }
            else if( s1.isSymAtom && s2.isSymAtom ){ // atom[s2] -> eq[ s1, s2 ]
                if( s1.SymAtom.equals(s2.SymAtom) ) // sym
                    output = ir.T;
                else
                    output = ir.NIL;
            }
            else // T -> NIL
                output = ir.NIL;
        }
        else if( eq( s1.left, s2.left ).SymAtom.equals("T") ){ // eq[ car[s1], car[s2] ] -> eq[ cdr[s1], cdr[s2] ]
            output = eq( s1.right, s2.right );
        }
        else{ // T -> NIL
            output = ir.NIL;
        }

        return output;
    }

    public static int count( SExp s ){
        int total = 0;

        if( s.isSymAtom && s.SymAtom.equals("NIL") )
            return total;
        else{
            total += 1;
            total += count( s.right );
            return total;
        }
    }
}
