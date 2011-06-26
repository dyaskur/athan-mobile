/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package athan.src.options;

import athan.src.Client.Menu;
import athan.src.Factory.ServiceFactory;
import com.sun.lwuit.Button;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.layouts.BoxLayout;

/**
 * Menu d'affichage du compas
 * 
 * @author Saad BENBOUZID
 */
public class MenuCompass extends Menu {

    protected String getHelp() {
        return ServiceFactory.getFactory().getResourceReader()
                .get("Menu.Help");
    }

    protected String getName() {
        return ServiceFactory.getFactory().getResourceReader()
                .get("MenuCompass");
    }

    protected void execute(final Form f) {
        // TODO
        f.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        f.addComponent(new Button("Test"));
        f.addComponent(new Label("Test"));
        f.addComponent(new Label("Test"));
        f.addComponent(new Button("Test"));
        f.addComponent(new Label("Test"));
        f.addComponent(new Label("Test"));
        f.addComponent(new Button("Test"));
        f.addComponent(new Label("Test"));
        f.addComponent(new Label("Test"));
        f.addComponent(new Button("Test"));
        f.addComponent(new Label("Test"));
        f.addComponent(new Label("Test"));
        f.addComponent(new Button("Test"));
        f.addComponent(new Label("Test"));
        f.addComponent(new Label("Test"));
        f.addComponent(new Button("Test"));
        f.addComponent(new Label("Test"));
        f.addComponent(new Label("Test"));
        f.addComponent(new Button("Test"));
        f.addComponent(new Label("Test"));
        f.addComponent(new Label("Test"));
    }

    protected String getIconBaseName() {
        return MENU_AFFICHAGE_COMPAS;
    }
}
