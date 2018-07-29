.PHONY: all clean run

SRCS = $(wildcard *.java)

all:
	javac *.java -Xdiags:verbose -Xlint:all
clean:
	rm -f *.class

run: all
	java App
