/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author alvarez
 */
public class Table {

    Table parent;
    ArrayList<TableRow> rows;

    public Table() {
        this.parent = null;
        this.rows = new ArrayList();
    }

    public boolean addTableRow(TableRow v) {
        if (!this.rows.contains(v)) {
            this.rows.add(v);
            return true;
        } else {
            System.err.println(v.id + " ya ha sido declarada");
        }
        return false;
    }

    public boolean addTableRow(String id, Object value, String type) {
        TableRow v = new TableRow(id, value, type);
        if (!this.rows.contains(v)) {
            this.rows.add(v);
            return true;
        } else {
            System.err.println(id + " ya ha sido declarada");
        }
        return false;
    }

    public void addFather(Table t) {
        this.parent = t;
    }
    
    public void print(){
        for (TableRow tr:this.rows) {
            System.out.println(tr.toString());
        }
        System.out.println();
    }
}
