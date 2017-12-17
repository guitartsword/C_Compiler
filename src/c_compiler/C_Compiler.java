package c_compiler;

import helpers.TableRow;
import helpers.TreeNode;
import helpers.Table;
import helpers.TableQuad;
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
            "prueba"
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
            TreeNode AST = (TreeNode) cupParser.parse().value;
            AST.reduceTreeNode();
            AST.saveTreeToFile(file);
            //x.prettyPrint();
            //x.saveTreeToFile(file);
            Table table = new Table();
            semantico(AST, table);
            cuadruplos(AST, table, 0);
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

    public static void semantico(TreeNode parent_node, Table table) {
        ArrayList<TreeNode> childs = parent_node.getChilds();
        for (TreeNode child : childs) {
            if (child.getValue().value.toString().contains("declaration")) {
                String type = child.getChilds().get(0).getValue().value.toString();
                getDeclarations(child.getChilds().get(1), type, table);
            } else if (child.getValue().value.equals("=")) {
                Asignacion(child, table);

            } else if(child.getValue().value.equals("function_definition")){
                Table child_table = new Table(table);
                table.addChild(child_table);
                ArrayList<TreeNode> function_definition_childs = child.getChilds();
                for(TreeNode function_child: function_definition_childs){
                    semantico(function_child,child_table);
                }
            } else if(child.getValue().value.equals("decl_stmnt_list") && !parent_node.getValue().value.equals("function_definition")) {
                Table child_table = new Table(table);
                table.addChild(child_table);
                semantico(child,child_table);
            }else{
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
                                System.err.println("Error en la linea " + (first.getValue().right + 1) + ", columna " + first.getValue().left + " en el token " + first.getValue().value + ": Varibales son de diferente tipo");
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
                        case sym.CONSTANT:
                        case sym.STRING_LITERAL:
                        case -1:
                            if (checkValueType(second, firstResult.type)) {
                                firstResult.value = second.getValue().value;
                            } else {
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
            case "parameter_list":
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
            case "parameter_declaration":
                type = node_childs.get(0).getValue().value.toString();
                child_id = node_childs.get(1);
                getDeclarations(child_id, type, table);
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

    private static TableQuad cuadruplos(TreeNode ast, Table symbols, int scope) {
        TableQuad cuadr = new TableQuad();
        ArrayList<TreeNode> childs = ast.getChilds();
        String op;
        String arg1;
        String arg2;
        String res;
        /*if(ast.getValue().value.equals("postfix_expression")){
            System.out.println(ast);
        }*/
        for (TreeNode child : childs) {
            if (child.getValue().value.equals("postfix_expression")) {
                //GENERATE IR POSTFIX EXPRESSION
                ArrayList<TreeNode> postfix_childs = child.getChilds();
                if(postfix_childs.size() >= 2){
                    //Si tiene 3 hijos
                    
                    //Primer hijo me dice que operador es
                    op = postfix_childs.get(0).getValue().value.toString();
                    //Obtengo el segundo hijo, normalmente no tiene hijos
                    Symbol arg1Sym = postfix_childs.get(1).getValue();
                    
                    //Si el operador es una function_call entonces
                    if(op.equals("function_call") && postfix_childs.size() == 3){
                        //Si es funcion y tiene parametros
                        //obtengo el tercer hijo, puede que tenga hijos
                        TreeNode params = postfix_childs.get(2);
                        cuadr.concat(getParams(params));
                    }
                    //Si el segundo hijo es un operador ++ o --
                    if(arg1Sym.sym == sym.INC_OP || arg1Sym.sym == sym.DEC_OP){
                        cuadr.addRow(arg1Sym.value.toString().substring(0, 1), op, "1", op);
                    }else{
                        cuadr.addRow(op, arg1Sym.value.toString());
                    }
                }
            } else if(child.getValue().value.equals("=")){
                ArrayList<TreeNode> equal_childs = child.getChilds();
                if (equal_childs.size() == 2){
                    //Obtengo al que se le asigna
                    TreeNode resNode = equal_childs.get(0);
                    //Lo que se le va a asignar
                    Symbol valueSym = equal_childs.get(1).getValue();
                    if(valueSym.sym == sym.IDENTIFIER || valueSym.sym == sym.CONSTANT || valueSym.sym == sym.STRING_LITERAL){
                        if(resNode.getValue().sym != -1){
                            cuadr.addRow("=", valueSym.value.toString(), resNode.getValue().value.toString());
                        }else if (resNode.getValue().value.equals("unary_expression")){
                            ArrayList<TreeNode> unary_childs = resNode.getChilds();
                            if(unary_childs.size() == 2){
                            Symbol unary_symbol = unary_childs.get(0).getValue();
                            String id = unary_symbol.value.toString();
                            id += unary_childs.get(1).getValue().value.toString();
                            //Aqui la validacion si es un puntero o una direccion
                            if(unary_symbol.sym == sym.ADRESS || unary_symbol.sym == sym.MUL)
                                cuadr.addRow("=", valueSym.value.toString(),id);
                            }
                        }
                    }
                }
            } else if(child.getValue().value.equals("function_definition")){
                System.out.println("SCOPE="+scope);
                Table scope_table = symbols.getChilds().get(scope++);
                cuadruplos(child, scope_table, 0);
            } else if (child.getValue().value.equals("decl_stmnt_list") && !ast.getValue().value.equals("function_definition")) {
                cuadruplos(child, symbols, 0);
            } else {
                cuadruplos(child, symbols, scope);
            }
        }
        /*int t=0;
        cuadr.addRow("=", "&vb", "aaaa");
        cuadr.addRow("=", "vb", "main");
        cuadr.addRow("if=", "1","1", "etiq1");
        cuadr.addRow("goto", "etiq2");
        cuadr.addRow("genetiq", "etiq1");
        cuadr.addRow("*", "10", "2", "t"+t++);
        cuadr.addRow("+", "t"+(t-1), "x", "t"+t++);
        cuadr.addRow("+", "t"+(t-1), "10", "t"+t++);
        cuadr.addRow("=", "t"+(t-1), "y");
        t=0;//LIMPIAR TEMPORALES
        cuadr.addRow("genetiq", "etiq2");
        cuadr.addRow("+","10","7","x"); //ver si usar o no temporal
        cuadr.addRow("=", "10", "y");
        cuadr.addRow("=", "10", "z");
        
        
        arg1 = ""+(int)'c';
        cuadr.addRow("=", arg1, "y");
        arg1 = ""+1*4;
        cuadr.addRow("[]=",arg1,"3","a");
        //GENERATE FUNCTION
        //PARAM 0
        arg1 = ""+0*4;
        param1="t"+t++;
        cuadr.addRow("=[]","a",arg1,param1);
        cuadr.addRow("param","0",param1);
        //PARAM 1
        arg1 = ""+0*4;
        param1="t"+t++;
        cuadr.addRow("=[]","a",arg1,param1);
        cuadr.addRow("param","1",param1);
        
        t=0;//LIMPIAR TEMPORALES
        //Call funcition
        cuadr.addRow("call", "somefunc2");
        cuadr.addRow("=", "RET", "t"+t++);
        */
        cuadr.print();
        return cuadr;
    }

    private static TableQuad getParams(TreeNode params) {
        ArrayList<TreeNode> paramChilds = params.getChilds();
        TableQuad cuadr = new TableQuad();
        for(TreeNode child: paramChilds){
            String child_value = child.getValue().value.toString();
            if(child.getChilds().isEmpty() && params.getValue().value.equals("expression")){
                //Si no tiene hijos entonces agrego el parametro
                cuadr.addRow("param", child_value);
            }else if(child_value.equals("unary_expression")){
                //Si es un puntero o una direccion
                ArrayList<TreeNode> unary_childs =  child.getChilds();
                if(unary_childs.size() == 2){
                    Symbol unary_symbol = unary_childs.get(0).getValue();
                    String param = unary_symbol.value.toString();
                    param += unary_childs.get(1).getValue().value.toString();
                    //Aqui la validacion si es un puntero o una direccion
                    if(unary_symbol.sym == sym.ADRESS || unary_symbol.sym == sym.MUL)
                        cuadr.addRow("param", param);
                }
            }else{
                getParams(child);
            }
        }
        if(params.getParent().getValue().value.equals("postfix_expression") && params.getChilds().isEmpty())
            cuadr.addRow("param", params.getValue().value.toString());
        return cuadr;
    }
}
