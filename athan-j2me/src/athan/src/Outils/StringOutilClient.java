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
package athan.src.Outils;

import java.util.Calendar;
import java.util.Date;

/**
 * Utilitaires pour la manipulation de chaînes.
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
