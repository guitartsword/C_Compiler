package c_compiler;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class C_Compiler {

    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here
        String[] files = {
            "main.c",
            "prueba.c"
        };
        buildLexer();
        buildParser();
        for (String file:files) {
            Thread.sleep(500);
            System.out.println("\nCompilando Archivo " + file);
            runFile(file);
            System.out.println("---------------------------------------------------------------\n");
        }
    }

    public static void buildLexer() {

        String paramsLexer[] = new String[3];
        paramsLexer[0] = "-d";
        paramsLexer[1] = "src/c_compiler/";
        paramsLexer[2] = "src/c_compiler/c.flex";
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
        params[4] = "src/c_compiler/c.cup";
        try {
            java_cup.Main.main(params);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void runFile(String file){
        try {
            parser cupParser = new parser(new FileReader("test/"+file));
            cupParser.parse();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(C_Compiler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(C_Compiler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
