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
public class Variable {

    String id;
    Object value;
    String type;

    public Variable(String id, Object value, String type) {
        this.id = id;
        this.value = value;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Variable) {
            Variable v = (Variable) o;
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
}
