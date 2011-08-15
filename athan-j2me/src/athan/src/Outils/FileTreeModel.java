/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package athan.src.Outils;

import com.sun.lwuit.tree.TreeModel;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;

/**
 *
 * @author BENBOUZID
 */
public class FileTreeModel implements TreeModel {
    
    public Vector getChildren(Object parent) {
        Vector response = new Vector();

        try {
            if(parent == null) {
                Enumeration e = FileSystemRegistry.listRoots();
                while(e.hasMoreElements()) {
                    response.addElement("file:///" + e.nextElement());
                }
            } else {
                String name = (String)parent;
                FileConnection c = (FileConnection)Connector.open(name, Connector.READ);
                Enumeration e = c.list();
                while(e.hasMoreElements()) {
                    response.addElement(name + e.nextElement());
                }
                c.close();
            }
        } catch(Throwable err) {
            err.printStackTrace();
            return getChildren(parent);
        }
        return response;
    }

    public boolean isLeaf(Object node) {
        boolean d = true;
        try {
            FileConnection c = (FileConnection)Connector.open((String)node, Connector.READ);
            d = c.isDirectory();
            c.close();
        } catch(Throwable err) {
            err.printStackTrace();
            return isLeaf(node);
        }
        return !d;
    }
}
