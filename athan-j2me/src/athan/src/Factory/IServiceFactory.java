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

import athan.src.Client.VuePrincipale;

/**
 * Interface de la Factory de l'application.
 *
 * @author Saad BENBOUZID
 */
public interface IServiceFactory {

    Preferences getPreferences();

    ResourceReader getResourceReader();

    VuePrincipale getVuePrincipale();

    void setResourceReader(ResourceReader pResourceReader);
}
