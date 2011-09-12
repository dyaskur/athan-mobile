/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package athan.src.Client;

import athan.src.Factory.Preferences;
import athan.src.Factory.ResourceReader;
import athan.src.Factory.ServiceFactory;
import athan.src.Factory.TacheTimer;
import athan.src.Outils.StringOutilClient;
import athan.src.Outils.TacheVibrer;
import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Form;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.table.TableLayout;
import java.io.InputStream;
import java.util.Date;
import java.util.Timer;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VolumeControl;

/**
 * Fenêtre principale
 * 
 * @author Saad BENBOUZID
 */
public class MainForm extends Menu {

    private static final int HAUTEUR_LABEL_DATE = 14;
    private static final int HAUTEUR_LABEL_PRIERE = 12;
    private static final int HAUTEUR_LABEL_HEADER_PRIERE = 10;

    /** Intervalle en ms entre la date courante et la date suivante */
    private static final int INTERVALLE_PREC_SUIV = 24 * 3600 * 1000;

    /**  Décalages dans le redimensionnement des bannières verticales
     * et des espacement des horaires des prières
     * une prière en moins */
    private static final int SCALE_GAP_UNITAIRE = 30;
    private static final int LABEL_GAP_UNITAIRE = 3;
    private static final int HEADER_LABEL_GAP_UNITAIRE = 3;
    private static final int HAUTEUR_BANNIERE = 160;
    private int mLabelGap = 0;

    private Date mHeureCourante;
    
    private Label mLabelLieu;
    private Command mCmdHomeHeureCourante;

    private Command mCmdDatePrecedente;
    private TextArea mTextAreaLibelleDate;
    private TextArea mTextAreaLibelleJourSemaine;
    private TextArea mTextAreaLibelleHeure;
    private Command mCmdDateSuivante;

    private Label mLabelHoraireImsak;
    private Label mLabelHoraireSohb;
    private Label mLabelHoraireChourouk;
    private Label mLabelHoraireDohr;
    private Label mLabelHoraireAsr;
    private Label mLabelHoraireMaghreb;
    private Label mLabelHoraireIshaa;

    private Main mMain;
    private Form currentForm;

    // Récupération du bundle de ressources
    private ResourceReader RESOURCES = ServiceFactory.getFactory().getResourceReader();

    private TableLayout.Constraint nouvelleContrainteListePrieres(TableLayout pTB) {
        return nouvelleContrainteListePrieres(pTB, HAUTEUR_LABEL_PRIERE + mLabelGap);
    }

    private TableLayout.Constraint nouvelleContrainteListePrieres(TableLayout pTB, int pHauteur) {
        TableLayout.Constraint contrainte = pTB.createConstraint();
        contrainte.setHeightPercentage(pHauteur);
        contrainte.setWidthPercentage(50);

        return contrainte;
    }

    public MainForm(Main pMain) {
        mMain = pMain;
    }

    public static void setOptionForm(boolean pShow) {

        // On crée le panel d'options
        OptionForm optionForm = new OptionForm(Main.getMainForm());
        if (Main.getOptionForm() != null){
            optionForm.setTransitionInAnimator(Main.getOptionForm().getTransitionInAnimator());
            optionForm.setTransitionOutAnimator(Main.getOptionForm().getTransitionOutAnimator());
        } else {
            optionForm.setTransitionOutAnimator(CommonTransitions.createFade(400));
        }

        // On instancie son contenu
        optionForm.setMainForm(Main.icons);

        // On affiche la form
        Main.setOptionForm(optionForm);
        if (pShow) {
            Main.getOptionForm().show();
        }
    }

    public String getName() {
        return ServiceFactory.getFactory().getResourceReader()
                .get("ApplicationTitle");
    }

    public String getIconBaseName() {
        return MENU_AFFICHAGE_COMPAS;
    }

    private void editerTextAreaLibelleDate(TextArea ta) {
        ta.setAlignment(TextArea.CENTER);
        ta.setUIID(UIID_LABEL_CURRENT_DATE);
        ta.setGrowByContent(true);
        //mTextAreaLibelleJour.setColumns(2);
        //mTextAreaLibelleDate.setRows(3);
        ta.setEditable(false);
        ta.setFocusable(false);
        ta.setPreferredH(HAUTEUR_LABEL_DATE);
    }

    protected void execute(Form f) {
        
        f.setLayout(new BoxLayout(BoxLayout.Y_AXIS));

        if (Main.isTactile()) {
            f.setTactileTouch(true);
            f.setSmoothScrolling(true);
        } else {
            f.setTactileTouch(false);
            f.setSmoothScrolling(false);
            f.setFocusScrolling(true);
        }

        // Conteneur du haut : lieu + home
        mLabelLieu = new Label("");
        mLabelLieu.setAlignment(Component.LEFT);
        mLabelLieu.setFocusable(true);
        mLabelLieu.setUIID(UIID_LABEL_CURRENT_CITY);
        mLabelLieu.getUnselectedStyle().setBgTransparency(0);
        mLabelLieu.getSelectedStyle().setBgTransparency(0);

        Container ctnVilleHome = new Container(new BorderLayout());
        ctnVilleHome.addComponent(BorderLayout.WEST, mLabelLieu);
        ctnVilleHome.setPreferredH(30);

        // Conteneur de parcours des dates
        mTextAreaLibelleDate = new TextArea();
        mTextAreaLibelleJourSemaine = new TextArea();
        mTextAreaLibelleHeure = new TextArea();
        editerTextAreaLibelleDate(mTextAreaLibelleDate);
        editerTextAreaLibelleDate(mTextAreaLibelleJourSemaine);
        editerTextAreaLibelleDate(mTextAreaLibelleHeure);
        Container ctnDates = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        ctnDates.addComponent(mTextAreaLibelleDate);
        ctnDates.addComponent(mTextAreaLibelleJourSemaine);
        ctnDates.addComponent(mTextAreaLibelleHeure);
        ctnDates.setPreferredH(3 * (HAUTEUR_LABEL_DATE + 5));

        // Commandes
        mCmdHomeHeureCourante = new Command(RESOURCES.get("CurrentDay")) {
             
            public void actionPerformed(ActionEvent ae) {
                try {
                    // On force le recalcul des horaires
                    ServiceFactory.getFactory().getVuePrincipale()
                            .rafraichir(new Date(), true, true);

                    // On redémarre le timer
                    redemarrerTimer();
                } catch(Exception exc) {
                    exc.printStackTrace();
                }
            }
        };



        mCmdDatePrecedente = new Command(RESOURCES.get("PrevDay")) {

            public void actionPerformed(ActionEvent ae) {
                // On interrompt le timer et affiche les résultats pour une date donnée
                try {
                    Main.getTimer().cancel();

                    Date jourPrecedent = new Date(mHeureCourante.getTime() - INTERVALLE_PREC_SUIV);

                    if (StringOutilClient.isDateMemeJour(jourPrecedent, new Date())) {
                        // On force le recalcul des horaires
                        ServiceFactory.getFactory().getVuePrincipale()
                                .rafraichir(new Date(), true, true);

                        // On redémarre le timer
                        redemarrerTimer();
                    } else {
                        ServiceFactory.getFactory().getVuePrincipale()
                                .rafraichir(jourPrecedent, false, true);
                        //mCmdHomeHeureCourante.setVisible(true);
                    }

                } catch(Exception exc) {
                    exc.printStackTrace();
                }
            }
        };

        mCmdDateSuivante = new Command(RESOURCES.get("NextDay")) {

            public void actionPerformed(ActionEvent ae) {
                // On interrompt le timer et affiche les résultats pour une date donnée
                try {
                    Main.getTimer().cancel();

                    Date jourSuivant = new Date(mHeureCourante.getTime() + INTERVALLE_PREC_SUIV);

                    if (StringOutilClient.isDateMemeJour(jourSuivant, new Date())) {
                        // On force le recalcul des horaires
                        ServiceFactory.getFactory().getVuePrincipale()
                                .rafraichir(new Date(), true, true);

                        // On redémarre le timer
                        redemarrerTimer();
                    } else {
                        ServiceFactory.getFactory().getVuePrincipale()
                                .rafraichir(jourSuivant, false, true);
                        //mCmdHomeHeureCourante.setVisible(true);
                    }

                } catch(Exception exc) {
                    exc.printStackTrace();
                }
            }
        };

        // Conteneur des horaires de prières
        TableLayout tbCtnPrieres = new TableLayout(8, 2);
        Container ctnPrieres = new Container();
        ctnPrieres.setLayout(tbCtnPrieres);
        mLabelHoraireImsak = new Label("");
        mLabelHoraireImsak.setAlignment(Component.RIGHT);
        mLabelHoraireSohb = new Label("");
        mLabelHoraireSohb.setAlignment(Component.RIGHT);
        mLabelHoraireChourouk = new Label("");
        mLabelHoraireChourouk.setAlignment(Component.RIGHT);
        mLabelHoraireDohr = new Label("");
        mLabelHoraireDohr.setAlignment(Component.RIGHT);
        mLabelHoraireAsr = new Label("");
        mLabelHoraireAsr.setAlignment(Component.RIGHT);
        mLabelHoraireMaghreb = new Label("");
        mLabelHoraireMaghreb.setAlignment(Component.RIGHT);
        mLabelHoraireIshaa = new Label("");
        mLabelHoraireIshaa.setAlignment(Component.RIGHT);

        boolean isImsakSelected = isImsakSelected();
        boolean isChouroukSelected = isChouroukSelected();
        int scaleGap = 0;
        mLabelGap = 0;
        int headerGap = 0;

        if (!isImsakSelected) {
            scaleGap += SCALE_GAP_UNITAIRE;
            mLabelGap += LABEL_GAP_UNITAIRE;
            headerGap += HEADER_LABEL_GAP_UNITAIRE;
        }
        if (!isChouroukSelected) {
            scaleGap += SCALE_GAP_UNITAIRE;
            mLabelGap += LABEL_GAP_UNITAIRE;
            headerGap += HEADER_LABEL_GAP_UNITAIRE;
        }

        ctnPrieres.addComponent(nouvelleContrainteListePrieres(tbCtnPrieres, HAUTEUR_LABEL_HEADER_PRIERE - headerGap), new Label());
        ctnPrieres.addComponent(nouvelleContrainteListePrieres(tbCtnPrieres, HAUTEUR_LABEL_HEADER_PRIERE - headerGap), new Label());
        if (isImsakSelected) {
            ctnPrieres.addComponent(nouvelleContrainteListePrieres(tbCtnPrieres), renvoyerLabelNomPriere(RESOURCES.get("Imsak")));
            ctnPrieres.addComponent(nouvelleContrainteListePrieres(tbCtnPrieres), mLabelHoraireImsak);
        }
        ctnPrieres.addComponent(nouvelleContrainteListePrieres(tbCtnPrieres), renvoyerLabelNomPriere(RESOURCES.get("Sobh")));
        ctnPrieres.addComponent(nouvelleContrainteListePrieres(tbCtnPrieres), mLabelHoraireSohb);
        if (isChouroukSelected) {
            ctnPrieres.addComponent(nouvelleContrainteListePrieres(tbCtnPrieres), renvoyerLabelNomPriere(RESOURCES.get("Chourouk")));
            ctnPrieres.addComponent(nouvelleContrainteListePrieres(tbCtnPrieres), mLabelHoraireChourouk);
        }
        ctnPrieres.addComponent(nouvelleContrainteListePrieres(tbCtnPrieres), renvoyerLabelNomPriere(RESOURCES.get("Dohr")));
        ctnPrieres.addComponent(nouvelleContrainteListePrieres(tbCtnPrieres), mLabelHoraireDohr);
        ctnPrieres.addComponent(nouvelleContrainteListePrieres(tbCtnPrieres), renvoyerLabelNomPriere(RESOURCES.get("Asr")));
        ctnPrieres.addComponent(nouvelleContrainteListePrieres(tbCtnPrieres), mLabelHoraireAsr);
        ctnPrieres.addComponent(nouvelleContrainteListePrieres(tbCtnPrieres), renvoyerLabelNomPriere(RESOURCES.get("Maghreb")));
        ctnPrieres.addComponent(nouvelleContrainteListePrieres(tbCtnPrieres), mLabelHoraireMaghreb);
        ctnPrieres.addComponent(nouvelleContrainteListePrieres(tbCtnPrieres), renvoyerLabelNomPriere(RESOURCES.get("Ishaa")));
        ctnPrieres.addComponent(nouvelleContrainteListePrieres(tbCtnPrieres), mLabelHoraireIshaa);
        ctnPrieres.setPreferredH(100);

        // Conteneur des horaires de prières + décor
        Image imgBdLeft = Main.icons.getImage("Border_left").scaled(-1, HAUTEUR_BANNIERE - scaleGap);
        Image imgBdRight = Main.icons.getImage("Border_right").scaled(-1, HAUTEUR_BANNIERE - scaleGap);
        Container ctnPrieresEtContour = new Container(new BorderLayout());
        ctnPrieresEtContour.addComponent(BorderLayout.WEST, new Label(imgBdLeft));
        ctnPrieresEtContour.addComponent(BorderLayout.CENTER, ctnPrieres);
        ctnPrieresEtContour.addComponent(BorderLayout.EAST, new Label(imgBdRight));

        // Affichage final
        f.addComponent(ctnVilleHome);
        f.addComponent(ctnDates);
        f.addComponent(ctnPrieresEtContour);

        // Gestion du comportement (ergonomie)
        if (!Main.isTactile()) {
           // RàF
        }

        f.addCommand(mCmdHomeHeureCourante, 0);
        f.addCommand(mCmdDatePrecedente, 1);
        f.addCommand(mCmdDateSuivante, 2);
        f.addCommand(Main.optionsCommand, 3);
        f.addCommand(Main.minimizeCommand, 4);
        f.addCommand(Main.exitCommand, 5);
        f.setBackCommand(Main.exitCommand);

        currentForm = f;
    }

    private boolean isImsakSelected() {
        boolean ret = false;

        try {
            ret = StringOutilClient.getValeurBooleenne(
                    Integer.parseInt(ServiceFactory.getFactory().getPreferences()
                        .get(Preferences.sDisplayImsak)));
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        return ret;
    }

    private boolean isChouroukSelected() {
        boolean ret = false;

        try {
            ret = StringOutilClient.getValeurBooleenne(
                    Integer.parseInt(ServiceFactory.getFactory().getPreferences()
                        .get(Preferences.sDisplayChourouk)));
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        return ret;
    }

    private Label renvoyerLabelNomPriere(String pNomPriere) {
        Label retour = new Label(pNomPriere);
        //retour.getUnselectedStyle().setFont(Main.theme.getFont(FONT_LABEL_PRAYER_NAME));
        //retour.getSelectedStyle().setFont(Main.theme.getFont(FONT_LABEL_PRAYER_NAME));
        retour.setUIID(UIID_LABEL_PRAYER_NAME);
        retour.getUnselectedStyle().setBgTransparency(0);
        retour.getSelectedStyle().setBgTransparency(0);
        if (!Main.isTactile()) {
            retour.setFocusable(true);
        }
        return retour;
    }

    private void redemarrerTimer() {
        Main.setTimer(new Timer());
        Main.getTimer().schedule(new TacheTimer(), 0, TacheTimer.DUREE_CYCLE);

        //mCmdHomeHeureCourante.setVisible(false);
    }

    public void traiterAlarme(String pPreferenceKey) {

        String alertMode = ServiceFactory.getFactory().getPreferences()
                .get(Preferences.sAlertMode);

        if (alertMode.equals(Preferences.MODE_NONE)) {
            // RàF

        } else if (alertMode.equals(Preferences.MODE_VIBRATE)) {
            // Vibrer
            Timer timerVibrer = new Timer();
            timerVibrer.schedule(new TacheVibrer(), 0, TacheVibrer.DUREE_CYCLE);

        } else if (alertMode.equals(Preferences.MODE_SONG)) {

            // Jouer l'appel

            // On récupère l'url du fichier son
            String urlSon = ServiceFactory.getFactory().getPreferences()
                    .get(Preferences.sAlertFile);

            if (StringOutilClient.isEmpty(urlSon)) {
                // Rien à jouer
                return;
            }

            String musicEncoding = StringOutilClient.EMPTY;
            if (urlSon.endsWith(FORMAT_WAV)) {
                 musicEncoding = "audio/x-wav";
            } else {
                musicEncoding = "audio/mp3";
            }

            try {

                // Charge la stream du fichier son à jouer
                FileConnection fc = (FileConnection)Connector.open(urlSon, Connector.READ);
                InputStream inputStream = (InputStream)fc.openInputStream();

                // Crée le player
                final Player musicPlayer = Manager.createPlayer(inputStream, musicEncoding);
                musicPlayer.prefetch();

                // Ajoute un listener
                musicPlayer.addPlayerListener(new PlayerListener() {
                    public void playerUpdate(Player player, String event, Object eventData) {
                        if (event.equals(PlayerListener.END_OF_MEDIA)) {
                            // Ferme la fenêtre
                            currentForm.showBack();
                        }
                    }
                });

                // Empêche les pics de volume au début de la musique
                VolumeControl volumeControl =
                            (VolumeControl) musicPlayer.getControl("VolumeControl");
                                    volumeControl.setLevel(100);

                // Joue le son
                musicPlayer.start();

                // Réassigne le volume
                volumeControl = (VolumeControl) musicPlayer.getControl("VolumeControl");
                volumeControl.setLevel(100);

                // Détruit la stream pour récupérer de la ressource
                inputStream.close();
                inputStream = null;

                // Crée une fenêtre avec possibilité d'arrêt
                Form stopForm = new Form(RESOURCES.get("Window.Help"));
                stopForm.setLayout(new BorderLayout());

                Button btnStop = new Button(Main.icons.getImage("Stop"));

                btnStop.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        try {
                            // Arrête la musique
                            musicPlayer.stop();

                            // Ferme la fenêtre
                            currentForm.showBack();
                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }
                    }
                });

                Image imgAllahAkbar  = Main.icons.getImage("AllahAkbar");
                Label lLabelPrayerRinging = new Label(retournerNomPriereAlerte(pPreferenceKey));
                lLabelPrayerRinging.setUIID(UIID_LABEL_PRAYER_NAME_RINGING);
                lLabelPrayerRinging.getUnselectedStyle().setBgTransparency(0);
                lLabelPrayerRinging.getSelectedStyle().setBgTransparency(0);
                if (!Main.isTactile()) {
                    lLabelPrayerRinging.setFocusable(true);
                }

                stopForm.addComponent(BorderLayout.NORTH, lLabelPrayerRinging);
                stopForm.addComponent(BorderLayout.CENTER, btnStop);
                stopForm.addComponent(BorderLayout.SOUTH, new Label(imgAllahAkbar));

                stopForm.setScrollable(false);

                Command fermer = new Command(RESOURCES.get("Menu.Close")) {
                    public void actionPerformed(ActionEvent evt) {
                        currentForm.showBack();
                    }
                };
                stopForm.addCommand(fermer);
                stopForm.setBackCommand(fermer);
                stopForm.show();

            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
    }

    private String retournerNomPriereAlerte(String pPreferenceKey) {

        String retour = StringOutilClient.EMPTY;

        if (Preferences.sAlertSobh.equals(pPreferenceKey)) {
            retour = RESOURCES.get("Sobh");
        } else if (Preferences.sAlertDohr.equals(pPreferenceKey)) {
            retour = RESOURCES.get("Dohr");
        } else if (Preferences.sAlertAsr.equals(pPreferenceKey)) {
            retour = RESOURCES.get("Asr");
        } else if (Preferences.sAlertMaghreb.equals(pPreferenceKey)) {
            retour = RESOURCES.get("Maghreb");
        } else if (Preferences.sAlertIshaa.equals(pPreferenceKey)) {
            retour = RESOURCES.get("Ihsaa");
        }

        return retour;
    }

    public Main getMain() {
        return mMain;
    }

    /**
     * @return the mLabelLieu
     */
    public Label getLabelLieu() {
        return mLabelLieu;
    }

    /**
     * @return the mTextAreaLibelleDate
     */
    public TextArea getTextAreaLibelleDate() {
        return mTextAreaLibelleDate;
    }

    /**
     * @return the mTextAreaLibelleJourSemaine
     */
    public TextArea getTextAreaLibelleJourSemaine() {
        return mTextAreaLibelleJourSemaine;
    }

    /**
     * @return the mTextAreaLibelleHeure
     */
    public TextArea getTextAreaLibelleHeure() {
        return mTextAreaLibelleHeure;
    }

    /**
     * @return the mLabelHoraireImsak
     */
    public Label getLabelHoraireImsak() {
        return mLabelHoraireImsak;
    }

    /**
     * @return the mLabelHoraireSohb
     */
    public Label getLabelHoraireSohb() {
        return mLabelHoraireSohb;
    }

    /**
     * @return the mLabelHoraireChourouk
     */
    public Label getLabelHoraireChourouk() {
        return mLabelHoraireChourouk;
    }

    /**
     * @return the mLabelHoraireDohr
     */
    public Label getLabelHoraireDohr() {
        return mLabelHoraireDohr;
    }

    /**
     * @return the mLabelHoraireAsr
     */
    public Label getLabelHoraireAsr() {
        return mLabelHoraireAsr;
    }

    /**
     * @return the mLabelHoraireMaghreb
     */
    public Label getLabelHoraireMaghreb() {
        return mLabelHoraireMaghreb;
    }

    /**
     * @return the mLabelHoraireIshaa
     */
    public Label getLabelHoraireIshaa() {
        return mLabelHoraireIshaa;
    }

    public void setHeureCourante(Date pHeureCourante) {
        mHeureCourante = pHeureCourante;
    }

    /*
    public void actionPerformed(ActionEvent evt) {
        Command cmd = evt.getCommand();
        System.out.println(cmd.getId());
        switch (cmd.getId()) {
            case COMMAND_JOUR_COUR:
                try {
                    // On force le recalcul des horaires
                    ServiceFactory.getFactory().getVuePrincipale()
                            .rafraichir(new Date(), true, true);

                    // On redémarre le timer
                    redemarrerTimer();
                } catch(Exception exc) {
                    exc.printStackTrace();
                }
                break;
            case COMMAND_JOUR_PREC:
                // On interrompt le timer et affiche les résultats pour une date donnée
                try {
                    Main.getTimer().cancel();

                    Date jourPrecedent = new Date(mHeureCourante.getTime() - INTERVALLE_PREC_SUIV);

                    if (StringOutilClient.isDateMemeJour(jourPrecedent, new Date())) {
                        // On force le recalcul des horaires
                        ServiceFactory.getFactory().getVuePrincipale()
                                .rafraichir(new Date(), true, true);

                        // On redémarre le timer
                        redemarrerTimer();
                    } else {
                        ServiceFactory.getFactory().getVuePrincipale()
                                .rafraichir(jourPrecedent, false, true);
                        //mCmdHomeHeureCourante.setVisible(true);
                    }

                } catch(Exception exc) {
                    exc.printStackTrace();
                }
                break;
            case COMMAND_JOUR_SUIV:
                // On interrompt le timer et affiche les résultats pour une date donnée
                try {
                    Main.getTimer().cancel();

                    Date jourSuivant = new Date(mHeureCourante.getTime() + INTERVALLE_PREC_SUIV);

                    if (StringOutilClient.isDateMemeJour(jourSuivant, new Date())) {
                        // On force le recalcul des horaires
                        ServiceFactory.getFactory().getVuePrincipale()
                                .rafraichir(new Date(), true, true);

                        // On redémarre le timer
                        redemarrerTimer();
                    } else {
                        ServiceFactory.getFactory().getVuePrincipale()
                                .rafraichir(jourSuivant, false, true);
                        //mCmdHomeHeureCourante.setVisible(true);
                    }

                } catch(Exception exc) {
                    exc.printStackTrace();
                }
                break;
        }
    }
    */
}
