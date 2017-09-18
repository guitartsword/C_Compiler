/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c_compiler;

import java.util.ArrayList;

/**
 *
 * @author Isaias Valle
 */

public class TreeNode {
    private TreeNode parent;
    private ArrayList<TreeNode> childs = new ArrayList();
    private Object value;
    
    public TreeNode(TreeNode copy){
        parent = copy.parent;
        childs = copy.childs;
        value = copy.value;
    }
    
    public TreeNode(TreeNode parent, Object value){
        this.parent = parent;
        this.value = value;
    }
    
    public boolean addChild(TreeNode newChild){
        newChild.setParent(this);
        return childs.add(newChild);
    }
    
    public boolean addChild(Object value){
        return childs.add(new TreeNode(this, value));
    }
    
    public void setParent(TreeNode parent){
        this.parent = parent;
    }
    
    public void removeParent(){
        this.parent = null;
    }
    
    public TreeNode getParent(){
        return this.parent;
    }
    
    public Object getValue(){
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return toString("", true);
    }
    
    private String toString(String indent, boolean last) {
        String tree = indent;
        if(last){
            tree += "└───";
            indent += "    ";
        }else{
            tree += "├───";
            indent += "│   ";
        }
        tree += value.toString() + "\n";
        int index = 0;
        for (TreeNode child:childs){
            index++;
            tree += child.toString(indent, index==childs.size());
        }
        return tree;
    }
    public void prettyPrint(){
        String indent = "    ";
        System.out.print("└───");
        System.out.println(value);
        int index = 0;
        for (TreeNode child:childs){
            index++;
            child.prettyPrint(indent, index==childs.size());
        }
    }
    private void prettyPrint(String indent, boolean last){
        System.out.print(indent);
        if(last){
            System.out.print("└───");
            indent += "    ";
        }else{
            System.out.print("├───");
            indent += "│   ";
        }
        System.out.println(value);
        int index = 0;
        for (TreeNode child:childs){
            index++;
            child.toString(indent, index==childs.size());
        }
    }
}
