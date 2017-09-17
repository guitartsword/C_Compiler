java -jar jflex-1.6.1.jar -d src/c_compiler/ src/c_compiler/c.flex
java -jar cup-0.11a.jar -destdir src/c_compiler/ -parser parser src/c_compiler/c.cup