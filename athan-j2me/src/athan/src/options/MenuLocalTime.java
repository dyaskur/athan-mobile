/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package athan.src.options;

import athan.src.Client.AthanException;
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
import com.sun.lwuit.layouts.GridLayout;
import java.util.Date;

/**
 * Menu de configuration de l'heure locale
 * 
 * @author Saad BENBOUZID
 */
public class MenuLocalTime extends Menu {

    private static final int HAUTEUR_LABEL = 18;
    private static final int HAUTEUR_LABEL_TOUS = 60;

    private Command mOK;

    private TextField mDecalage;
    private ComboBox mFormatHoraire;

    protected String getHelp() {
        return ServiceFactory.getFactory().getResourceReader()
                .get("Menu.Help");
    }

    protected String getName() {
        return ServiceFactory.getFactory().getResourceReader()
                .get("MenuLocalTime");
    }

    protected String getIconBaseName() {
        return MENU_CONFIG_HEURE_LOCALE;
    }

    private void editerTextField(TextField pTextField) {
        pTextField.setUIID(UIID_LABEL_LOCALISATION_INFO);
        pTextField.setAlignment(TextField.LEFT);
        pTextField.setRows(1);
        pTextField.setPreferredH(HAUTEUR_LABEL);
    }

    protected void execute(final Form f) {
        final ResourceReader RESSOURCE = ServiceFactory.getFactory()
                            .getResourceReader();

        applyTactileSettings(f);

        Label lLabelDecalage = new Label(RESSOURCE.get("TimeLag"));
        editerLabel(lLabelDecalage);
        Label lLabelFormatHoraire = new Label(RESSOURCE.get("TimeFormat"));
        editerLabel(lLabelFormatHoraire);

        mDecalage = new TextField();
        editerTextField(mDecalage);

        mFormatHoraire = new ComboBox(TIME_FORMAT);

        Container ctnSaisie = new Container(new GridLayout(2, 2));
        ctnSaisie.addComponent(lLabelDecalage);
        ctnSaisie.addComponent(mDecalage);
        ctnSaisie.addComponent(new Label(""));
        ctnSaisie.addComponent(new Label(""));
        ctnSaisie.addComponent(lLabelFormatHoraire);
        ctnSaisie.addComponent(mFormatHoraire);
        ctnSaisie.setPreferredH(HAUTEUR_LABEL_TOUS);
        //ctnSaisie.setScrollable(true);

        f.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        f.addComponent(new Label());
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
                        if (Math.abs(decalage) > 12) {
                            throw new AthanException("> 12");
                        }
                    } else {
                        throw new AthanException("empty");
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
                        .set(Preferences.sFormatHoraire, Integer.toString(mFormatHoraire.getSelectedIndex()));

                    // On enregistre les paramètres dans la mémoire du téléphone
                    ServiceFactory.getFactory().getPreferences()
                            .save();

                    // On rafraîchit l'affichage des prières
                    ServiceFactory.getFactory().getVuePrincipale()
                            .rafraichir(new Date(), true, true);

                    // Message de confirmation modif
                    Command okCommand = new Command(RESSOURCE.get("Command.OK"));
                    Dialog.show(RESSOURCE.get("propertiesSavedTitle"), RESSOURCE.get("propertiesSavedContent"), okCommand,
                            new Command[] {okCommand}, Dialog.TYPE_INFO, null, TIMEOUT_CONFIRMATION_MODIF,
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
        int lFormatHoraire = 0;

        try {
            lDecalage = ServiceFactory.getFactory().getPreferences()
                        .get(Preferences.sDecalageHoraire);
            lFormatHoraire = Integer.parseInt(ServiceFactory.getFactory().getPreferences()
                        .get(Preferences.sFormatHoraire));
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        mDecalage.setText(lDecalage);
        mFormatHoraire.setSelectedIndex(lFormatHoraire);
    }

    private void initialiserClaviers() {
        VirtualKeyboard vkbEntier = new VirtualKeyboard();
        vkbEntier.addInputMode(KB_INTEGER_MODE, KB_INTEGER);
        vkbEntier.setInputModeOrder(new String[]{KB_INTEGER_MODE});
        VirtualKeyboard.bindVirtualKeyboard(mDecalage, vkbEntier);
    }

    private void editerLabel(Label pLabel) {
        pLabel.setUIID(UIID_LABEL_INFO_NAME);
        pLabel.getUnselectedStyle().setBgTransparency(0);
        pLabel.getSelectedStyle().setBgTransparency(0);
        pLabel.setFocusable(true);
        pLabel.setAlignment(Component.LEFT);
        pLabel.setPreferredH(HAUTEUR_LABEL);
    }
}
