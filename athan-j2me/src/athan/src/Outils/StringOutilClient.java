/*
 * Utilitaires
 */

package athan.src.Outils;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Saad BENBOUZID
 */
public class StringOutilClient {

    public static final String EMPTY = "";

    public static final int TRUE = 1;
    public static final int FALSE = 0;

    public static boolean isEmpty(String pString) {
        return pString == null || pString.trim().length() == 0;
    }

    public static int getValeurBooleenne(boolean pBool) {
        if (pBool) {
            return TRUE;
        } else {
            return FALSE;
        }
    }

    public static boolean getValeurBooleenne(int pInt) {
        if (pInt == TRUE) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isDateMemeJour(Date pDate1, Date pDate2) {

        boolean retour = false;

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(pDate1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(pDate2);

        if (cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
                && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) {
            retour = true;
        }

        return retour;
    }

}
