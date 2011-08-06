/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package athan.src.Priere;

import athan.src.Factory.ResourceReader;
import athan.src.Factory.ServiceFactory;
import athan.src.SalaahCalc.TimeFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Contient une horaire de prière
 *
 * @author Saad BENBOUZID
 */
public class Horaire {

    private String mHoraire;
    private boolean mEstProchaine;

    public Horaire(String pHoraire, int pTimeFormat) {

        mHoraire  = pHoraire;
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

            if (pHoraire.substring(6,8).equals(RESSOURCE.get("PM"))) {
                calHoraire.set(Calendar.AM_PM, Calendar.PM);
            } else {
                calHoraire.set(Calendar.AM_PM, Calendar.AM);
            }

            if (calHoraire.getTime().getTime() > calActuel.getTime().getTime()) {
                mEstProchaine = true;
            }
        }
    }

    /**
     * @return the mHoraire
     */
    public String getHoraireFormate() {
        return mHoraire;
    }

    /**
     * @return the mEstProchaine
     */
    public boolean isEstProchaine() {
        return mEstProchaine;
    }

    /**
     * @param mEstProchaine the mEstProchaine to set
     */
    public void setEstProchaine(boolean mEstProchaine) {
        this.mEstProchaine = mEstProchaine;
    }
}
