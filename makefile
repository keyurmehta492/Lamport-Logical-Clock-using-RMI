JFLAGS = -g
JC = javac

.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
Lamport.java\
IMasterObj.java\
MasterObj.java\
LamportPo.java\
IProcessObj.java\
ProcessObj.java\
Queue.java\

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) */*.class