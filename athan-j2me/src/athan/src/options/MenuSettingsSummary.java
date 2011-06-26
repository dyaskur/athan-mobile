/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package athan.src.options;

import athan.src.Client.Menu;
import athan.src.Factory.ServiceFactory;
import com.sun.lwuit.Form;

/**
 * Menu récapitulatif des configurations
 * 
 * @author Saad BENBOUZID
 */
public class MenuSettingsSummary extends Menu {

    protected String getHelp() {
        return ServiceFactory.getFactory().getResourceReader()
                .get("Menu.Help");
    }

    protected String getIconBaseName() {
        return MENU_RECAPITULATIF_MODES;
    }

    protected String getName() {
        return ServiceFactory.getFactory().getResourceReader()
                .get("MenuSettingsSummary");
    }

    protected void execute(final Form f) {
        // TODO
    }
}
