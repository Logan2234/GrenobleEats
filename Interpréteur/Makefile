all: compile exec

compile:
	javac -d bin -classpath lib/sqlite-jdbc-3.40.0.0.jar -sourcepath . Test.java

exec:
	java -classpath bin:lib/sqlite-jdbc-3.40.0.0.jar Test

clean:
	rm -rf bin/*.class