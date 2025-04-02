package MtechCommon;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Mike
 */
public class MtechDate {

    char chDelim = '-';

    /**
     *
     */
    public long days;

    /**
     *
     */
    public static final int MON = 1;

    /**
     *
     */
    public static final int SUN = 7;

    /**
     *
     */
    public static final int CD_LONG_DDMMYY = 0;

    /**
     *
     */
    public static final int CD_STRING_YYYYMMDD_OR_YY_MM_DD = 10;
    public static final int CD_STRING_YYYY_MM_DD = 13;
    //#define CD_DDMMYY 1
    //#define CD_YYMMDD 2
    //#define CD_MMDDYY 3
    //#define CD_CHAR_YYDDD 4
    //#define CD_LONG_YYDDD 5
    //#define CD_DOS_FILE_DATE 6
    //#define CD_TODAY 7

    /**
     *
     */
    public static final int CD_DDMMYYYY = 8;
    //#define CD_LONG_DDMMYYYY 9
// #define CD_CHAR_SORT_EITHER 10
// #define CD_LONG_DAYS 11
// #define CD_CHAR_EURO_EITHER 12

    /**
     *
     * @param in_days
     */
    public void setDays(long in_days) {
        days = in_days;
        //System.out.println("Setdays " + days);
    } // end setDays

    /**
     *
     */
    public void setToday() {
        int rc = this.dte2days(7, "dummy");

        //System.out.println("SetToday " + days);
    }
    /**
     * ******************************************************************
     * GRGJUL.C Routine to convert a date from gregorian to julian. Both input
     * and output are long integers. A negative value is returned if an error is
     * detected. These return codes are: -1 = date too large -2 = invalid day -3
     * = invalid month M.C.Betteridge 13th february 1990. Changes 04 Mar 92 -
     * MCB - fix bug in leap year processing
     ********************************************************************
     */
    final int FEB = 2;

    long grgjul(long grg_date_parm) {
        long juldate;
        long wkgrg;
        long mm, yy, dd;
        boolean leap_year;
        /* 1 = Leap year 0 = non leap year */
 /* these 2 arrays have dummy first entries */
        long julian_days[] = {0, 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334};
        long days_in_month[] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        if (grg_date_parm > 999999) {
            return (-1);
        }

        wkgrg = grg_date_parm;
        yy = wkgrg % 100;
        /* extract year */
        wkgrg /= 100;
        /* shift right and lose year */
        mm = wkgrg % 100;
        /* extract month */
        dd = wkgrg / 100;
        /* shift right and lose month */

        if ((mm < 1) || (mm > 12)) /* validate month */ {
            return (-2);
        }

        /* test for leap year */
        leap_year = (yy % 4 == 0) ? true : false;

        if (leap_year) {
            days_in_month[FEB] = 29;
        } else {
            days_in_month[FEB] = 28;
        }

        if (dd > days_in_month[(int) mm]) {
            return (-3);
        }

        juldate = (yy * 1000) + dd + julian_days[(int) mm];

        if (leap_year && mm > FEB) {
            juldate++;
        }

        return (juldate);
    }

    /* End of GRGJUL */


    /**
     * ***********************************************************************
     */
    /* DTE2DAYS.C - Convert date to number of days relative to 1st January    */
 /* 1990. Where 01/01/00 is day 1.                                         */
 /* Parameters                                                             */
 /* 1) Input date format                                                   */
 /*         0 = DDMMYY   long (European)                                   */
 /*         1 = DD/MM/YY char (European)                                   */
 /*         2 = YY/MM/DD char (Sort)                                       */
 /*         3 = MM/DD/YY char (US)                                         */
 /*         4 = YYDDD    char (Julian)                                     */
 /*         5 = YYDDD    long (Julian)                                     */
 /*         6 = DOS file date format unsigned short                        */
 /*         7 = Current date - input date ignored                          */
 /*         8 = DD/MM/YYYY char (European)                                 */
 /*         9 = DDMMYYYY long (European)                                   */
 /*        10 = YY/MM/DD or YYYYMMDD char (sort)                           */
 /*        11 = reserved                                                   */
 /*        12 = DD/MM/YY or DDMMYYYY char (European)                       */
 /*        13 = YYYY/MM/DD char with / or -                     */
 /* 2) Input date char[9] or long integer                                  */
 /* 3) Output date long                                                    */
 /* Return codes                                                           */
 /* 0) OK                                                                  */
 /* 1) Invalid date format indicator passed                                */
 /* 2) Invalid gregorian date passed                                       */
 /* 3) Invalid julian date passed                                          */
 /* Changes                                                                */
 /* ~~~~~~~                                                                */
 /*  19/01/92 MCB Fix bug whereby Jan & Feb in a leap year gave wrong date */
 /*  29/07/93 MCB Include processing for DOS file date, and century.       */
 /*  03/08/93 MCB Include processing for current date.                     */
 /*  23/05/94 MCB Convert to C++. Correct non-leap year processing (again!)*/
 /*  24/05/94 MCB Include processing for DD/MM/YYYY with century.          */
 /*  18/07/96 MCB Include processing for DDMMYYYY (long) with century.     */
 /*  20/09/96 MCB Include processing date format 10.                       */
 /*  02/11/96 MCB Include processing date format 12.                       */
    /**
     * @param in_format
     *
     * @param in_date
     *
     * @param out_days
     * @return *********************************************************************
     */
    public int dte2days(int in_format, long in_date, long out_days) {
        int rc = 0;
        /* Return code */
        int j;
        long dd, mm, yy, ddd;
        long ddmmyy = 0;
        long ddmmccyy = 0;
        long yyddd = 0;
        long century = 0;

        switch (in_format) /* Convert date to standard format */ {
            case 0:
                /* DDMMYY */
                yyddd = grgjul(in_date);
                if (yyddd < 0) {
                    rc = 2;
                }
                break;
            default:
                rc = 1;
        }
        /* End switch */
        return (dte2days(century, yyddd));
    }

    /**
     *
     * @param in_format
     * @param in_date
     * @return
     */
    public int dte2days(int in_format, String in_date) {

        int rc = 0;
        /* Return code */
        int j;
        long dd, mm, yy, ddd;
        long ddmmyy = 0;
        long ddmmccyy = 0;
        long yyddd = 0;
        long century = 0;

        if (in_date.length() <= 0) {
            return 2;
        }

        StringBuffer wk_char = new StringBuffer(11);

        switch (in_format) /* Convert date to standard format */ {
            case 0:
                /* DDMMYY */
                break;
            case 1:
                /* DD/MM/YY */
                // ddmmyy = dateval (in_date->char_greg);
                if (ddmmyy < 0) {
                    rc = 2;
                }
                break;
            case 2:
                /* YY/MM/DD */
                // ddmmyy = exdate (in_date->char_greg);
                if (ddmmyy < 0) {
                    rc = 2;
                }
                break;
            case 3:
                /* MM/DD/YY */
                // ddmmyy = exdateus (in_date->char_greg);
                if (ddmmyy < 0) {
                    rc = 2;
                }
                break;
            case 4:
                /* YYDDD char */
                // j = sscanf (in_date->char_jul,"%ld",&yyddd);
                // if ((j <= 0) || (julchk(yyddd) < 0))
                //    rc = 3;
                break;
            case 5:
                /* YYDDD long */
                // yyddd = in_date->long_jul;
                // if (julchk(yyddd) < 0)
                //    rc = 3;
                break;
            case 6:
                /* DOS file date */
                // dd = in_date->file_date & 0x001F;
                // mm = (in_date->file_date >> 5) & 0x000F;
                // yy = ((in_date->file_date >> 9) & 0x007f) + 1980;
                // century = yy / 100;
                // yy %= 100;
                // ddmmyy = ((long)dd * 10000L) + (mm * 100) + yy;
                break;
            case 7:
                /* Current date */
                java.util.Date today = new java.util.Date();
                java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd-MM-yyyy");
                ddmmccyy = datevalc(df.format(today));
                ddmmyy = ((ddmmccyy / 10000L) * 100L) + ddmmccyy % 100L;
                century = (ddmmccyy % 10000L) / 100L;

                //  System.out.println("dte2days today " + ddmmccyy + " " + df.format(today));
                // regs.h.ah = 0x2A;                 /* Get date from DOS */
                // int86(0x21,&regs,&regs);
                /* dd in dl, mm in  dh, yyyy in cx */
                // ddmmyy  = ((long)regs.h.dl * 10000L) + (regs.h.dh * 100) + (regs.x.cx % 100);
                // century = regs.x.cx / 100;
                break;
            case 8:
                /* DD/MM/YYYY */
                ddmmccyy = datevalc(in_date);
                if (ddmmccyy < 0) {
                    rc = 2;
                }
                ddmmyy = ((ddmmccyy / 10000L) * 100L) + ddmmccyy % 100L;
                century = (ddmmccyy % 10000L) / 100L;
                break;
            case 9:
                /* DDMMYYYY */
                // ddmmccyy = in_date->long_greg;
                ddmmyy = ((ddmmccyy / 10000L) * 100L) + ddmmccyy % 100L;
                century = (ddmmccyy % 10000L) / 100L;
                yyddd = grgjul(ddmmyy);
                /* Validate date */
                if (yyddd < 0) {
                    rc = 2;
                }
                ddmmyy = 0;
                /* Prevent re-conversion */
                break;
            case 10:
                /* YY/MM/DD or YYYYMMDD */
                ddmmccyy = datevalc(in_date);
                // if (in_date->char_greg[2] == chDelim) { /* Assume YY/MM/DD */
                //    ddmmyy = exdate (in_date->char_greg);
                //if (ddmmccyy < 0) {
                ///    rc = 2;
                //century = (ddmmyy % 100 < 80) ? 20 : 19;
                //  break;
                // } else {  
                // ddmmccyy = datevalc (wk_char);
                if (ddmmccyy < 0) {
                    rc = 2;
                }
                ddmmyy = ((ddmmccyy / 10000L) * 100L) + ddmmccyy % 100L;
                century = (ddmmccyy % 10000L) / 100L;
                //      } /* endif */
                break;
            case 13:
                /* YYYY/MM/DD */
                String wkdate = in_date.substring(0, 4)
                        + in_date.substring(5, 7)
                        + in_date.substring(8);
//                System.out.println(wkdate + " " + in_date);
                ddmmccyy = datevalc(wkdate);

                if (ddmmccyy < 0) {
                    rc = 2;
                }
                ddmmyy = ((ddmmccyy / 10000L) * 100L) + ddmmccyy % 100L;
                century = (ddmmccyy % 10000L) / 100L;
                //      } /* endif */
                break;
//   case CD_CHAR_EURO_EITHER:  /* DD/MM/YY or DDMMYYYD */
            //    if (in_date->char_greg[2] == chDelim) { /* Assume DD/MM/YY */
            //       ddmmyy = dateval (in_date->char_greg);
            //        if (ddmmyy < 0)
            //           rc = 2;
            //        century = (ddmmyy % 100 < 80) ? 20 : 19;
            //       break;
            //    } else {                          /* Assume DDMMYYYY */
            //       wk_char[0] = in_date->char_greg[0];
            //       wk_char[1] = in_date->char_greg[1];
            //       wk_char[2] = chDelim;
            ///       wk_char[3] = in_date->char_greg[2];
            //       wk_char[4] = in_date->char_greg[3];
            //       wk_char[5] = chDelim;
            //       wk_char[6] = in_date->char_greg[4];
            //       wk_char[7] = in_date->char_greg[5];
            //       wk_char[8] = in_date->char_greg[6];
            //       wk_char[9] = in_date->char_greg[7];
            //       ddmmccyy = datevalc (wk_char);
            //       if (ddmmccyy < 0)
            //          rc = 2;
            //       ddmmyy = ((ddmmccyy / 10000L) * 100L) + ddmmccyy % 100L;
            //       century = (ddmmccyy % 10000L) / 100L;
            //    } /* endif */
            //   break;
            default:
                rc = 1;
        }
        /* End switch */

        if (rc == 0) {
            if (ddmmyy > 0) {
                yyddd = grgjul(ddmmyy);
            } // end if

            rc = dte2days(century, yyddd);
        }
        return (rc);
    }

    /*End of dte2days */


    private int dte2days(long century, long in_date) {

        int rc = 0;
        /* Return code */
        int j;
        long dd, mm, yy, ddd;
        long ddmmyy = 0;
        long ddmmccyy = 0;
        long yyddd = in_date;
        ;

        if (century > 0) {
            century = (century - 19) * 36525;
            /* Days in century */
        }
        /* endif */


        ddd = yyddd % 1000;
        yy = yyddd / 1000;

        days = (long) (yy * 365) + (long) (yy / 4) + ddd;
        /* Add on no. of days in century if   */
 /* it is the 21st century.            */
        days += century;
        /* Add 1 day if it is not a leap year */
        if (yy % 4 != 0) {
            days += 1;
        }
        /* endif */


        return (rc);
    }

    /*End of dte2days */


    /**
     * *************************************************************************
     */
    /* Subroutine to validate dates and convert to binary in long int format    */
 /*  Returns -1 if slashes are not present                                   */
 /*  Returns -2 if dd mm cc or yy are non-numeric                            */
 /*  Returns -3 if month is not in range 1-12                                */
 /*  Returns -4 if day is not in range for month                             */
 /*  Returns -5 if year is out of range                                      */
 /*  Returns -6 if year is invalid (not 2 or 4 digits)                       */
 /* M.Betteridge 24th May 94. Based on DATEVAL                               */
    /**
     * @param input_date
     *
     * @return
     * @return  ************************************************************************
     */
    public long datevalc(String input_date) {

        int wk = 0;
        int pow;
        int j;
        long ddmmccyy = 0;
        long d = -1;
        long m = -1;
        long y = -1;
        int days_in_month[] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        // if ((input_date.charAt(2) != chDelim)
        //         | (input_date.charAt(5) != chDelim)) {
        ///     return (-1);
        // }
        int item = 0;
        pow = 1;
        for (j = input_date.length() - 1; j >= 0; j--) {
            if ((input_date.charAt(j) >= '0')
                    && (input_date.charAt(j) <= '9')) {
                wk += ((input_date.charAt(j) - '0') * pow);
                pow *= 10;
                //    System.out.println("datevalc " + wk + "$" + pow);
            } else {
                if ((input_date.charAt(j) == '-')
                        || (input_date.charAt(j) == '/')
                        || j == 0) {
                    switch (item++) {
                        case 0:
                            y = wk;
                            break;
                        case 1:
                            m = wk;
                            break;
                        case 2:
                            d = wk;
                    } // end switch
                    pow = 1;

                    //       System.out.println("datevalc " + wk + "$" + pow + "Â£" + item + "y" + y + "m" + m + "d" + d);
                    wk = 0;
                } else {
                    return (-2);
                }
            }
        }
        /*End for */

        // Delimiters may not have been found
        if ((y == -1) && (m == -1)) {
            y = wk / 10000;
            m = (wk / 100) % 100;
            wk = wk % 100;
        } // end if
        d = wk;

        /* Convert year to binary and check for leap year */
        if ((y < 0) || (y > 2099)) {
            return -5;
        } // end if

        if (y < 100) {
            if (y < 40) {
                y += 2000;
            } else {
                y += 1000;
            } // end if
        } // end if

        if ((y < 1000)
                || (y > 9999)) {
            return -6;
        }  //end if

        if (y % 4 == 0) {
            days_in_month[2] = 29;
        } else {
            days_in_month[2] = 28;
        }
        /* endif */

 /* Convert month to binary and validate */
        if ((m < 1) || (m > 12)) {
            return (-3);
        }

        /* Convert day to binary and validate */
        j = (int) m;
        /* move from long to int */
        if ((d < 1) || (d > days_in_month[j])) {
            return (-4);
        }

        ddmmccyy = d * 1000000;
        ddmmccyy += m * 10000;
        ddmmccyy += y;

        return (ddmmccyy);
    }

    /*End dateval */


    /**
     * ***********************************************************************
     */
    /* DAYS2DTE.C - Convert number of days since 1st January 1990 to a julian */
 /* date.                                                                  */
 /* Parameters                                                             */
 /* 1) Input number of days                                                */
 /* Returns                                                                */
 /* Julian date (long)                                                     */
 /* M.C.Betteridge 20th August 1990                                        */
 /* 26/01/92 MCB Add 1 to date returned to correspond with change made in  */
 /*              dte2days.                                                 */
 /* 02/03/92 MCB get rid of float variables, handle leap years correctly.  */
 /* 01/11/92 MCB handle leap years correctly, make it match 123 concept.   */
 /* 26/05/94 MCB fix bug whereby last day of century became day 0 of next. */
 /* 02/10/96 MCB fix to days divisible by 1461 appearing as 01/01 not 31/12*/
    /**
     * @param in_days
     * @param in_days
     * @return
     * @return  ***********************************************************************
     */
    public long days2dte(long in_days) {
        long yy, yyddd, cc;
        long no_4_years, rem;
        int year_in_period;

        no_4_years = in_days / 1461L;
        /* number of days in 4 years     */
        rem = in_days % 1461L;
        yy = no_4_years * 4L;
        cc = (in_days - 1) / 36525; // days in century
        cc += 19;
        //  System.out.println("days2dte cc=" +cc  + " yy="+yy);

        if (rem <= 366) {
            if (rem == 0) {
                rem = 365;
                year_in_period = -1;
            } else {
                year_in_period = 0;
            }
            /* endif */
        } else {
            rem -= 366;
            if (rem <= 365) {
                year_in_period = 1;
            } else {
                rem -= 365;
                if (rem <= 365) {
                    year_in_period = 2;
                } else {
                    rem -= 365;
                    year_in_period = 3;
                }
                /* endif */
            }
            /* endif */
        }
        /* endif */
        yy += year_in_period;
        yy %= 100; // trim to two digits
        yyddd = (cc * 100000L) + (yy * 1000L) + rem;
        //  System.out.println("v3 days2dte " + yyddd  + " yy="+yy+ " cc="+cc);
        return (yyddd);
    }

    /*End of days2dte */


    /**
     * *************************************************************************
     */
    /* DAYNUM.C Function to determine which day of the week a given date falls  */
 /* on. Monday = day 1, Sunday = day 7. The input is a long int containing   */
 /* ddmmyy.                                                                  */
 /*  Calls DTE2DAYS. The return code is negated and passed back.             */
 /* M.Betteridge 15th Jan 91                                                 */
 /* Changes..                                                                */
 /* 02/11/92 MCB Reflect changes made in dte2days.               --  @A1 --  */
 /* 26/10/92 MCB Include daynum_from_days, convert to c++        --  @A2 --  */
    /**
     * *************************************************************************
     */
    //************************************************************************//
    private int give_daynum(long days) {
        long j;
        int week_day[] = {6, 7, 1, 2, 3, 4, 5};
        j = days % 7;
        return (week_day[(int) j]);
    }

    /*End give_daynum */
//************************************************************************//

    /**
     *
     * @param input_date
     * @return
     */
    public int daynum(long input_date) {
        int j;
        long days = 0;
        ;

        j = dte2days(CD_LONG_DDMMYY, input_date, days);

        if (j != 0) {
            return (-j);
        }
        /* endif */

        return (give_daynum(days));
    }

    /*End daynum */
//************************************************************************//

    /**
     *
     * @param input_days
     * @return
     */
    public int daynum_from_days(long input_days) {
        return (give_daynum(input_days));
    }

    /*End daynum_from_days */

    /**
     *
     * @return
     */
    public int daynum() {
        return (give_daynum(days));
    } // end daynum

    /**
     * *************************************************************************
     */
    /* FINDDATE - Function to find a date relative to another. The input        */
 /* consists of the day of week required (mon = 1), a < or > and the base    */
 /* date as a long integer containing a serial date. The return is the date  */
 /* of the specified day before or after the input date. If actual check is  */
 /* for the day <= or >= theinput date. Errors result in a negative return   */
 /* value. Otherwise a day number relative to 1st Jan 1900 is returned.      */
 /*  Calls DAYNUM   - Get day of week for given date.                        */
 /*        DTE2DAYS - Convert date to number of days since 1st Jan 1900      */
 /*        DAYS2DTE - Convert number of days since 1st Jan 1900 to julian.   */
 /* M.Betteridge 16th Jan 91                                                 */
 /* Changes                                                                  */
 /* ~~~~~~~                                                                  */
 /* 02/NOV/96 MCB Use serial input date rather than ddmmyy.                  */
    /**
     * @param in_daynum
     *
     * @param in_operator
     *
     * @return
     * @return  ***********************************************************************
     */
    public long finddate(int in_daynum, char in_operator) {
        long diff;
        long day_of_in_date;                   //* Weekday of input
        long days_into_century = days;
        long daynum = (long) in_daynum;

        if (daynum < MON || daynum > SUN) {
            return (-10);
        }
        /* endif */

        day_of_in_date = daynum_from_days(days_into_century);

        diff = (daynum - day_of_in_date);
        switch ((char) in_operator) {
            case '<':
                /* Find preceding day */
                if (diff > 0) {
                    days_into_century += (diff - 7);
                } else {
                    days_into_century += (diff);
                }
                /* endif */
                break;
            case '>':
                if (diff < 0) {
                    days_into_century += (diff + 7);
                } else {
                    days_into_century += (diff);
                }
                /* endif */
                break;
            default:
                return (-11);
        }
        /* endswitch */
        return (days_into_century);
    }

    /*End daynum */


    /**
     * ******************************************************************
     */
    /* JULGRGC.C Routine to convert a date from julian to gregorian. The */
 /* century will be included in the output date.                      */
 /* The input may be specified with or without the century. If no     */
 /* century is given, then years less than 80 are assumed to be 21st  */
 /* century, and those above to be 20th century.                      */
 /* Both input and output are long integers. A negative value is      */
 /* returned if an error is detected. These return codes are:         */
 /*     -2 = day less than 1                                          */
 /*     -3 = day greater than 366 (leap year)                         */
 /*     --4 = day greater than 365 (non leap year)                     */
 /* M.C.Betteridge 25th May 1994 (based on julgrg)                    */
 /* Amendments                                                        */
 /* ~~~~~~~~~~                                                        */
 /* 17 Jul 1996 MCB Correct century handling                          */
 /* 02 Oct 1996 MCB Handle relative centuries (ie 001=1901 101=2001)  */
    /**
     * ******************************************************************
     */
    long julgrgc(long jul_date_parm) {
        long grgdate;
        long ddd;
        long yy;
        long cc;
        int j = 0;
        int julian_days[] = {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 366};

        ddd = jul_date_parm % 1000;
        /* extract days */
        if (ddd < 1) {
            return (-2);
        }
        yy = jul_date_parm / 1000;
        /* extract year */
        cc = (yy / 100);
        /* extract century */
        // System.out.println("julgrgc cc=" +cc);
        if (cc == 0) {
            cc = (yy < 80) ? 20 : 19;
        } else {
            if (cc < 19) {
                /* Assume relative century */
                cc += 19;
            }
            /* endif */
        }
        /* endif */
        // System.out.println("julgrgc after cc=" +cc);
        yy %= 100;
        /* lose century */

 /* test for leap year */
        if ((yy == 0 && cc % 4 == 0) || (yy != 0 && yy % 4 == 0)) {
            if (ddd > 366) {
                return (-3);
            }
            if (ddd == 60) {
                j = 1; //FEB;
            } else {
                if (ddd > 60) {
                    ddd--;
                }
            }
        } else {
            if (ddd > 365) {
                return (-4);
            }
        }

        if (j != FEB) {
            for (j = 0; (ddd > julian_days[j + 1]); j++) {
            }
        }

        grgdate = (cc * 100L) + yy;
        /* put in year */
        grgdate += (((long) j + 1) * 10000L);
        /* put in month */
        grgdate += ((ddd - (long) julian_days[j]) * 1000000L);
        /* put in day */
        return (grgdate);
    }

    /* End of JULGRG */


    /**
     * ******************************************************************
     */
    /* EDITGRGC.C Routine to take a gregorian date of form ddmmyyyy in a */
 /* long integer, and convert it to a string of the form dd/mm/yyyy.  */
 /* EDITGRGC_YYYYMMDD supplies the return date as YYYYMMDD (i.e. no   */
 /* slashes                                                           */
 /* editgrgc_DDMMYYYY_insert_slashes takes a character input date and */
 /* put in the slahes to form DD/MM/YYYY.                             */
 /* The date is assumed to be valid.                                  */
 /* M.C.Betteridge 26th May 1994 (based on editgrg)                   */
 /* MCB 11OCT1996  Add EDITGRGC_YYYYMMDD                              */
 /* MCB 02NOV1996  Add EDITGRGC_DDMMYYYY_INSERT_SLASHES               */
    /**
     * @param grg_date_parm
     * @return ******************************************************************
     */
    public String editgrgc(long grg_date_parm) {
        String wk_date = null;
        StringBuffer editted_date = new StringBuffer(11);
        int j;

        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        nf.setMinimumIntegerDigits(8);
        nf.setGroupingUsed(false);
        wk_date = nf.format(grg_date_parm);

        editted_date.append(wk_date.charAt(0));
        editted_date.append(wk_date.charAt(1));
        editted_date.append(chDelim);
        editted_date.append(wk_date.charAt(2));
        editted_date.append(wk_date.charAt(3));
        editted_date.append(chDelim);
        editted_date.append(wk_date.charAt(4));
        editted_date.append(wk_date.charAt(5));
        editted_date.append(wk_date.charAt(6));
        editted_date.append(wk_date.charAt(7));

        return (editted_date.toString());
    }

    /* End of EDITGRGC */


    String editgrgc_YYYYMMDD(long grg_date_parm) {
        String wk_date = null;
        StringBuffer editted_date = new StringBuffer(11);
        int j;

        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        nf.setMinimumIntegerDigits(8);
        nf.setGroupingUsed(false);
        wk_date = nf.format(grg_date_parm);

        editted_date.append(wk_date.charAt(4));
        editted_date.append(wk_date.charAt(5));
        editted_date.append(wk_date.charAt(6));
        editted_date.append(wk_date.charAt(7));
        editted_date.append(chDelim);
        editted_date.append(wk_date.charAt(2));
        editted_date.append(wk_date.charAt(3));
        editted_date.append(chDelim);
        editted_date.append(wk_date.charAt(0));
        editted_date.append(wk_date.charAt(1));

        return (editted_date.toString());

    }

    /* End of EDITGRGC_YYYYMMDD */


    String editgrgc_DDMMYYYY_insert_slashes(String grg_date_parm) {
        StringBuffer editted_date = new StringBuffer(11);

        editted_date.append(grg_date_parm.charAt(0));
        editted_date.append(grg_date_parm.charAt(1));
        editted_date.append(chDelim);
        editted_date.append(grg_date_parm.charAt(2));
        editted_date.append(grg_date_parm.charAt(3));
        editted_date.append(chDelim);
        editted_date.append(grg_date_parm.charAt(4));
        editted_date.append(grg_date_parm.charAt(5));
        editted_date.append(grg_date_parm.charAt(6));
        editted_date.append(grg_date_parm.charAt(7));

        return (editted_date.toString());
    }

    /* End of EDITGRGC_DDMMYYYY */

    /**
     *
     * @return
     */
    public String get_DD_MM_YYYY() {
        StringBuffer editted_date = new StringBuffer(11);

        long yyddd = this.days2dte(days);

        //  System.out.println("V2 " +  yyddd +  "days" + days);
        return (this.editgrgc(this.julgrgc(yyddd)));

    }

    /* End of EDITGRGC_DDMMYYYY */

    /**
     *
     * @return
     */
    public String get_YYYY_MM_DD() {
        StringBuffer editted_date = new StringBuffer(11);

        long yyddd = this.days2dte(days);
        return (this.editgrgc_YYYYMMDD(this.julgrgc(yyddd)));

    }
    /* End of EDITGRGC_DDMMYYYY */

}
