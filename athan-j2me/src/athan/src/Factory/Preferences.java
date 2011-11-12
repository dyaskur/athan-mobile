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
 * Gestion de la sauvegarde et de la restauration des préférences utilisateurs
 * pour l'ensemble de l'application.
 *
 * @author Saad BENBOUZID
 */
public class Preferences {

    public static final String LANGUE_FR = "fr";
    public static final String LANGUE_EN = "en";
    public static final String MODE_NONE = "N";
    public static final String MODE_FLASH = "V";
    public static final String MODE_SONG = "S";
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
    public static final String sCustomImsakSelector = "customImsakSelector";
    public static final String sCustomImsakValue = "customImsakValue";
    public static final String sCustomMaghrebSelector = "customMaghrebSelector";
    public static final String sCustomMaghrebValue = "customMaghrebValue";
    public static final String sCustomIshaaSelector = "customIshaaSelector";
    public static final String sCustomIshaaValue = "customIshaaValue";
    public static final String sDisplayImsak = "displayImsak";
    public static final String sDisplayChourouk = "displayChourouk";
    public static final String sLangue = "langue";
    public static final String sAlertSobh = "alertSobh";
    public static final String sAlertDohr = "alertDohr";
    public static final String sAlertAsr = "alertAsr";
    public static final String sAlertMaghreb = "alertMaghreb";
    public static final String sAlertIshaa = "alertIshaa";
    public static final String sAlertMode = "alertMode";
    public static final String sAlertFile = "alertFile";
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
        int lImsakSelector = Integer.parseInt(get(sCustomImsakSelector));
        double lImsakValue = Double.parseDouble(get(sCustomImsakValue));
        int lMaghrebSelector = Integer.parseInt(get(sCustomMaghrebSelector));
        double lMaghrebValue = Double.parseDouble(get(sCustomMaghrebValue));
        int lIshaaSelector = Integer.parseInt(get(sCustomIshaaSelector));
        double lIshaaValue = Double.parseDouble(get(sCustomIshaaValue));

        retour.setFajrAngle(lFajrAngle);
        retour.setImsakSelector(lImsakSelector);
        retour.setMaghrebSelector(lMaghrebSelector);
        retour.setImsakValue(lImsakValue);
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
        put(sCityName, "Lyon");
        put(sLatitude, "45.75");
        put(sLongitude, "4.85");
        put(sTimeZone, String.valueOf(SalaahTimeCalculator.getTimeZone()));
        put(sDecalageHoraire, "0");
        put(sFormatHoraire, "0");
        put(sMethodeJuridiqueAsr, "0");
        put(sCalculationMethod, "6");
        put(sCustomFajrAngle, "13");
        put(sCustomImsakSelector, "1");
        put(sCustomImsakValue, "15");
        put(sCustomMaghrebSelector, "1");
        put(sCustomMaghrebValue, "0");
        put(sCustomIshaaSelector, "0");
        put(sCustomIshaaValue, "12");
        put(sDisplayImsak, Integer.toString(StringOutilClient.FALSE));
        put(sDisplayChourouk, Integer.toString(StringOutilClient.FALSE));
        put(sLangue, LANGUE_EN);
        put(sAlertSobh, Integer.toString(StringOutilClient.TRUE));
        put(sAlertDohr, Integer.toString(StringOutilClient.FALSE));
        put(sAlertAsr, Integer.toString(StringOutilClient.FALSE));
        put(sAlertMaghreb, Integer.toString(StringOutilClient.FALSE));
        put(sAlertIshaa, Integer.toString(StringOutilClient.FALSE));
        put(sAlertMode, MODE_FLASH);
        put(sAlertFile, StringOutilClient.EMPTY);
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
