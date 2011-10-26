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
package athan.src.Client;

import athan.src.Factory.Preferences;
import athan.src.Factory.ServiceFactory;
import athan.src.Outils.StringOutilClient;
import athan.src.Priere.Horaire;
import athan.src.Priere.PrieresJournee;
import athan.src.SalaahCalc.CalculationCustomParams;
import athan.src.SalaahCalc.CalculationMethods;
import athan.src.SalaahCalc.JuristicMethods;
import athan.src.SalaahCalc.SalaahTimeCalculator;
import com.sun.lwuit.Label;
import java.util.Calendar;
import java.util.Date;

/**
 * Classe utilis�e en singleton pour l'accc�s aux �l�ments graphiques
 * de la page principale {@link MainForm}.
 *
 * @author Saad BENBOUZID
 */
public class VuePrincipale extends AthanConstantes {

    private PrieresJournee mPrieresJournee;
    private boolean mProchainePriereRenseignee;
    private String[] mHorairesPrieres;
    private Calendar mHoraireDernierAppel;

    public VuePrincipale() {
    }

    public void rafraichir(Date pDate, boolean isHeureCourante, boolean pForcerCalcul) {

        String sLatitude = StringOutilClient.EMPTY;
        String sLongitude = StringOutilClient.EMPTY;
        int formatHoraire = 0;
        int methodeJuridiqueAsr = 0;
        int decalageHoraire = 0;
        int calculationMethod = 0;
        CalculationCustomParams customParams;

        try {

            if (estMinuitPile(pDate) || pForcerCalcul) {

                sLatitude = ServiceFactory.getFactory().getPreferences().get(Preferences.sLatitude);
                sLongitude = ServiceFactory.getFactory().getPreferences().get(Preferences.sLongitude);
                formatHoraire = Integer.parseInt(ServiceFactory.getFactory().getPreferences().get(Preferences.sFormatHoraire));
                methodeJuridiqueAsr = Integer.parseInt(ServiceFactory.getFactory().getPreferences().get(Preferences.sMethodeJuridiqueAsr));
                decalageHoraire = Integer.parseInt(ServiceFactory.getFactory().getPreferences().get(Preferences.sDecalageHoraire));
                calculationMethod = Integer.parseInt(ServiceFactory.getFactory().getPreferences().get(Preferences.sCalculationMethod));
                customParams = ServiceFactory.getFactory().getPreferences().getCalculationCustomParams();

                // Calcul des pri�res
                SalaahTimeCalculator calc = new SalaahTimeCalculator();
                calc.setCalculationMethod(new CalculationMethods(calculationMethod), customParams);
                calc.setAsrJurusticType(new JuristicMethods(methodeJuridiqueAsr));
                calc.setTimeFormat(formatHoraire);

                mHorairesPrieres = calc.getPrayerTimes(pDate,
                        Double.parseDouble(sLatitude),
                        Double.parseDouble(sLongitude),
                        new Integer(SalaahTimeCalculator.getTimeZone() + decalageHoraire));

                // Simulation
//                Calendar calsimul = Calendar.getInstance();
//                calsimul.setTime(pDate);
//                String hh = calsimul.get(Calendar.HOUR_OF_DAY) + "";
//                if (calsimul.get(Calendar.HOUR_OF_DAY) < 10) {
//                    hh = "0" + hh;
//                }
//                String mm = calsimul.get(Calendar.MINUTE) + 1 + "";
//                if (calsimul.get(Calendar.MINUTE) + 1 < 10) {
//                    mm = "0" + mm;
//                }
//                mHorairesPrieres[1] = hh + ":" + mm;

                mPrieresJournee = new PrieresJournee(
                        pDate,
                        new Horaire(mHorairesPrieres[SalaahTimeCalculator.IMSAK], formatHoraire),
                        new Horaire(mHorairesPrieres[SalaahTimeCalculator.FAJR], formatHoraire),
                        new Horaire(mHorairesPrieres[SalaahTimeCalculator.SUNRISE], formatHoraire),
                        new Horaire(mHorairesPrieres[SalaahTimeCalculator.DOHR], formatHoraire),
                        new Horaire(mHorairesPrieres[SalaahTimeCalculator.ASR], formatHoraire),
                        new Horaire(mHorairesPrieres[SalaahTimeCalculator.MAGHRIB], formatHoraire),
                        new Horaire(mHorairesPrieres[SalaahTimeCalculator.ISHAA], formatHoraire));

                // Affecte l'horaire du dernier appel � l'heure courante
                // Ce qui permet par ailleurs de ne pas sonner d�s le
                // rafra�chissement s'il y a une co�ncidence � ce
                // moment-l�
                Calendar cal = Calendar.getInstance();
                cal.setTime(pDate);
                mHoraireDernierAppel = cal;

            } else {
                formatHoraire = Integer.parseInt(ServiceFactory.getFactory().getPreferences().get(Preferences.sFormatHoraire));

                mPrieresJournee.setDateJour(pDate);
            }

            // Rafa�chit tous les composants
            renseignerLieu();
            renseignerPrieres(isHeureCourante);
            renseignerDate(isHeureCourante);

            // Redessine le panel
            Main.getMainForm().getForm().repaint();

            // Affecte l'heure courante
            Main.getMainForm().setHeureCourante(pDate);

            // V�rifie s'il y a une alarme
            traiterAlarmes(pDate);

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    private boolean memeHoraire(Calendar pCal1, Calendar pCal2) {

        return (pCal1.get(Calendar.YEAR) == pCal2.get(Calendar.YEAR)
                && pCal1.get(Calendar.MONTH) == pCal2.get(Calendar.MONTH)
                && pCal1.get(Calendar.DAY_OF_MONTH) == pCal2.get(Calendar.DAY_OF_MONTH)
                && pCal1.get(Calendar.HOUR_OF_DAY) == pCal2.get(Calendar.HOUR_OF_DAY)
                && pCal1.get(Calendar.MINUTE) == pCal2.get(Calendar.MINUTE));
    }

    private boolean faireSonner(String pPreferenceKey) {
        return (Integer.parseInt(ServiceFactory.getFactory().getPreferences().get(pPreferenceKey)) == StringOutilClient.TRUE);
    }

    private boolean traiterAlarmePriere(Calendar pHeureCourante,
            Horaire pHoraire,
            String pPreferenceKey) {

        boolean retour = false;

        if (memeHoraire(pHeureCourante, pHoraire.getHoraireDate())
                && !memeHoraire(pHeureCourante, mHoraireDernierAppel)) {
            if (faireSonner(pPreferenceKey)) {
                retour = true;
            }

            // On affecte l'heure du dernier appel
            mHoraireDernierAppel = pHeureCourante;
        }
        return retour;
    }

    private void traiterAlarmes(Date pDate) {

        Calendar pHeureCourante = Calendar.getInstance();
        pHeureCourante.setTime(pDate);

        // On parcourt toutes les pri�res
        if (traiterAlarmePriere(pHeureCourante, mPrieresJournee.getSobh(), Preferences.sAlertSobh)) {
            Main.getMainForm().traiterAlarme(Preferences.sAlertSobh);
        } else if (traiterAlarmePriere(pHeureCourante, mPrieresJournee.getDohr(), Preferences.sAlertDohr)) {
            Main.getMainForm().traiterAlarme(Preferences.sAlertDohr);
        } else if (traiterAlarmePriere(pHeureCourante, mPrieresJournee.getAsr(), Preferences.sAlertAsr)) {
            Main.getMainForm().traiterAlarme(Preferences.sAlertAsr);
        } else if (traiterAlarmePriere(pHeureCourante, mPrieresJournee.getMaghreb(), Preferences.sAlertMaghreb)) {
            Main.getMainForm().traiterAlarme(Preferences.sAlertMaghreb);
        } else if (traiterAlarmePriere(pHeureCourante, mPrieresJournee.getIshaa(), Preferences.sAlertIshaa)) {
            Main.getMainForm().traiterAlarme(Preferences.sAlertIshaa);
        }
    }

    private void renseignerLieu() {

        String lieu = "";

        try {
            String ville = ServiceFactory.getFactory().getPreferences().get(Preferences.sCityName);
            if (!StringOutilClient.isEmpty(ville)) {
                lieu += ville;
            }
            String region = ServiceFactory.getFactory().getPreferences().get(Preferences.sRegionName);
            if (!StringOutilClient.isEmpty(region)) {
                lieu += ", " + region;
            }
            String pays = ServiceFactory.getFactory().getPreferences().get(Preferences.sCountryName);
            if (!StringOutilClient.isEmpty(pays)) {
                lieu += ", " + pays;
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        Main.getMainForm().getLabelLieu().setText(lieu);
    }

    private void renseignerDate(boolean isHeureCourante) {
        String[] dateJour = mPrieresJournee.getDateFormattee();

        if (isHeureCourante) {
            Main.getMainForm().getLabelLibelleHeure().setText(mPrieresJournee.getHoraire());
            Main.getMainForm().getLabelLibelleHeure().setVisible(true);
        } else {
            Main.getMainForm().getLabelLibelleHeure().setText(StringOutilClient.EMPTY);
            Main.getMainForm().getLabelLibelleHeure().setVisible(false);
        }

        try {
            // Libell� de la date
            Main.getMainForm().getLabelLibelleDate().setText(dateJour[0]);
            // Jour de la semaine
            Main.getMainForm().getLabelLibelleJourSemaine().setText(dateJour[1]);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    private void renseignerPrieres(boolean pIsHeureCourante) {

        mProchainePriereRenseignee = false;

        Main.getMainForm().getLabelHoraireImsak().setText(mPrieresJournee.getImsak().getHoraireFormate());
        changerFontPriere(Main.getMainForm().getLabelHoraireImsak(),
                mPrieresJournee.getImsak().isEstProchaine(),
                false); // afin de ne pas compter l'Imsak comme une pri�re

        Main.getMainForm().getLabelHoraireSohb().setText(mPrieresJournee.getSobh().getHoraireFormate());
        changerFontPriere(Main.getMainForm().getLabelHoraireSohb(),
                mPrieresJournee.getSobh().isEstProchaine(),
                pIsHeureCourante);

        Main.getMainForm().getLabelHoraireChourouk().setText(mPrieresJournee.getChourouk().getHoraireFormate());
        changerFontPriere(Main.getMainForm().getLabelHoraireChourouk(),
                mPrieresJournee.getChourouk().isEstProchaine(),
                false); // afin de ne pas compter le Chourouk comme une pri�re

        Main.getMainForm().getLabelHoraireDohr().setText(mPrieresJournee.getDohr().getHoraireFormate());
        changerFontPriere(Main.getMainForm().getLabelHoraireDohr(),
                mPrieresJournee.getDohr().isEstProchaine(),
                pIsHeureCourante);

        Main.getMainForm().getLabelHoraireAsr().setText(mPrieresJournee.getAsr().getHoraireFormate());
        changerFontPriere(Main.getMainForm().getLabelHoraireAsr(),
                mPrieresJournee.getAsr().isEstProchaine(),
                pIsHeureCourante);

        Main.getMainForm().getLabelHoraireMaghreb().setText(mPrieresJournee.getMaghreb().getHoraireFormate());
        changerFontPriere(Main.getMainForm().getLabelHoraireMaghreb(),
                mPrieresJournee.getMaghreb().isEstProchaine(),
                pIsHeureCourante);

        Main.getMainForm().getLabelHoraireIshaa().setText(mPrieresJournee.getIshaa().getHoraireFormate());
        changerFontPriere(Main.getMainForm().getLabelHoraireIshaa(),
                mPrieresJournee.getIshaa().isEstProchaine(),
                pIsHeureCourante);
    }

    private void changerFontPriere(Label pLabel, boolean pIsProchaine, boolean pIsHeureCourante) {
        if (pIsProchaine && !mProchainePriereRenseignee && pIsHeureCourante) {
            pLabel.setUIID(UIID_LABEL_NEXT_PRAYER);
            mProchainePriereRenseignee = true;
        } else {
            pLabel.setUIID(UIID_LABEL_PRAYER);
        }
    }

    private boolean estMinuitPile(Date pDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(pDate);
        return cal.get(Calendar.HOUR_OF_DAY) == 0
                && cal.get(Calendar.MINUTE) == 0
                && cal.get(Calendar.SECOND) == 0;
    }
}
