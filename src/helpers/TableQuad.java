/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.util.ArrayList;

/**
 *
 * @author Isaias Valle
 */
public class TableQuad {
    ArrayList<TableRowQuad> rows = new ArrayList();
    ArrayList<TableRowQuad> headerRows = new ArrayList();
    private int msgcount = 0;

    public void addRow(TableRowQuad new_row){
        rows.add(new_row);
    }
    public void addRow(String op, String arg){
        rows.add(new TableRowQuad(op,arg));
    }
    public void addRow(String op, String arg, String res){
        rows.add(new TableRowQuad(op,arg,res));
    }
    public void addRow(String op, String arg1, String arg2, String res){
        rows.add(new TableRowQuad(op,arg1,arg2,res));
    }
    public void concat(TableQuad other){
        this.rows.addAll(other.rows);
        this.headerRows.addAll(other.headerRows);
        this.msgcount+=other.msgcount;
    }
    public ArrayList<TableRowQuad> getRows(){
        return rows;
    }
    public void print(){
        for(TableRowQuad row:headerRows){
            System.out.println(row.toString());
        }
    }

    public void addMessage(String new_msg) {
        String msg = ".asciiz "+new_msg;
        headerRows.add(new TableRowQuad("newmsg", msg));
    }
}
