/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package athan.src.Client;

/**
 *
 * @author Saad BENBOUZID
 */
public class AthanConstantes {

    // Variables
    public static final int TIMEOUT_GPS = 15; // en secondes
    public static final int TIMEOUT_FENETRE_ERROR = 0; // en ms
    public static final int TIMEOUT_CONFIRMATION_MODIF = 2000; // en ms
    public static final String PATTERN_LIBELLE = "%s"; // en secondes
    public static final int DUREE_VIBRATION_UNITAIRE = 500; // en ms
    public static final int DUREE_ATTENTE_FLASH_UNITAIRE = 1000; // en ms
    public static final int NB_FLASHS = 5;

    // Constantes
    public static final String FORMAT_WAV = ".wav";
    public static final String FORMAT_MP3 = ".mp3";
    

    // Geocoding
    public static final String [] INDICATIF_PAYS = {
        "ar",
        "de",
        "en",
        "es",
        "fr"
    };

    // Langues
    public static final String [] LANGUE_APPLICATIONS = {
        "English",
        "Français"
    };

    // Méthodes de calcul
    public static final String [] CALCULATION_METHOD_FR = {
        "Jafari",
        "Karachi",
        "Société Islamique Nord-Américaine",
        "Ligue Islamique Mondiale",
        "Mecque",
        "Egypte",
        "Personnalisée"
    };

    public static final String [] CALCULATION_METHOD_EN = {
        "Jafari",
        "Karachi",
        "Islamic Society of North America",
        "Muslim World League",
        "Mekkah",
        "Egypt",
        "Custom"
    };

    public static final String [] CALCULATION_ASR_METHOD = {
        "Shafii",
        "Hanafi"
    };

    // Format horaire
    public static final String [] TIME_FORMAT = {
        "24",
        "12"
    };

    // Icones
    public static final String MENU_AFFICHAGE_COMPAS = "MenuAffichageCompas";
    public static final String MENU_PRAYERS = "MenuPrayers";
    public static final String MENU_ALARMES = "MenuAlarmes";
    public static final String MENU_CHOISIR_VILLE = "MenuChoisirVille";
    public static final String MENU_CONFIG_HEURE_LOCALE = "MenuConfigHeureLocale";
    public static final String MENU_CORRECTION_CALENDRIER = "MenuCorrectionCalendrier";
    public static final String MENU_LANGAGE_APPLICATION = "MenuLangageApplication";
    public static final String MENU_METHODE_CALCUL = "MenuMethodeCalcul";
    public static final String MENU_RECAPITULATIF_MODES = "MenuRecapitulatifModes";

    // Ressources
    public static final String RESSOURCE_ICONS = "icons.res";
    public static final String RESSOURCE_LANGUAGES = "languages.res";
    public static final String RESSOURCE_THEME = "theme.res";
    public static final String LOCALIZATION_BUNDLE = "localize";

    // Theme
    public static final String UIID_BUTTON_OPTIONS = "DemoButton";
    public static final String UIID_LABEL_NEXT_PRAYER = "LabelNextPrayer";
    public static final String UIID_LABEL_PRAYER = "LabelPrayer";
    public static final String UIID_LABEL_CURRENT_DATE = "LabelCurrentDate";
    public static final String UIID_LABEL_LOCALISATION_INFO = "LabelLocalisationInfo";
    public static final String UIID_LABEL_ALERTSONG_INFO = "LabelAlertSongInfo";
    public static final String UIID_TEXTAREA_SEARCH_TOOLTIP = "TextAreaSearchTooltip";
    public static final String UIID_LABEL_PRAYER_NAME = "LabelPrayerName";
    public static final String UIID_LABEL_CURRENT_CITY = "LabelCurrentCity";
    public static final String UIID_LABEL_INFO_NAME = "LabelInfoName";
    public static final String UIID_LABEL_PRAYER_NAME_RINGING = "LabelRingingPrayerName";

    public static final String FONT_LABEL_NEXT_PRAYER = "NextPrayer";
    public static final String FONT_LABEL_PRAYER = "Prayer";
    //public static final String FONT_LABEL_PRAYER_NAME = "PrayerName";
    //public static final String FONT_LABEL_INFO_NAME = "LabelInfoName";

    public static final String[] TRANSITION_TEXT_NO3D = {"Slide Up", "Slide Down", "Fade"};

    // Claviers
    public static final String[][] KB_FLOATS = new String[][]{
        {"0", "1", "2", "3", "4", "5", "$Delete$"},
        {"6", "7", "8", "9", ".", "-", "$OK$"}
    };
    public static final String KB_FLOATS_MODE = "Val.";

    public static final String[][] KB_INTEGER = new String[][]{
        {"0", "1", "2", "3", "4", "5", "$Delete$"},
        {"6", "7", "8", "9", "-", "$OK$"}
    };
    public static final String KB_INTEGER_MODE = "Val.";

    public static final String[][] KB_NOMS_fr = new String[][]{
        {"\u0061", "\u007A", "\u0065", "\u0072", "\u0074", "\u0079",
         "\u0075", "\u0069", "\u006F", "\u0070"},
        {"\u0071", "\u0073", "\u0064", "\u0066", "\u0067", "\u0068",
         "\u006A", "\u006B", "\u006C", "\u006D"},
        {"$Shift$", "\u0077", "\u0078", "\u0063", "\u0076", "\u0062",
         "\u006E", "$Delete$"},
        {"$Mode$", "$Space$", "$OK$"}
    };
    public static final String[][] KB_NOMS_FR = new String[][]{
        {"\u0041", "\u005A", "\u0045", "\u0052", "\u0054", "\u0059",
         "\u0055", "\u0049", "\u004F", "\u0050"},
        {"\u0051", "\u0053", "\u0044", "\u0046", "\u0047", "\u0048",
         "\u004A", "\u004B", "\u004C", "\u004D"},
        {"$Shift$", "\u0057", "\u0058", "\u0043", "\u0056", "\u0042",
         "\u004E", "$Delete$"},
        {"$Mode$", "$Space$", "$OK$"}
    };
    public static final String KB_NOMS_fr_MODE = "a-z";
    public static final String KB_NOMS_FR_MODE = "A-Z";

    public static final String[][] KB_NOMS_us = new String[][]{
         {"\u0071", "\u007A", "\u0065", "\u0072", "\u0074", "\u0079",
         "\u0075", "\u0069", "\u006F", "\u0070"},
        {"\u0061", "\u0073", "\u0064", "\u0066", "\u0067", "\u0068",
         "\u006A", "\u006B", "\u006C", "\u006D"},
        {"$Shift$", "\u0077", "\u0078", "\u0063", "\u0076", "\u0062",
         "\u006E", "$Delete$"},
        {"$Mode$", "$Space$", "$OK$"}
    };
    public static final String[][] KB_NOMS_US = new String[][]{
        {"\u0051", "\u005A", "\u0045", "\u0052", "\u0054", "\u0059",
         "\u0055", "\u0049", "\u004F", "\u0050"},
        {"\u0041", "\u0053", "\u0044", "\u0046", "\u0047", "\u0048",
         "\u004A", "\u004B", "\u004C", "\u004D"},
        {"$Shift$", "\u0057", "\u0058", "\u0043", "\u0056", "\u0042",
         "\u004E", "$Delete$"},
        {"$Mode$", "$Space$", "$OK$"}
    };
    public static final String KB_NOMS_us_MODE = "a-z";
    public static final String KB_NOMS_US_MODE = "A-Z";

     public static final String[][] KB_SYMBOLS = new String[][]{
        {"\u0027", "\u0028", "\u0029", "\u005B", "\u005D", "\u005F"},
        {"\u005E", "\u0040", "\u003A", "\u0023", "\u0024", "\u003B"},
        {"\u002D", "\u002E", "\u003C", "\u003D", "\u003E", "\u007E"},
        {"\u00DF", "\u0021", "\u00A1", "\"", "\u00E7", "\u00C7"},
        {"$Mode$", "\u00D7", "\u0300", "\u0301", "\u0302", "$Delete$", "$OK$"}
    };
    public static final String KB_SYMBOLS_MODE = "?#-";
}
