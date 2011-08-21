/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package athan.src.Outils;

import athan.src.Client.AthanConstantes;
import com.sun.lwuit.Display;
import java.util.TimerTask;

/**
 * T�che principale du timer de vibration
 *
 * @author BENBOUZID
 */
public class TacheVibrer extends TimerTask {

    public final static int DUREE_CYCLE = 800;
    public final static int NB_VIBRATIONS = 5;

    public static int nbVibrations = 0;

    public final void run() {
        try {
            if (nbVibrations < NB_VIBRATIONS) {
                // Fait vibrer le t�l�phone
                Display.getInstance().vibrate(AthanConstantes.DUREE_VIBRATION_UNITAIRE);
            } else {
                // Arr�te sa propre t�che
                this.cancel();
            }

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
