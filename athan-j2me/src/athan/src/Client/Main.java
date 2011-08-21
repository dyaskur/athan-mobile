/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package athan.src.Client;

import athan.src.Factory.ResourceReader;
import athan.src.Factory.ServiceFactory;
import athan.src.Factory.TacheTimer;
import com.sun.lwuit.Command;
import com.sun.lwuit.Display;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.impl.midp.VKBImplementationFactory;
import com.sun.lwuit.plaf.UIManager;
import com.sun.lwuit.util.Resources;
import java.util.Date;
import java.util.Timer;

/**
 *
 * @author BENBOUZID
 */
public class Main extends javax.microedition.midlet.MIDlet
                    implements ActionListener {

    private static final int OPTIONS_COMMAND = 1;
    public static final Command optionsCommand = new Command("", OPTIONS_COMMAND);
    private static final int EXIT_COMMAND = 2;
    public static final Command exitCommand = new Command("", EXIT_COMMAND);
    private static final int MINIMIZE_COMMAND = 3;
    public static final Command minimizeCommand = new Command("", MINIMIZE_COMMAND);

    public static Resources theme;
    public static Resources icons;
    public static Resources languages;

    public static boolean sIsTactile;

    private static OptionForm sOptionForm;
    private static MainForm sMainForm;

    private static Timer sTimer;

    /**
     * Affecte un nouveau marqueur
     */
    public static void setTimer(Timer pTimer) {
        sTimer = pTimer;
    }

    /**
     * @return le timer
     */
    public static Timer getTimer() {
        return sTimer;
    }

    /**
     * @return le panel d'options
     */
    public static OptionForm getOptionForm() {
        return sOptionForm;
    }
    
    /**
     * Affecte le panel d'options
     */
    public static void setOptionForm(OptionForm pOptionForm) {
        sOptionForm = pOptionForm;
    }

    /**
     * @return le panel principal
     */
    public static MainForm getMainForm() {
        return sMainForm;
    }

    /**
     * Affecte le panel principal
     */
    public static void setMainForm(MainForm pMainForm) {
        sMainForm = pMainForm;
    }

    /**
     * @return the sIsTactile
     */
    public static boolean isTactile() {
        return sIsTactile;
    }

    protected void startApp() {
        try {

            //By using the VKBImplementationFactory.init() we automatically
            //bundle the LWUIT Virtual Keyboard.
            //If your application is not aimed to touch screen devices,
            //this line of code should be removed.
            VKBImplementationFactory.init();
            Display.init(this);
            
            // Test sur le type de t�l�phone
            sIsTactile = Display.getInstance().isTouchScreenDevice();
            if (sIsTactile) {
                UIManager.getInstance().getLookAndFeel().setTouchMenus(true);
                UIManager.getInstance().getLookAndFeel().setTactileTouchDuration(100);
                System.out.println("TELEPHONE TACTILE");
            } else {
                System.out.println("TELEPHONE NON TACTILE");
            }

            // Traite les ressources de l'application
            theme = Resources.open("/" + AthanConstantes.RESSOURCE_THEME);
            UIManager.getInstance().setThemeProps(theme.getTheme(theme.getThemeResourceNames()[0]));
            icons = Resources.open("/" + AthanConstantes.RESSOURCE_ICONS);
            languages = Resources.open("/" + AthanConstantes.RESSOURCE_LANGUAGES);

            // On cr�e la factory
            try {
                ServiceFactory.newInstance();
            } catch (Exception exc) {
                exc.printStackTrace();
                // On quitte l'application
                quitter();
            }

            ResourceReader RESSOURCE = ServiceFactory.getFactory().getResourceReader();
            optionsCommand.setCommandName(RESSOURCE.get("Command.Options"));
            exitCommand.setCommandName(RESSOURCE.get("Command.Exit"));
            minimizeCommand.setCommandName("Command.Minimize");

            //although calling directly to setMainForm(res) will work on
            //most devices, a good coding practice will be to allow the midp
            //thread to return and to do all the UI on the EDT.
            Display.getInstance().callSerially(new Runnable() {
                public void run() {
                    setMainForm(icons);
                }
            });

        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    protected void pauseApp() {
    }

    protected void destroyApp(boolean arg0) {
    }

    void setMainForm(Resources r) {

        // On cr�e la form d'options
        MainForm.setOptionForm(false);

        sMainForm = new MainForm(this);
        sMainForm.run(exitCommand, this, false);

        // On force le premier calcul des pri�res
        ServiceFactory.getFactory().getVuePrincipale()
                .rafraichir(new Date(), true, true);

        // On cr�e le timer
        sTimer = new Timer();
        sTimer.schedule(new TacheTimer(), 0, TacheTimer.DUREE_CYCLE);
    }

     public void actionPerformed(ActionEvent pActionEvent) {

        Command cmd = pActionEvent.getCommand();
        switch (cmd.getId()) {
            case OPTIONS_COMMAND: 
                // On affiche le menu des commandes
                if (getMainForm() != null) {
                    // On arr�te le timer
                    Main.getTimer().cancel();
                    // On affiche les options
                    getMainForm().setOptionForm(true);
                }
                break;
            case MINIMIZE_COMMAND:
                // On minimise l'application
                boolean retour = Display.getInstance().minimizeApplication();
                if (!retour) {
                    javax.microedition.lcdui.Display.getDisplay(this).setCurrent(null);
                }
                break;
            case EXIT_COMMAND:
                // On quitte l'application
                quitter();
                break;
            default: 
                // Commande inconnue
                break;
        }
    }

    private void quitter() {
        destroyApp(true);
        this.notifyDestroyed();
    }
}
