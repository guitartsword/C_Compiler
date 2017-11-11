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

    String id;
    Table father;
    ArrayList<Variable> variables;

    public Table(String id) {
        this.id = id;
        this.father = null;
        this.variables = new ArrayList();
    }

    public boolean addVariable(Variable v) {
        if (!this.variables.contains(v)) {
            this.variables.add(v);
            return true;
        }
        return false;
    }

    public boolean addVariable(String id, Object value, String type) {
        Variable v = new Variable(id, value, type);
        if (!this.variables.contains(v)) {
            this.variables.add(v);
            return true;
        }
        return false;
    }

    public void addFather(Table t) {
        this.father = t;
    }

    public void addFather(String id) {
        Table t = new Table(id);
        this.father = t;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Table) {
            Table t = (Table) o;
            return this == t || this.id.equals(t.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }
}
