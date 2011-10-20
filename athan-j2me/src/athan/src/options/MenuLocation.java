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
import athan.web.LocationFetcher;
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
import com.sun.lwuit.impl.midp.VirtualKeyboard;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.table.TableLayout;
import java.rmi.RemoteException;
import java.util.Date;

/**
 * Set of the button types available in the UI
 * 
 * @author Saad BENBOUZID
 */
public class MenuLocation extends Menu {

    private static final int HAUTEUR_LABEL = 15;
    private static final int HAUTEUR_LABEL_TOUS = 70;
    private TextField mTextFieldNomVille;
    private TextField mTextFieldNomRegion;
    private TextField mTextFieldNomPays;
    private TextField mTextFieldLat;
    private TextField mTextFieldLng;
    private Command mGpsSearch;
    private Command mApiSearch;
    private Command mManualSearch;
    private Command mOK;
    private Command mAnnuler;
    private boolean utiliserGPS = false;
    private ResourceReader RESSOURCE = ServiceFactory.getFactory().getResourceReader();

    protected String getHelp() {
        return ServiceFactory.getFactory().getResourceReader().getHelpMenuLocation();
    }

    protected String getName() {
        return ServiceFactory.getFactory().getResourceReader().get("MenuLocation");
    }

    protected String getIconBaseName() {
        return MENU_CHOISIR_VILLE;
    }

    private void editerTypeLabel(Label pLabel) {
        pLabel.setUIID(UIID_LABEL_INFO_NAME);
        pLabel.getUnselectedStyle().setBgTransparency(0);
        pLabel.getSelectedStyle().setBgTransparency(0);
        pLabel.setFocusable(true);
        pLabel.setAlignment(Component.LEFT);
    }

    private void editerTextField(TextField pTextField, int pAlignment) {
        pTextField.setUIID(UIID_LABEL_LOCALISATION_INFO);
        pTextField.setAlignment(pAlignment);
        pTextField.setRows(1);
        pTextField.setFocusable(true);
        pTextField.setPreferredH(HAUTEUR_LABEL);
    }

    protected void execute(final Form f) {

        applyTactileSettings(f);

        TableLayout tblLayoutInfosLocalisation = new TableLayout(5, 4);
        Container ctnInfosLocalisation = new Container();
        ctnInfosLocalisation.setLayout(tblLayoutInfosLocalisation);
        //tblLayoutInfosLocalisation.createConstraint().setWidthPercentage(100);

        Label lLabelNomVille = new Label(RESSOURCE.get("CityName"));
        editerTypeLabel(lLabelNomVille);
        Label lLabelNomRegion = new Label(RESSOURCE.get("RegionName"));
        editerTypeLabel(lLabelNomRegion);
        Label lLabelNomPays = new Label(RESSOURCE.get("CountryName"));
        editerTypeLabel(lLabelNomPays);
        Label lLabelLatLng = new Label(RESSOURCE.get("LatLng"));
        editerTypeLabel(lLabelLatLng);

        mTextFieldNomVille = new TextField();
        editerTextField(mTextFieldNomVille, TextField.LEFT);
        mTextFieldNomRegion = new TextField();
        editerTextField(mTextFieldNomRegion, TextField.LEFT);
        mTextFieldNomPays = new TextField();
        editerTextField(mTextFieldNomPays, TextField.LEFT);
        mTextFieldLat = new TextField();
        editerTextField(mTextFieldNomVille, TextField.RIGHT);
        mTextFieldLng = new TextField();
        editerTextField(mTextFieldNomVille, TextField.RIGHT);

        ctnInfosLocalisation.addComponent(getCtnLayoutLocalisation(tblLayoutInfosLocalisation, 100, 4),
                new Label());
        ctnInfosLocalisation.addComponent(getCtnLayoutLocalisation(tblLayoutInfosLocalisation, 30, 1),
                lLabelLatLng);
        ctnInfosLocalisation.addComponent(getCtnLayoutLocalisation(tblLayoutInfosLocalisation, 34, 1),
                mTextFieldLat);
        ctnInfosLocalisation.addComponent(getCtnLayoutLocalisation(tblLayoutInfosLocalisation, 2, 1),
                new Label());
        ctnInfosLocalisation.addComponent(getCtnLayoutLocalisation(tblLayoutInfosLocalisation, 34, 1),
                mTextFieldLng);
        ctnInfosLocalisation.addComponent(getCtnLayoutLocalisation(tblLayoutInfosLocalisation, 30, 1),
                lLabelNomVille);
        ctnInfosLocalisation.addComponent(getCtnLayoutLocalisation(tblLayoutInfosLocalisation, 70, 3),
                mTextFieldNomVille);
        ctnInfosLocalisation.addComponent(getCtnLayoutLocalisation(tblLayoutInfosLocalisation, 30, 1),
                lLabelNomRegion);
        ctnInfosLocalisation.addComponent(getCtnLayoutLocalisation(tblLayoutInfosLocalisation, 70, 3),
                mTextFieldNomRegion);
        ctnInfosLocalisation.addComponent(getCtnLayoutLocalisation(tblLayoutInfosLocalisation, 30, 1),
                lLabelNomPays);
        ctnInfosLocalisation.addComponent(getCtnLayoutLocalisation(tblLayoutInfosLocalisation, 70, 3),
                mTextFieldNomPays);
        ctnInfosLocalisation.setPreferredH(HAUTEUR_LABEL_TOUS);

        mGpsSearch = new Command(RESSOURCE.get("GPSSearch")) {

            public void actionPerformed(ActionEvent ae) {
                handlerGpsSearch(f);
            }
        };

        mApiSearch = new Command(RESSOURCE.get("CitySearch")) {

            public void actionPerformed(ActionEvent ae) {
                handlerApiSearch(f);
            }
        };

        mManualSearch = new Command(RESSOURCE.get("ManualSearch")) {

            public void actionPerformed(ActionEvent ae) {
                changerModeEdition(true, f);
            }
        };

        mOK = new Command(RESSOURCE.get("Command.OK")) {

            public void actionPerformed(ActionEvent ae) {
                if (sauvegarderParametresEcran(f)) {
                    changerModeEdition(false, f);
                    initialiserInfosLocalisation();
                }
            }
        };

        mAnnuler = new Command(RESSOURCE.get("Command.Cancel")) {

            public void actionPerformed(ActionEvent ae) {
                changerModeEdition(false, f);
                initialiserInfosLocalisation();
            }
        };

        f.setLayout(new BorderLayout());
        f.addComponent(BorderLayout.CENTER, ctnInfosLocalisation);

        // Gestion du comportement (ergonomie)
        if (!Main.isTactile()) {
            mTextFieldNomVille.setFocusable(true);
            mTextFieldNomRegion.setFocusable(true);
            mTextFieldNomPays.setFocusable(true);
            mTextFieldLat.setFocusable(true);
            mTextFieldLng.setFocusable(true);
        }

        changerModeEdition(false, f);
        initialiserInfosLocalisation();
        initialiserClaviers();

        // Ajout des commandes au menu
        int posCmd = 0;
        if (utiliserGPS) {
            f.addCommand(mGpsSearch, posCmd++);
        }
        f.addCommand(mApiSearch, posCmd++);
        f.addCommand(mManualSearch, posCmd++);

        replacerCommandesPrincipales();
    }

    private void handlerApiSearch(final Form f) {

        final Form parametersForm = new Form(RESSOURCE.get("GeocodingWindowTitle"));
        parametersForm.setLayout(new BorderLayout());

        parametersForm.setFocusable(true);

        TableLayout tbGrid = new TableLayout(4, 2);
        Container grid = new Container(tbGrid);
        grid.setLayout(tbGrid);

        Label lLabelNomVille = new Label(RESSOURCE.get("CityName"));
        lLabelNomVille.setUIID(UIID_LABEL_INFO_NAME);
        lLabelNomVille.getUnselectedStyle().setBgTransparency(0);
        lLabelNomVille.getSelectedStyle().setBgTransparency(0);
        lLabelNomVille.setFocusable(true);
        lLabelNomVille.setAlignment(Component.LEFT);

        Label lLabelNomRegion = new Label(RESSOURCE.get("RegionName"));
        lLabelNomRegion.setUIID(UIID_LABEL_INFO_NAME);
        lLabelNomRegion.getUnselectedStyle().setBgTransparency(0);
        lLabelNomRegion.getSelectedStyle().setBgTransparency(0);
        lLabelNomRegion.setFocusable(true);
        lLabelNomRegion.setAlignment(Component.LEFT);

        Label lLabelNomPays = new Label(RESSOURCE.get("CountryName"));
        lLabelNomPays.setUIID(UIID_LABEL_INFO_NAME);
        lLabelNomPays.getUnselectedStyle().setBgTransparency(0);
        lLabelNomPays.getSelectedStyle().setBgTransparency(0);
        lLabelNomPays.setFocusable(true);
        lLabelNomPays.setAlignment(Component.LEFT);

        Label lLabelLanguage = new Label(RESSOURCE.get("GeocodingIndicative"));
        lLabelLanguage.setUIID(UIID_LABEL_INFO_NAME);
        lLabelLanguage.getUnselectedStyle().setBgTransparency(0);
        lLabelLanguage.getSelectedStyle().setBgTransparency(0);
        lLabelLanguage.setFocusable(true);
        lLabelLanguage.setAlignment(Component.LEFT);

        final TextField lTextFieldNomVille = new TextField();
        lTextFieldNomVille.setUIID(UIID_LABEL_LOCALISATION_INFO);
        lTextFieldNomVille.setAlignment(TextField.LEFT);
        lTextFieldNomVille.setText(mTextFieldNomVille.getText());
        lTextFieldNomVille.setEditable(true);
        lTextFieldNomVille.setRows(1);

        final TextField lTextFieldNomRegion = new TextField();
        lTextFieldNomRegion.setUIID(UIID_LABEL_LOCALISATION_INFO);
        lTextFieldNomRegion.setAlignment(TextField.LEFT);
        lTextFieldNomRegion.setText(mTextFieldNomRegion.getText());
        lTextFieldNomRegion.setEditable(true);
        lTextFieldNomRegion.setRows(1);

        final TextField lTextFieldNomPays = new TextField();
        lTextFieldNomPays.setUIID(UIID_LABEL_LOCALISATION_INFO);
        lTextFieldNomPays.setAlignment(TextField.LEFT);
        lTextFieldNomPays.setText(mTextFieldNomPays.getText());
        lTextFieldNomPays.setEditable(true);
        lTextFieldNomPays.setRows(1);

        final ComboBox lCbIndicatif = new ComboBox(INDICATIF_PAYS);
        if (ServiceFactory.getFactory().getPreferences().getLangue().equals(Preferences.LANGUE_EN)) {
            lCbIndicatif.setSelectedIndex(2, true);
        } else if (ServiceFactory.getFactory().getPreferences().getLangue().equals(Preferences.LANGUE_EN)) {
            lCbIndicatif.setSelectedIndex(4, true);
        } else {
            lCbIndicatif.setSelectedIndex(2, true);
        }

        VirtualKeyboard.bindVirtualKeyboard(lTextFieldNomVille,
                VirtualKeyboard.getVirtualKeyboard(mTextFieldNomVille));
        VirtualKeyboard.bindVirtualKeyboard(lTextFieldNomRegion,
                VirtualKeyboard.getVirtualKeyboard(mTextFieldNomRegion));
        VirtualKeyboard.bindVirtualKeyboard(lTextFieldNomPays,
                VirtualKeyboard.getVirtualKeyboard(mTextFieldNomPays));

        grid.addComponent(getCtnLayoutGeocoding(tbGrid, 50, 1),
                lLabelNomVille);
        grid.addComponent(getCtnLayoutGeocoding(tbGrid, 50, 1),
                lTextFieldNomVille);
        grid.addComponent(getCtnLayoutGeocoding(tbGrid, 50, 1),
                lLabelNomRegion);
        grid.addComponent(getCtnLayoutGeocoding(tbGrid, 50, 1),
                lTextFieldNomRegion);
        grid.addComponent(getCtnLayoutGeocoding(tbGrid, 50, 1),
                lLabelNomPays);
        grid.addComponent(getCtnLayoutGeocoding(tbGrid, 50, 1),
                lTextFieldNomPays);
        grid.addComponent(getCtnLayoutGeocoding(tbGrid, 50, 1),
                lLabelLanguage);
        grid.addComponent(getCtnLayoutGeocoding(tbGrid, 50, 1),
                lCbIndicatif);

        //parametersForm.setScrollable(true);
        parametersForm.addComponent(BorderLayout.CENTER, grid);

        Command searchCommand = new Command(RESSOURCE.get("Command.Search")) {

            public void actionPerformed(ActionEvent evt) {
                boolean peutChercher = false;

                // Recherche
                if ((lTextFieldNomVille.getText() != null
                        && lTextFieldNomVille.getText().trim().length() > 0)
                        || (lTextFieldNomRegion.getText() != null
                        && lTextFieldNomRegion.getText().trim().length() > 0)
                        || (lTextFieldNomPays.getText() != null
                        && lTextFieldNomPays.getText().trim().length() > 0)) {
                    peutChercher = true;
                }

                if (!peutChercher) {
                    Command annulerCommand = new Command(RESSOURCE.get("Command.OK")) {

                        public void actionPerformed(ActionEvent evt) {
                            // Fail Parameters
                        }
                    };

                    Dialog.show(RESSOURCE.get("GeocodingWindowParametersLackTitle"),
                            RESSOURCE.get("GeocodingWindowParametersLackContent"), annulerCommand,
                            new Command[]{annulerCommand}, Dialog.TYPE_INFO, null, TIMEOUT_FENETRE_ERROR,
                            CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, true, 1000));
                    return;
                } else {
                    // On lance la recherche

                    //LocationFetcherService_Stub service = new LocationFetcherService_Stub();
                    //service._setProperty(LocationFetcherService_Stub.SESSION_MAINTAIN_PROPERTY, new Boolean(true));

                    final LocationFetcher service = new LocationFetcher();

                    new Thread(new Runnable() {

                        public void run() {
                            try {

                                athan.web.Location lLocation = service.geoname(
                                        lTextFieldNomVille.getText(),
                                        lTextFieldNomRegion.getText(),
                                        lTextFieldNomPays.getText(),
                                        INDICATIF_PAYS[lCbIndicatif.getSelectedIndex()]);

                                mTextFieldNomVille.setText(lLocation.getCityName());
                                mTextFieldNomRegion.setText(lLocation.getRegionName());
                                mTextFieldNomPays.setText(lLocation.getCountryName());

                                if (lLocation.getCoordinates() != null) {
                                    mTextFieldLat.setText(
                                            new Double(lLocation.getCoordinates().getLat()).toString());
                                    mTextFieldLng.setText(
                                            new Double(lLocation.getCoordinates().getLng()).toString());
                                }

                                // On bascule en demande de validation par l'utilisateur
                                changerModeEdition(true, f);

                            } catch (RemoteException exc) {
                                //exc.printStackTrace();
                                Command annulerCommand = new Command(RESSOURCE.get("Command.OK")) {

                                    public void actionPerformed(ActionEvent evt) {
                                    }
                                };
                                Dialog.show(RESSOURCE.get("errorTitle"),
                                        RESSOURCE.get("GeocodingWindowRemoteException"), annulerCommand,
                                        new Command[]{annulerCommand}, Dialog.TYPE_ERROR, null, TIMEOUT_FENETRE_ERROR,
                                        CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, true, 1000));
                                return;
                            } catch (AthanException exc) {
                                //exc.printStackTrace();
                                Command annulerCommand = new Command(RESSOURCE.get("Command.OK")) {

                                    public void actionPerformed(ActionEvent evt) {
                                    }
                                };
                                Dialog.show(RESSOURCE.get("errorTitle"),
                                        RESSOURCE.get("GeocodingWindowCustomException") + "\n" + exc.getMessage(), annulerCommand,
                                        new Command[]{annulerCommand}, Dialog.TYPE_ERROR, null, TIMEOUT_FENETRE_ERROR,
                                        CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, true, 1000));
                                return;
                            } catch (Exception exc) {
                                //exc.printStackTrace();
                                Command annulerCommand = new Command(RESSOURCE.get("Command.OK")) {

                                    public void actionPerformed(ActionEvent evt) {
                                    }
                                };
                                Dialog.show(RESSOURCE.get("errorTitle"),
                                        RESSOURCE.get("GeocodingWindowUnknownException"), annulerCommand,
                                        new Command[]{annulerCommand}, Dialog.TYPE_ERROR, null, TIMEOUT_FENETRE_ERROR,
                                        CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, true, 1000));
                                return;
                            }

                            // Geocoding OK
                            f.showBack();
                        }
                    }).start();
                }
            }
        };

        Command cancelCommand = new Command(RESSOURCE.get("Command.Cancel")) {

            public void actionPerformed(ActionEvent evt) {
                // Annuler
                f.showBack();
            }
        };

        Command helpCommand = new Command(RESSOURCE.get("Command.Help")) {

            public void actionPerformed(ActionEvent evt) {
                Form helpForm = new Form(RESSOURCE.get("Window.Help"));
                helpForm.setLayout(new BorderLayout());
                TextArea helpText = new TextArea(RESSOURCE.getContenu_ConsignesGeocoding(),
                        5, 10);
                helpText.setUIID(UIID_TEXTAREA_HELP);
                helpText.setEditable(false);
                helpForm.setScrollable(true);
                helpText.setFocusable(true);
                helpForm.addComponent(BorderLayout.CENTER, helpText);
                Command c = new Command(RESSOURCE.get("Menu.Back")) {

                    public void actionPerformed(ActionEvent evt) {
                        parametersForm.showBack();
                    }
                };
                helpForm.addCommand(c);
                helpForm.setBackCommand(c);
                helpForm.show();
            }
        };

        parametersForm.addCommand(searchCommand, 0);
        parametersForm.addCommand(helpCommand, 1);
        parametersForm.addCommand(cancelCommand, 2);
        parametersForm.setBackCommand(cancelCommand);
        parametersForm.show();
    }

    private void handlerGpsSearch(final Form f) {
        boolean success = true;
        /*
        Criteria cr = new Criteria();
        cr.setHorizontalAccuracy(500);
        LocationProvider lp;
         */
        final String[] latLng = new String[2];

        try {
            /*
            lp = LocationProvider.getInstance(cr);
            Location l = lp.getLocation(TIMEOUT_GPS); // timeout de 15 secondes
            Coordinates c = l.getQualifiedCoordinates();

            if (c != null ) {
            double lat = c.getLatitude();
            double lon = c.getLongitude();

            latLng[0] = Double.toString(lat);
            latLng[1] = Double.toString(lon);
            }
             */
        } catch (Exception ex) {
            ex.printStackTrace();
            success = false;
        }

        Command okCommand = new Command(RESSOURCE.get("Command.OK")) {

            public void actionPerformed(ActionEvent evt) {
                // OK Gps
                okGpsPerformed(latLng, f);
            }
        };

        Command annulerCommand = new Command(RESSOURCE.get("Command.Cancel")) {

            public void actionPerformed(ActionEvent evt) {
                // Fail Gps
            }
        };

        String contenuDialogue;
        int dialogType;
        if (success) {
            dialogType = Dialog.TYPE_INFO;
            contenuDialogue = RESSOURCE.get("fetchGPSContent");
            contenuDialogue += "\n";
            contenuDialogue += RESSOURCE.get("Latitude") + " " + latLng[0];
            contenuDialogue += "\n";
            contenuDialogue += RESSOURCE.get("Longitude") + " " + latLng[1];

            Dialog.show(RESSOURCE.get("fetchGPSTitle"), contenuDialogue, annulerCommand,
                    new Command[]{annulerCommand, okCommand}, dialogType, null, TIMEOUT_FENETRE_ERROR,
                    CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, true, 1000));

        } else {
            dialogType = Dialog.TYPE_ERROR;
            contenuDialogue = RESSOURCE.get("ErrorFetchGPS");

            Dialog.show(RESSOURCE.get("fetchGPSTitle"), contenuDialogue, annulerCommand,
                    new Command[]{annulerCommand}, dialogType, null, TIMEOUT_FENETRE_ERROR,
                    CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, true, 1000));
        }
    }

    private void changerModeEdition(boolean editable, Form form) {

        mTextFieldLat.setEditable(editable);
        mTextFieldLng.setEditable(editable);
        mTextFieldNomPays.setEditable(editable);
        mTextFieldNomRegion.setEditable(editable);
        mTextFieldNomVille.setEditable(editable);

        int posCmd = 0;
        if (editable) {
            form.addCommand(mOK, posCmd++);
            form.addCommand(mAnnuler, posCmd++);

            form.removeCommand(mGpsSearch);
            form.removeCommand(mApiSearch);
            form.removeCommand(mManualSearch);
        } else {
            if (utiliserGPS) {
                form.addCommand(mGpsSearch, posCmd++);
            }
            form.addCommand(mApiSearch, posCmd++);
            form.addCommand(mManualSearch, posCmd++);

            form.removeCommand(mOK);
            form.removeCommand(mAnnuler);
        }

        replacerCommandesPrincipales();

        form.repaint();
    }

    private boolean sauvegarderParametresEcran(Form pFormAppelante) {
        // On vérifie que les paramètres sont corrects
        double latitude = 0.0;
        double longitude = 0.0;
        boolean okDonnees = true;
        try {
            latitude = Double.valueOf(mTextFieldLat.getText()).doubleValue();
            longitude = Double.valueOf(mTextFieldLng.getText()).doubleValue();

            if (latitude < 0.0 || latitude > 90.0) {
                okDonnees = false;
            }

            if (longitude < -180.0 || longitude > 180.0) {
                okDonnees = false;
            }

        } catch (Exception exc) {
            okDonnees = false;
        }

        if (!okDonnees) {
            // Message d'erreur
            Command okCommand = new Command(RESSOURCE.get("Command.OK"));
            Dialog.show(RESSOURCE.get("errorTitle"), RESSOURCE.get("errorLocationParameters"), okCommand,
                    new Command[]{okCommand}, Dialog.TYPE_ERROR, null, TIMEOUT_FENETRE_ERROR,
                    CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, true, 1000));
            // Sortie
            return false;
        }

        try {
            ServiceFactory.getFactory().getPreferences().set(Preferences.sLatitude, Double.toString(latitude));
            ServiceFactory.getFactory().getPreferences().set(Preferences.sLongitude, Double.toString(longitude));
            ServiceFactory.getFactory().getPreferences().set(Preferences.sCityName, mTextFieldNomVille.getText());
            ServiceFactory.getFactory().getPreferences().set(Preferences.sRegionName, mTextFieldNomRegion.getText());
            ServiceFactory.getFactory().getPreferences().set(Preferences.sCountryName, mTextFieldNomPays.getText());

            // On enregistre les paramètres dans la mémoire du téléphone
            ServiceFactory.getFactory().getPreferences().save();

            // On rafraîchit l'affichage des prières
            ServiceFactory.getFactory().getVuePrincipale().rafraichir(new Date(), true, true);

            // Message de confirmation modif
            Command okCommand = new Command(RESSOURCE.get("Command.OK"));
            Dialog.show(RESSOURCE.get("propertiesSavedTitle"), RESSOURCE.get("propertiesSavedContent"), okCommand,
                    new Command[]{okCommand}, Dialog.TYPE_INFO, null, TIMEOUT_CONFIRMATION_MODIF,
                    CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, true, 1000));

            pFormAppelante.showBack();
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        return true;
    }

    private void initialiserInfosLocalisation() {

        String lLat = StringOutilClient.EMPTY;
        String lLng = StringOutilClient.EMPTY;
        String lVille = StringOutilClient.EMPTY;
        String lRegion = StringOutilClient.EMPTY;
        String lPays = StringOutilClient.EMPTY;

        try {
            lLat = ServiceFactory.getFactory().getPreferences().get(Preferences.sLatitude);
            lLng = ServiceFactory.getFactory().getPreferences().get(Preferences.sLongitude);
            lVille = ServiceFactory.getFactory().getPreferences().get(Preferences.sCityName);
            lRegion = ServiceFactory.getFactory().getPreferences().get(Preferences.sRegionName);
            lPays = ServiceFactory.getFactory().getPreferences().get(Preferences.sCountryName);

        } catch (Exception exc) {
            exc.printStackTrace();
        }

        mTextFieldLat.setText(lLat);
        mTextFieldLng.setText(lLng);
        mTextFieldNomVille.setText(lVille);
        mTextFieldNomRegion.setText(lRegion);
        mTextFieldNomPays.setText(lPays);
    }

    private TableLayout.Constraint getCtnLayoutLocalisation(TableLayout pTB,
            int pPourcentage,
            int pHorizontalSpan) {
        TableLayout.Constraint contrainte = pTB.createConstraint();
        if (pPourcentage == 100) {
            contrainte.setHeightPercentage(10);
        } else {
            //contrainte.setHeightPercentage(18);
        }
        contrainte.setHorizontalSpan(pHorizontalSpan);
        contrainte.setWidthPercentage(pPourcentage);
        return contrainte;
    }

    private TableLayout.Constraint getCtnLayoutGeocoding(TableLayout pTB,
            int pPourcentage,
            int pHorizontalSpan) {
        TableLayout.Constraint contrainte = pTB.createConstraint();
        if (pPourcentage == 100) {
            //contrainte.setHeightPercentage(10);
        } else {
            //contrainte.setHeightPercentage(30);
        }
        contrainte.setHorizontalSpan(pHorizontalSpan);
        contrainte.setWidthPercentage(pPourcentage);
        return contrainte;
    }

    private void okGpsPerformed(String[] pLatLng, Form pForm) {

        mTextFieldLat.setText(pLatLng[0]);
        mTextFieldLng.setText(pLatLng[1]);

        changerModeEdition(true, pForm);
    }

    private void initialiserClaviers() {
        VirtualKeyboard vkbCoordonnees = new VirtualKeyboard();
        vkbCoordonnees.addInputMode(KB_FLOATS_MODE, KB_FLOATS);
        vkbCoordonnees.setInputModeOrder(new String[]{KB_FLOATS_MODE});
        VirtualKeyboard.bindVirtualKeyboard(mTextFieldLat, vkbCoordonnees);
        VirtualKeyboard.bindVirtualKeyboard(mTextFieldLng, vkbCoordonnees);

        VirtualKeyboard vkbNoms = new VirtualKeyboard();
        if (ServiceFactory.getFactory().getPreferences().getLangue().equals(Preferences.LANGUE_EN)) {
            vkbNoms.addInputMode(KB_NOMS_US_MODE, KB_NOMS_US);
            vkbNoms.addInputMode(KB_NOMS_us_MODE, KB_NOMS_us);
            vkbNoms.addInputMode(KB_SYMBOLS_MODE, KB_SYMBOLS);
            vkbNoms.setInputModeOrder(new String[]{KB_NOMS_US_MODE,
                        KB_NOMS_us_MODE, VirtualKeyboard.NUMBERS_MODE,
                        KB_SYMBOLS_MODE});
        } else if (ServiceFactory.getFactory().getPreferences().getLangue().equals(Preferences.LANGUE_FR)) {
            vkbNoms.addInputMode(KB_NOMS_FR_MODE, KB_NOMS_FR);
            vkbNoms.addInputMode(KB_NOMS_fr_MODE, KB_NOMS_fr);
            vkbNoms.addInputMode(KB_SYMBOLS_MODE, KB_SYMBOLS);
            vkbNoms.setInputModeOrder(new String[]{KB_NOMS_FR_MODE,
                        KB_NOMS_fr_MODE, VirtualKeyboard.NUMBERS_MODE,
                        KB_SYMBOLS_MODE});
        } else {
            vkbNoms.addInputMode(KB_NOMS_US_MODE, KB_NOMS_US);
            vkbNoms.addInputMode(KB_NOMS_us_MODE, KB_NOMS_us);
            vkbNoms.addInputMode(KB_SYMBOLS_MODE, KB_SYMBOLS);
            vkbNoms.setInputModeOrder(new String[]{KB_NOMS_US_MODE,
                        KB_NOMS_us_MODE, VirtualKeyboard.NUMBERS_MODE,
                        KB_SYMBOLS_MODE});
        }
        VirtualKeyboard.bindVirtualKeyboard(mTextFieldNomVille, vkbNoms);
        VirtualKeyboard.bindVirtualKeyboard(mTextFieldNomRegion, vkbNoms);
        VirtualKeyboard.bindVirtualKeyboard(mTextFieldNomPays, vkbNoms);
    }
}
