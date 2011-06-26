/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package athan.src.options;

import athan.src.Client.Menu;
import athan.src.Factory.ServiceFactory;
import com.sun.lwuit.Form;

/**
 * Menu réglages des alarmes
 * 
 * @author Saad BENBOUZID
 */
public class MenuAlerts extends Menu {

    protected String getHelp() {
        return ServiceFactory.getFactory().getResourceReader()
                .get("Menu.Help");
    }

    protected String getName() {
        System.out.println(this.getClass().getName());
        return ServiceFactory.getFactory().getResourceReader()
                .get("MenuAlerts");
    }

    protected String getIconBaseName() {
        return MENU_ALARMES;
    }

    protected void execute(final Form f) {
        // TODO
    }
}
