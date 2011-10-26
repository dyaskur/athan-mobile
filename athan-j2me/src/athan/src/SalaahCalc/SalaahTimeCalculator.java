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
package athan.src.SalaahCalc;

import athan.src.Factory.ResourceReader;
import athan.src.Factory.ServiceFactory;
import athan.src.microfloat.Real;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Moteur de calcul des horaires de prière.
 * Algorithmes de calculs récupérés depuis http://praytimes.org/
 * et refactorisés de C# vers J2ME.
 *
 * @author Hamid Zarrabi-Zadeh (Javascript code original)
 * @author Jandost Khoso (réécriture pour C#)
 * @author Saad BENBOUZID (réécriture pour J2ME + gestion de l'Imsak)
 */
public class SalaahTimeCalculator {

    public static final int IMSAK = 0;
    public static final int FAJR = 1;
    public static final int SUNRISE = 2;
    public static final int DOHR = 3;
    public static final int ASR = 4;
    public static final int SUNSET = 5;
    public static final int MAGHRIB = 6;
    public static final int ISHAA = 7;
    public static final int POS_FAJR_ANGLE = 0;
    public static final int POS_IMSAK_SELECTOR = 1;
    public static final int POS_IMSAK_VALUE = 2;
    public static final int POS_MAGHRIB_SELECTOR = 3;
    public static final int POS_MAGHRIB_VALUE = 4;
    public static final int POS_ISHAA_SELECTOR = 5;
    public static final int POS_ISHAA_VALUE = 6;
    // Adjusting Methods for Higher Latitudes
    private static final int NONE = 0;          // No adjustment
    private static final int MIDNIGHT = 1;      // middle of night
    private static final int ONE_SEVENTH = 2;    // 1/7th of night
    private static final int ANGLE_BASED = 3;    // angle/60th of night
    // Time Names
    private static final String[] TIME_NAMES = new String[]{
        "Imsak",
        "Fajr",
        "Sunrise",
        "Dhuhr",
        "Asr",
        "Sunset",
        "Maghrib",
        "Isha"
    };
    private static final int PRAYERS_COUNT = TIME_NAMES.length;
    /** calculation method (Mekkah) */
    private int calcMethod = 4;
    /** Juristic method for Asr */
    private int asrJuristic = 0;
    /** minutes after mid-day for Dhuhr */
    private int dhuhrMinutes = 0;
    /** adjusting method for higher latitudes */
    private int adjustHighLats = 1;
    /** time format */
    private int timeFormat = TimeFormat.H24.getValue();
    /** latitude */
    private double lat;
    /** longitude */
    private double lng;
    /** time-zone */
    private int timeZone;
    /** Julian date */
    private double JDate;
    /** calculation */
    // number of iterations needed to compute times, this should never be more than 1;
    private int numIterations = 1;
    private ResourceReader RESSOURCE = ServiceFactory.getFactory().getResourceReader();
    /**
     * methodParams[methodNum] = new Array(fa, ms, mv, is, iv);
     *      fa : fajr angle
     *      is : imsak selector (0 = angle; 1 = minutes before fajr)
     *      iv : imsak parameter value (in angle or minutes)
     *      ms : maghrib selector (0 = angle; 1 = minutes after sunset)
     *      mv : maghrib parameter value (in angle or minutes)
     *      is : isha selector (0 = angle; 1 = minutes after maghrib)
     *      iv : isha parameter value (in angle or minutes)
     */
    private double[][] methodParams = new double[][]{
        new double[]{16, 0, 19.5, 0, 4, 0, 14}, //Jafari
        new double[]{18, 0, 19.5, 1, 0, 0, 18}, //Karachi
        new double[]{15, 0, 19.5, 1, 0, 0, 15}, //ISNA
        new double[]{18, 0, 19.5, 1, 0, 0, 17}, //MWL
        new double[]{18.5, 0, 19.5, 1, 0, 1, 90}, //Makkah
        new double[]{19.5, 0, 19.5, 1, 0, 0, 17.5}, //Egypt
        new double[]{12, 1, 15, 1, 0, 0, 12} //Custom
    };
    private int paramsCount = methodParams[0].length;

    /**
     * Returns the prayer times for a given date , the date format is specified as indiviual settings.
     *
     * @param year Year to use when calculating times</param>
     * @param month Month to use when calculating times</param>
     * @param day Day to use when calculating times</param>
     * @param latitude Lattitude to use when calculating times</param>
     * @param longitude Longitude to use when calculating times</param>
     * @param timeZone Time zone to use when calculating times</param>
     * @return a String Array containing the Salaah times
     */
    private String[] getDatePrayerTimes(int year, int month, int day, double latitude, double longitude, Integer timeZone) {
        lat = latitude;
        lng = longitude;
        this.timeZone = effectiveTimeZone(timeZone);
        JDate = julianDate(year, month, day) - longitude / (15 * 24);
        return computeDayTimes();
    }

    /**
     * Returns the prayer times for a given date , the date format is specified as indiviual settings.
     *
     * @param year Year to use when calculating times</param>
     * @param month Month to use when calculating times</param>
     * @param day Day to use when calculating times</param>
     * @param latitude Lattitude to use when calculating times</param>
     * @param longitude Longitude to use when calculating times</param>
     * @param timeZone Time zone to use when calculating times</param>
     *
     * @return
     * A String Array containing the Salaah times.
     * The time is in the 24 hour format.
     * The array is structured as such.
     *
     * 0.Imsak
     * 1.Fajr
     * 2.Sunrise
     * 3.Dhuhr
     * 4.Asr
     * 5.Sunset
     * 6.Maghrib
     * 7.Isha
     */
    public String[] getPrayerTimes(Date date, double latitude, double longitude, Integer timeZone) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return this.getDatePrayerTimes(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH),
                latitude, longitude, timeZone);
    }

    /**
     * This method is used to set the calculation method for the salaah times.
     * There are 7 calculation types available.
     * Jafari  = 0
     * Karachi = 1
     * ISNA    = 2
     * MWL     = 3
     * Makkah  = 4
     * Egypt   = 5
     * Custom  = 6
     */
    public void setCalculationMethod(CalculationMethods methodToUse,
            CalculationCustomParams params) {
        this.calcMethod = methodToUse.getValue();

        if (CalculationMethods.Custom.getValue() == methodToUse.getValue()) {
            setFajrAngle(params.getFajrAngle());
            if (params.getImsakSelector() == 0) {
                setImsakAngle(params.getImsakValue());
            } else {
                setImsakMinutes(params.getImsakValue());
            }
            if (params.getMaghrebSelector() == 0) {
                setMaghribAngle(params.getMaghrebValue());
            } else {
                setMaghribMinutes(params.getMaghrebValue());
            }
            if (params.getIshaaSelector() == 0) {
                setIshaAngle(params.getIshaaValue());
            } else {
                setIshaMinutes(params.getIshaaValue());
            }
        }
    }

    /**
     * set the juristic method for Asr
     */
    public void setAsrJurusticType(JuristicMethods selectedJuristicion) {
        this.asrJuristic = selectedJuristicion.getValue();
    }

    /**
     * set the angle for calculating Fajr
     */
    public void setFajrAngle(double angle) {
        this.setCustomParams(new Double[]{new Double(angle), null, null, null, null, null, null});
    }

    /**
     * set the angle for calculating Imsak
     */
    public void setImsakAngle(double angle) {
        this.setCustomParams(new Double[]{null, new Double(0), new Double(angle), null, null, null, null});
    }

    /**
     * set the angle for calculating Maghrib
     */
    public void setMaghribAngle(double angle) {
        this.setCustomParams(new Double[]{null, null, null, new Double(0), new Double(angle), null, null});
    }

    /**
     * set the angle for calculating Isha
     */
    public void setIshaAngle(double angle) {
        this.setCustomParams(new Double[]{null, null, null, null, null, new Double(0), new Double(angle)});
    }

    /**
     * set the minutes after mid-day for calculating Dhuhr
     */
    private void setDhuhrMinutes(int minutes) {
        this.dhuhrMinutes = minutes;
    }

    /*
     * set the minutes before Fajr for calculating Imsak
     */
    public void setImsakMinutes(double minutes) {
        this.setCustomParams(new Double[]{null, new Double(1), new Double(minutes), null, null, null, null});
    }

    /*
     * set the minutes after Sunset for calculating Maghrib
     */
    public void setMaghribMinutes(double minutes) {
        this.setCustomParams(new Double[]{null, null, null, new Double(1), new Double(minutes), null, null});
    }

    /*
     * set the minutes after Maghrib for calculating Isha
     */
    public void setIshaMinutes(double minutes) {
        this.setCustomParams(new Double[]{null, null, null, null, null, new Double(1), new Double(minutes)});
    }

    /*
     * set custom values for calculation parameters
     */
    private void setCustomParams(Double[] userParams) {
        for (int i = 0; i < paramsCount; i++) {
            if (userParams[i] == null) {
                this.methodParams[CalculationMethods.Custom.getValue()][i] = this.methodParams[this.calcMethod][i];
            } else {
                this.methodParams[CalculationMethods.Custom.getValue()][i] = userParams[i].doubleValue();
            }
        }
        this.calcMethod = CalculationMethods.Custom.getValue();
    }

    /*
     * set adjusting method for higher latitudes
     */
    private void setHighLatsMethod(int methodID) {
        this.adjustHighLats = methodID;
    }

    /*
     * set the time format
     */
    public void setTimeFormat(int timeFormat) {
        this.timeFormat = timeFormat;
    }

    /*
     * convert float hours to 24h format
     */
    private String floatToTime24(Double time) {
        if (time == null) {
            return "";
        } else {
            double _time = time.doubleValue();
            _time = this.fixhour(_time + 0.5 / 60);  // add 0.5 minutes to round
            double hours = Math.floor(_time);
            double minutes = Math.floor((_time - hours) * 60);
            return this.twoDigitsFormat(new Double(hours).intValue()) + TimeFormat.TIME_SEPARATOR + this.twoDigitsFormat(new Double(minutes).intValue());
        }
    }

    /*
     * convert float hours to 12h format with no suffix
     */
    private String floatToTime12NS(double time) {
        return this.floatToTime12(new Double(time), true);
    }

    /*
     * convert float hours to 12h format
     */
    private String floatToTime12(Double time, boolean noSuffix) {
        if (time == null) {
            return "";
        } else {
            double _time = time.doubleValue();
            _time = this.fixhour(_time + 0.5 / 60);  // add 0.5 minutes to round
            double hours = Math.floor(_time);
            double minutes = Math.floor((_time - hours) * 60);
            String suffix = hours >= 12 ? " " + RESSOURCE.get("PM") : " " + RESSOURCE.get("AM");
            hours = (hours + 12 - 1) % 12 + 1;
            return this.twoDigitsFormat(new Double(hours).intValue()) + TimeFormat.TIME_SEPARATOR + this.twoDigitsFormat(new Double(minutes).intValue()) + (noSuffix ? "" : suffix);
        }
    }

    /**
     * References:
     * http://www.ummah.net/astronomy/saltime (dead-link)
     * http://aa.usno.navy.mil/faq/docs/SunApprox.html
     * compute declination angle of sun and equation of time
     */
    private double[] sunPosition(double jd) {
        double D = jd - 2451545.0;
        double g = this.fixangle(357.529 + 0.98560028 * D);
        double q = this.fixangle(280.459 + 0.98564736 * D);
        double L = this.fixangle(q + 1.915 * this.dsin(g) + 0.020 * this.dsin(2 * g));

        double R = 1.00014 - 0.01671 * this.dcos(g) - 0.00014 * this.dcos(2 * g);
        double e = 23.439 - 0.00000036 * D;

        double d = this.darcsin(this.dsin(e) * this.dsin(L));
        double RA = this.darctan2(this.dcos(e) * this.dsin(L), this.dcos(L)) / 15.0;

        RA = this.fixhour(RA);
        double EqT = q / 15 - RA;

        return new double[]{d, EqT};
    }

    /**
     * compute equation of time
     * @param jd
     * @return
     */
    private double equationOfTime(double jd) {
        return this.sunPosition(jd)[1];
    }

    /**
     * compute declination angle of sun
     * @param jd
     * @return
     */
    private double sunDeclination(double jd) {
        return this.sunPosition(jd)[0];
    }

    /**
     *
     * compute mid-day (Dhuhr, Zawal) time
     * @param t
     * @return
     */
    private double computeMidDay(double t) {
        double T = this.equationOfTime(this.JDate + t);
        double Z = this.fixhour(12 - T);
        return Z;
    }

    /**
     * compute time for a given angle G
     * @param G
     * @param t
     * @return
     */
    private double computeTime(double G, double t) {
        double D = this.sunDeclination(this.JDate + t);
        double Z = this.computeMidDay(t);
        double V = ((double) (1 / 15d)) * this.darccos((-this.dsin(G) - this.dsin(D) * this.dsin(this.lat))
                / (this.dcos(D) * this.dcos(this.lat)));
        return Z + (G > 90 ? -V : V);
    }

    /**
     * compute the time of Asr
     * @param step
     * @param t
     * @return
     */
    private double computeAsr(int step, double t) // Shafii: step=1, Hanafi: step=2
    {
        double D = this.sunDeclination(this.JDate + t);
        double G = -this.darccot(step + this.dtan(Math.abs(this.lat - D)));
        return this.computeTime(G, t);
    }

    /**
     * compute prayer times at given julian date
     * @param times
     * @return
     */
    private Double[] computeTimes(Double[] times) {
        Double[] t = this.dayPortion(times);

        double Imsak = 0;
        // Imsak angle
        if (this.methodParams[this.calcMethod][1] == 0) {
            Imsak = this.computeTime(180 - this.methodParams[this.calcMethod][2], t[IMSAK].doubleValue());
        }

        double Fajr = this.computeTime(180 - this.methodParams[this.calcMethod][0], t[FAJR].doubleValue());
        double Sunrise = this.computeTime(180 - 0.833, t[SUNRISE].doubleValue());
        double Dhuhr = this.computeMidDay(t[DOHR].doubleValue());
        double Asr = this.computeAsr(1 + this.asrJuristic, t[ASR].doubleValue());

        double Sunset = this.computeTime(0.833, t[SUNSET].doubleValue());

        double Maghrib = 0;
        double Isha = 0;

        // Maghreb angle
        if (this.methodParams[this.calcMethod][3] == 0) {
            Maghrib = this.computeTime(this.methodParams[this.calcMethod][4], t[MAGHRIB].doubleValue());
        }

        // Ishaa angle
        if (this.methodParams[this.calcMethod][5] == 0) {
            Isha = this.computeTime(this.methodParams[this.calcMethod][6], t[ISHAA].doubleValue());
        }

        return new Double[]{new Double(Imsak), new Double(Fajr), new Double(Sunrise), new Double(Dhuhr),
                    new Double(Asr), new Double(Sunset), new Double(Maghrib), new Double(Isha)};
    }

    /**
     * compute prayer times at given julian date
     * @return
     */
    private String[] computeDayTimes() {
        Double[] times = new Double[]{new Double(5.0), new Double(5.0), new Double(6.0), new Double(12.0),
            new Double(13.0), new Double(18.0), new Double(18.0), new Double(18.0)}; //default times

        for (double i = 1; i <= this.numIterations; i++) {
            times = this.computeTimes(times);
        }

        times = this.adjustTimes(times);

        return this.adjustTimesFormat(times);
    }

    /**
     * adjust times in a prayer time array
     * @param times
     * @return
     */
    private Double[] adjustTimes(Double[] times) {
        for (int i = 0; i < PRAYERS_COUNT; i++) {
            times[i] = new Double(times[i].doubleValue() + this.timeZone - this.lng / 15);
        }

        // Imsak minutes
        if (this.methodParams[this.calcMethod][1] == 1) {
            times[IMSAK] = new Double(times[FAJR].doubleValue() - this.methodParams[this.calcMethod][2] / 60);
        }

        // Dhur minutes
        times[DOHR] = new Double(times[DOHR].doubleValue() + this.dhuhrMinutes / 60); //Dhuhr

        // Maghrib minutes
        if (this.methodParams[this.calcMethod][3] == 1) {
            times[MAGHRIB] = new Double(times[SUNSET].doubleValue() + this.methodParams[this.calcMethod][4] / 60);
        }

        // Ishaa minutes
        if (this.methodParams[this.calcMethod][5] == 1) {
            times[ISHAA] = new Double(times[MAGHRIB].doubleValue() + this.methodParams[this.calcMethod][6] / 60);
        }

        if (this.adjustHighLats != NONE) {
            times = this.adjustHighLatTimes(times);
        }
        return times;
    }

    /**
     * convert times array to given time format
     * @param times
     * @return
     */
    private String[] adjustTimesFormat(Double[] times) {
        String[] returnData = new String[times.length];

        for (int i = 0; i < PRAYERS_COUNT; i++) {
            if (this.timeFormat == TimeFormat.H12.getValue()) {
                returnData[i] = floatToTime12(times[i], false);
            } else {
                returnData[i] = floatToTime24(times[i]);
            }
        }
        return returnData;
    }

    /**
     * adjust Fajr, Isha and Maghrib for locations in higher latitudes
     * @param times
     * @return
     */
    private Double[] adjustHighLatTimes(Double[] times) {
        double nightTime = this.timeDiff(times[SUNSET].doubleValue(), times[SUNRISE].doubleValue()); // sunset to sunrise

        // Adjust Fajr
        double FajrDiff = this.nightPortion(methodParams[this.calcMethod][0]) * nightTime;
        if (times[FAJR].isNaN() || this.timeDiff(times[FAJR].doubleValue(), times[SUNRISE].doubleValue()) > FajrDiff) {
            times[FAJR] = new Double(times[SUNRISE].doubleValue() - FajrDiff);
            times[IMSAK] = new Double(times[FAJR].doubleValue() - FajrDiff);
        }

        // Adjust Ishaa
        double IshaAngle = (this.methodParams[this.calcMethod][5] == 0) ? this.methodParams[this.calcMethod][6] : 18;
        double IshaDiff = this.nightPortion(IshaAngle) * nightTime;
        if (times[ISHAA].isNaN() || this.timeDiff(times[SUNSET].doubleValue(), times[ISHAA].doubleValue()) > IshaDiff) {
            times[ISHAA] = new Double(times[SUNSET].doubleValue() + IshaDiff);
        }

        // Adjust Maghrib
        double MaghribAngle = (this.methodParams[this.calcMethod][3] == 0) ? this.methodParams[this.calcMethod][4] : 4;
        double MaghribDiff = this.nightPortion(MaghribAngle) * nightTime;
        if (times[MAGHRIB].isNaN() || this.timeDiff(times[SUNSET].doubleValue(), times[MAGHRIB].doubleValue()) > MaghribDiff) {
            times[MAGHRIB] = new Double(times[SUNSET].doubleValue() + MaghribDiff);
        }

        return times;
    }

    /**
     * the night portion used for adjusting times in higher latitudes
     * @param angle
     * @return
     */
    private double nightPortion(double angle) {
        if (this.adjustHighLats == ANGLE_BASED) {
            return 1 / 60 * angle;
        }
        if (this.adjustHighLats == MIDNIGHT) {
            return 1 / 2d;
        }
        if (this.adjustHighLats == ONE_SEVENTH) {
            return 1 / 7d;
        }
        return 0;
    }

    /**
     * convert hours to day portions
     * @param times
     * @return
     */
    private Double[] dayPortion(Double[] times) {
        for (int i = 0; i < PRAYERS_COUNT; i++) {
            times[i] = new Double(times[i].doubleValue() / 24);
        }
        return times;
    }

    /**
     * compute the difference between two times
     * @param time1
     * @param time2
     * @return
     */
    private double timeDiff(int time1, int time2) {
        return this.fixhour(time2 - time1);
    }

    private double timeDiff(double time1, double time2) {
        return this.fixhour(time2 - time1);
    }

    /**
     * add a leading 0 if necessary
     * @param num
     * @return
     */
    private String twoDigitsFormat(int num) {
        return (num < 10) ? "0" + new Integer(num).toString() : new Integer(num).toString();
    }

    /**
     * calculate julian date from a calendar date
     * @param year
     * @param month
     * @param day
     * @return
     */
    private double julianDate(int year, int month, int day) {
        double A = Math.floor((double) (year / 100));
        double B = Math.floor(A / 4);
        double C = 2 - A + B;
        double Dd = day;
        double Ee = Math.floor(365.25 * (year + 4716));
        double F = Math.floor(30.6001 * (month + 1));

        double JD = C + Dd + Ee + F - 1524.5;

        return JD;
    }

    private Date createDate(int year, int month, int day) {

        return createDate(year, month, day, TimeZone.getDefault());
    }

    private Date createDate(int year, int month, int day, TimeZone timeZone) {

        Calendar cal = Calendar.getInstance(timeZone);

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
//        cal.setTimeZone(timeZone);

        return cal.getTime();
    }

    /**
     * convert a calendar date to julian date (second method)
     * @param year
     * @param month
     * @param day
     * @return
     */
    private double calcJD(int year, int month, int day) {
        double J1970 = 2440588.0;

        //Date date = new DateTime(year, month - 1, day);
        Date date = createDate(year, month - 1, day);
        //TimeSpan TS = new TimeSpan(year, month - 1, day);
        //var ms = TS.TotalMilliseconds;   // # of milliseconds since midnight Jan 1, 1970
        long ms = date.getTime();
        double days = Math.floor((double) ms / (1000 * 60 * 60 * 24));
        return J1970 + days - 0.5;
    }

    /**
     * compute local time-zone
     * @return
     */
    public static int getTimeZone() {
        int diff = TimeZone.getDefault().getRawOffset(); // en millisecondes
        diff /= 1000; // en secondes
        diff /= 3600; // en heure

        return diff;
    }

    public double[][] getMethodParams() {
        return this.methodParams;
    }

    /**
     * compute base time-zone of the system
     * @return
     */
    private int getBaseTimeZone() {
        return getTimeZone();
    }

    /**
     * detect daylight saving in a given date
     * @return
     */
    private boolean detectDaylightSaving() {
        //return this.getTimeZone(date) != this.getBaseTimeZone();
        return TimeZone.getDefault().useDaylightTime();
    }

    /**
     * return effective timezone for a given date
     * @param timeZone
     * @return
     */
    private int effectiveTimeZone(Integer timeZone) {
        if (timeZone == null) {
            timeZone = new Integer(getTimeZone());
        }
        return timeZone.intValue();
    }

    /**
     * degree sin
     * @param d
     * @return
     */
    private double dsin(double d) {
        return Math.sin(this.dtr(d));

    }

    /**
     * degree cos
     * @param d
     * @return
     */
    private double dcos(double d) {
        return Math.cos(this.dtr(d));
    }

    /**
     * degree tan
     * @param d
     * @return
     */
    private double dtan(double d) {
        return Math.tan(this.dtr(d));
    }

    /**
     * degree arcsin
     * @param x
     * @return
     */
    private double darcsin(double x) {
        //return this.rtd(Math.Asin(x));
        Real r = new Real(Double.toString(x));
        r.asin();
        r = this.rtd(r);
        return Double.parseDouble(r.toString());
    }

    /**
     * degree arccos
     * @param x
     * @return
     */
    private double darccos(double x) {
        Real r = new Real(Double.toString(x));
        r.acos();
        r = this.rtd(r);
        return Double.parseDouble(r.toString());
    }

    /**
     * degree arctan
     * @param x
     * @return
     */
    private double darctan(double x) {
        Real r = new Real(Double.toString(x));
        r.atan();
        return Double.parseDouble(r.toString());
    }

    /**
     * degree arctan2
     * @param y
     * @param x
     * @return
     */
    private double darctan2(double y, double x) {
        //return this.rtd(Math.Atan2(y, x));
        double M_PI = Math.PI;
        double M_PI_2 = M_PI / 2.0;
        double absx, absy, val;
        if (x == 0 && y == 0) {
            return this.rtd(0);
        }
        absy = y < 0 ? -y : y;
        absx = x < 0 ? -x : x;
        if (absy - absx == absy) {
            /* x negligible compared to y */
            return this.rtd(y < 0 ? -M_PI_2 : M_PI_2);
        }
        if (absx - absy == absx) {
            /* y negligible compared to x */
            val = 0.0;
        } else {
            val = darctan(y / x);
        }
        if (x > 0) {
            /* first or fourth quadrant; already correct */
            return this.rtd(val);
        }
        if (y < 0) {
            /* third quadrant */
            return this.rtd(val - M_PI);
        }
        return this.rtd(val + M_PI);
    }

    /**
     * degree arccot
     * @param x
     * @return
     */
    private double darccot(double x) {
        Real r = new Real(Double.toString(1 / x));
        r.atan();
        r = this.rtd(r);
        return Double.parseDouble(r.toString());
    }

    /**
     * degree to radian
     * @param d
     * @return
     */
    private double dtr(double d) {
        return (d * Math.PI) / 180.0;
    }

    /**
     * radian to degree
     * @param r
     * @return
     */
    private double rtd(double r) {
        return (r * 180.0) / Math.PI;
    }

    /**
     * radian to degree
     * @param r
     * @return
     */
    private Real rtd(Real r) {
        r.mul(new Real("180"));
        r.div(Real.PI);
        return r;
    }

    /**
     * range reduce angle in degrees.
     * @param a
     * @return
     */
    private double fixangle(double a) {
        a = a - 360.0 * (Math.floor(a / 360.0));
        a = a < 0 ? a + 360.0 : a;
        return a;
    }

    /**
     * range reduce hours to 0..23
     * @param a
     * @return
     */
    private double fixhour(double a) {
        a = a - 24.0 * (Math.floor(a / 24.0));
        a = a < 0 ? a + 24.0 : a;
        return a;
    }
}
