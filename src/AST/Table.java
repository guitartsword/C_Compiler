/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.util.ArrayList;

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

    public Table(Table parent) {
        this.parent = parent;
        this.rows = new ArrayList();
    }

    public boolean addTableRow(TableRow to_add) {
        TableRow result = search(to_add);
        if (result == null) {
            this.rows.add(to_add);
            return true;
        } else {
            System.err.println("Error en variable " + to_add.id + ", ya ha sido declarada");
        }
        return false;
    }

    public boolean addTableRow(String id, Object value, String type) {
        TableRow to_add = new TableRow(id, value, type);
        TableRow result = search(to_add);
        if (result == null) {
            this.rows.add(to_add);
            return true;
        } else {
            System.err.println("Error en variable " + id + ", ya ha sido declarada");
        }
        return false;
    }

    public void setParent(Table t) {
        this.parent = t;
    }

    public TableRow search(TableRow id) {
        for (TableRow row : rows) {
            if (row.equals(id)) {
                return row;
            }
        }
        if (parent != null) {
            return parent.search(id);
        }
        return null;
    }

    public void print() {
        for (TableRow tr : this.rows) {
            System.out.println(tr.toString());
        }
        System.out.println();
    }
}
