//    Athan Mobile - Prayer Times Software
//    Copyright (C) 2011 - Saad BENBOUZID
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.
package athan.src.Outils;

import com.sun.lwuit.tree.TreeModel;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;

/**
 * Modèle pour la récupération récursive de l'ensemble des répertoires
 * et fichiers du mobile.
 *
 * @author Saad BENBOUZID
 */
public class FileTreeModel implements TreeModel {

    public Vector getChildren(Object parent) {

        Vector response = new Vector();

        try {
            if (parent == null) {
                Enumeration e = FileSystemRegistry.listRoots();
                while (e.hasMoreElements()) {
                    Object obj = e.nextElement();
                    response.addElement("file:///" + obj);
                }
            } else {
                String name = (String) parent;
                FileConnection c = (FileConnection) Connector.open(name, Connector.READ);
                Enumeration e = c.list();
                while (e.hasMoreElements()) {
                    Object obj = e.nextElement();
                    response.addElement(name + obj);
                }
                c.close();
            }
        } catch (Throwable err) {
            err.printStackTrace();
            return getChildren(parent);
        }
        return response;
    }

    public boolean isLeaf(Object node) {
        boolean d = true;
        try {
            FileConnection c = (FileConnection) Connector.open((String) node, Connector.READ);
            d = c.isDirectory();
            c.close();
        } catch (Throwable err) {
            err.printStackTrace();
            return isLeaf(node);
        }
        return !d;
    }
}
