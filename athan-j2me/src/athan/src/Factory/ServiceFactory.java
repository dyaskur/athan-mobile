/*
 * Cr�ateur et conteneur de services
 */

package athan.src.Factory;

import athan.src.Client.AthanException;
import athan.src.Client.VuePrincipale;

/**
 * Factory
 *
 * @author Saad BENBOUZID
 */
public class ServiceFactory implements IServiceFactory {

    private static IServiceFactory sFactory;

    private Preferences mPreferences;
    private ResourceReader mResourceReader;
    private VuePrincipale mVuePrincipale;

    /**
     * R�cup�re la factory
     * @return la factory
     */
    public static IServiceFactory getFactory() {
        return sFactory;
    }

    /**
     * Cr�e la factory
     */
    public static void newInstance() throws AthanException {
        if (sFactory == null) {
            sFactory = (IServiceFactory) new ServiceFactory();
        }
    }

    public ServiceFactory() throws AthanException {
        // Instancie les principaux services
        try {
            // Gestionnaire de pr�f�rences utilisateurs
            mPreferences = new Preferences(Preferences.RECORD_STORE_NAME);
            mResourceReader = new ResourceReader(mPreferences);
            mVuePrincipale = new VuePrincipale();
        } catch (Exception exc) {
            throw new AthanException("Probl�me � la cr�ation");
        }
    }

    /**
     * @return the mPreferences
     */
    public Preferences getPreferences() {
        return mPreferences;
    }

    /**
     * @return the mResourceReader
     */
    public ResourceReader getResourceReader() {
        return mResourceReader;
    }

    /**
     * assigns the mResourceReader
     */
    public void setResourceReader(ResourceReader pResourceReader) {
        mResourceReader = pResourceReader;
    }

    /**
     * @return the mVuePrincipale
     */
    public VuePrincipale getVuePrincipale() {
        return mVuePrincipale;
    }
}
