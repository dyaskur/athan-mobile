/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package athan.src.options;

import athan.src.Client.Main;
import athan.src.Client.Menu;
import athan.src.Factory.Preferences;
import athan.src.Factory.ResourceReader;
import athan.src.Factory.ServiceFactory;
import athan.src.Outils.FileTreeModel;
import athan.src.Outils.StringOutilClient;
import com.sun.lwuit.Button;
import com.sun.lwuit.CheckBox;
import com.sun.lwuit.ComboBox;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Form;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.TextField;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.SelectionListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.layouts.GridLayout;
import com.sun.lwuit.tree.Tree;
import java.io.InputStream;
import java.util.Date;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.control.VolumeControl;

/**
 * Menu réglages des alarmes
 * 
 * @author Saad BENBOUZID
 */
public class MenuAlerts extends Menu {

    private static final int HAUTEUR_LABEL = 22;
    private static final int HAUTEUR_LABEL_TOUS = 130;
    private static final String IMAGE_BROWSE_SONG = "BrowseSong";
    private static final String IMAGE_FOLDER = "Folder";
    private static final String IMAGE_FOLDER_CLOSED = "FolderClosed";
    private static final String IMAGE_FILE = "File";
    private CheckBox mAlerterSobh;
    private CheckBox mAlerterDohr;
    private CheckBox mAlerterAsr;
    private CheckBox mAlerterMaghreb;
    private CheckBox mAlerterIshaa;
    private ComboBox mChoixAlerte;
    private TextField mFichierSon;
    private Button mChoixFichier;
    private Command mOK;

    private Container mCtnPrieres;

    private final ResourceReader RESSOURCE = ServiceFactory.getFactory().getResourceReader();

    protected String getHelp() {
        return ServiceFactory.getFactory().getResourceReader().get("Menu.Help");
    }

    protected String getName() {
        return ServiceFactory.getFactory().getResourceReader().get("MenuAlerts");
    }

    protected String getIconBaseName() {
        return MENU_ALARMES;
    }

    protected void execute(final Form f) {

        applyTactileSettings(f);

        Label lLabelSobh = new Label(RESSOURCE.get("Sobh"));
        editerLabel(lLabelSobh);
        Label lLabelDohr = new Label(RESSOURCE.get("Dohr"));
        editerLabel(lLabelDohr);
        Label lLabelAsr = new Label(RESSOURCE.get("Asr"));
        editerLabel(lLabelAsr);
        Label lLabelMaghreb = new Label(RESSOURCE.get("Maghreb"));
        editerLabel(lLabelMaghreb);
        Label lLabelIshaa = new Label(RESSOURCE.get("Ishaa"));
        editerLabel(lLabelIshaa);

        mAlerterSobh = new CheckBox();
        mAlerterDohr = new CheckBox();
        mAlerterAsr = new CheckBox();
        mAlerterMaghreb = new CheckBox();
        mAlerterIshaa = new CheckBox();

        Label lLabelAlertType = new Label(RESSOURCE.get("AlertType"));
        editerLabel(lLabelAlertType);

        String[] choixAlertes = {
            RESSOURCE.get("AlertNone"),
            RESSOURCE.get("AlertSong"),
            RESSOURCE.get("AlertFlashing")
        };

        mChoixAlerte = new ComboBox(choixAlertes);
        mChoixAlerte.addSelectionListener(new SelectionListener() {

            public void selectionChanged(int oldSelected, int newSelected) {
                handlerChoixAlerte(newSelected);
            }
        });

        mFichierSon = new TextField();
        editerAlertSong(mFichierSon);
        mFichierSon.setEditable(false);

        mChoixFichier = new Button(Main.icons.getImage(IMAGE_BROWSE_SONG));

        mChoixFichier.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                // Sélection d'un fichier son
                try {
                    renvoyerUrlFichier(f);

                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });

        mCtnPrieres = new Container(new GridLayout(5, 2));
        mCtnPrieres.addComponent(lLabelSobh);
        mCtnPrieres.addComponent(mAlerterSobh);
        mCtnPrieres.addComponent(lLabelDohr);
        mCtnPrieres.addComponent(mAlerterDohr);
        mCtnPrieres.addComponent(lLabelAsr);
        mCtnPrieres.addComponent(mAlerterAsr);
        mCtnPrieres.addComponent(lLabelMaghreb);
        mCtnPrieres.addComponent(mAlerterMaghreb);
        mCtnPrieres.addComponent(lLabelIshaa);
        mCtnPrieres.addComponent(mAlerterIshaa);
        mCtnPrieres.setPreferredH(HAUTEUR_LABEL_TOUS);

        Container ctnChoix = new Container(new GridLayout(1, 2));
        ctnChoix.addComponent(lLabelAlertType);
        ctnChoix.addComponent(mChoixAlerte);

        Container ctnFichierSon = new Container(new BorderLayout());
        ctnFichierSon.addComponent(BorderLayout.CENTER, mFichierSon);
        ctnFichierSon.addComponent(BorderLayout.EAST, mChoixFichier);

        f.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        f.addComponent(mCtnPrieres);
        f.addComponent(new Label());
        f.addComponent(ctnChoix);
        f.addComponent(ctnFichierSon);

        mOK = new Command(RESSOURCE.get("Command.OK")) {

            public void actionPerformed(ActionEvent ae) {

                boolean contenuOk = true;

                try {
                    ServiceFactory.getFactory().getPreferences().set(Preferences.sAlertSobh, Integer.toString(
                            StringOutilClient.getValeurBooleenne(mAlerterSobh.isSelected())));

                    ServiceFactory.getFactory().getPreferences().set(Preferences.sAlertDohr, Integer.toString(
                            StringOutilClient.getValeurBooleenne(mAlerterDohr.isSelected())));

                    ServiceFactory.getFactory().getPreferences().set(Preferences.sAlertAsr, Integer.toString(
                            StringOutilClient.getValeurBooleenne(mAlerterAsr.isSelected())));

                    ServiceFactory.getFactory().getPreferences().set(Preferences.sAlertMaghreb, Integer.toString(
                            StringOutilClient.getValeurBooleenne(mAlerterMaghreb.isSelected())));

                    ServiceFactory.getFactory().getPreferences().set(Preferences.sAlertIshaa, Integer.toString(
                            StringOutilClient.getValeurBooleenne(mAlerterIshaa.isSelected())));

                    if (mChoixAlerte.getSelectedIndex() == 0) {
                        ServiceFactory.getFactory().getPreferences().set(Preferences.sAlertMode, Preferences.MODE_NONE);
                    } else if (mChoixAlerte.getSelectedIndex() == 1) {
                        ServiceFactory.getFactory().getPreferences().set(Preferences.sAlertMode, Preferences.MODE_SONG);
                    } else if (mChoixAlerte.getSelectedIndex() == 2) {
                        ServiceFactory.getFactory().getPreferences().set(Preferences.sAlertMode, Preferences.MODE_FLASH);
                    }

                    ServiceFactory.getFactory().getPreferences().set(Preferences.sAlertFile, mFichierSon.getText());

                    if (mChoixAlerte.getSelectedIndex() == 1
                            && StringOutilClient.isEmpty(mFichierSon.getText())) {
                        contenuOk = false;
                    }

                    if (!contenuOk) {
                        // Message d'erreur
                        Command okCommand = new Command(RESSOURCE.get("Command.OK"));
                        Dialog.show(RESSOURCE.get("errorTitle"), RESSOURCE.get("errorSongAlertNoFile"), okCommand,
                                new Command[]{okCommand}, Dialog.TYPE_ERROR, null, TIMEOUT_FENETRE_ERROR,
                                CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, true, 1000));
                        return;
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
        initialiserInfosSelections();
    }

    private void renvoyerUrlFichier(final Form pFormCourante) {

        // Création de l'arbre
        Tree.setFolderIcon(Main.icons.getImage(IMAGE_FOLDER_CLOSED));
        Tree.setFolderOpenIcon(Main.icons.getImage(IMAGE_FOLDER));
        Tree.setNodeIcon(Main.icons.getImage(IMAGE_FILE));

        final FileTreeModel model = new FileTreeModel();

        final Tree tree = new Tree(model) {

            protected String childToDisplayLabel(Object child) {
                if (((String) child).endsWith("/")) {
                    return ((String) child).substring(((String) child).lastIndexOf('/', ((String) child).length() - 2));
                }
                return ((String) child).substring(((String) child).lastIndexOf('/'));
            }
        };

        // Création de la fenêtre
        Form treeForm = new Form(RESSOURCE.get("FormSongFileSelection"));
        treeForm.setLayout(new BorderLayout());
        treeForm.setScrollable(false);
        treeForm.addComponent(BorderLayout.CENTER, tree);
        Command back = new Command(RESSOURCE.get("Command.Back")) {

            public void actionPerformed(ActionEvent evt) {
                pFormCourante.showBack();
            }
        };
        Command ok = new Command(RESSOURCE.get("Command.Select")) {

            public void actionPerformed(ActionEvent evt) {

                boolean ok = true;

                Object elem = tree.getSelectedItem();
                // On vérifie qu'il s'agit d'un fichier et non d'un dossier
                if (model.isLeaf(elem)) {
                    // On vérifie qu'il s'agit d'un fichier au bon format
                    if (!((String) elem).endsWith(FORMAT_WAV)
                            && !((String) elem).endsWith(FORMAT_MP3)) {
                        ok = false;
                    }
                } else {
                    ok = false;
                }

                if (ok) {
                    // Assigne le nom du fichier son aux propriétés de la fenêtre
                    mFichierSon.setText((String) tree.getSelectedItem());
                    pFormCourante.showBack();
                } else {
                    // Message d'erreur
                    Command okCommand = new Command(RESSOURCE.get("Command.OK"));
                    Dialog.show(RESSOURCE.get("errorTitle"), RESSOURCE.get("errorSongFileFormat"),
                            okCommand,
                            new Command[]{okCommand}, Dialog.TYPE_ERROR, null, TIMEOUT_FENETRE_ERROR,
                            CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, true, 1000));
                }
            }
        };

        tree.addLeafListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {

            }
        });

        treeForm.addCommand(back);
        treeForm.addCommand(ok);
        treeForm.setBackCommand(back);
        treeForm.show();
    }

    private void initialiserInfosSelections() {

        boolean isSobhSelected = false;
        boolean isDohrSelected = false;
        boolean isAsrSelected = false;
        boolean isMaghrebSelected = false;
        boolean isIshaaSelected = false;

        String urlFichier = StringOutilClient.EMPTY;
        String mode = StringOutilClient.EMPTY;

        try {
            isSobhSelected = StringOutilClient.getValeurBooleenne(
                    Integer.parseInt(ServiceFactory.getFactory().getPreferences().get(Preferences.sAlertSobh)));
            isDohrSelected = StringOutilClient.getValeurBooleenne(
                    Integer.parseInt(ServiceFactory.getFactory().getPreferences().get(Preferences.sAlertDohr)));
            isAsrSelected = StringOutilClient.getValeurBooleenne(
                    Integer.parseInt(ServiceFactory.getFactory().getPreferences().get(Preferences.sAlertAsr)));
            isMaghrebSelected = StringOutilClient.getValeurBooleenne(
                    Integer.parseInt(ServiceFactory.getFactory().getPreferences().get(Preferences.sAlertMaghreb)));
            isIshaaSelected = StringOutilClient.getValeurBooleenne(
                    Integer.parseInt(ServiceFactory.getFactory().getPreferences().get(Preferences.sAlertIshaa)));

            urlFichier = ServiceFactory.getFactory().getPreferences().get(Preferences.sAlertFile);
            mode = ServiceFactory.getFactory().getPreferences().get(Preferences.sAlertMode);
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        mAlerterSobh.setSelected(isSobhSelected);
        mAlerterDohr.setSelected(isDohrSelected);
        mAlerterAsr.setSelected(isAsrSelected);
        mAlerterMaghreb.setSelected(isMaghrebSelected);
        mAlerterIshaa.setSelected(isIshaaSelected);

        mFichierSon.setText(urlFichier);

        if (Preferences.MODE_NONE.equals(mode)) {
            mChoixAlerte.setSelectedIndex(0);
        } else if (Preferences.MODE_SONG.equals(mode)) {
            mChoixAlerte.setSelectedIndex(1);
        } else if (Preferences.MODE_FLASH.equals(mode)) {
            mChoixAlerte.setSelectedIndex(2);
        } else {
            // Par défaut si problème
            mChoixAlerte.setSelectedIndex(0);
        }
    }

    private void handlerChoixAlerte(int index) {

        if (index == 0) {
            // Aucun
            mCtnPrieres.setVisible(false);

            mChoixFichier.setVisible(false);
            mFichierSon.setVisible(false);

        } else if (index == 1) {
            // Son
            mCtnPrieres.setVisible(true);

            mChoixFichier.setVisible(true);
            mFichierSon.setVisible(true);
        } else if (index == 2) {
            // Flash
            mCtnPrieres.setVisible(true);

            mChoixFichier.setVisible(false);
            mFichierSon.setVisible(false);
        }
    }

    private void editerLabel(Label pLabel) {
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
        pTextField.setWidth(40);
    }

    public class FichierSon {

        public String mFichierSon;

        public String getFichierSon() {
            return mFichierSon;
        }

        public void setFichierSon(String pFichierSon) {
            mFichierSon = pFichierSon;
        }

        public boolean isAssigned() {
            return !StringOutilClient.isEmpty(mFichierSon);
        }
    }
}
