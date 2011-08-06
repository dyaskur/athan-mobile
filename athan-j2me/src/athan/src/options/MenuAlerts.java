/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package athan.src.options;

import athan.src.Client.Main;
import athan.src.Client.Menu;
import athan.src.Factory.ResourceReader;
import athan.src.Factory.ServiceFactory;
import com.sun.lwuit.Button;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextField;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.table.TableLayout;

/**
 * Menu réglages des alarmes
 * 
 * @author Saad BENBOUZID
 */
public class MenuAlerts extends Menu {

    private static final int HAUTEUR_LABEL = 15;
    private static final int HAUTEUR_LABEL_TOUS = 120;

    private static final int POURCENTAGE_LABEL = 25;
    private static final int POURCENTAGE_TEXTFIELD = 50;
    private static final int POURCENTAGE_BROWSE = 25;

    private static final String IMAGE_BROWSE_SONG = "BrowseSong";

    private TextField mAlertSongSobh;
    private TextField mAlertSongDohr;
    private TextField mAlertSongAsr;
    private TextField mAlertSongMaghreb;
    private TextField mAlertSongIshaa;

    private Button mChoixSobh;
    private Button mChoixDohr;
    private Button mChoixAsr;
    private Button mChoixMaghreb;
    private Button mChoixIshaa;

    protected String getHelp() {
        return ServiceFactory.getFactory().getResourceReader()
                .get("Menu.Help");
    }

    protected String getName() {
        return ServiceFactory.getFactory().getResourceReader()
                .get("MenuAlerts");
    }

    protected String getIconBaseName() {
        return MENU_ALARMES;
    }

    protected void execute(final Form f) {
        final ResourceReader RESSOURCE = ServiceFactory.getFactory()
                            .getResourceReader();

        applyTactileSettings(f);

        TableLayout tblLayoutInfosLocalisation = new TableLayout(6, 4);
        Container ctsAlerts = new Container();
        ctsAlerts.setLayout(tblLayoutInfosLocalisation);

        Label lLabelSobh = new Label(RESSOURCE.get("Sobh"));
        editerLabelNomPriere(lLabelSobh);
        Label lLabelDohr = new Label(RESSOURCE.get("Dohr"));
        editerLabelNomPriere(lLabelDohr);
        Label lLabelAsr = new Label(RESSOURCE.get("Asr"));
        editerLabelNomPriere(lLabelAsr);
        Label lLabelMaghreb = new Label(RESSOURCE.get("Maghreb"));
        editerLabelNomPriere(lLabelMaghreb);
        Label lLabelIshaa = new Label(RESSOURCE.get("Ishaa"));
        editerLabelNomPriere(lLabelIshaa);

        mAlertSongSobh = new TextField();
        editerAlertSong(mAlertSongSobh);
        mAlertSongDohr = new TextField();
        editerAlertSong(mAlertSongDohr);
        mAlertSongAsr = new TextField();
        editerAlertSong(mAlertSongAsr);
        mAlertSongMaghreb = new TextField();
        editerAlertSong(mAlertSongMaghreb);
        mAlertSongIshaa = new TextField();
        editerAlertSong(mAlertSongIshaa);

        mChoixSobh = new Button(Main.icons.getImage(IMAGE_BROWSE_SONG));
        mChoixDohr = new Button(Main.icons.getImage(IMAGE_BROWSE_SONG));
        mChoixAsr = new Button(Main.icons.getImage(IMAGE_BROWSE_SONG));
        mChoixMaghreb = new Button(Main.icons.getImage(IMAGE_BROWSE_SONG));
        mChoixIshaa = new Button(Main.icons.getImage(IMAGE_BROWSE_SONG));

        ctsAlerts.addComponent(getCtnLayoutAlerts(tblLayoutInfosLocalisation, 100, 4),
                                    new Label());
        ctsAlerts.addComponent(getCtnLayoutAlerts(tblLayoutInfosLocalisation, POURCENTAGE_LABEL, 1),
                                    lLabelSobh);
        ctsAlerts.addComponent(getCtnLayoutAlerts(tblLayoutInfosLocalisation, POURCENTAGE_TEXTFIELD, 2),
                                    mAlertSongSobh);
        ctsAlerts.addComponent(getCtnLayoutAlerts(tblLayoutInfosLocalisation, POURCENTAGE_BROWSE, 1),
                                    mChoixSobh);
        ctsAlerts.addComponent(getCtnLayoutAlerts(tblLayoutInfosLocalisation, POURCENTAGE_LABEL, 1),
                                    lLabelDohr);
        ctsAlerts.addComponent(getCtnLayoutAlerts(tblLayoutInfosLocalisation, POURCENTAGE_TEXTFIELD, 2),
                                    mAlertSongDohr);
        ctsAlerts.addComponent(getCtnLayoutAlerts(tblLayoutInfosLocalisation, POURCENTAGE_BROWSE, 1),
                                    mChoixDohr);
        ctsAlerts.addComponent(getCtnLayoutAlerts(tblLayoutInfosLocalisation, POURCENTAGE_LABEL, 1),
                                    lLabelAsr);
        ctsAlerts.addComponent(getCtnLayoutAlerts(tblLayoutInfosLocalisation, POURCENTAGE_TEXTFIELD, 2),
                                    mAlertSongAsr);
        ctsAlerts.addComponent(getCtnLayoutAlerts(tblLayoutInfosLocalisation, POURCENTAGE_BROWSE, 1),
                                    mChoixAsr);
        ctsAlerts.addComponent(getCtnLayoutAlerts(tblLayoutInfosLocalisation, POURCENTAGE_LABEL, 1),
                                    lLabelMaghreb);
        ctsAlerts.addComponent(getCtnLayoutAlerts(tblLayoutInfosLocalisation, POURCENTAGE_TEXTFIELD, 2),
                                    mAlertSongMaghreb);
        ctsAlerts.addComponent(getCtnLayoutAlerts(tblLayoutInfosLocalisation, POURCENTAGE_BROWSE, 1),
                                    mChoixMaghreb);
        ctsAlerts.addComponent(getCtnLayoutAlerts(tblLayoutInfosLocalisation, POURCENTAGE_LABEL, 1),
                                    lLabelIshaa);
        ctsAlerts.addComponent(getCtnLayoutAlerts(tblLayoutInfosLocalisation, POURCENTAGE_TEXTFIELD, 2),
                                    mAlertSongIshaa);
        ctsAlerts.addComponent(getCtnLayoutAlerts(tblLayoutInfosLocalisation, POURCENTAGE_BROWSE, 1),
                                    mChoixIshaa);
        ctsAlerts.setPreferredH(HAUTEUR_LABEL_TOUS);
        
        f.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        f.addComponent(ctsAlerts);
    }

    private void editerLabelNomPriere(Label pLabel) {
        pLabel.setUIID(UIID_LABEL_INFO_NAME);
        pLabel.getUnselectedStyle().setBgTransparency(0);
        pLabel.getSelectedStyle().setBgTransparency(0);
        pLabel.setFocusable(true);
        pLabel.setAlignment(Component.LEFT);
    }

    private void editerAlertSong(TextField pTextField) {
        pTextField.setUIID(UIID_LABEL_ALERTSONG_INFO);
        pTextField.setAlignment(TextField.LEFT);
        pTextField.setRows(1);
        pTextField.setFocusable(true);
        pTextField.setPreferredH(HAUTEUR_LABEL);
    }

    private TableLayout.Constraint getCtnLayoutAlerts(TableLayout pTB,
                                                    int pPourcentage,
                                                    int pHorizontalSpan) {
        TableLayout.Constraint contrainte = pTB.createConstraint();
        if (pPourcentage == 100) {
            contrainte.setHeightPercentage(10);
        } else {
            contrainte.setHeightPercentage(18);
        }
        contrainte.setHorizontalSpan(pHorizontalSpan);
        contrainte.setWidthPercentage(pPourcentage);
        return contrainte;
    }
}
