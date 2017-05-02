all: build

build:
	scalac src/main/scala/heuristics.scala src/main/scala/Main.scala src/main/scala/Nsquare.scala src/main/scala/Search.scala src/main/scala/Utility.scala src/main/scala/EBFsolver.scala src/main/scala/PatternDatabase.scala -d Project.jar
#	scalac src/main/scala/*.scala -d Project.jar

clean:
	rm -f src/main/scala/*.class
	rm -f Project.jar
