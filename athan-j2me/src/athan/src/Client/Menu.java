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
package athan.src.Client;

import athan.src.Factory.ResourceReader;
import athan.src.Factory.ServiceFactory;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.geom.Dimension;

/**
 * Classe parente de toutes les fenêtres de l'application,
 * dont la fenêtre principale et les fenêtres de menus.
 * 
 * @author Saad BENBOUZID
 */
public abstract class Menu
        extends AthanConstantes {

    private Form mForm;
    private Command mBackCommand;
    private Command mHelpCommand;

    /**
     * returns the name of the icon base
     */
    protected abstract String getName();

    /**
     * returns the name of the icon base
     */
    protected abstract String getIconBaseName();

    /**
     * Invoked by the main code to start the demo
     */
    public final void run(final Command backCommand,
            ActionListener commandListener,
            boolean showHelp) {
        System.gc();

        final ResourceReader RESOURCE = ServiceFactory.getFactory().getResourceReader();

        mForm = new Form(getName());

        if (showHelp) {

            mHelpCommand = new Command(RESOURCE.get("Menu.Help")) {

                public void actionPerformed(ActionEvent evt) {
                    Form helpForm = new Form(RESOURCE.get("Window.Help"));
                    helpForm.setLayout(new BorderLayout());
                    TextArea helpText = new TextArea(getHelp(), 5, 10);
                    helpText.setEditable(false);
                    helpText.setUIID(UIID_TEXTAREA_HELP);
                    helpForm.setScrollable(true);
                    helpForm.addComponent(BorderLayout.CENTER, helpText);
                    Command c = new Command(RESOURCE.get("Menu.Back")) {

                        public void actionPerformed(ActionEvent evt) {
                            mForm.show();
                        }
                    };
                    helpForm.addCommand(c);
                    helpForm.setBackCommand(c);
                    helpForm.show();
                }
            };
        }

        // Commandes
        int posCmd = 0;
        mBackCommand = backCommand;

        mForm.addCommandListener(commandListener);

        if (mHelpCommand != null) {
            mForm.addCommand(mHelpCommand, posCmd++);
        }
        mForm.addCommand(mBackCommand, posCmd++);
        mForm.setBackCommand(backCommand);
        execute(mForm);
        mForm.show();
    }

    /**
     * Replace les commandes principales (retour et help)
     * en fin de panel menu, afin qu'au moins la commande de
     * retour soit la (dernière) plus visible
     */
    public void replacerCommandesPrincipales() {

        mForm.removeCommand(mBackCommand);
        if (mHelpCommand != null) {
            mForm.removeCommand(mHelpCommand);
        }

        int posCmd = mForm.getCommandCount();
        if (mHelpCommand != null) {
            mForm.addCommand(mHelpCommand, posCmd++);
        }
        mForm.addCommand(mBackCommand, posCmd++);
    }

    /**
     * Returns the text that should appear in the help command
     */
    protected abstract String getHelp();

    /**
     * The demo should place its UI into the given form 
     */
    protected abstract void execute(final Form f);

    /**
     * Helper method that allows us to create a pair of components label and the given
     * component in a horizontal layout with a minimum label width
     */
    protected Container createPair(String label, Component c, int minWidth) {
        Container pair = new Container(new BorderLayout());
        Label l = new Label(label);
        Dimension d = l.getPreferredSize();
        d.setWidth(Math.max(d.getWidth(), minWidth));
        l.setPreferredSize(d);
        pair.addComponent(BorderLayout.WEST, l);
        pair.addComponent(BorderLayout.CENTER, c);
        return pair;
    }

    /**
     * Helper method that allows us to create a pair of components label and the given
     * component in a horizontal layout
     */
    protected Container createPair(String label, Component c) {
        return createPair(label, c, 0);
    }

    /**
     * Méthode de callback sur clic du bouton retour
     */
    protected abstract void cleanup();

    public Form getForm() {
        return mForm;
    }

    protected void applyTactileSettings(Form pForm) {
        if (Main.isTactile()) {
            pForm.setTactileTouch(true);
            pForm.setSmoothScrolling(true);
        } else {
            pForm.setTactileTouch(false);
            pForm.setSmoothScrolling(false);
            pForm.setFocusScrolling(true);
        }
    }
}
