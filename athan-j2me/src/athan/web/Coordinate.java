package athan.web;

import java.util.Hashtable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

public final class Coordinate extends SoapObject {

    private double lat;
    private double lng;

    public Coordinate() {
        super("", "");
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLat() {
        return this.lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLng() {
        return this.lng;
    }

    public int getPropertyCount() {
        return 2;
    }

    public Object getProperty(int __index) {
        switch (__index) {
            case 0:
                return new Double(lat);
            case 1:
                return new Double(lng);
        }
        return null;
    }

    public void setProperty(int __index, Object __obj) {
        switch (__index) {
            case 0:
                lat = Double.parseDouble(__obj.toString());
                break;
            case 1:
                lng = Double.parseDouble(__obj.toString());
                break;
        }
    }

    public void getPropertyInfo(int __index, Hashtable __table, PropertyInfo __info) {
        switch (__index) {
            case 0:
                __info.name = "lat";
                __info.type = Double.class;
                break;
            case 1:
                __info.name = "lng";
                __info.type = Double.class;
                break;
        }
    }

    public static Coordinate parseSoapObject(SoapObject soapObject) {
        int _len = soapObject.getPropertyCount();
        athan.web.Coordinate _returned = new athan.web.Coordinate();
        for (int _i = 0; _i < _len; _i++) {
            _returned.setProperty(_i, soapObject.getProperty(_i));
        }
        return _returned;
    }

    /**
     * Makes fields consistents by replacing null values with default values
     */
    public void makeConsistentFields() {
        if (lat == Double.NaN) {
            lat = 0.0;
        }
        if (lng == Double.NaN) {
            lng = 0.0;
        }
    }
}
