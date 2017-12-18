/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.util.ArrayList;

/**
 *
 * @author alvarez
 */
public class Table {

    Table parent = null;
    ArrayList<TableRow> rows = new ArrayList();
    ArrayList<Table> childs = new ArrayList();
    
    public Table(){
    }
    public Table(Table parent) {
        this.parent = parent;
    }

    public boolean addTableRow(TableRow to_add) {
        TableRow result = searchLocal(to_add);
        if (result == null) {
            this.rows.add(to_add);
            return true;
        } else {
            System.err.println("Error en variable " + to_add.id + ", ya ha sido declarada");
        }
        return false;
    }

    public boolean addTableRow(String id, Object value, String type, int offset) {
        TableRow to_add = new TableRow(id, value, type, offset);
        TableRow result = searchLocal(to_add);
        if (result == null) {
            this.rows.add(to_add);
            return true;
        } else {
            System.err.println("Error en variable " + id + ", ya ha sido declarada");
        }
        return false;
    }

    public void setParent(Table parent) {
        this.parent = parent;
    }

    public void addChild(Table child) {
        child.parent = this;
        childs.add(child);
    }

    public TableRow searchLocal(TableRow id) {
        for (TableRow row : rows) {
            if (row.equals(id)) {
                return row;
            }
        }
        return null;
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

    public TableRow search(String id) {
        for (TableRow row : rows) {
            if (row.id.equals(id)) {
                return row;
            }
        }
        if (parent != null) {
            return parent.search(id);
        }
        return null;
    }

    public int getActualOffset() {
        if (rows.size() > 0) {
            TableRow lastRow = rows.get(rows.size() - 1);
            return lastRow.offset + getTypeSize(lastRow.type);
        }
        return 0;
    }
    public ArrayList<Table> getChilds(){
        return childs;
    }
    public void print() {
        System.out.println("PARENT");
        for (TableRow tr : this.rows) {
            System.out.println(tr.toString());
        }

        int size = childs.size();
        System.out.println("CHILD SIZE=" + size);
        for (int i = 0; i < size; i++) {
            System.out.printf("CHILD[%d] DATA\n", i);
            childs.get(i).print();
            System.out.println();
        }
    }

    public static int getTypeSize(String type) {
        switch (type) {
            case "char":
                return 1;
            case "short":
                return 2;
            case "int":
            case "long":
            case "float":
                return 4;
            case "double":
                return 8;
        }
        if (type.contains("->") || type.contains("Pointer")) {
            return 4;
        }
        return 0;
    }
}
