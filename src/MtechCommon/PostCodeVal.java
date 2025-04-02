package MtechCommon;





/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.regex.*;

/**
 *
 * @author Mike
 */
public class PostCodeVal {

    Pattern alpha = Pattern.compile("[A-Z]");
    Pattern num = Pattern.compile("[1-9]");

    public void PostCodeVal() {
    }   // end constructor

    public boolean Validate(String InPostCode) {

        Matcher to1 = alpha.matcher(InPostCode);
        String mask1 = to1.replaceAll("A");
        Matcher to2 = num.matcher(mask1);
        String mask = to2.replaceAll("9");

        
        if ((mask.equals("AA9 9AA")
                || (mask.equals("AA99 9AA"))
                || (mask.equals("A9 9AA"))
                || (mask.equals("A99 9AA"))
                || (mask.equals("AA9A 9AA")))) {
            return true;
        } // end if
        return false;
    } // end Validate method
}
