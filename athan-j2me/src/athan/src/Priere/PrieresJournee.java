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

package athan.src.Priere;

import athan.src.Factory.Preferences;
import athan.src.Factory.ResourceReader;
import athan.src.Factory.ServiceFactory;
import athan.src.Outils.StringOutilClient;
import athan.src.SalaahCalc.TimeFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Classe représentant l'ensemble des horaires des prières d'une journée donnée.
 *
 * @author BENBOUZID
 */
public class PrieresJournee {

    private Calendar mCalendar;

    private Horaire mImsak;
    private Horaire mSobh;
    private Horaire mChourouk;
    private Horaire mDohr;
    private Horaire mAsr;
    private Horaire mMaghreb;
    private Horaire mIshaa;

    public PrieresJournee(Date pDateJour,
                            Horaire pImsak,
                            Horaire pSohb,
                            Horaire pChourouk,
                            Horaire pDohr,
                            Horaire pAsr,
                            Horaire pMaghreb,
                            Horaire pIshaa) {

        setDateJour(pDateJour);

        mImsak = pImsak;
        mSobh = pSohb;
        mChourouk = pChourouk;
        mDohr = pDohr;
        mAsr = pAsr;
        mMaghreb = pMaghreb;
        mIshaa = pIshaa;
    }

    private String retournerNombre(int pNombre) {
        if (pNombre < 10) {
            return "0" + String.valueOf(pNombre);
        } else {
            return String.valueOf(pNombre);
        }
    }

    public String getHoraire() {
        
        int formatHoraire = 0;
        
        try {
            formatHoraire = Integer.parseInt(ServiceFactory.getFactory().getPreferences()
                                .get(Preferences.sFormatHoraire));
        } catch (Exception exc) {

        }

        if (formatHoraire == TimeFormat.H12.getValue()) {
            String prefixe = StringOutilClient.EMPTY;

            if (mCalendar.get(Calendar.AM_PM) == Calendar.AM) {
                prefixe = "AM";
            } else {
                prefixe = "PM";
            }
            return prefixe + " " + retournerNombre(mCalendar.get(Calendar.HOUR))
                    + ":" + retournerNombre(mCalendar.get(Calendar.MINUTE))
                    + ":" + retournerNombre(mCalendar.get(Calendar.SECOND));

        } else if (formatHoraire == TimeFormat.H24.getValue()) {
            return retournerNombre(mCalendar.get(Calendar.HOUR_OF_DAY))
                    + ":" + retournerNombre(mCalendar.get(Calendar.MINUTE))
                    + ":" + retournerNombre(mCalendar.get(Calendar.SECOND));
        } else {
            return String.valueOf(mCalendar.getTime().getTime());
        }
    }

    public String getJour() {
        int j = mCalendar.get(Calendar.DAY_OF_MONTH);
        if (j < 10) {
            return "0" + j;
        } else {
            return "" + j;
        }
    }

    public String getMois() {
        int m = mCalendar.get(Calendar.MONTH) + 1;
        if (m < 10) {
            return "0" + m;
        } else {
            return "" + m;
        }
    }

    public String getAnnee() {
        int a = mCalendar.get(Calendar.YEAR);
        if (a < 10) {
            return "0" + a;
        } else {
            return "" + a;
        }
    }

    public String[] getDateFormattee() {
        ResourceReader RESSOURCE = ServiceFactory.getFactory()
                .getResourceReader();
        
        return RESSOURCE.getDateFormattee(mCalendar);
    }

    /**
     * @return the mImsak
     */
    public Horaire getImsak() {
        return mImsak;
    }

    /**
     * @return the mSobh
     */
    public Horaire getSobh() {
        return mSobh;
    }

    /**
     * @return the mChourouk
     */
    public Horaire getChourouk() {
        return mChourouk;
    }

    /**
     * @return the mDohr
     */
    public Horaire getDohr() {
        return mDohr;
    }

    /**
     * @return the mAsr
     */
    public Horaire getAsr() {
        return mAsr;
    }

    /**
     * @return the mMaghreb
     */
    public Horaire getMaghreb() {
        return mMaghreb;
    }

    /**
     * @return the mIshaa
     */
    public Horaire getIshaa() {
        return mIshaa;
    }

    public void setDateJour(Date pDateJour) {
        mCalendar = Calendar.getInstance(TimeZone.getDefault());
        mCalendar.setTime(pDateJour);
    }
}
/**
    public static final int POS_FAJR_ANGLE = 0;
    public static final int POS_IMSAK_SELECTOR = 1;
    public static final int POS_IMSAK_VALUE = 2;
    public static final int POS_MAGHRIB_SELECTOR = 3;
    public static final int POS_MAGHRIB_VALUE = 4;
    public static final int POS_ISHAA_SELECTOR = 5;
    public static final int POS_ISHAA_VALUE = 6;
 */