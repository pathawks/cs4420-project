all: build jar

build:
	scalac src/main/scala/*.scala

jar:
	jar cvf Project.jar -C ./src/main/scala/project/ .

clean:
	rm -f src/main/scala/*.class
	rm -f Project.jar
