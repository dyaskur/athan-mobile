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
package athan.src.location;

/**
 * Classe abstraite utilisant la JSR-179 (Location API) afin que les librairies correspondantes ne soientt pas chargées avec l'application
 *
 * @author Saad BENBOUZID
 */
public abstract class LocationProvider {
    // define the interface through which we'll access location information

    public abstract double getLatitude();

    public abstract double getLongitude();

    /**
     *  this method will be used to get access to a LocationProvider class
     */
    public static LocationProvider getProvider() throws ClassNotFoundException {
        LocationProvider provider;
        try {
            // this will throw an exception if JSR-179 is missing
            Class.forName("javax.microedition.location.Location");

            // more about this class later
            Class c = Class.forName("athan.src.location.LocationImplementation");
            provider = (LocationProvider) (c.newInstance());
        } catch (Exception e) {
            throw new ClassNotFoundException("No Location API");
        }
        return provider;
    }
}
