/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package athan.src.Factory;

import athan.src.Client.VuePrincipale;

/**
 * Interface de la Factory
 *
 * @author BENBOUZID
 */
public interface IServiceFactory {
    
    Preferences getPreferences();

    ResourceReader getResourceReader();

    VuePrincipale getVuePrincipale();

    void setResourceReader(ResourceReader pResourceReader);
}
