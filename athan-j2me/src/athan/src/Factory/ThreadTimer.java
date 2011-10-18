/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package athan.src.Factory;

import athan.src.Client.Main;
import java.util.Date;

/**
 * Tâche principale de la tâche de rafraîchissement de l'heure
 *
 * @author BENBOUZID
 */
public class ThreadTimer extends Thread {

    public final static long DUREE_CYCLE = 1000L;

    public final void run() {

        try {
            
            while(Main.getMainForm().isTimerHeureCourante()) {
                // Appel les services de calcul et d'affichage
                // à partir de la date courante
                ServiceFactory.getFactory().getVuePrincipale().rafraichir(new Date(), true, false);

                Thread.sleep(DUREE_CYCLE);
            }

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
