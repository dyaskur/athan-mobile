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

import athan.src.Client.Menu;
import athan.src.Factory.Preferences;
import athan.src.Factory.ResourceReader;
import athan.src.Factory.ServiceFactory;
import athan.src.Outils.StringOutilClient;
import com.sun.lwuit.CheckBox;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.layouts.GridLayout;
import java.util.Date;

/**
 * Menu de choix des prières non obligatoires à afficher ou non
 * dans la fenêtre principale.
 * 
 * @author Saad BENBOUZID
 */
public class MenuPrayers extends Menu {

    private static final int HAUTEUR_LABEL = 22;
    private static final int HAUTEUR_LABEL_TOUS = 70;
    private CheckBox mImsak;
    private CheckBox mChourouk;
    private Command mOK;

    protected String getHelp() {
        return ServiceFactory.getFactory().getResourceReader().getHelpMenuPrayers();
    }

    protected String getName() {
        return ServiceFactory.getFactory().getResourceReader().get("MenuPrayers");
    }

    protected void execute(final Form f) {
        final ResourceReader RESSOURCE = ServiceFactory.getFactory().getResourceReader();

        applyTactileSettings(f);

        Label lLabelImsak = new Label(RESSOURCE.get("DisplayImsak"));
        editerLabel(lLabelImsak);
        Label lLabelChourouk = new Label(RESSOURCE.get("DisplayChourouk"));
        editerLabel(lLabelChourouk);

        mImsak = new CheckBox();
        editerCheckBox(mImsak);
        mChourouk = new CheckBox();
        editerCheckBox(mChourouk);

        Container ctnSaisie = new Container(new GridLayout(3, 2));
        ctnSaisie.addComponent(lLabelImsak);
        ctnSaisie.addComponent(mImsak);
        ctnSaisie.addComponent(new Label(""));
        ctnSaisie.addComponent(new Label(""));
        ctnSaisie.addComponent(lLabelChourouk);
        ctnSaisie.addComponent(mChourouk);
        ctnSaisie.setPreferredH(HAUTEUR_LABEL_TOUS);
        //ctnSaisie.setScrollable(true);

        f.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        f.addComponent(new Label());
        f.addComponent(ctnSaisie);

        mOK = new Command(RESSOURCE.get("Command.OK")) {

            public void actionPerformed(ActionEvent ae) {

                try {
                    ServiceFactory.getFactory().getPreferences().set(Preferences.sDisplayImsak, Integer.toString(
                            StringOutilClient.getValeurBooleenne(mImsak.isSelected())));

                    ServiceFactory.getFactory().getPreferences().set(Preferences.sDisplayChourouk, Integer.toString(
                            StringOutilClient.getValeurBooleenne(mChourouk.isSelected())));

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
        initialiserInfosSelections();
    }

    protected String getIconBaseName() {
        return MENU_PRAYERS;
    }

    private void editerLabel(Label pLabel) {
        pLabel.setUIID(UIID_LABEL_INFO_NAME);
        pLabel.getUnselectedStyle().setBgTransparency(0);
        pLabel.getSelectedStyle().setBgTransparency(0);
        pLabel.setFocusable(true);
        pLabel.setAlignment(Component.LEFT);
        pLabel.setPreferredH(HAUTEUR_LABEL);
    }

    private void editerCheckBox(CheckBox pCheckBox) {
        pCheckBox.setAlignment(CheckBox.RIGHT);
    }

    private void initialiserInfosSelections() {

        boolean isImsakSelected = false;
        boolean isChouroukSelected = false;

        try {
            isImsakSelected = StringOutilClient.getValeurBooleenne(
                    Integer.parseInt(ServiceFactory.getFactory().getPreferences().get(Preferences.sDisplayImsak)));
            isChouroukSelected = StringOutilClient.getValeurBooleenne(
                    Integer.parseInt(ServiceFactory.getFactory().getPreferences().get(Preferences.sDisplayChourouk)));
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        mImsak.setSelected(isImsakSelected);
        mChourouk.setSelected(isChouroukSelected);
    }

    protected void cleanup() {
    }
}
