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
import athan.src.SalaahCalc.CalculationMethods;
import athan.src.SalaahCalc.JuristicMethods;
import athan.src.SalaahCalc.SalaahTimeCalculator;
import com.sun.lwuit.Font;
import com.sun.lwuit.Label;
import java.util.Date;

/**
 * Accès aux éléments graphiques de la page principale
 *
 * @author BENBOUZID
 */
public class VuePrincipale implements AthanConstantes {

    private PrieresJournee mPrieresJournee;

    public VuePrincipale() {
    }

    public void rafraichir(Date pDate, boolean isHeureCourante) {

        String sLatitude = StringOutilClient.EMPTY;
        String sLongitude = StringOutilClient.EMPTY;
        int formatHoraire = 0;
        int methodeJuridiqueAsr = 0;
        int decalageHoraire = 0;

        try {
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
        

            // Calcul des prières
            SalaahTimeCalculator calc = new SalaahTimeCalculator();
            calc.setCalculationMethod(CalculationMethods.Custom);
            calc.setTimeFormat(formatHoraire);
            if (methodeJuridiqueAsr == 0) {
                calc.setAsrJurusticionType(JuristicMethods.Shafii);
            } else {
                calc.setAsrJurusticionType(JuristicMethods.Hanafi);
            }

            String[] lesHoraires = calc.getPrayerTimes(pDate,
                                            Double.parseDouble(sLatitude),
                                            Double.parseDouble(sLongitude),
                                            new Integer(SalaahTimeCalculator.getTimeZone() + decalageHoraire));

            mPrieresJournee = new PrieresJournee(
                     pDate,
                     new Horaire(lesHoraires[0], formatHoraire),
                     new Horaire(lesHoraires[1], formatHoraire),
                     new Horaire(lesHoraires[2], formatHoraire),
                     new Horaire(lesHoraires[3], formatHoraire),
                     new Horaire(lesHoraires[4], formatHoraire),
                     new Horaire(lesHoraires[6], formatHoraire));

            // Rafaîchit tous les composants
            afficherLogo();
            renseignerLieu();
            renseignerPrieres();
            renseignerDate(isHeureCourante);
            if (isHeureCourante) {
                rafraichirProchainePriere();
            }

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
            lieu = ServiceFactory.getFactory().getPreferences()
                                     .get(Preferences.sCityName);
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        Main.getMainForm().getLabelLieu().setText(lieu);
    }

    private void renseignerDate(boolean isHeureCourante) {
        String[] dateJour = mPrieresJournee.getDateFormattee();

        if (isHeureCourante) {
            dateJour[1] += " " + mPrieresJournee.getHoraire();
        }

        Main.getMainForm().getLabelLibelleJour().setText(dateJour[0] + "\n" + dateJour[1]);
        Main.getMainForm().getLabelLibelleDate().setText(dateJour[1]);
    }

    private void renseignerPrieres() {

        Main.getMainForm().getLabelHoraireSohb().setText(mPrieresJournee.getSobh().getHoraireFormate());
        if (!mPrieresJournee.getSobh().isEstProchaine()) {
            changerFontPriereNormale(Main.getMainForm().getLabelHoraireSohb());
        }

        Main.getMainForm().getLabelHoraireChourouk().setText(mPrieresJournee.getChourouk().getHoraireFormate());
        if (!mPrieresJournee.getChourouk().isEstProchaine()) {
            changerFontPriereNormale(Main.getMainForm().getLabelHoraireChourouk());
        }

        Main.getMainForm().getLabelHoraireDohr().setText(mPrieresJournee.getDohr().getHoraireFormate());
        if (!mPrieresJournee.getDohr().isEstProchaine()) {
            changerFontPriereNormale(Main.getMainForm().getLabelHoraireDohr());
        }

        Main.getMainForm().getLabelHoraireAsr().setText(mPrieresJournee.getAsr().getHoraireFormate());
        if (!mPrieresJournee.getAsr().isEstProchaine()) {
            changerFontPriereNormale(Main.getMainForm().getLabelHoraireAsr());
        }

        Main.getMainForm().getLabelHoraireMaghreb().setText(mPrieresJournee.getMaghreb().getHoraireFormate());
        if (!mPrieresJournee.getMaghreb().isEstProchaine()) {
            changerFontPriereNormale(Main.getMainForm().getLabelHoraireMaghreb());
        }

        Main.getMainForm().getLabelHoraireIshaa().setText(mPrieresJournee.getIshaa().getHoraireFormate());
        if (!mPrieresJournee.getIshaa().isEstProchaine()) {
            changerFontPriereNormale(Main.getMainForm().getLabelHoraireIshaa());
        }
    }

    private void changerFontPriereNormale(Label pLabel) {
        Font font = Main.theme.getFont(FONT_LABEL_PRAYER);
        pLabel.getStyle().setFont(font);
        pLabel.setUIID(UIID_LABEL_PRAYER);
    }

    private void changerFontProchainePriere(Label pLabel) {
        Font font = Main.theme.getFont(FONT_LABEL_NEXT_PRAYER);
        pLabel.getStyle().setFont(font);
        pLabel.setUIID(UIID_LABEL_NEXT_PRAYER);
    }

    private void rafraichirProchainePriere() {

        if (mPrieresJournee.getSobh().isEstProchaine()) {
            changerFontProchainePriere(Main.getMainForm().getLabelHoraireSohb());
        } else if (mPrieresJournee.getDohr().isEstProchaine()) {
            changerFontProchainePriere(Main.getMainForm().getLabelHoraireDohr());
        } else if (mPrieresJournee.getAsr().isEstProchaine()) {
            changerFontProchainePriere(Main.getMainForm().getLabelHoraireAsr());
        } else if (mPrieresJournee.getMaghreb().isEstProchaine()) {
            changerFontProchainePriere(Main.getMainForm().getLabelHoraireMaghreb());
        } else if (mPrieresJournee.getIshaa().isEstProchaine()) {
            changerFontProchainePriere(Main.getMainForm().getLabelHoraireIshaa());
        }
    }
}
