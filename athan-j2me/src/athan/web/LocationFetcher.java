package athan.web;

import athan.src.Client.AthanException;
import athan.src.Outils.StringOutilClient;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransport;

public final class LocationFetcher {

    public athan.web.Location geoname(java.lang.String cityName, java.lang.String countryName, java.lang.String regionName, java.lang.String language) throws Exception {
        SoapObject _client = new SoapObject("http://web.athan/", "geoname");
        _client.addProperty("cityName", cityName);
        _client.addProperty("countryName", countryName);
        _client.addProperty("regionName", regionName);
        _client.addProperty("language", language);
        SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        _envelope.bodyOut = _client;
        HttpTransport _ht = new HttpTransport(Configuration.getWsUrl());
        _ht.debug = true;
        _ht.call(null, _envelope);

        // On vérifie qu'il n'y a pas eu une "LocationException" côté serveur
        if (_envelope.getResponse() instanceof SoapPrimitive) {
            String locationException = StringOutilClient.EMPTY;
            try {
                locationException = ((SoapPrimitive) _envelope.getResponse()).toString();
            } catch (Exception exc) {
            }

            throw new AthanException(locationException);
        }

        SoapObject _ret = (SoapObject) _envelope.getResponse();
        int _len = _ret.getPropertyCount();
        athan.web.Location _returned = new athan.web.Location();
        for (int _i = 0; _i < _len; _i++) {
            _returned.setProperty(_i, _ret.getProperty(_i));
        }

        // Replaces null values
        _returned.makeConsistentFields();

        return _returned;
    }
}
