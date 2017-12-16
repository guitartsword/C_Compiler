/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.util.Objects;

/**
 *
 * @author alvarez
 */
public class TableRow {

    public String id;
    public Object value;
    public String type;
    public int offset;

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
    
    public TableRow(String id, Object value, String type, int offset) {
        this.id = id;
        this.value = value;
        this.type = type;
        this.offset = offset;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TableRow) {
            TableRow v = (TableRow) o;
            return (this == v) || this.id.equals(v.id);
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
        return "id= " + id + ", value= " + value + ", type= " + type + ", offset= " + offset;
    }

}
