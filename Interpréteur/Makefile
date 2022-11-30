all: compile exec

compile:
	javac -d bin -classpath lib/ojdbc6.jar -sourcepath . Test.java

exec:
	java -classpath bin:lib/ojdbc6.jar Test
