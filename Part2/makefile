JC = javac
JCR = java

.SUFFIXES: .java .class
.java.class:
	$(JC) LispInterpreter.java

CLASSES = \
	LispInterpreter.java

default: classes exec-tests

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

exec-tests: classes
	$(JCR) LispInterpreter

.PHONY: default clean classes exec-tests
