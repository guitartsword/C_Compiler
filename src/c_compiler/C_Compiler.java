package c_compiler;

import AST.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java_cup.runtime.Symbol;

public class C_Compiler {

    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here
        String[] files = {
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
            x.saveTreeToFile(file);
            x.reduceTreeNode();
            //x.prettyPrint();
            //x.saveTreeToFile(file);
            Table table = new Table();
            semantico(x, table);
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

    public static void semantico(TreeNode parent_node, Table table) {
        ArrayList<TreeNode> childs = parent_node.getChilds();
        for (TreeNode child : childs) {
            if (child.getValue().value.equals("declaration")) {
                String type = child.getChilds().get(0).getValue().value.toString();
                getDeclarations(child.getChilds().get(1), type, table);
            } else if (child.getValue().value.equals("decl_stmnt_list")) {
                Table child_table = new Table(table);
                table.addChild(child_table);
                semantico(child, child_table);
            } else if (child.getValue().value.equals("=")) {
                Asignacion(child, table);

            } else {
                semantico(child, table);
            }
        }
    }

    public static void Asignacion(TreeNode node, Table table) {
        if (node.getChilds().size() == 2) {
            TreeNode first = node.getChilds().get(0);
            TreeNode second = node.getChilds().get(1);

            if (first.getValue().sym == 2) {
                TableRow firstResult = table.search(first.getValue().value.toString());
                if (firstResult != null) {
                    if (second.getValue().value.equals("unary_expression")) {
                        if (second.getChilds().get(0).getValue().sym == 71) {
                            if (!firstResult.type.contains("Pointer")) {
                                System.err.println("Error en la linea " + (second.getValue().right + 1) + ", columna " + second.getValue().left + " en el token " + second.getValue().value + ": Varibales son de diferente tipo");
                            }
                        }
                    }
                    switch (second.getValue().sym) {
                        case 2:
                            TableRow secondResult = table.search(second.getValue().value.toString());
                            if (secondResult != null) {
                                if (firstResult.type.equals(secondResult.type)) {
                                    firstResult.value = secondResult.value;
                                } else {
                                    System.err.println("Error en la linea " + (first.getValue().right + 1) + ", columna " + first.getValue().left + " en el token " + first.getValue().value + ": Varibales son de diferente tipo");
                                }
                            } else {
                                System.err.println("Error en la linea " + (second.getValue().right + 1) + ", columna " + second.getValue().left + " en el token " + second.getValue().value + ": Variable no ha sido declarada");
                            }
                            break;
                        case 3:
                        case 4:
                        case -1:
                            if (checkValueType(second, firstResult.type)) {
                                firstResult.value = second.getValue().value;
                            } else if (second.getChilds().get(0).getValue().sym != 71) {
                                System.err.println("Error en la linea " + (first.getValue().right + 1) + ", columna " + first.getValue().left + " en el token " + first.getValue().value + ": Varibales son de diferente tipo");
                            }
                            break;
                        case 75:
                            //System.err.println("Aritmetica");
                            break;

                    }

                } else {
                    System.err.println("Error en la linea " + (first.getValue().right + 1) + ", columna " + first.getValue().left + " en el token " + first.getValue().value + ": Variable no ha sido declarada");
                }
            }
        }
    }

    public static void getDeclarations(TreeNode node, String type, Table table) {
        String id = node.getValue().value.toString();
        ArrayList<TreeNode> node_childs = node.getChilds();
        TreeNode child_id, child_value;
        switch (id) {
            case "init_declarator_list":
                for (TreeNode child : node_childs) {
                    getDeclarations(child, type, table);
                }
                break;
            case "init_declarator":
                child_id = node_childs.get(0);
                child_value = node_childs.get(1);
                if (child_id.getValue().value.equals("declarator")) {
                    child_id = child_id.getChilds().get(1);
                    if (checkValueType(child_value, "Pointer(" + type + ")")) {
                        int offset = table.getActualOffset();
                        table.addTableRow(child_id.getValue().value.toString(), child_value.getValue().value, "Pointer(" + type + ")", offset);
                    } else {
                        System.err.println("Error en variable " + child_id.getValue().value.toString() + ", asignacion no es de tipo " + type + "*");
                    }
                } else if (checkValueType(child_value, type)) {
                    int offset = table.getActualOffset();
                    table.addTableRow(child_id.getValue().value.toString(), child_value.getValue().value, type, offset);
                } else {
                    System.err.println("Error en variable " + child_id.getValue().value.toString() + ", asignacion no es de tipo " + type);
                }
                break;
            case "direct_declarator": {
                String declaration_type = node_childs.get(0).getValue().value.toString();
                if (declaration_type.equals("function_declarator")) {
                    child_id = node_childs.get(1);
                    String parameter_types = "";
                    if (node_childs.size() > 2) {
                        parameter_types = node_childs.get(2).getValue().value.toString();
                        if (parameter_types.equals("parameter_list")) {
                            ArrayList<String> params = new ArrayList();
                            ArrayList<TreeNode> param_childs = node_childs.get(2).getChilds();
                            for (TreeNode param : param_childs) {
                                params.add(param.getValue().value.toString());
                            }
                            parameter_types = String.join(" x ", params);
                        }
                    }
                    parameter_types = type + " -> " + parameter_types;
                    int offset = table.getActualOffset();
                    table.addTableRow(child_id.getValue().value.toString(), null, parameter_types, offset);
                } else if (declaration_type.equals("array_declarator")) {
                    //Array declaration here
                }
                break;
            }
            case "declarator":
                child_id = node_childs.get(1);
                int offset = table.getActualOffset();
                table.addTableRow(child_id.getValue().value.toString(), null, "Pointer(" + type + ")", offset);
                break;
            default:
                offset = table.getActualOffset();
                table.addTableRow(id, null, type, offset);
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
                } else if (value.sym >= 74 && value.sym <= 77) {
                    Double result = artimetica(node);
                    if (result < 0) {
                        result = result * -1;
                        return setValidNumber(result.toString(), node, type, true);
                    } else {
                        return setValidNumber(result.toString(), node, type, false);
                    }
                }
            case "char":
                return value.sym == 3;
            case "Pointer(char)":
                return value.sym == 4;
        }

        return false;
    }

    public static Double artimetica(TreeNode node) {
        int id = node.getValue().sym;
        if (node.getChilds().size() > 0) {
            if (id != -1) {
                double a = artimetica(node.getChilds().get(0));
                double b = artimetica(node.getChilds().get(1));
                switch (id) {
                    case 74:
                        return a - b;
                    case 75:
                        return a + b;
                    case 76:
                        return a * b;
                    case 77:
                        return a / b;
                    default:
                        return 0.0;
                }
            } else {
                TreeNode new_node = node.getChilds().get(1);
                switch (new_node.getValue().sym) {
                    case -1:
                        if (node.getChilds().get(0).getValue().sym == 74) {
                            return artimetica(new_node) * -1;
                        } else {
                            return artimetica(new_node);
                        }
                    case 2:
                        //buscar identifier
                        return 0.0;
                    default:
                        Symbol temp_val = node.getChilds().get(0).getValue();
                        if (node.getChilds().get(0).getValue().sym == 74) {
                            return Double.parseDouble(new_node.getValue().value.toString()) * -1;
                        } else {
                            System.err.println("Error en la linea " + temp_val.right + ", columna " + temp_val.left + ", no se esperaba " + temp_val.value.toString() + " en operacion aritmetica");
                            return Double.parseDouble(new_node.getValue().value.toString());
                        }
                }
            }
        } else {
            return Double.parseDouble(node.getValue().value.toString());
        }
    }

    public static boolean setValidNumber(String value, TreeNode node, String type, boolean negative) {
        Double ret;
        if (value.matches("[0-9.]*")) {
            ret = Double.parseDouble(value);
        } else {
            String c = value.replace("\'", "");
            Integer ascii = (int) value.replace("\'", "").charAt(0);
            ret = ascii.doubleValue();
        }
        node.deleteChilds();
        if (negative) {
            ret = ret * -1;
        }
        switch (type) {
            case "short":
                if (ret.shortValue() >= -32768 && ret.shortValue() <= 32767) {
                    node.setValue(ret.shortValue());
                    return true;
                }
                break;
            case "long":
                node.setValue(ret.longValue());
                return true;
            case "int":
                node.setValue(ret.intValue());
                return true;
            case "float":
                node.setValue(ret.floatValue());
                return true;
            case "double":
                node.setValue(ret);
                return true;
            default:
                break;
        }
        return false;
    }
}
