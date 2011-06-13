/*
 * Gère les sauvegardes et chargements des préférences utilisateurs
 */

package athan.src.Factory;

import athan.src.SalaahCalc.SalaahTimeCalculator;
import athan.src.Outils.StringOutilClient;
import athan.src.SalaahCalc.CalculationCustomParams;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

/**
 *
 * @author Saad BENBOUZID
 */
public class Preferences {

    public static final String LANGUE_FR = "fr";
    public static final String LANGUE_EN = "en";

    public static final String RECORD_STORE_NAME = "preferences";
    
    public static final String sCountryName = "countryName";
    public static final String sRegionName = "regionName";
    public static final String sCityName = "cityName";
    public static final String sLatitude = "latitude";
    public static final String sLongitude = "longitude";
    public static final String sTimeZone = "timeZone";
    public static final String sDecalageHoraire = "decalageHoraire";
    public static final String sFormatHoraire = "formatHoraire";
    public static final String sMethodeJuridiqueAsr = "methodeJuristiqueAsr";
    public static final String sCalculationMethod = "methodeCalcul";
    public static final String sCustomFajrAngle = "customFajrAngle";
    public static final String sCustomMaghrebSelector = "customMaghrebSelector";
    public static final String sCustomMaghrebValue = "customMaghrebValue";
    public static final String sCustomIshaaSelector = "customIshaaSelector";
    public static final String sCustomIshaaValue = "customIshaaValue";
    public static final String sLangue = "langue";

    private String mRecordStoreName;
    private Hashtable mHashtable;

    public Preferences(String recordStoreName)
            throws RecordStoreException {
        mRecordStoreName = recordStoreName;
        mHashtable = new Hashtable();

        // Récupère les paramètres par défaut
        loadDefaults();

        // Tente de récupérer ce qu'il y a en mémoire
        load();
    }

    public String get(String key) {
        return (String) mHashtable.get(key);
    }

    public CalculationCustomParams getCalculationCustomParams() {

        CalculationCustomParams retour = new CalculationCustomParams();

        double lFajrAngle = Double.parseDouble(get(sCustomFajrAngle));
        int lMaghrebSelector = Integer.parseInt(get(sCustomMaghrebSelector));
        double lMaghrebValue = Double.parseDouble(get(sCustomMaghrebValue));
        int lIshaaSelector = Integer.parseInt(get(sCustomIshaaSelector));
        double lIshaaValue = Double.parseDouble(get(sCustomIshaaValue));

        retour.setFajrAngle(lFajrAngle);
        retour.setMaghrebSelector(lMaghrebSelector);
        retour.setMaghrebValue(lMaghrebValue);
        retour.setIshaaSelector(lIshaaSelector);
        retour.setIshaaValue(lIshaaValue);
        
        return retour;
    }
    
    public void set(String key, String value) {
        mHashtable.put(key, value);
        System.out.println("Attribut \"" + key + "\" changé en \""
                + value + "\"");
    }

    public void put(String key, String value) {
        if (value == null) {
            value = "";
        }
        mHashtable.put(key, value);
    }

    private void load() throws RecordStoreException {
        RecordStore rs = null;
        RecordEnumeration re = null;

        try {
            rs = RecordStore.openRecordStore(mRecordStoreName, true);
            re = rs.enumerateRecords(null, null, false);
            while (re.hasNextElement()) {
                byte[] raw = re.nextRecord();
                String pref = new String(raw);
                // Parse out the name.
                int index = pref.indexOf('|');
                String name = pref.substring(0, index);
                String value = pref.substring(index + 1);
                if (!StringOutilClient.isEmpty(value)) {
                    put(name, value);
                    System.out.println("Attribut \"" + name + "\" chargé en \""
                        + value + "\"");
                }
            }
        } finally {
            if (re != null) {
                re.destroy();
            }
            if (rs != null) {
                rs.closeRecordStore();
            }
        }
    }

    public void save() throws RecordStoreException {
        RecordStore rs = null;
        RecordEnumeration re = null;
        try {
            rs = RecordStore.openRecordStore(mRecordStoreName, true);
            re = rs.enumerateRecords(null, null, false);

            // First remove all records, a little clumsy.
            while (re.hasNextElement()) {
                int id = re.nextRecordId();
                rs.deleteRecord(id);
            }

            // Now save the preferences records.
            Enumeration keys = mHashtable.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                String value = get(key);
                String pref = key + "|" + value;
                byte[] raw = pref.getBytes();
                rs.addRecord(raw, 0, raw.length);
                System.out.println("Attribut \"" + key + "\" sauvegardé en \""
                        + value + "\"");
            }
        } finally {
            if (re != null) {
                re.destroy();
            }
            if (rs != null) {
                rs.closeRecordStore();
            }
        }
    }

    private void loadDefaults() throws RecordStoreException {
        put(sCountryName, "France");
        put(sRegionName, "Rhône-Alpes");
        put(sCityName, "Villeurbanne");
        put(sLatitude, "45.7667");
        put(sLongitude, "4.8833");
        put(sTimeZone, String.valueOf(SalaahTimeCalculator.getTimeZone()));
        put(sDecalageHoraire, "0");
        put(sFormatHoraire, "0");
        put(sMethodeJuridiqueAsr, "0");
        put(sCalculationMethod, "6");
        put(sCustomFajrAngle, "12");
        put(sCustomMaghrebSelector, "1");
        put(sCustomMaghrebValue, "0");
        put(sCustomIshaaSelector, "0");
        put(sCustomIshaaValue, "12");
        put(sLangue, LANGUE_EN);
    }

    public String getLangue() {
        String retour = LANGUE_EN;

        if (mHashtable.containsKey(sLangue)
                && get(sLangue) != null) {
            retour = get(sLangue);
        }

        return retour;
    }
}

