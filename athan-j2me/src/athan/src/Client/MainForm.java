/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package athan.src.Client;

import athan.src.Factory.ResourceReader;
import athan.src.Factory.ServiceFactory;
import athan.src.Factory.TacheTimer;
import athan.src.Outils.StringOutilClient;
import com.sun.lwuit.Button;
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
import java.util.Date;
import java.util.Timer;

/**
 * Fenêtre principale
 * 
 * @author Saad BENBOUZID
 */
public class MainForm extends Menu
           implements AthanConstantes {

    /** Intervalle en ms entre la date courante et la date suivante */
    private static final int INTERVALLE_PREC_SUIV = 24 * 3600 * 1000;

    private static final int HAUTEUR_LABEL_PRIERE = 12;

    private ResourceReader RESOURCES;

    private Date mHeureCourante;
    
    private Label mLabelLieu;
    private Button mHomeHeureCourante;

    private Button mDatePrecedente;
    private TextArea mTextAreaLibelleJour;
    private Button mDateSuivante;

    private Label mLabelHoraireImsak;
    private Label mLabelHoraireSohb;
    private Label mLabelHoraireChourouk;
    private Label mLabelHoraireDohr;
    private Label mLabelHoraireAsr;
    private Label mLabelHoraireMaghreb;
    private Label mLabelHoraireIshaa;

    private Main mMain;

    private TableLayout.Constraint nouvelleContrainteListePrieres(TableLayout pTB) {
        return nouvelleContrainteListePrieres(pTB, HAUTEUR_LABEL_PRIERE);
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

    /**
     * @return the mLabelLieu
     */
    public Label getLabelLieu() {
        return mLabelLieu;
    }

    /**
     * @return the mTextAreaLibelleJour
     */
    public TextArea getTextAreaLibelleJour() {
        return mTextAreaLibelleJour;
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

    public String getName() {
        return ServiceFactory.getFactory().getResourceReader()
                .get("ApplicationTitle");
    }

    public String getIconBaseName() {
        return MENU_AFFICHAGE_COMPAS;
    }

    protected void execute(Form f) {
        // Récupération du bundle de ressources
        RESOURCES = ServiceFactory.getFactory().getResourceReader();
        
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
        mHomeHeureCourante = new Button(Main.icons.getImage("Home"));
        Container ctnVilleHome = new Container(new BorderLayout());
        ctnVilleHome.addComponent(BorderLayout.WEST, mLabelLieu);
        ctnVilleHome.addComponent(BorderLayout.EAST, mHomeHeureCourante);
        mHomeHeureCourante.setVisible(false);
        mHomeHeureCourante.setFocusable(true);
        ctnVilleHome.setPreferredH(30);

        // Conteneur de parcours des dates
        mTextAreaLibelleJour = new TextArea("");
        mTextAreaLibelleJour.setAlignment(Component.CENTER);
        mTextAreaLibelleJour.setUIID(UIID_LABEL_CURRENT_DATE);
        mTextAreaLibelleJour.setGrowByContent(true);
        //mTextAreaLibelleJour.setColumns(2);
        mTextAreaLibelleJour.setRows(3);
        mTextAreaLibelleJour.setEditable(false);
        mTextAreaLibelleJour.setFocusable(false);
        mDatePrecedente = new Button(Main.icons.getImage("Previous"));
        mDatePrecedente.setFocusable(true);
        mDateSuivante = new Button(Main.icons.getImage("Next"));
        mDateSuivante.setFocusable(true);
        Container ctnDates = new Container(new BorderLayout());
        ctnDates.addComponent(BorderLayout.WEST, mDatePrecedente);
        ctnDates.addComponent(BorderLayout.CENTER, mTextAreaLibelleJour);
        ctnDates.addComponent(BorderLayout.EAST, mDateSuivante);        

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
        ctnPrieres.addComponent(nouvelleContrainteListePrieres(tbCtnPrieres, 10), new Label());
        ctnPrieres.addComponent(nouvelleContrainteListePrieres(tbCtnPrieres, 10), new Label());
        ctnPrieres.addComponent(nouvelleContrainteListePrieres(tbCtnPrieres), renvoyerLabelNomPriere(RESOURCES.get("Imsak")));
        ctnPrieres.addComponent(nouvelleContrainteListePrieres(tbCtnPrieres), mLabelHoraireImsak);
        ctnPrieres.addComponent(nouvelleContrainteListePrieres(tbCtnPrieres), renvoyerLabelNomPriere(RESOURCES.get("Sobh")));
        ctnPrieres.addComponent(nouvelleContrainteListePrieres(tbCtnPrieres), mLabelHoraireSohb);
        ctnPrieres.addComponent(nouvelleContrainteListePrieres(tbCtnPrieres), renvoyerLabelNomPriere(RESOURCES.get("Chourouk")));
        ctnPrieres.addComponent(nouvelleContrainteListePrieres(tbCtnPrieres), mLabelHoraireChourouk);
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
        Image imgBdLeft = Main.icons.getImage("Border_left").scaled(16, -1);
        Image imgBdRight = Main.icons.getImage("Border_right").scaled(16, -1);
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
           mDatePrecedente.setFocusable(true);
           mDateSuivante.setFocusable(true);
           mHomeHeureCourante.setFocusable(true);
        }

        mHomeHeureCourante.addActionListener(new ActionListener() {

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
        });

        mDatePrecedente.addActionListener(new ActionListener() {

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
                        mHomeHeureCourante.setVisible(true);
                    }

                } catch(Exception exc) {
                    exc.printStackTrace();
                }
            }
        });

        mDateSuivante.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                // On interrompt le timer et affiche les résultats pour une date donnée
                try {
                    Main.getTimer().cancel();
                    System.out.println("timer arrêté !");

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
                        mHomeHeureCourante.setVisible(true);
                    }

                } catch(Exception exc) {
                    exc.printStackTrace();
                }
            }
        });

        f.addCommand(Main.optionsCommand);
        f.addCommand(Main.exitCommand);
        f.setBackCommand(Main.exitCommand);
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

        mHomeHeureCourante.setVisible(false);
    }

    public Main getMain() {
        return mMain;
    }
}
