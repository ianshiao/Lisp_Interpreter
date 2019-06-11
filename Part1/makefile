JC = javac
JCR = java

.SUFFIXES: .java .class
.java.class:
	$(JC) lisp.java

CLASSES = \
	lisp.java

default: classes exec-tests

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

exec-tests: classes
	$(JCR) lisp

.PHONY: default clean classes exec-tests
