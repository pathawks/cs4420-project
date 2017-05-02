all: build

build:
	scalac src/main/scala/*.scala -d Project.jar

clean:
	rm -f src/main/scala/*.class
	rm -f Project.jar
