/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
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
import com.sun.lwuit.plaf.UIManager;
import com.sun.lwuit.geom.Dimension;

/**
 * Base class for a demo contains common code for all demo pages
 * 
 * @author BENBOUZID
 */
public abstract class Menu
           extends AthanConstantes {

    private Form mForm;

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
            mForm.addCommand(new Command(RESOURCE.get("Menu.Help")) {
                public void actionPerformed(ActionEvent evt) {
                    Form helpForm = new Form(RESOURCE.get("Window.Help"));
                    helpForm.setLayout(new BorderLayout());
                    TextArea helpText = new TextArea(getHelpImpl(), 5, 10);
                    helpText.setEditable(false);
                    helpForm.setScrollable(false);
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
            });
        }
        mForm.addCommand(backCommand);
        mForm.setCommandListener(commandListener);
        mForm.setBackCommand(backCommand);
        execute(mForm);
        mForm.show();
    }
    
    /**
     * Returns the text that should appear in the help command
     */
    private String getHelpImpl() {
        String h = getHelp();
        return UIManager.getInstance().localize(h, h);
    }

    /**
     * Returns the text that should appear in the help command
     */
    protected String getHelp() {
        // return a key value for localization
        String n = getClass().getName();
        return n.substring(n.lastIndexOf('.') + 1) + ".help";
    }
    
    /**
     * The demo should place its UI into the given form 
     */
    protected abstract void execute(Form f);

  
    /**
     * Helper method that allows us to create a pair of components label and the given
     * component in a horizontal layout with a minimum label width
     */
    protected Container createPair(String label, Component c, int minWidth) {
        Container pair = new Container(new BorderLayout());
        Label l =  new Label(label);
        Dimension d = l.getPreferredSize();
        d.setWidth(Math.max(d.getWidth(), minWidth));
        l.setPreferredSize(d);
        pair.addComponent(BorderLayout.WEST,l);
        pair.addComponent(BorderLayout.CENTER, c);
        return pair;
    }
    
      /**
     * Helper method that allows us to create a pair of components label and the given
     * component in a horizontal layout
     */
     protected Container createPair(String label, Component c) {
         return createPair(label,c,0);
     }
    
     public void cleanup() {
     }

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
