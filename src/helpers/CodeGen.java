/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFileChooser;
/**
 *
 * @author Isaias Valle
 */
public class CodeGen {
    private File file;
    private String mipsHeader = "";
    public CodeGen(File file){
        this.file = file;
    }
    //icr = intermediate code representation
    public void generateCode(TableQuad icr, Table symbolsTable) throws IOException{
        FileOutputStream fileOut = new FileOutputStream(file);
        fileOut.write(mipsHeader.getBytes());
        ArrayList<TableRowQuad> tr = icr.getRows();
        int paramCount = 0;
        for(TableRowQuad row: tr){
            String op = row.op;
            
            switch(op){
                case "genetiq":{
                    fileOut.write((row.arg1+"\n").getBytes());
                    break;
                }
                case "param":{
                    String toWrite = "$a" + paramCount;
                    if(row.arg1.equals("_msgstring"))
                        toWrite += ", _msg"+paramCount;
                    else
                        toWrite += ", " + row.arg1;
                    toWrite = "move " + toWrite + "\n";
                    paramCount++;
                    fileOut.write(toWrite.getBytes());
                }
            }
        }
        fileOut.close();
    }
    public void setMipsHeader(String append){
        mipsHeader += append;
    }
}
