package c_compiler;

import AST.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java_cup.runtime.Symbol;

public class C_Compiler {

    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here
        String[] files = {
            "main",
            "prueba",
            "small"
        };
        //buildLexer();
        //buildParser();
        for (String file : files) {
            runFile(file);
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

    public static void runFile(String file) throws InterruptedException {
        System.out.println("\n---------------------------------------------------------------");
        System.out.println("Compilando Archivo " + file);
        Thread.sleep(50);
        try {
            parser cupParser = new parser(new FileReader("test/" + file + ".c"));

            TreeNode x = (TreeNode) cupParser.parse().value;
            x.reduceTreeNode();
            //x.prettyPrint();
            x.saveTreeToFile(file);
            Table table = semantico(x);
            Thread.sleep(50);
            table.print();
            x.saveTreeToFile(file);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(C_Compiler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(C_Compiler.class.getName()).log(Level.SEVERE, null, ex);
        }
        Thread.sleep(50);
        System.out.println("Compilacion Completada");
        System.out.println("---------------------------------------------------------------\n");
    }

    public static Table semantico(TreeNode parent_node) {
        Table table = new Table();
        ArrayList<TreeNode> declarations = parent_node.getNodes("declaration");
        for (TreeNode node : declarations) {
            String type = node.getChilds().get(0).getValue().value.toString();
            getDeclarations(node.getChilds().get(1), type, table);
        }
        return table;
    }

    public static void getDeclarations(TreeNode node, String type, Table table) {
        String id = node.getValue().value.toString();
        switch (id) {
            case "init_declarator_list":
                for (TreeNode child : node.getChilds()) {
                    getDeclarations(child, type, table);
                }
                break;
            case "init_declarator":
                TreeNode child_id = node.getChilds().get(0);
                TreeNode child_value = node.getChilds().get(1);
                if (child_id.getValue().value.equals("declarator")) {
                    if (checkValueType(child_value, "Pointer(" + type + ")")) {
                        child_id = child_id.getChilds().get(1);
                        table.addTableRow(child_id.getValue().value.toString(), child_value.getValue().value, "Pointer(" + type + ")");
                    } else {
                        System.err.println("Error en variable " + child_id.getValue().value.toString() + ", asignacion no es de tipo Pointer(" + type + ")");
                    }
                } else if (checkValueType(child_value, type)) {
                    table.addTableRow(child_id.getValue().value.toString(), child_value.getValue().value, type);
                } else {
                    System.err.println("Error en variable " + child_id.getValue().value.toString() + ", asignacion no es de tipo " + type);
                }
                break;
            case "direct_declarator":
                //No se cuando se usa este case pero existe
                break;
            case "declarator":
                child_id = node.getChilds().get(1);
                table.addTableRow(child_id.getValue().value.toString(), null, "Pointer(" + type + ")");
                break;
            default:
                table.addTableRow(id, null, type);
        }
    }

    public static boolean checkValueType(TreeNode node, String type) {
        Symbol value = node.getValue();
        Double x;
        switch (type) {
            case "int":
            case "short":
            case "long":
            case "float":
            case "double":
                if (value.sym == 3) {
                    return setValidNumber(value.value.toString(), node, type, false);
                } else if (value.value.toString().equals("unary_expression")) {
                    return setValidNumber(node.getChilds().get(1).getValue().value.toString(), node, type, true);
                }else if(value.sym >= 74 && value.sym <= 77){
                    //aritmetica
                    System.out.println("aritmetica " +value.sym);
                }
            case "char":
                return value.sym == 3;
            case "Pointer(char)":
                return value.sym == 4;
        }
        
        return false;
    }

    public static boolean setValidNumber(String value, TreeNode node, String type, boolean negative) {
        Double ret;
        if (value.toString().matches("[0-9.]*")) {
            ret = Double.parseDouble(value.toString());
        } else {
            String c = value.toString().replace("\'", "");
            Integer ascii = (int) value.toString().replace("\'", "").charAt(0);
            ret = ascii.doubleValue();
        }
        node.deleteChilds();
        if (negative) {
            ret = ret * -1;
        }
        if (type.equals("short")) {
            if (ret.shortValue() >= -32768 && ret.shortValue() <= 32767) {
                node.setValue(ret.shortValue());
                return true;
            }
        } else if (type.equals("long")) {
            node.setValue(ret.longValue());
            return true;
        } else if (type.equals("int")) {
            node.setValue(ret.intValue());
            return true;
        } else if (type.equals("float")) {
            node.setValue(ret.floatValue());
            return true;
        } else if (type.equals("double")) {
            node.setValue(ret);
            return true;
        }
        return false;
    }
}
