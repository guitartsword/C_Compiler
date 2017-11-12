/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.util.Objects;

/**
 *
 * @author alvarez
 */
public class TableRow {

    String id;
    Object value;
    String type;

    public TableRow() {
        this.id =  "";
        this.value = null;
        this.type = "";
    }

    public TableRow(String id, Object value, String type) {
        this.id = id;
        this.value = value;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TableRow) {
            TableRow v = (TableRow) o;
            return (this == v) || (this.id.equals(v.id) && this.type.equals(v.type));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.id);
        hash = 73 * hash + Objects.hashCode(this.type);
        return hash;
    }

    @Override
    public String toString() {
        return "id= " + id + ", value= " + value + ", type= " + type;
    }

}
