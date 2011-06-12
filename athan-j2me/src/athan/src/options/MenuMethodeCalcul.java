/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package athan.src.options;

import athan.src.Client.Main;
import athan.src.Client.Menu;
import athan.src.Factory.Preferences;
import athan.src.Factory.ResourceReader;
import athan.src.Factory.ServiceFactory;
import athan.src.Outils.StringOutilClient;
import com.sun.lwuit.ComboBox;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextField;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.impl.midp.VirtualKeyboard;
import com.sun.lwuit.layouts.BoxLayout;

/**
 * Menu méthode de calcul
 * 
 * @author Saad BENBOUZID
 */
public class MenuMethodeCalcul extends Menu {

    private static final int HAUTEUR_LABEL = 18;
    private static final int HAUTEUR_LABEL_TOUS = 25;

    private Command mOK;

    private TextField mDecalage;

    public String getName() {
        return "Calcul";
    }

    public String getIconBaseName() {
        return MENU_METHODE_CALCUL;
    }

    protected String getHelp() {
        return "Aide";
    }

    protected void execute(final Form f) {
        final ResourceReader RESSOURCE = ServiceFactory.getFactory()
                            .getResourceReader();

        if (Main.isTactile()) {
            f.setTactileTouch(true);
            f.setSmoothScrolling(true);
        } else {
            f.setTactileTouch(false);
            f.setSmoothScrolling(false);
            f.setFocusScrolling(true);
        }

        Label lLabelDecalage = new Label(RESSOURCE.get("TimeLag"));
        lLabelDecalage.setUIID(UIID_LABEL_INFO_NAME);
        lLabelDecalage.getUnselectedStyle().setBgTransparency(0);
        lLabelDecalage.getSelectedStyle().setBgTransparency(0);
        lLabelDecalage.setFocusable(true);
        lLabelDecalage.setAlignment(Component.LEFT);
        lLabelDecalage.setPreferredH(HAUTEUR_LABEL);

        mDecalage = new TextField();
        mDecalage.setUIID(UIID_LABEL_LOCALISATION_INFO);
        mDecalage.setAlignment(TextField.LEFT);
        mDecalage.setRows(1);
        mDecalage.setPreferredH(HAUTEUR_LABEL);

        Container ctnSaisie = new Container(new BoxLayout(BoxLayout.X_AXIS));
        ctnSaisie.addComponent(lLabelDecalage);
        ctnSaisie.addComponent(mDecalage);
        ctnSaisie.setPreferredH(HAUTEUR_LABEL_TOUS);

        f.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        f.addComponent(ctnSaisie);

        // Gestion du comportement (ergonomie)
        if (!Main.isTactile()) {
           //mOK.setFocusable(true);
        }

        mOK = new Command(RESSOURCE.get("Command.OK")) {

            public void actionPerformed(ActionEvent ae) {
                // On vérifie la saisie
                boolean contenuOk = true;
                int decalage = 0;
                try {
                    String s_decalage = mDecalage.getText();
                    if (!StringOutilClient.isEmpty(s_decalage)) {
                        decalage = Integer.parseInt(s_decalage);
                    } else {
                        contenuOk = false;
                    }
                } catch(Exception exc) {
                    contenuOk = false;
                }

                if (!contenuOk) {
                    final ResourceReader RESSOURCE = ServiceFactory.getFactory()
                                    .getResourceReader();

                    // Message d'erreur
                    Command okCommand = new Command(RESSOURCE.get("Command.OK"));
                    Dialog.show(RESSOURCE.get("errorTitle"), RESSOURCE.get("errorTimeLagParameters"), okCommand,
                            new Command[] {okCommand}, Dialog.TYPE_ERROR, null, TIMEOUT_FENETRE_ERROR,
                            CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, true, 1000));
                    return;
                }

                try {
                    ServiceFactory.getFactory().getPreferences()
                        .set(Preferences.sDecalageHoraire, Integer.toString(decalage));

                    ServiceFactory.getFactory().getPreferences()
                            .save();

                    // Message de confirmation modif
                    Command okCommand = new Command(RESSOURCE.get("Command.OK"));
                    Dialog.show(RESSOURCE.get("propertiesSavedTitle"), RESSOURCE.get("propertiesSavedContent"), okCommand,
                            new Command[] {okCommand}, Dialog.TYPE_INFO, null, 2000,
                            CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, true, 1000));
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        };

        f.addCommand(mOK);
        initialiserInfosDecalage();
        initialiserClaviers();
    }

    private void initialiserInfosDecalage() {

        String lDecalage = "0";

        try {
            lDecalage = ServiceFactory.getFactory().getPreferences()
                        .get(Preferences.sDecalageHoraire);

        } catch (Exception exc) {
            exc.printStackTrace();
        }

        mDecalage.setText(lDecalage);
    }

    private void initialiserClaviers() {
        VirtualKeyboard vkbEntier = new VirtualKeyboard();
        vkbEntier.addInputMode(KB_INTEGER_MODE, KB_INTEGER);
        vkbEntier.setInputModeOrder(new String[]{KB_INTEGER_MODE});
        VirtualKeyboard.bindVirtualKeyboard(mDecalage, vkbEntier);
    }
}
