/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

/**
 *
 * @author alvarez
 */
public class TableRowQuad {

    public String op;
    public String arg1;
    public String arg2;
    public String res;
    
    TableRowQuad(String op, String arg1){
        this.op = op;
        this.arg1 = arg1;
    }
    
    TableRowQuad(String op, String arg1, String res){
        this.op = op;
        this.arg1 = arg1;
        this.res = res;
    }
    
    TableRowQuad(String op, String arg1, String arg2, String res){
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.res = res;
    }
    
    @Override
    public String toString() {
        return "op= " + op + ", arg1= " + arg1 + ", arg2= " + arg2 + ", res= " + res;
    }

}
