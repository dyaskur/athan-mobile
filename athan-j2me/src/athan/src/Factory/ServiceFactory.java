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
package athan.src.Factory;

import athan.src.Client.AthanException;
import athan.src.Client.VuePrincipale;

/**
 * Factory de l'application
 * <br>
 * Contient l'ensemble des services (singletons) utiles à l'application.
 *
 * @author Saad BENBOUZID
 */
public class ServiceFactory implements IServiceFactory {

    private static IServiceFactory sFactory;
    private Preferences mPreferences;
    private ResourceReader mResourceReader;
    private VuePrincipale mVuePrincipale;

    /**
     * Récupère la factory
     * @return la factory
     */
    public static IServiceFactory getFactory() {
        return sFactory;
    }

    /**
     * Crée la factory
     */
    public static void newInstance() throws AthanException {
        if (sFactory == null) {
            sFactory = (IServiceFactory) new ServiceFactory();
        }
    }

    public ServiceFactory() throws AthanException {
        // Instancie les principaux services
        try {
            // Gestionnaire de préférences utilisateurs
            mPreferences = new Preferences(Preferences.RECORD_STORE_NAME);
            mResourceReader = new ResourceReader(mPreferences);
            mVuePrincipale = new VuePrincipale();
        } catch (Exception exc) {
            throw new AthanException("Problème à la création");
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
