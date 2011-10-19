/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package athan.src.options;

import athan.src.Client.Main;
import athan.src.Client.Menu;
import athan.src.Factory.Preferences;
import athan.src.Factory.ResourceReader;
import athan.src.Factory.ServiceFactory;
import athan.src.Outils.StringOutilClient;
import athan.src.microfloat.Real;
import com.sun.lwuit.Container;
import com.sun.lwuit.Form;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;

/**
 * Menu d'affichage de la boussole
 * 
 * @author Saad BENBOUZID
 */
public class MenuCompass extends Menu {

    private static final double ERREUR_CALCUL = -1.0;
    private static final double LATITUDE_MECQUE = 21.42667;
    private static final double LONGITUDE_MECQUE = 39.82611;
    
    private static final int INDEX_RESULTAT_ANGLE = 0;
    private static final int INDEX_RESULTAT_DISTANCE = 1;

    private Label mLabelDistance;
    private Label mLabelAngle;
    private final ResourceReader RESSOURCE = ServiceFactory.getFactory().getResourceReader();

    protected String getHelp() {
        return ServiceFactory.getFactory().getResourceReader().get("Menu.Help");
    }

    protected String getName() {
        return ServiceFactory.getFactory().getResourceReader().get("MenuCompass");
    }

    private void editerLabel(Label pLabel) {
        pLabel.setUIID(UIID_LABEL_COMPASS_INFO);
        pLabel.getUnselectedStyle().setBgTransparency(0);
        pLabel.getSelectedStyle().setBgTransparency(0);
        pLabel.setFocusable(true);
        pLabel.setAlignment(Label.CENTER);
    }

    protected void execute(final Form f) {

        applyTactileSettings(f);

        Image imgBackGround = Main.icons.getImage("CompasBG");
        Image imgForeGround = Main.icons.getImage("CompasFG");

        // On récupère la valeur de l'angle
        double latitude = Double.parseDouble(ServiceFactory.getFactory().getPreferences().get(Preferences.sLatitude));
        double longitude = Double.parseDouble(ServiceFactory.getFactory().getPreferences().get(Preferences.sLongitude));

        // Calcule l'angle
        double[] calculs = getAngle(latitude, longitude);

        // Initialise l'image à une boussole vierge
        Image imgBoussole = imgBackGround;

        // Initialise les labels
        mLabelAngle = new Label();
        editerLabel(mLabelAngle);
        mLabelDistance = new Label();
        editerLabel(mLabelDistance);

        if (calculs[INDEX_RESULTAT_ANGLE] == ERREUR_CALCUL || calculs[INDEX_RESULTAT_DISTANCE] == ERREUR_CALCUL) {
            // Erreur de calcul
            mLabelAngle.setText(RESSOURCE.get("errorCompassAngleCalculation"));
            mLabelDistance.setText(RESSOURCE.get("errorCompassDistanceCalculation"));
        } else if (latitude == LATITUDE_MECQUE && longitude == LONGITUDE_MECQUE) {
            // On est déjà à La Mecque
            mLabelAngle.setText(RESSOURCE.get("infoCompassAlreadyMekkah"));
            mLabelDistance.setText(StringOutilClient.EMPTY);
        } else {
            // Tourne l'image du pointeur
            int angleArrondi = (int) Math.ceil(calculs[INDEX_RESULTAT_ANGLE]);
            imgForeGround = imgForeGround.rotate(angleArrondi);
            imgBoussole = getOverlay(imgBackGround, imgForeGround, f.getStyle().getBgColor());

            // Distance
            int distance = (int) Math.ceil(calculs[INDEX_RESULTAT_DISTANCE]);
            mLabelAngle.setText(Integer.toString(angleArrondi) + RESSOURCE.get("compassAngleSummary"));
            mLabelDistance.setText(Integer.toString(distance) + RESSOURCE.get("compassDistanceSummary"));
        }

        // Conteneur de labels
        Container ctnLabels = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        ctnLabels.addComponent(mLabelAngle);
        ctnLabels.addComponent(mLabelDistance);

        Label lblImageBoussole = new Label(imgBoussole);
        lblImageBoussole.setAlignment(Label.CENTER);

        // Conteneur de labels
        Container ctnGlobal = new Container(new BorderLayout());
        ctnGlobal.addComponent(BorderLayout.CENTER, lblImageBoussole);
        ctnGlobal.addComponent(BorderLayout.SOUTH, ctnLabels);

        ctnGlobal.setPreferredW(f.getWidth());

        f.addComponent(ctnGlobal);
    }

    private Image getOverlay(Image background, Image overlay, int bgColor) {
        Image img = Image.createImage(background.getWidth(), background.getHeight());
        Graphics mG = img.getGraphics();
        int x = background.getWidth() - overlay.getWidth();
        int y = background.getHeight() - overlay.getHeight();

        mG.setClip(0, 0, background.getWidth(), background.getHeight());
        mG.setColor(bgColor);
        mG.fillRect(0, 0, background.getWidth(), background.getHeight());

        mG.drawImage(background, x, y);
        mG.drawImage(overlay, x, y);

        return img;
    }

    private double[] getAngle(double lat, double lon) {

        double retour[] = {ERREUR_CALCUL, ERREUR_CALCUL};

        try {
            double R = 6371; //rayon terrestre en km
            double dLat = (LATITUDE_MECQUE - lat) * Math.PI / 180.0;
            double dLon = (LONGITUDE_MECQUE - lon) * Math.PI / 180.0;
            double lat1 = lat * Math.PI / 180.0;
            double lat2 = LATITUDE_MECQUE * Math.PI / 180.0;

            // angle
            double y = Math.sin(dLon) * Math.cos(lat2);
            double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);
            retour[INDEX_RESULTAT_ANGLE] = atan2(y, x) * 180.0 / Math.PI;

            // distance
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
            double c = 2 * atan2(Math.sqrt(a), Math.sqrt(1 - a));
            retour[INDEX_RESULTAT_DISTANCE] = R * c;

        } catch (Exception exc) {
            exc.printStackTrace();
        }

        return retour;
    }

    private double atan2(double y, double x) {
        double M_PI = Math.PI;
        double M_PI_2 = M_PI / 2.0;
        double absx, absy, val;
        if (x == 0 && y == 0) {
            return 0;
        }
        absy = y < 0 ? -y : y;
        absx = x < 0 ? -x : x;
        if (absy - absx == absy) {
            /* x negligible compared to y */
            return y < 0 ? -M_PI_2 : M_PI_2;
        }
        if (absx - absy == absx) {
            /* y negligible compared to x */
            val = 0.0;
        } else {
            val = arctan(y / x);
        }
        if (x > 0) {
            /* first or fourth quadrant; already correct */
            return val;
        }
        if (y < 0) {
            /* third quadrant */
            return val - M_PI;
        }
        return val + M_PI;
    }

    private double arctan(double x) {
        Real r = new Real(Double.toString(x));
        r.atan();
        return Double.parseDouble(r.toString());
    }

    protected String getIconBaseName() {
        return MENU_AFFICHAGE_COMPAS;
    }
}
