package c_compiler;

import AST.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            String type = (String) (node.getChilds().get(0)).getValue();
            getDeclarations(node.getChilds().get(1), type, table);
        }
        return table;
    }

    public static void getDeclarations(TreeNode node, String type, Table table) {
        String id = node.getValue().toString();
        switch (id) {
            case "init_declarator_list":
                for (TreeNode child : node.getChilds()) {
                    getDeclarations(child, type, table);
                }
                break;
            case "init_declarator":
                String child_id = node.getChilds().get(0).getValue().toString();
                String value = node.getChilds().get(1).getValue().toString();
                if (child_id.equals("declarator")) {
                    if (checkValueType(node.getChilds().get(1), "Pointer(" + type + ")")) {
                        child_id = node.getChilds().get(0).getChilds().get(1).getValue().toString();
                        table.addTableRow(child_id, value, "Pointer(" + type + ")");
                    } else {

                    }
                } else if (checkValueType(node.getChilds().get(1), type)) {
                    value = node.getChilds().get(1).getValue().toString();
                    table.addTableRow(child_id, value, type);
                } else {
                    System.err.println("Error en variable " + child_id + ", asignacion no es de tipo " + type);
                }
                break;
            case "direct_declarator":
                //No se cuando se usa este case pero existe
                break;
            case "declarator":
                child_id = node.getChilds().get(1).getValue().toString();
                table.addTableRow(child_id, null, "Pointer(" + type + ")");
                break;
            default:
                table.addTableRow(id, null, type);
        }
    }

    public static boolean checkValueType(TreeNode node, String type) {
        Object value = node.getValue();
        //System.out.println("type: " + type);
        switch (type) {
            case "int":
                if ((value.toString().contains("\'") && (value.toString().length() == 3 || (value.toString().contains("\\") && value.toString().length() == 4)))) {
                    String c = value.toString().replace("\'", "");
                    int ascii = (int) value.toString().replace("\'", "").charAt(0);
                    node.setValue(ascii);
                    return true;
                }
                if (value.toString().equals("unary_expression")) {
                    Double x = Double.parseDouble(node.getChilds().get(1).getValue().toString()) * -1;
                    node.setValue(x.intValue());
                    return true;
                }

                if (value.toString().matches("[0-9.]*")) {
                    Double x = Double.parseDouble(value.toString());
                    node.setValue(x.intValue());
                    return true;
                }
                return false;
            case "char":
                return (value.toString().contains("\'") && (value.toString().length() == 3 || (value.toString().contains("\\") && value.toString().length() == 4)))
                        || (value.toString().matches("[0-9.]*"));

            case "short":
                if ((value.toString().contains("\'") && (value.toString().length() == 3 || (value.toString().contains("\\") && value.toString().length() == 4)))) {
                    String c = value.toString().replace("\'", "");
                    short ascii = (short) value.toString().replace("\'", "").charAt(0);
                    node.setValue(ascii);
                    return true;
                }
                if (value.toString().equals("unary_expression")) {
                    Double x = Double.parseDouble(node.getChilds().get(1).getValue().toString()) * -1;
                    if (x.longValue() >= -32768 && x.longValue() <= 32767) {
                        node.setValue(x.shortValue());
                        return true;
                    }
                }
                if (value.toString().matches("[0-9.]*")) {
                    Double x = Double.parseDouble(value.toString());
                    if (x.longValue() >= -32768 && x.longValue() <= 32767) {
                        node.setValue(x.shortValue());
                        return true;
                    }
                }
                return false;

            case "long":
                if ((value.toString().contains("\'") && (value.toString().length() == 3 || (value.toString().contains("\\") && value.toString().length() == 4)))) {
                    String c = value.toString().replace("\'", "");
                    long ascii = (long) value.toString().replace("\'", "").charAt(0);
                    node.setValue(ascii);
                    return true;
                }
                if (value.toString().equals("unary_expression")) {
                    Double x = Double.parseDouble(node.getChilds().get(1).getValue().toString()) * -1;
                    node.setValue(x.longValue());
                    return true;
                }
                if (value.toString().matches("[0-9.]*")) {
                    Double x = Double.parseDouble(value.toString());
                    node.setValue(x.longValue());
                    return true;
                }
                return false;

            case "float":
                if ((value.toString().contains("\'") && (value.toString().length() == 3 || (value.toString().contains("\\") && value.toString().length() == 4)))) {
                    String c = value.toString().replace("\'", "");
                    float ascii = (float) value.toString().replace("\'", "").charAt(0);
                    node.setValue(ascii);
                    return true;
                }
                if (value.toString().equals("unary_expression")) {
                    Float x = Float.parseFloat(node.getChilds().get(1).getValue().toString()) * -1;
                    node.setValue(x);
                    return true;
                }
                if (value.toString().matches("[0-9.]*")) {
                    Float x = Float.parseFloat(value.toString());
                    node.setValue(x);
                    return true;
                }
                return false;
            case "double":
                if ((value.toString().contains("\'") && (value.toString().length() == 3 || (value.toString().contains("\\") && value.toString().length() == 4)))) {
                    String c = value.toString().replace("\'", "");
                    double ascii = (double) value.toString().replace("\'", "").charAt(0);
                    node.setValue(ascii);
                    return true;
                }
                if (value.toString().equals("unary_expression")) {
                    Double x = Double.parseDouble(node.getChilds().get(1).getValue().toString()) * -1;
                    node.setValue(x);
                    return true;
                }
                if (value.toString().matches("[0-9.]*")) {
                    Double x = Double.parseDouble(value.toString());
                    node.setValue(x);
                    return true;
                }
                return false;
            case "Pointer(char)":
                return value.toString().contains("\'");
        }
        return false;
    }

}
