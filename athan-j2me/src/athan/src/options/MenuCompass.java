/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package athan.src.options;

import athan.src.Client.Main;
import athan.src.Client.Menu;
import athan.src.Factory.Preferences;
import athan.src.Factory.ResourceReader;
import athan.src.Factory.ServiceFactory;
import athan.src.microfloat.Real;
import com.sun.lwuit.Container;
import com.sun.lwuit.Form;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.layouts.BorderLayout;

/**
 * Menu d'affichage de la boussole
 * 
 * @author Saad BENBOUZID
 */
public class MenuCompass extends Menu {

    private static final double ERREUR_CALCUL = -1.0;
    private static final double LATITUDE_MECQUE = 21.42667;
    private static final double LONGITUDE_MECQUE = 39.82611;

    private final ResourceReader RESSOURCE = ServiceFactory.getFactory().getResourceReader();

    protected String getHelp() {
        return ServiceFactory.getFactory().getResourceReader().get("Menu.Help");
    }

    protected String getName() {
        return ServiceFactory.getFactory().getResourceReader().get("MenuCompass");
    }

    protected void execute(final Form f) {

        applyTactileSettings(f);

        Image imgBackGround = Main.icons.getImage("CompasBG");
        Image imgForeGround = Main.icons.getImage("CompasFG");

        // On récupère la valeur de l'angle
        double latitude = Double.parseDouble(ServiceFactory.getFactory().getPreferences().get(Preferences.sLatitude));
        double longitude = Double.parseDouble(ServiceFactory.getFactory().getPreferences().get(Preferences.sLongitude));

        double angle = getAngle(latitude, longitude);

        if (angle == ERREUR_CALCUL) {
            // TODO Erreur de calcul
        } else if (latitude == LATITUDE_MECQUE && longitude == LONGITUDE_MECQUE) {
            // TODO déjà à La Mecque
        } else {
            // Tourne l'image du pointeur
            imgForeGround = imgForeGround.rotate((int) Math.ceil(angle));
        }

        System.out.println(angle);

        Image img = getOverlay(imgBackGround, imgForeGround, f.getStyle().getBgColor());

        Label lblImage = new Label(img);
        lblImage.setAlignment(Label.CENTER);

        Container ctnImage = new Container(new BorderLayout());
        ctnImage.addComponent(BorderLayout.CENTER, lblImage);

        f.addComponent(ctnImage);

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

    private double getAngle(double lat, double lon) {

        double retour = ERREUR_CALCUL;

        try {

            double dLon = (LONGITUDE_MECQUE - lon) * Math.PI / 180.0;
            double lat1 = lat * Math.PI / 180.0;
            double lat2 = LATITUDE_MECQUE * Math.PI / 180.0;

            double y = Math.sin(dLon) * Math.cos(lat2);
            double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);
            retour = atan2(y, x) * 180.0 / Math.PI;

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
        System.out.println("x=" + x);
        System.out.println("arctan =" + r.toString());
        return Double.parseDouble(r.toString());
    }

    protected String getIconBaseName() {
        return MENU_AFFICHAGE_COMPAS;
    }
}
