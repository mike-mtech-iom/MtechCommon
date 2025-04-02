package MtechCommon;

/**
 * Used to format and parse integers and floats as decimal numbers
 *
 * @author Mike Betteridge
 */
public class MtechFormat {
    // C++ routines for NUMEDIT object converted to java
// MCB 4th February 2000
// Changes
// ~~~~~~~
// MCB 2nd March 2001 - Allow for currency
// MCB 20th August 2001 - Correctly allow for negatives

    char thousands_sep;
    char decimal_point;
    String currency_sign;
    int min_len;
    int wk_len;
    StringBuffer editted_str = new StringBuffer(26);
    StringBuffer wk_area = new StringBuffer(24);
    Boolean currency_required = false;

    int decplaces = 0;
    int edit_val;

    /**
     * Constructor set default parameters
     */
    public MtechFormat() {
        thousands_sep = ',';
        decimal_point = '.';
        currency_sign = new String("£");

        min_len = -1;
        //                              012345678901234567890123
        editted_str = new StringBuffer("000000000000000000000000");
        wk_area = new StringBuffer(24);
    } // end MtechFormat constructor

    /**
     * Edits the value to a string with optional currency, no leading zeroes,
     * thousands separators, decimal point and fixed number of decimal places.
     *
     * @return Formatted string
     */
    String do_edit() {
        char wkchar;
        int j = 23;
        for (int y = 0; y < 24; y++) {
            editted_str.setCharAt(y, '0');
        } // end for

        int rem;
        int len = 0;

        int decplace = 23 - decplaces;
        boolean negative;
        if (edit_val < 0) {
            negative = true;
            edit_val *= -1;
        } else {
            negative = false;
        } // end if
        int thousand_mark = 0;

        while (edit_val != 0 || len < min_len) {
            if (j == decplace) {
                editted_str.setCharAt(j, this.decimal_point);
                j--;
                len++;
                thousand_mark = 1;
            } // end if
            rem = edit_val % 10;
            wkchar = (char) (rem + '0');
            editted_str.setCharAt(j, wkchar);
            len++;
            j--;
            edit_val /= 10;

            if (thousand_mark > 0 && ++thousand_mark == 4) {
                // don't want leading commas
                if (edit_val > 0) {
                    editted_str.setCharAt(j, this.thousands_sep);
                    j--;
                    len++;
                    thousand_mark = 1;
                } // end if
            } // end if

        } // end while
        if (negative) {
            editted_str.setCharAt(j--, '-');
        } // end if
        j++;
        return editted_str.substring(j).toString();
    } // end do_edit

    /**
     * float version
     *
     * @param numedit_val
     * @return Formatted string
     *
     */
    public String edit(float numedit_val) {
        currency_required = false;
        wk_area = new StringBuffer(Float.toString(numedit_val));
        return (do_edit());
    }

    /**
     * long integer version
     *
     * @param numedit_val
     * @return Formatted string
     *
     */
    public String edit(long numedit_val) {
        currency_required = false;
        wk_area = new StringBuffer(Long.toString(numedit_val));
        return (do_edit());
    }

    /**
     * float version with currency sign
     *
     * @param numedit_val
     * @return Formatted string
     *
     */
    public String edit_curr(float numedit_val) {
        currency_required = false;
        wk_area = new StringBuffer(Float.toString(numedit_val));
        return (do_edit());
    }

    /**
     * integer version, with decimal places specified
     *
     * @param numedit_val integer to be editted
     * @param dec number of decimal places
     * @return Formatted string
     *
     */
    public String edit(int numedit_val, int dec) {
        this.decplaces = dec;
        this.edit_val = numedit_val;
        currency_required = false;
        min_len = dec + 2;
        return (do_edit());
    }

    /*
     char *numedit::edit_curr(long &numedit_val) {
     currency_required = 1;

     wk_len = sprintf (wk_area,"%lu.%02lu",numedit_val/100,numedit_val%100);
     return(do_edit());
     }

     char *numedit::edit_curr(long &numedit_val, bool in_currency_required) {
     currency_required = in_currency_required;

     long digits = numedit_val / 100;
     long decs = abs(numedit_val % 100);

     wk_len = sprintf (wk_area,"%ld.%02ld",digits,decs);
     return(do_edit());
     }
     */
    /**
     *
     * @param pval String to parse
     * @param dec_places Number of decimal places
     * @return integer value
     * @throws BadNumberException
     */
    public int parse_to_int(String pval, int dec_places) throws BadNumberException {
        int outval = 0;
        int j;
        int k = dec_places + 1;
        int m = 0;
        int decimalAt = -99;
        int y = 1;
        boolean decimalfnd = false;
        boolean negative = false;
        String inval = pval.trim();
        int inval_len = inval.length();

        for (j = inval_len - 1; j >= 0; j--) {
            m++;
            char wk = inval.charAt(j);
            if (wk == this.decimal_point) {

                if ((m > k) || (decimalAt != -99)) {
                    throw new BadNumberException("Invalid decimal");
                } // end if

                decimalAt = m;
                decimalfnd = true;

            } else if (wk == this.thousands_sep) {
                // ignore it
            } else if (wk == '-') {
                negative = true;
            } else if (wk == '+') {
                negative = false;
            } else if (wk < '0' || wk > '9') {
                throw new BadNumberException("Invalid digit");
            } else {
                int n = wk - '0';
                outval += n * y;
                y *= 10;
            } // end if
        } // end for
        if (decimalAt < dec_places + 1) {
            if (decimalAt == -99) {
                outval *= (Math.pow(10, dec_places));
            } else {
                outval *= (Math.pow(10, (dec_places - decimalAt + 1)));
            } // end if
        } // end if

        if (negative) {
            outval *= -1;
        } // end if
        return (outval);

    }

    /**
     *
     */
    public static class BadNumberException extends Exception {

        String mymsg;

        /**
         *
         * @param msg
         */
        public BadNumberException(String msg) {
            mymsg = msg;
        }

        /**
         *
         * @return
         */
        public String getMessage() {
            return mymsg;
        }
    }
}
