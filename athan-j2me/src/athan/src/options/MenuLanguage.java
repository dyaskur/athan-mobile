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

import athan.src.Client.Main;
import athan.src.Client.Menu;
import athan.src.Factory.Preferences;
import athan.src.Factory.ResourceReader;
import athan.src.Factory.ServiceFactory;
import com.sun.lwuit.ComboBox;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.events.ActionEvent;
import java.util.Date;

/**
 * Menu choix de la langue de l'application.
 * 
 * @author Saad BENBOUZID
 */
public class MenuLanguage extends Menu {

    private static final int HAUTEUR_LABEL = 23;
    private static final int HAUTEUR_LABEL_TOUS = 25;
    private Command mOK;
    private ComboBox mLangCmb;

    protected String getHelp() {
        return ServiceFactory.getFactory().getResourceReader().getHelpMenuLanguage();
    }

    protected String getIconBaseName() {
        return MENU_LANGAGE_APPLICATION;
    }

    protected String getName() {
        return ServiceFactory.getFactory().getResourceReader().get("MenuApplicationLanguage");
    }

    protected void execute(final Form f) {
        final ResourceReader RESSOURCE = ServiceFactory.getFactory().getResourceReader();

        applyTactileSettings(f);

        Label lLabelDecalage = new Label(RESSOURCE.get("LanguageChoice"));
        lLabelDecalage.setUIID(UIID_LABEL_INFO_NAME);
        lLabelDecalage.getUnselectedStyle().setBgTransparency(0);
        lLabelDecalage.getSelectedStyle().setBgTransparency(0);
        lLabelDecalage.setFocusable(true);
        lLabelDecalage.setAlignment(Component.LEFT);
        lLabelDecalage.setPreferredH(HAUTEUR_LABEL);

        mLangCmb = new ComboBox(LANGUE_APPLICATIONS);
        mLangCmb.setSelectedIndex(0);

        Container ctnSaisie = new Container(new BoxLayout(BoxLayout.X_AXIS));
        ctnSaisie.addComponent(lLabelDecalage);
        ctnSaisie.addComponent(mLangCmb);
        ctnSaisie.setPreferredH(HAUTEUR_LABEL_TOUS);

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
                String s_lang = Preferences.LANGUE_EN;

                try {
                    int i_lang = mLangCmb.getSelectedIndex();

                    if (i_lang == 0) {
                        s_lang = Preferences.LANGUE_EN;
                    } else if (i_lang == 1) {
                        s_lang = Preferences.LANGUE_FR;
                    }

                } catch (Exception exc) {
                    contenuOk = false;
                }

                if (!contenuOk) {
                    final ResourceReader RESSOURCE = ServiceFactory.getFactory().getResourceReader();

                    // Message d'erreur
                    Command okCommand = new Command(RESSOURCE.get("Command.OK"));
                    Dialog.show(RESSOURCE.get("errorTitle"), RESSOURCE.get("errorLangApplicationParameters"),
                            okCommand,
                            new Command[]{okCommand}, Dialog.TYPE_ERROR, null, TIMEOUT_FENETRE_ERROR,
                            CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, true, 1000));
                    return;
                }

                try {
                    ServiceFactory.getFactory().getPreferences().set(Preferences.sLangue, s_lang);

                    // On enregistre les paramètres dans la mémoire du téléphone
                    ServiceFactory.getFactory().getPreferences().save();

                    // On rafraîchit l'affichage des prières
                    ServiceFactory.getFactory().getVuePrincipale().rafraichir(new Date(), true, true);

                    /*
                    // On recharge les paramètres linguistiques de l'application
                    ServiceFactory.getFactory().setResourceReader(
                    new ResourceReader(ServiceFactory.getFactory()
                    .getPreferences())
                    );
                     */

                    // Message de confirmation modif
                    Command okCommand = new Command(RESSOURCE.get("Command.OK"));
                    Dialog.show(RESSOURCE.get("propertiesSavedTitle"), RESSOURCE.get("warningLangChanging"), okCommand,
                            new Command[]{okCommand}, Dialog.TYPE_INFO, null, TIMEOUT_FENETRE_ERROR,
                            CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, true, 1000));

                    f.showBack();
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        };

        f.addCommand(mOK);
        initialiserInfosLangue();
    }

    private void initialiserInfosLangue() {

        String lLang = Preferences.LANGUE_EN;

        try {
            lLang = ServiceFactory.getFactory().getPreferences().get(Preferences.sLangue);

        } catch (Exception exc) {
            exc.printStackTrace();
        }

        if (lLang.equals(Preferences.LANGUE_EN)) {
            mLangCmb.setSelectedIndex(0);
        } else if (lLang.equals(Preferences.LANGUE_FR)) {
            mLangCmb.setSelectedIndex(1);
        }
    }

    protected void cleanup() {
    }
}
