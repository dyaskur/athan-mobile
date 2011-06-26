/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package athan.src.options;

import athan.src.Client.Menu;
import athan.src.Factory.ServiceFactory;
import com.sun.lwuit.Form;

/**
 * Menu correction du calendrier
 * 
 * @author Saad BENBOUZID
 */
public class MenuCalendarCorrection extends Menu {

    protected String getHelp() {
        return ServiceFactory.getFactory().getResourceReader()
                .get("Menu.Help");
    }

    protected String getName() {
        return ServiceFactory.getFactory().getResourceReader()
                .get("MenuCalendarCorrection");
    }

    protected String getIconBaseName() {
        return MENU_CORRECTION_CALENDRIER;
    }

    protected void execute(final Form f) {
       // TODO
    }
}
