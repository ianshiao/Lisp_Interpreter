# Lisp Interpreter
**Run**
>make

**Description**

- Each complete s-expression will be followed by a line containing a single “$” sign.
- The last s-expression will be followed by a line containing “$$”.
- An identifier can consist of a letter of the alphabet followed by one or more letters or digits, and only uppercase letters can be used.
- An integer can be signed or unsigned; thus, 42, -42, +42, are all valid.
- The only characters that will allow on the input stream are uppercase letters, digits, white-space, and $.

## Part1 ##
- Read in an input s-expression in either dot or list notation (or both), convert it into the appropriate internal representation, and ouput the s-expression in the dot notation.

**Sample input and output**

       ( 2 . (3 4))
       $
    > (2 . (3 . (4 . NIL)))

       ( 2 . (3 4) . 5)
    > **error: unexpected dot**
       $

       ( 2 . ((3 4) . 5))
       $
    > (2 . ((3 . (4 . NIL)) . 5))

       ( 2 . (3 4) $ 5)
    > **error: unexpected dollar**
       $

       ( 2 (3 . 4) (5 . 6))
       $
    > (2 . ((3 . 4) . ((5 . 6) . NIL)))

       (CAR (QUOTE (A . B)))
       $
    > (CAR . ((QUOTE . ((A . B) . NIL)) . NIL))

       (CONS 4 (QUOTE (A . B)))
       $
    >  (CONS . (4 . ((QUOTE . ((A . B) . NIL)) . NIL)))

       (CONS 4 (A . B))
       $
    >  (CONS . (4 . ((A . B) . NIL)))

       (DEFUN SILLY (A B) (PLUS A B))
       $
    >  (DEFUN . (SILLY . ((A . (B . NIL)) . ((PLUS . (A . (B . NIL))) . NIL))))

       (SILLY 5 6)
       $
    >  (SILLY . (5 . (6 . NIL)))

       (SILLY (CAR (QUOTE (5 . 6))) (CDR (QUOTE (5 . 6))) )
       $
    >  (SILLY . ( (CAR . ((QUOTE . ((5 . 6) . NIL)) . NIL)) .
                   ( (CDR . ((QUOTE . ((5 . 6) . NIL)) . NIL)) . NIL)))

       (DEFUN MINUS2 (A B) (MINUS A B))
       $
    >  (DEFUN . (MINUS2 . ((A . (B . NIL)) . ((MINUS . (A . (B . NIL))) . NIL))))

       (DEFUN NOTSOSILLY (A B) 
                (COND
                   ((EQ A 0) (PLUS B 1))
                   ((EQ B 0) (NOTSOSILLY (MINUS2 A 1) 1))
                   (T (NOTSOSILLY (MINUS2 A 1) (NOTSOSILLY A (MINUS2 B 1))))
                 ))
       $
    >  (DEFUN . (NOTSOSILLY . ((A . (B . NIL)) . ((COND . ( ( (EQ . (A . (0 . NIL))) . ( (PLUS . (B . (1 . NIL))) . NIL)) . ( ( (EQ . (B . (0 . NIL))) . ( (NOTSOSILLY . ((MINUS2 .(A . (1 . NIL))) . (1 . NIL))) . NIL)) . ( (T . ((NOTSOSILLY . ( (MINUS2 . (A . (1 . NIL))) . 
                     ( (NOTSOSILLY . (A . ((MINUS2 . (B . (1 . NIL))) . NIL))) . NIL))) . NIL)) . NIL)))) . NIL))))

       (NOTSOSILLY 0 0)
       $$
    >  (NOTSOSILLY . (0 . (0 . NIL)))
    >  Bye!

## Part2 ##
- The main part of the Lisp expression evaluator, i.e., eval[] and associated functions.

**Sample input and output**

      (CAR (QUOTE (A . B)))
    > A

      (CONS 4 (QUOTE (A . B)))
    > (4 . (A . B))
    dot notation: 
      (CONS . (4 . ((QUOTE . ((A . B) . NIL)) . NIL)))

      (CONS 4 (A . B))
    > error (trying to evaluate (A . B) )
    dot notation: 
      (CONS . (4 . ((A . B) . NIL)))

      (DEFUN (SILLY (A B)) (PLUS A B))
    > SILLY
    dot notation: 
      (DEFUN . ((SILLY . ((A . (B . NIL)) . ((PLUS . (A . (B . NIL))) . NIL))))

      (SILLY 5 6)
    > 11
    dot notation: 
      (SILLY . (5 . (6 . NIL)))

      (SILLY (CAR (QUOTE (5 . 6))) (CDR (QUOTE (5 . 6))) )
    > 11
    dot notation: 
     (SILLY . ( (CAR . ((QUOTE . ((5 . 6) . NIL)) . NIL)) .
                   ( (CDR . ((QUOTE . ((5 . 6) . NIL)) . NIL)) . NIL)))

      (DEFUN (MINUS2 (A B)) (MINUS A B))
    > MINUS2
     (DEFUN . (MINUS2 . ((A . (B . NIL)) . ((MINUS . (A . (B . NIL))) . NIL))))

     (DEFUN (NOTSOSILLY (A B)) 
                (COND
                   ((EQ A 0) (PLUS B 1))
                   ((EQ B 0) (NOTSOSILLY (MINUS2 A 1) 1))
                   (T (NOTSOSILLY (MINUS2 A 1) (NOTSOSILLY A (MINUS2 B 1))))
                 ))
    > NOTSOSILLY
      (DEFUN . (NOTSOSILLY . ((A . (B . NIL)) . ((COND . ( ( (EQ . (A . (0 . NIL))) . ( (PLUS . (B . (1 . NIL))) . NIL)) . ( ( (EQ . (B . (0 . NIL))) . ( (NOTSOSILLY . ((MINUS2 .(A . (1 . NIL))) . (1 . NIL))) . NIL)) . ( (T . ((NOTSOSILLY . ( (MINUS2 . (A . (1 . NIL))) . 
                     ( (NOTSOSILLY . (A . ((MINUS2 . (B . (1 . NIL))) . NIL))) . NIL))) . NIL)) . NIL)))) . NIL))))

      (NOTSOSILLY 0 0)
    > 1

      (NOTSOSILLY 1 1)
    > 3
