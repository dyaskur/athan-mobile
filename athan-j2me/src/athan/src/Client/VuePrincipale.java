/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
 * Accès aux éléments graphiques de la page principale
 *
 * @author BENBOUZID
 */
public class VuePrincipale extends AthanConstantes {

    private PrieresJournee mPrieresJournee;
    private boolean mProchainePriereRenseignee;
    private String[] mHorairesPrieres;

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
                sLatitude = ServiceFactory.getFactory().getPreferences()
                                .get(Preferences.sLatitude);
                sLongitude = ServiceFactory.getFactory().getPreferences()
                                    .get(Preferences.sLongitude);
                formatHoraire = Integer.parseInt(ServiceFactory.getFactory().getPreferences()
                                    .get(Preferences.sFormatHoraire));
                methodeJuridiqueAsr = Integer.parseInt(ServiceFactory.getFactory().getPreferences()
                                    .get(Preferences.sMethodeJuridiqueAsr));
                decalageHoraire = Integer.parseInt(ServiceFactory.getFactory().getPreferences()
                                    .get(Preferences.sDecalageHoraire));
                calculationMethod = Integer.parseInt(ServiceFactory.getFactory().getPreferences()
                                    .get(Preferences.sCalculationMethod));
                customParams = ServiceFactory.getFactory().getPreferences()
                                    .getCalculationCustomParams();

                // Calcul des prières
                SalaahTimeCalculator calc = new SalaahTimeCalculator();
                calc.setCalculationMethod(new CalculationMethods(calculationMethod), customParams);
                calc.setAsrJurusticType(new JuristicMethods(methodeJuridiqueAsr));
                calc.setTimeFormat(formatHoraire);

                mHorairesPrieres = calc.getPrayerTimes(pDate,
                                                Double.parseDouble(sLatitude),
                                                Double.parseDouble(sLongitude),
                                                new Integer(SalaahTimeCalculator.getTimeZone() + decalageHoraire));

                mPrieresJournee = new PrieresJournee(
                         pDate,
                         new Horaire(mHorairesPrieres[SalaahTimeCalculator.IMSAK], formatHoraire),
                         new Horaire(mHorairesPrieres[SalaahTimeCalculator.FAJR], formatHoraire),
                         new Horaire(mHorairesPrieres[SalaahTimeCalculator.SUNRISE], formatHoraire),
                         new Horaire(mHorairesPrieres[SalaahTimeCalculator.DOHR], formatHoraire),
                         new Horaire(mHorairesPrieres[SalaahTimeCalculator.ASR], formatHoraire),
                         new Horaire(mHorairesPrieres[SalaahTimeCalculator.MAGHRIB], formatHoraire),
                         new Horaire(mHorairesPrieres[SalaahTimeCalculator.ISHAA], formatHoraire));
            } else {
                formatHoraire = Integer.parseInt(ServiceFactory.getFactory().getPreferences()
                                    .get(Preferences.sFormatHoraire));

                mPrieresJournee.setDateJour(pDate);
            }

            // Rafaîchit tous les composants
            afficherLogo();
            renseignerLieu();
            renseignerPrieres(isHeureCourante);
            renseignerDate(isHeureCourante);

            // Redessine le panel
            Main.getMainForm().getForm().repaint();

            // Affecte l'heure courante
            Main.getMainForm().setHeureCourante(pDate);

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    
    private void afficherLogo() {
    }

    private void renseignerLieu() {
        String lieu = "";
        
        try {
            String ville = ServiceFactory.getFactory().getPreferences()
                                     .get(Preferences.sCityName);
            if (!StringOutilClient.isEmpty(ville)) {
                lieu += ville;
            }
            String region = ServiceFactory.getFactory().getPreferences()
                                     .get(Preferences.sRegionName);
            if (!StringOutilClient.isEmpty(region)) {
                lieu += ", " + region;
            }
            String pays = ServiceFactory.getFactory().getPreferences()
                                     .get(Preferences.sCountryName);
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
            dateJour[1] += "\n" + mPrieresJournee.getHoraire();
        }
        try {
            Main.getMainForm().getTextAreaLibelleJour().setText(dateJour[0]
                    + "\n" + dateJour[1]);
        } catch(Exception exc) {
            exc.printStackTrace();
            System.out.println("Rows = " + Main.getMainForm().getTextAreaLibelleJour()
                            .getActualRows());
        }        
    }

    private void renseignerPrieres(boolean pIsHeureCourante) {

        mProchainePriereRenseignee = false;

        Main.getMainForm().getLabelHoraireImsak().setText(mPrieresJournee.getImsak().getHoraireFormate());
        changerFontPriere(Main.getMainForm().getLabelHoraireImsak(),
                    mPrieresJournee.getImsak().isEstProchaine(),
                    false); // afin de ne pas compter l'Imsak comme une prière

        Main.getMainForm().getLabelHoraireSohb().setText(mPrieresJournee.getSobh().getHoraireFormate());
        changerFontPriere(Main.getMainForm().getLabelHoraireSohb(),
                    mPrieresJournee.getSobh().isEstProchaine(),
                    pIsHeureCourante);

        Main.getMainForm().getLabelHoraireChourouk().setText(mPrieresJournee.getChourouk().getHoraireFormate());
        changerFontPriere(Main.getMainForm().getLabelHoraireChourouk(),
                    mPrieresJournee.getChourouk().isEstProchaine(),
                    false); // afin de ne pas compter le Chourouk comme une prière

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
