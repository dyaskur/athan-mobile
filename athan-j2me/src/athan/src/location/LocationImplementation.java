/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package athan.src.location;

import athan.src.Client.AthanConstantes;
import javax.microedition.location.Coordinates;
import javax.microedition.location.Criteria;
import javax.microedition.location.Location;

/**
 * Implémentation privée de {@link LocationProvider}
 *
 * @author Saad BENBOUZID
 */
class LocationImplementation extends LocationProvider {

    private double latitude = 0.0;
    private double longitude = 0.0;

    LocationImplementation() {
        /*
        We must have a no-parameter constructor, or Class.newInstance() cannot
        create an instance of this class
         */
        try {
            Criteria cr = new Criteria();
            cr.setHorizontalAccuracy(500);
            javax.microedition.location.LocationProvider lp = javax.microedition.location.LocationProvider.getInstance(cr);
            Location l = lp.getLocation(AthanConstantes.TIMEOUT_GPS); // timeout de 15 secondes
            Coordinates c = l.getQualifiedCoordinates();

            if (c != null) {
                latitude = c.getLatitude();
                longitude = c.getLongitude();
            }
        } catch (Exception lexc) {
            lexc.printStackTrace();
        }
    }

    // implement the abstract LocationProvider methods
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
