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
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VolumeControl;

/**
 * Menu réglages des alarmes.
 * 
 * @author Saad BENBOUZID
 */
public class MenuAlerts extends Menu {

    private static final int HAUTEUR_LABEL = 22;
    private static final int HAUTEUR_LABEL_TOUS = 130;
    private static final String IMAGE_BROWSE_SONG = "BrowseSong";
    private static final String IMAGE_PLAYSTOP_SONG = "AlertPlayStop";
    private static final String IMAGE_FOLDER = "Folder";
    private static final String IMAGE_FOLDER_CLOSED = "FolderClosed";
    private static final String IMAGE_FILE = "File";
    private CheckBox mAlerterSobh;
    private CheckBox mAlerterDohr;
    private CheckBox mAlerterAsr;
    private CheckBox mAlerterMaghreb;
    private CheckBox mAlerterIshaa;
    private ComboBox mChoixAlerte;
    private ComboBox mChoixVolume;
    private TextField mFichierSon;
    private Button mChoixFichier;
    private Button mPlayStop;
    private Player mPlayer;

    private Command mOK;
    private Container mCtnPrieres;
    private Container mCtnLecture;
    private final ResourceReader RESSOURCE = ServiceFactory.getFactory().getResourceReader();

    protected String getHelp() {
        return ServiceFactory.getFactory().getResourceReader().getHelpMenuAlerts();
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
        mChoixFichier.setFocusable(true);
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

        // Fonctionnalités sons (play/stop)
        mPlayStop = new Button(Main.icons.getImage(IMAGE_PLAYSTOP_SONG));
        mPlayStop.setFocusable(true);
        mPlayStop.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                // Lecture / Arrêt du fichier son
                if (RESSOURCE.get("ButtonPlay").equals(mPlayStop.getText())) {
                    jouerSon();
                } else if (RESSOURCE.get("ButtonStop").equals(mPlayStop.getText())) {
                    stopperSon();
                }
            }
        });

        // Contrôle du volume
        Label lblVolume = new Label(RESSOURCE.get("Volume"));
        editerLabel(lblVolume);
        String[] choixVolumes = {
            "0", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100"
        };
        mChoixVolume = new ComboBox(choixVolumes);
        mChoixVolume.setWidth(150);

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

        mCtnLecture = new Container(new BoxLayout(BoxLayout.X_AXIS));
        mCtnLecture.addComponent(mPlayStop);
        mCtnLecture.addComponent(lblVolume);
        mCtnLecture.addComponent(mChoixVolume);

        f.setLayout(new BoxLayout(BoxLayout.Y_AXIS));

        f.addComponent(ctnChoix);
        f.addComponent(ctnFichierSon);
        f.addComponent(mCtnLecture);
        f.addComponent(new Label());
        f.addComponent(mCtnPrieres);

        mOK = new Command(RESSOURCE.get("Command.OK")) {

            public void actionPerformed(ActionEvent ae) {

                // On stoppe l'éventuelle lecture
                stopperSon();

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
                    ServiceFactory.getFactory().getPreferences().set(Preferences.sVolume, (String) mChoixVolume.getSelectedItem());

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

    /**
     * Bascule l'icone entre Play/Stop
     * <br>
     * true = Play visible ; false = Stop visible
     */
    private void switcherIconePlayStop(boolean playEnable) {
        if (playEnable) {
            mPlayStop.setText(RESSOURCE.get("ButtonPlay"));
        } else {
            mPlayStop.setText(RESSOURCE.get("ButtonStop"));
        }

        // Rafraîchit la vue suite au changement d'icone
        mPlayStop.repaint();
        mPlayStop.requestFocus();
    }

    /**
     * Stoppe la lecture du fichier son
     */
    private void stopperSon() {
        try {
            if (mPlayer != null) {

                mPlayer.stop();
                mPlayer = null;
            }

            switcherIconePlayStop(true);

        } catch (Exception exc) {
            exc.printStackTrace();
            switcherIconePlayStop(true);
        }
    }

    /**
     * Joue le fichier son s'il existe
     */
    private void jouerSon() {

        try {

            // On arrête toute éventuelle lecture précédente
            stopperSon();

            if (!StringOutilClient.isEmpty(mFichierSon.getText())) {

                // Charge la stream du fichier son Ã  jouer
                FileConnection fc = (FileConnection) Connector.open(mFichierSon.getText(), Connector.READ);
                InputStream inputStream = (InputStream) fc.openInputStream();

                String musicEncoding = StringOutilClient.EMPTY;
                if (mFichierSon.getText().toLowerCase().endsWith(FORMAT_WAV)) {
                    musicEncoding = "audio/x-wav";
                } else {
                    musicEncoding = "audio/mp3";
                }

                mPlayer = Manager.createPlayer(inputStream, musicEncoding);
                mPlayer.prefetch();

                // Ajoute un listener
                mPlayer.addPlayerListener(new PlayerListener() {

                    public void playerUpdate(Player player, String event, Object eventData) {
                        if (event.equals(PlayerListener.END_OF_MEDIA)) {
                            switcherIconePlayStop(true);
                        }
                    }
                });

                // Empêche les pics de volume au début de la musique
                VolumeControl volumeControl =
                        (VolumeControl) mPlayer.getControl("VolumeControl");
                if (volumeControl != null) {
                    volumeControl.setLevel(Integer.parseInt((String) mChoixVolume.getSelectedItem()));
                }

                // Joue le son
                mPlayer.start();

                switcherIconePlayStop(false);

                // Réassigne le volume
                volumeControl = (VolumeControl) mPlayer.getControl("VolumeControl");
                volumeControl.setLevel(Integer.parseInt((String) mChoixVolume.getSelectedItem()));

                // Détruit la stream pour récupérer de la ressource
                inputStream.close();
                inputStream = null;
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            switcherIconePlayStop(true);
        }
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
                    if (!((String) elem).toLowerCase().endsWith(FORMAT_WAV.toLowerCase())
                            && !((String) elem)..toLowerCase().endsWith(FORMAT_MP3.toLowerCase())) {
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
        String volume = "0";

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

            volume = ServiceFactory.getFactory().getPreferences().get(Preferences.sVolume);

        } catch (Exception exc) {
            exc.printStackTrace();
        }

        mAlerterSobh.setSelected(isSobhSelected);
        mAlerterDohr.setSelected(isDohrSelected);
        mAlerterAsr.setSelected(isAsrSelected);
        mAlerterMaghreb.setSelected(isMaghrebSelected);
        mAlerterIshaa.setSelected(isIshaaSelected);

        mFichierSon.setText(urlFichier);
        mChoixVolume.setSelectedItem(volume);

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

        // On force l'appel au handler
        handlerChoixAlerte(mChoixAlerte.getSelectedIndex());
    }

    private void handlerChoixAlerte(int index) {

        if (index == 0) {
            // Aucun
            afficherPrieres(false);

            mChoixFichier.setVisible(false);
            mFichierSon.setVisible(false);
            afficherGestionnaireSons(false);
            mPlayStop.setEnabled(false);
        } else if (index == 1) {
            // Son
            afficherPrieres(true);

            mChoixFichier.setVisible(true);
            mFichierSon.setVisible(true);
            afficherGestionnaireSons(true);
            mPlayStop.setEnabled(true);
            stopperSon();
        } else if (index == 2) {
            // Flash
            afficherPrieres(true);

            mChoixFichier.setVisible(false);
            mFichierSon.setVisible(false);
            afficherGestionnaireSons(false);
            mPlayStop.setEnabled(false);
        }
    }

    /**
     * Affiche ou masque le contenu du conteneur de prières (labels + coches)
     * @param afficher
     */
    private void afficherPrieres(boolean afficher) {
        for (int i = 0; i < mCtnPrieres.getComponentCount(); i++) {
            mCtnPrieres.getComponentAt(i).setVisible(afficher);
        }
    }

    /**
     * Affiche ou masque le contenu du conteneur de prières (labels + coches)
     * @param afficher
     */
    private void afficherGestionnaireSons(boolean afficher) {
        for (int i = 0; i < mCtnLecture.getComponentCount(); i++) {
            mCtnLecture.getComponentAt(i).setVisible(afficher);
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

    protected void cleanup() {
        // On stoppe l'éventuelle lecture en cours
        stopperSon();
    }
}