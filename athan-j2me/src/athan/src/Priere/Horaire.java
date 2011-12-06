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

    private String horaire;
    private Calendar horaireDate;
    private int timeFormat;

    public Horaire(String horaire, int timeFormat) {

        this.horaire = horaire;
        this.timeFormat = timeFormat;

        ResourceReader RESSOURCE = ServiceFactory.getFactory().getResourceReader();

        Calendar calActuel = Calendar.getInstance();
        calActuel.setTime(new Date());

        Calendar calHoraire = Calendar.getInstance();
        calHoraire.setTime(new Date());

        if (timeFormat == TimeFormat.H24.getValue()) {
            calHoraire.set(Calendar.HOUR_OF_DAY, Integer.parseInt(horaire.substring(0, 2)));
            calHoraire.set(Calendar.MINUTE, Integer.parseInt(horaire.substring(3, 5)));
        } else if (timeFormat == TimeFormat.H12.getValue()) {
            calHoraire.set(Calendar.HOUR, Integer.parseInt(horaire.substring(0, 2)));
            calHoraire.set(Calendar.MINUTE, Integer.parseInt(horaire.substring(3, 5)));

            if (horaire.substring(6, 8).equals(RESSOURCE.get("PM"))) {
                calHoraire.set(Calendar.AM_PM, Calendar.PM);
            } else {
                calHoraire.set(Calendar.AM_PM, Calendar.AM);
            }
        }

        horaireDate = calHoraire;
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
        return horaire;
    }

    /**
     * Renvoie true si l'horaire est supérieure à l'heure courante
     * 
     * @return
     */
    public boolean isEstProchaine() {
        
        boolean estProchaine = false;
        
        Calendar calActuel = Calendar.getInstance();
        calActuel.setTime(new Date());

        if (timeFormat == TimeFormat.H24.getValue()) {
            if (horaireDate.getTime().getTime() > calActuel.getTime().getTime()) {
                estProchaine = true;
            }
        } else if (timeFormat == TimeFormat.H12.getValue()) {
            if (horaireDate.getTime().getTime() > calActuel.getTime().getTime()) {
                estProchaine = true;
            }
        }

        return estProchaine;
    }

    public Calendar getHoraireDate() {
        return horaireDate;
    }
}
