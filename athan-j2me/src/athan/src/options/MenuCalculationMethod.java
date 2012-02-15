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
package athan.src.options;

import athan.src.Client.AthanException;
import athan.src.Client.Menu;
import athan.src.Factory.Preferences;
import athan.src.Factory.ResourceReader;
import athan.src.Factory.ServiceFactory;
import athan.src.Outils.StringOutilClient;
import athan.src.SalaahCalc.CalculationMethods;
import athan.src.SalaahCalc.SalaahTimeCalculator;
import athan.src.microfloat.Real;
import com.sun.lwuit.ComboBox;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.TextField;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.SelectionListener;
import com.sun.lwuit.impl.midp.VirtualKeyboard;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.layouts.GridLayout;
import com.sun.lwuit.table.TableLayout;
import java.util.Date;

/**
 * Menu méthodes de calcul.
 * 
 * @author Saad BENBOUZID
 */
public class MenuCalculationMethod extends Menu {

    private static final int HAUTEUR_LABEL = 20;
    private static final int HAUTEUR_LABEL_TOUS = 50;
    private Command mOK;
    private ComboBox mChoixMethode;
    private ComboBox mImsakSelector;
    private ComboBox mMaghrebSelector;
    private ComboBox mIshaaSelector;
    private ComboBox mAsrJuridiqueMethode;
    private TextField mFajrAngle;
    private TextField mImsakValue;
    private TextField mMaghrebValue;
    private TextField mIshaaValue;

    protected String getHelp() {
        return ServiceFactory.getFactory().getResourceReader().getHelpMenuCalculationMethod();
    }

    protected String getName() {
        return ServiceFactory.getFactory().getResourceReader().get("MenuCalculationMethod");
    }

    protected String getIconBaseName() {
        return MENU_METHODE_CALCUL;
    }

    private TableLayout.Constraint getCtnLayoutParams(TableLayout pTB,
            int pPourcentage,
            int pHorizontalSpan) {
        TableLayout.Constraint contrainte = pTB.createConstraint();
        if (pPourcentage == 100) {
            //contrainte.setHeightPercentage(10);
        } else {
            //contrainte.setHeightPercentage(18);
        }
        contrainte.setHorizontalSpan(pHorizontalSpan);
        contrainte.setWidthPercentage(pPourcentage);
        return contrainte;
    }

    private void choixSelectionChange() {

        if (mChoixMethode.getSelectedIndex()
                != CalculationMethods.Custom.getValue()) {

            mFajrAngle.setEditable(false);
            mImsakSelector.setEnabled(false);
            mImsakValue.setEditable(false);
            mMaghrebSelector.setEnabled(false);
            mMaghrebValue.setEditable(false);
            mIshaaSelector.setEnabled(false);
            mIshaaValue.setEditable(false);

            int methode = CalculationMethods.Jafari.getValue();

            switch (mChoixMethode.getSelectedIndex()) {
                // Jafari
                case 0:
                    methode = CalculationMethods.Jafari.getValue();
                    break;
                // Karachi
                case 1:
                    methode = CalculationMethods.Karachi.getValue();
                    break;
                // ISNA
                case 2:
                    methode = CalculationMethods.ISNA.getValue();
                    break;
                // MWL
                case 3:
                    methode = CalculationMethods.MWL.getValue();
                    break;
                // Makkah
                case 4:
                    methode = CalculationMethods.Makkah.getValue();
                    break;
                // Egypt
                case 5:
                    methode = CalculationMethods.Egypt.getValue();
                    break;
                default:
                    // type inconnu...
                    methode = CalculationMethods.Jafari.getValue();
                    break;
            }

            double[] valeurs = new SalaahTimeCalculator().getMethodParams()[methode];
            mFajrAngle.setText(Double.toString(valeurs[SalaahTimeCalculator.POS_FAJR_ANGLE]));
            mImsakSelector.setSelectedIndex((int) valeurs[SalaahTimeCalculator.POS_IMSAK_SELECTOR]);
            mImsakValue.setText(Double.toString(valeurs[SalaahTimeCalculator.POS_IMSAK_VALUE]));
            mMaghrebSelector.setSelectedIndex((int) valeurs[SalaahTimeCalculator.POS_MAGHRIB_SELECTOR]);
            mMaghrebValue.setText(Double.toString(valeurs[SalaahTimeCalculator.POS_MAGHRIB_VALUE]));
            mIshaaSelector.setSelectedIndex((int) valeurs[SalaahTimeCalculator.POS_ISHAA_SELECTOR]);
            mIshaaValue.setText(Double.toString(valeurs[SalaahTimeCalculator.POS_ISHAA_VALUE]));

        } else {
            mFajrAngle.setEditable(true);
            mImsakSelector.setEnabled(true);
            mImsakValue.setEditable(true);
            mMaghrebSelector.setEnabled(true);
            mMaghrebValue.setEditable(true);
            mIshaaSelector.setEnabled(true);
            mIshaaValue.setEditable(true);

            initialiserCustomParams();
        }
    }

    private void editerLabelCombo(Label pLabel) {
        pLabel.setUIID(UIID_LABEL_INFO_NAME);
        pLabel.getUnselectedStyle().setBgTransparency(0);
        pLabel.getSelectedStyle().setBgTransparency(0);
        pLabel.setFocusable(true);
        pLabel.setAlignment(Component.LEFT);
        pLabel.setPreferredH(HAUTEUR_LABEL);
    }

    protected void execute(final Form f) {
        final ResourceReader RESSOURCE = ServiceFactory.getFactory().getResourceReader();

        applyTactileSettings(f);

        Label lLabelChoixMethode = new Label(RESSOURCE.get("CalculationMethod"));
        editerLabelCombo(lLabelChoixMethode);

        Label lLabelAsrMethod = new Label(RESSOURCE.get("AsrJuristicMethod"));
        editerLabelCombo(lLabelAsrMethod);

        String[] imsakValues = {RESSOURCE.get("Angle"), RESSOURCE.get("MinutesBF")};
        String[] maghrebValues = {RESSOURCE.get("Angle"), RESSOURCE.get("MinutesAS")};
        String[] ishaaValues = {RESSOURCE.get("Angle"), RESSOURCE.get("MinutesAM")};

        if (ServiceFactory.getFactory().getPreferences().getLangue().equals(Preferences.LANGUE_EN)) {
            mChoixMethode = new ComboBox(CALCULATION_METHOD_EN);
        } else if (ServiceFactory.getFactory().getPreferences().getLangue().equals(Preferences.LANGUE_FR)) {
            mChoixMethode = new ComboBox(CALCULATION_METHOD_FR);
        } else {
            // Par défaut
            mChoixMethode = new ComboBox(CALCULATION_METHOD_EN);
        }
        mChoixMethode.setSelectedIndex(0);
        mChoixMethode.addSelectionListener(new SelectionListener() {

            public void selectionChanged(int oldSelected, int newSelected) {
                if (oldSelected != newSelected) {
                    choixSelectionChange();
                }
            }
        });

        mAsrJuridiqueMethode = new ComboBox(CALCULATION_ASR_METHOD);
        mAsrJuridiqueMethode.setSelectedIndex(0);

        mImsakSelector = new ComboBox(imsakValues);
        mImsakSelector.setSelectedIndex(0);
        mMaghrebSelector = new ComboBox(maghrebValues);
        mMaghrebSelector.setSelectedIndex(0);
        mIshaaSelector = new ComboBox(ishaaValues);
        mIshaaSelector.setSelectedIndex(0);

        mFajrAngle = new TextField();
        editerParametrerLabel(mFajrAngle);
        mImsakValue = new TextField();
        editerParametrerLabel(mImsakValue);
        mMaghrebValue = new TextField();
        editerParametrerLabel(mMaghrebValue);
        mIshaaValue = new TextField();
        editerParametrerLabel(mIshaaValue);

        Container ctnSaisie = new Container(new GridLayout(2, 2));
        ctnSaisie.addComponent(lLabelChoixMethode);
        ctnSaisie.addComponent(mChoixMethode);
        ctnSaisie.addComponent(lLabelAsrMethod);
        ctnSaisie.addComponent(mAsrJuridiqueMethode);
        ctnSaisie.setPreferredH(HAUTEUR_LABEL_TOUS);

        TableLayout tblLayoutParams = new TableLayout(4, 3);
        Container lCtnParams = new Container();

        lCtnParams.setLayout(tblLayoutParams);

        lCtnParams.addComponent(getCtnLayoutParams(tblLayoutParams, 60, 2),
                retournerLabelParam(RESSOURCE.get("fajrAngle")));
        lCtnParams.addComponent(getCtnLayoutParams(tblLayoutParams, 40, 1),
                mFajrAngle);

        lCtnParams.addComponent(getCtnLayoutParams(tblLayoutParams, 30, 1),
                retournerLabelParam(RESSOURCE.get("imsakParam")));
        lCtnParams.addComponent(getCtnLayoutParams(tblLayoutParams, 50, 1),
                mImsakSelector);
        lCtnParams.addComponent(getCtnLayoutParams(tblLayoutParams, 20, 1),
                mImsakValue);

        lCtnParams.addComponent(getCtnLayoutParams(tblLayoutParams, 30, 1),
                retournerLabelParam(RESSOURCE.get("maghrebParam")));
        lCtnParams.addComponent(getCtnLayoutParams(tblLayoutParams, 50, 1),
                mMaghrebSelector);
        lCtnParams.addComponent(getCtnLayoutParams(tblLayoutParams, 20, 1),
                mMaghrebValue);

        lCtnParams.addComponent(getCtnLayoutParams(tblLayoutParams, 30, 1),
                retournerLabelParam(RESSOURCE.get("ishaaParam")));
        lCtnParams.addComponent(getCtnLayoutParams(tblLayoutParams, 50, 1),
                mIshaaSelector);
        lCtnParams.addComponent(getCtnLayoutParams(tblLayoutParams, 20, 1),
                mIshaaValue);

        f.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        f.addComponent(ctnSaisie);
        f.addComponent(lCtnParams);

        mOK = new Command(RESSOURCE.get("Command.OK")) {

            public void actionPerformed(ActionEvent ae) {
                // On vérifie la saisie
                boolean contenuOk = true;
                int calculationMethod = mChoixMethode.getSelectedIndex();

                double fajrAngle = 0.0;
                int imsakSelector = mImsakSelector.getSelectedIndex();
                int maghrebSelector = mMaghrebSelector.getSelectedIndex();
                int ishaaSelector = mIshaaSelector.getSelectedIndex();
                double imsakValue = 0.0;
                double maghrebValue = 0.0;
                double ishaaValue = 0.0;

                if (calculationMethod == CalculationMethods.Custom.getValue()) {
                    try {
                        String s_fajrAngle = mFajrAngle.getText();
                        String s_imsakValue = mImsakValue.getText();
                        String s_maghrebValue = mMaghrebValue.getText();
                        String s_ishaaValue = mIshaaValue.getText();
                        if (!StringOutilClient.isEmpty(s_fajrAngle)
                                && !StringOutilClient.isEmpty(s_imsakValue)
                                && !StringOutilClient.isEmpty(s_maghrebValue)
                                && !StringOutilClient.isEmpty(s_ishaaValue)) {

                            fajrAngle = Double.parseDouble(s_fajrAngle);
                            imsakValue = Double.parseDouble(s_imsakValue);
                            maghrebValue = Double.parseDouble(s_maghrebValue);
                            ishaaValue = Double.parseDouble(s_ishaaValue);

                            if (!estAngleCorrect(fajrAngle)
                                    || (imsakSelector == 0 && !estAngleCorrect(imsakValue))
                                    || (imsakSelector == 1 && !estMinutesCorrect(imsakValue))
                                    || (maghrebSelector == 0 && !estAngleCorrect(maghrebValue))
                                    || (maghrebSelector == 1 && !estMinutesCorrect(maghrebValue))
                                    || (ishaaSelector == 0 && !estAngleCorrect(ishaaValue))
                                    || (ishaaSelector == 1 && !estMinutesCorrect(ishaaValue))) {
                                throw new AthanException("Out of bound values");
                            }

                        } else {
                            throw new AthanException("Empty values");
                        }
                    } catch (Exception exc) {
                        contenuOk = false;
                    }
                }

                if (!contenuOk) {
                    final ResourceReader RESSOURCE = ServiceFactory.getFactory().getResourceReader();

                    // Message d'erreur
                    Command okCommand = new Command(RESSOURCE.get("Command.OK"));
                    Dialog.show(RESSOURCE.get("errorTitle"), RESSOURCE.get("errorCalculationParameters"), okCommand,
                            new Command[]{okCommand}, Dialog.TYPE_ERROR, null, TIMEOUT_FENETRE_ERROR,
                            CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, true, 1000));

                    return;
                }

                try {
                    ServiceFactory.getFactory().getPreferences().set(Preferences.sCalculationMethod, Integer.toString(calculationMethod));

                    if (calculationMethod == CalculationMethods.Custom.getValue()) {
                        ServiceFactory.getFactory().getPreferences().set(Preferences.sCustomFajrAngle, Double.toString(fajrAngle));
                        ServiceFactory.getFactory().getPreferences().set(Preferences.sCustomImsakSelector, Integer.toString(imsakSelector));
                        ServiceFactory.getFactory().getPreferences().set(Preferences.sCustomImsakValue, Double.toString(imsakValue));
                        ServiceFactory.getFactory().getPreferences().set(Preferences.sCustomMaghrebSelector, Integer.toString(maghrebSelector));
                        ServiceFactory.getFactory().getPreferences().set(Preferences.sCustomMaghrebValue, Double.toString(maghrebValue));
                        ServiceFactory.getFactory().getPreferences().set(Preferences.sCustomIshaaSelector, Integer.toString(ishaaSelector));
                        ServiceFactory.getFactory().getPreferences().set(Preferences.sCustomIshaaValue, Double.toString(ishaaValue));
                    }

                    // On enregistre les paramètres dans la mémoire du téléphone
                    ServiceFactory.getFactory().getPreferences().save();

                    // On rafraîchit l'affichage des prières
                    ServiceFactory.getFactory().getVuePrincipale().rafraichir(new Date(), true, true);

                    // Message de confirmation modif
                    Command okCommand = new Command(RESSOURCE.get("Command.OK"));
                    Dialog.show(RESSOURCE.get("propertiesSavedTitle"), RESSOURCE.get("propertiesSavedContent"), okCommand,
                            new Command[]{okCommand}, Dialog.TYPE_INFO, null, TIMEOUT_CONFIRMATION_MODIF,
                            CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, true, 1000));

                    f.showBack();
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        };

        f.addCommand(mOK);
        initialiserInfosParam();
        initialiserClaviers();
    }

    private boolean estAngleCorrect(double pAngle) {
        return pAngle >= 0.0 && pAngle <= 20.0;
    }

    private boolean estMinutesCorrect(double pMinutes) {
        return (pMinutes - Math.floor(pMinutes) == 0.0)
                && pMinutes >= 0 && pMinutes <= 120;
    }

    private void editerParametrerLabel(TextArea pTextArea) {
        pTextArea.setUIID(UIID_LABEL_LOCALISATION_INFO);
        pTextArea.setAlignment(TextField.LEFT);
        pTextArea.setRows(1);
        pTextArea.setPreferredH(HAUTEUR_LABEL);
    }

    private void initialiserInfosParam() {

        initialiserCustomParams();

        int lAsrMethod = 0;
        int lCalcMethod = 0;

        try {
            lAsrMethod = Integer.parseInt(ServiceFactory.getFactory().getPreferences().get(Preferences.sMethodeJuridiqueAsr));
            lCalcMethod = Integer.parseInt(ServiceFactory.getFactory().getPreferences().get(Preferences.sCalculationMethod));
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        mAsrJuridiqueMethode.setSelectedIndex(lAsrMethod);

        mChoixMethode.setSelectedIndex(lCalcMethod);
        choixSelectionChange();
    }

    private void initialiserCustomParams() {

        String lFajrValue = "0";
        int lImsakSelector = 0;
        int lMaghrebSelector = 0;
        int lIshaaSelector = 0;
        String lImsakValue = "0";
        String lMaghrebValue = "0";
        String lIshaaValue = "0";

        try {
            lFajrValue = ServiceFactory.getFactory().getPreferences().get(Preferences.sCustomFajrAngle);
            lImsakSelector = Integer.parseInt(ServiceFactory.getFactory().getPreferences().get(Preferences.sCustomImsakSelector));
            lImsakValue = ServiceFactory.getFactory().getPreferences().get(Preferences.sCustomImsakValue);
            lMaghrebSelector = Integer.parseInt(ServiceFactory.getFactory().getPreferences().get(Preferences.sCustomMaghrebSelector));
            lMaghrebValue = ServiceFactory.getFactory().getPreferences().get(Preferences.sCustomMaghrebValue);
            lIshaaSelector = Integer.parseInt(ServiceFactory.getFactory().getPreferences().get(Preferences.sCustomIshaaSelector));
            lIshaaValue = ServiceFactory.getFactory().getPreferences().get(Preferences.sCustomIshaaValue);

        } catch (Exception exc) {
            exc.printStackTrace();
        }

        mFajrAngle.setText(lFajrValue);
        mImsakSelector.setSelectedIndex(lImsakSelector);
        if (lImsakSelector == 0) {
            mImsakValue.setText(lImsakValue);
        } else {
            Real rImsakValue = new Real(lImsakValue);
            mImsakValue.setText(new Integer(rImsakValue.toInteger()).toString());
        }
        mMaghrebSelector.setSelectedIndex(lMaghrebSelector);
        if (lMaghrebSelector == 0) {
            mMaghrebValue.setText(lMaghrebValue);
        } else {
            Real rMaghrebValue = new Real(lMaghrebValue);
            mMaghrebValue.setText(new Integer(rMaghrebValue.toInteger()).toString());
        }
        mIshaaSelector.setSelectedIndex(lIshaaSelector);
        if (lIshaaSelector == 0) {
            mIshaaValue.setText(lIshaaValue);
        } else {
            Real rIshaaValue = new Real(lIshaaValue);
            mIshaaValue.setText(new Integer(rIshaaValue.toInteger()).toString());
        }
    }

    private void initialiserClaviers() {
        VirtualKeyboard vkbAngles = new VirtualKeyboard();
        vkbAngles.addInputMode(KB_FLOATS_MODE, KB_FLOATS);
        vkbAngles.setInputModeOrder(new String[]{KB_FLOATS_MODE});
        VirtualKeyboard.bindVirtualKeyboard(mFajrAngle, vkbAngles);
        VirtualKeyboard.bindVirtualKeyboard(mImsakValue, vkbAngles);
        VirtualKeyboard.bindVirtualKeyboard(mMaghrebValue, vkbAngles);
        VirtualKeyboard.bindVirtualKeyboard(mIshaaValue, vkbAngles);
    }

    private Label retournerLabelParam(String pTitle) {
        Label lLabel = new Label(pTitle);
        lLabel.setUIID(UIID_LABEL_INFO_NAME);
        lLabel.getUnselectedStyle().setBgTransparency(0);
        lLabel.getSelectedStyle().setBgTransparency(0);
        lLabel.setFocusable(true);
        lLabel.setAlignment(Component.LEFT);
        lLabel.setPreferredH(HAUTEUR_LABEL);

        return lLabel;
    }

    protected void cleanup() {
    }
}
