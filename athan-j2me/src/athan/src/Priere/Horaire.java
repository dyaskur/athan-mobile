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

import athan.src.Factory.ResourceReader;
import athan.src.Factory.ServiceFactory;
import athan.src.Outils.StringOutilClient;
import athan.src.SalaahCalc.TimeFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Classe représentant une horaire de prière.
 *
 * @author Saad BENBOUZID
 */
public class Horaire {

    private String mHoraire;
    private boolean mEstProchaine;
    private Calendar mHoraireDate;

    public Horaire(String pHoraire, int pTimeFormat) {

        mHoraire = pHoraire;
        mEstProchaine = false;

        ResourceReader RESSOURCE = ServiceFactory.getFactory().getResourceReader();

        Calendar calActuel = Calendar.getInstance();
        calActuel.setTime(new Date());

        Calendar calHoraire = Calendar.getInstance();
        calHoraire.setTime(new Date());

        if (pTimeFormat == TimeFormat.H24.getValue()) {
            calHoraire.set(Calendar.HOUR_OF_DAY, Integer.parseInt(pHoraire.substring(0, 2)));
            calHoraire.set(Calendar.MINUTE, Integer.parseInt(pHoraire.substring(3, 5)));

            if (calHoraire.getTime().getTime() > calActuel.getTime().getTime()) {
                mEstProchaine = true;
            }
        } else if (pTimeFormat == TimeFormat.H12.getValue()) {
            calHoraire.set(Calendar.HOUR, Integer.parseInt(pHoraire.substring(0, 2)));
            calHoraire.set(Calendar.MINUTE, Integer.parseInt(pHoraire.substring(3, 5)));

            if (pHoraire.substring(6, 8).equals(RESSOURCE.get("PM"))) {
                calHoraire.set(Calendar.AM_PM, Calendar.PM);
            } else {
                calHoraire.set(Calendar.AM_PM, Calendar.AM);
            }

            if (calHoraire.getTime().getTime() > calActuel.getTime().getTime()) {
                mEstProchaine = true;
            }
        }

        mHoraireDate = calHoraire;
    }

    public static String renvoieHeureMinute(Date pDate, int pTimeFormat) {

        String retour = StringOutilClient.EMPTY;

        Calendar calActuel = Calendar.getInstance();
        calActuel.setTime(pDate);

        if (pTimeFormat == TimeFormat.H24.getValue()) {
            retour += calActuel.get(Calendar.HOUR_OF_DAY);
            retour += TimeFormat.TIME_SEPARATOR;
            retour += calActuel.get(Calendar.MINUTE);
        } else if (pTimeFormat == TimeFormat.H12.getValue()) {
            retour += calActuel.get(Calendar.HOUR);
            retour += TimeFormat.TIME_SEPARATOR;
            retour += calActuel.get(Calendar.MINUTE);
        }

        return retour;
    }

    public String getHoraireFormate() {
        return mHoraire;
    }

    public boolean isEstProchaine() {
        return mEstProchaine;
    }

    public Calendar getHoraireDate() {
        return mHoraireDate;
    }
}
