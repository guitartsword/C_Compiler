/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Isaias Valle
 */
public class TreeNode {

    private TreeNode parent;
    private ArrayList<TreeNode> childs = new ArrayList();
    private Object value;

    public TreeNode(TreeNode copy) {
        parent = copy.parent;
        childs = copy.childs;
        value = copy.value;
    }

    public TreeNode(TreeNode parent, Object value) {
        this.parent = parent;
        this.value = value;
    }

    public boolean addChild(TreeNode newChild) {
        newChild.parent = this;
        return childs.add(newChild);
    }

    public boolean addChild(Object value) {
        return childs.add(new TreeNode(this, value));
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public void removeParent() {
        this.parent = null;
    }

    public TreeNode getParent() {
        return this.parent;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public ArrayList<TreeNode> getChilds() {
        return childs;
    }

    public void reduceTreeNode() {
        if (childs.size() == 1) {
            value = childs.get(0).value;
            childs = childs.get(0).childs;
            this.reduceTreeNode();
        }
        if (childs.size() > 1) {
            for (TreeNode child : childs) {
                child.reduceTreeNode();
            }
        }
    }

    @Override
    public String toString() {
        return toString("", true);
    }

    private String toString(String indent, boolean last) {
        String tree = indent;
        if (last) {
            tree += "└───";
            indent += "    ";
        } else {
            tree += "├───";
            indent += "│   ";
        }
        tree += value.toString() + "\n";
        int index = 0;
        for (TreeNode child : childs) {
            index++;
            tree += child.toString(indent, index == childs.size());
        }
        return tree;
    }

    public void prettyPrint() {
        String indent = "";
        System.out.println(value);
        int index = 0;
        for (TreeNode child : childs) {
            index++;
            child.prettyPrint(indent, index == childs.size());
        }
    }

    private void prettyPrint(String indent, boolean last) {
        System.out.print(indent);
        if (last) {
            System.out.print("└───");
            indent += "    ";
        } else {
            System.out.print("├───");
            indent += "│   ";
        }
        System.out.println(value);
        int index = 0;
        for (TreeNode child : childs) {
            index++;
            child.prettyPrint(indent, index == childs.size());
        }
    }

    public void saveTreeToFile(String name) {
        try {
            FileOutputStream out = new FileOutputStream(name + ".tree");
            try {
                writeToFile("", true, out);
                out.flush();
                out.close();
            } catch (IOException iOException) {
                out.flush();
                out.close();
                System.err.println("WritngToFile Error");
            }
        } catch (FileNotFoundException ex) {
            System.err.println("FileNotFound Error");
        } catch (IOException ex) {
            System.err.println("WritngToFile Error");
        }
    }

    private void writeToFile(String indent, boolean last, FileOutputStream out) throws IOException {
        out.write(indent.getBytes());
        if (last) {
            out.write("└───".getBytes());
            indent += "    ";
        } else {
            out.write("├───".getBytes());
            indent += "│   ";
        }
        out.write(value.toString().getBytes());
        out.write("\n".getBytes());
        int index = 0;
        for (TreeNode child : childs) {
            index++;
            child.writeToFile(indent, index == childs.size(), out);
        }
    }

    public ArrayList<TreeNode> getNodes(Object value) {
        ArrayList<TreeNode> result = new ArrayList();
        if (this.value.equals(value)) {
            result.add(this);
        } else {
            for (TreeNode node : childs) {
                result.addAll(node.getNodes(value));
            }
        }
        return result;
    }

    public void getDeclarations(String type, Table table) {
        String id = (String) this.value;
        switch (id) {
            case "init_declarator_list":
                for (TreeNode child : this.childs) {
                    child.getDeclarations(type, table);
                }
                break;
            case "init_declarator":
                String child_id = (String) this.childs.get(0).getValue();
                String value = (String) this.childs.get(1).getValue();
                if (child_id == "declarator") {
                    child_id = (String) this.childs.get(0).getChilds().get(1).getValue();
                    table.addTableRow(child_id, value, "Pointer(" + type + ")");
                } else {
                    table.addTableRow(child_id, value, type);
                }
                break;
            case "direct_declarator":
                //No se cuando se usa este case pero existe
                break;
            case "declarator":
                child_id = (String) this.childs.get(1).getValue();
                table.addTableRow(child_id, null, "Pointer(" + type + ")");
                break;
            default:
                table.addTableRow(id, null, type);
        }
    }
}
