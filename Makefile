all: build jar

build:
	scalac src/main/scala/*.scala

jar:
	jar cfm Project.jar manifest.mf  -C src/main/scala/project/ .

clean:
	rm -f src/main/scala/*.class
	rm -f Project.jar
