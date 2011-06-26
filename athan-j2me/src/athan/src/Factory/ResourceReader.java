/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package athan.src.Factory;

import athan.src.Client.AthanConstantes;
import athan.src.Client.Main;
import athan.src.Outils.StringOutilClient;
import com.sun.lwuit.plaf.UIManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Calendar;
import org.kxml.Xml;
import org.kxml.parser.ParseEvent;
import org.kxml.parser.XmlParser;

/**
 *
 * @author Saad BENBOUZID
 */
public class ResourceReader implements AthanConstantes {

    private final static String RESOURCE_FR = "properties_fr-FR.xml";
    private final static String RESOURCE_EN = "properties_en-US.xml";

    private String mLocale;
    private String xmlFileName;

    // Contenus
    private String contenu_about;
    private String contenu_consignesGeocoding;

    public ResourceReader(Preferences lPreferences) throws IOException {
        this(null, lPreferences);
    }

    public ResourceReader(String pLocale, Preferences lPreferences) throws IOException {

        try {
            if (pLocale == null) {
                pLocale = lPreferences.getLangue();
            }

            if (pLocale.equals(Preferences.LANGUE_EN)) {
                xmlFileName = RESOURCE_EN;
            } else if (pLocale.equals(Preferences.LANGUE_FR)) {
                xmlFileName = RESOURCE_FR;
            }

            mLocale = pLocale;

            // Affecte le resource bundle de localization
            /*
            String defaultLocale = System.getProperty("microedition.locale");
            if (defaultLocale != null) {
                System.out.println(defaultLocale);
                if (defaultLocale.length() > 2) {
                    defaultLocale = defaultLocale.substring(0,2);
                }
            } else {
                defaultLocale = "en";
            }
            */
            UIManager.getInstance().setResourceBundle(Main.languages
                                        .getL10N(LOCALIZATION_BUNDLE, mLocale));

        } catch (Exception exc) {
            // valeur par défaut
            xmlFileName = RESOURCE_EN;
            exc.printStackTrace();
        }

        // On parse tout le contenu pour l'enregistrer dans des variables
        // locales
        parse(this.getClass().getResourceAsStream("/" + xmlFileName));
    }

    public String get(String pKey) {
        return UIManager.getInstance().localize(pKey, mLocale);
    }

    public void parse(InputStream in) throws IOException {

        Reader reader = new InputStreamReader(in, "UTF-8");
        XmlParser parser = new XmlParser(reader);

        parser.skip();
        parser.read(Xml.START_TAG, null, "strings");

        // Contenus
        contenu_about = lireTexte(parser);
        contenu_consignesGeocoding = lireTexte(parser);
    }

    private String lireTexte(XmlParser pParser) throws IOException {

        String retour = StringOutilClient.EMPTY;
        boolean lire = true;

        while(lire) {
            ParseEvent pe = pParser.read();
            if (pe.getType() == Xml.START_TAG) {
                pe = pParser.read();
                retour = pe.getText();
                lire = false;
            }
        }

        return retour;
    }

    /*
    // Noms des prières
    public String getSobh() { return sobh; }
    public String getChourouk() { return chourouk; }
    public String getDohr() { return dohr; }
    public String getAsr() { return asr; }
    public String getMaghreb() { return maghreb; }
    public String getIshaa() { return ishaa; }

    // Menus
    public String getMenu_About() { return menu_about; }
    public String getMenu_Retour() { return menu_retour; }
    public String getMenu_Aide() { return menu_aide; }

    // Titres des fenêtres
    public String getFenetre_Aide() { return fenetre_aide; }
    */
    // Contenus
    public String getContenu_About() { return contenu_about; }
    public String getContenu_ConsignesGeocoding() { return contenu_consignesGeocoding; }

    /**
     * 1er index : jour de la semaine
     * 2e index : reste de la date
     *
     * @param pCalendar date
     * @return la date formattée
     */
    public String[] getDateFormattee(Calendar pCalendar) {

        String[] retour = new String[2];

        String jour = StringOutilClient.EMPTY;

        switch(pCalendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                jour = this.get("Monday");
                break;
            case Calendar.TUESDAY:
                jour = this.get("Tuesday");
                break;
            case Calendar.WEDNESDAY:
                jour = this.get("Wednesday");
                break;
            case Calendar.THURSDAY:
                jour = this.get("Thursday");
                break;
            case Calendar.FRIDAY:
                jour = this.get("Friday");
                break;
            case Calendar.SATURDAY:
                jour = this.get("Saturday");
                break;
            case Calendar.SUNDAY:
                jour = this.get("Sunday");
                break;
            default:
                break;
        }

        String mois = StringOutilClient.EMPTY;

        switch(pCalendar.get(Calendar.MONTH)) {
            case Calendar.JANUARY:
                mois = this.get("January");
                break;
            case Calendar.FEBRUARY:
                mois = this.get("February");
                break;
            case Calendar.MARCH:
                mois = this.get("March");
                break;
            case Calendar.APRIL:
                mois = this.get("April");
                break;
            case Calendar.MAY:
                mois = this.get("May");
                break;
            case Calendar.JUNE:
                mois = this.get("June");
                break;
            case Calendar.JULY:
                mois = this.get("July");
                break;
            case Calendar.AUGUST:
                mois = this.get("August");
                break;
            case Calendar.SEPTEMBER:
                mois = this.get("September");
                break;
            case Calendar.OCTOBER:
                mois = this.get("October");
                break;
            case Calendar.NOVEMBER:
                mois = this.get("November");
                break;
            case Calendar.DECEMBER:
                mois = this.get("December");
                break;
            default:
                break;
        }

        if (mLocale.equals(Preferences.LANGUE_EN)) {
            retour[0] = jour;
            retour[1] = mois + " the " + pCalendar.get(Calendar.DAY_OF_MONTH)
                    + retournerSuffixeNombreEn(pCalendar.get(Calendar.DAY_OF_MONTH))
                    + " " + pCalendar.get(Calendar.YEAR);
        } else if (mLocale.equals(Preferences.LANGUE_FR)) {
            retour[0] = jour;
            retour[1] = pCalendar.get(Calendar.DAY_OF_MONTH)
                    + " " + mois + " " + pCalendar.get(Calendar.YEAR);
        }

        return retour;
    }

    private String retournerSuffixeNombreEn(int pNombre) {

        String retour = "th";

        if (pNombre % 10 == 1) {
            retour = "st";
        } else if (pNombre % 10 == 2) {
            retour = "nd";
        }

        return retour;
    }

}
