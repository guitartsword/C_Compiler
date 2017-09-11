package c_compiler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java_cup.Lexer;
//import java_cup.parser;

public class C_Compiler {

    public static void main(String[] args) {
        // TODO code application logic here
        String file = "./main.c";
        System.out.println("EXECUTING JFLEX");
        buildLexer();
        System.out.println("FINISHED EXECUTING JFLEX");
        
        System.out.println("EXECUTING CUP");
        buildParser();
        System.out.println("FINISHED EXECUTING CUP");
        
        System.out.println("RUNNING ANALYZER");
        runFile(file);
        System.out.println("FINISHED EXECUTING ANALYZER");
    }

    public static void buildLexer() {

        String paramsLexer[] = new String[3];
        paramsLexer[0] = "-d";
        paramsLexer[1] = "src/c_compiler/";
        paramsLexer[2] = "src/c_compiler/Lexico.flex";
        try {
            jflex.Main.generate(paramsLexer);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void buildParser() {
        String params[] = new String[5];

        params[0] = "-destdir";
        params[1] = "src/c_compiler/";
        params[2] = "-parser";
        params[3] = "parser";
        params[4] = "src/c_compiler/sintactico.cup";
        try {
            java_cup.Main.main(params);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void runFile(String file){
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("main.c"));
            Lexer lexer = new Lexer(reader);
            parser cupParser = new parser(lexer);
            cupParser.parse();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(C_Compiler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(C_Compiler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
