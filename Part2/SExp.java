/* 
    6341 Project part 2
    SExp.java
    Author: Yi Hsiao
*/

class SExp{
    public int IntAtom;
    public String SymAtom;
    public SExp left;
    public SExp right;
    public boolean isIntAtom;
    public boolean isSymAtom;

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
