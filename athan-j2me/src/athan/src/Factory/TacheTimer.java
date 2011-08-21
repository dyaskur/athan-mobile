/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package athan.src.Factory;

import java.util.Date;
import java.util.TimerTask;

/**
 * T�che principale du timer de rafra�chissement de l'heure
 *
 * @author BENBOUZID
 */
public class TacheTimer extends TimerTask {

    public final static int DUREE_CYCLE = 1000;

    public final void run() {
        try {
            // Appel les services de calcul et d'affichage
            // � partir de la date courante
            ServiceFactory.getFactory().getVuePrincipale()
                    .rafraichir(new Date(), true, false);

            //System.out.println("Calcul timer\n");

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
