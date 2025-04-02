package MtechCommon;

/**
 *
 * @author mike_
 */
public class CalcHours {                         
    public int totalMins = 0;
    public boolean ExcelTime = false;
    public String calculate(String inStart, String inEnd,short breaks, boolean negative) {
        int startHour = Integer.parseInt(inStart.substring(0, 2));
        int startMins = Integer.parseInt(inStart.substring(3, 5));
        int endHour = Integer.parseInt(inEnd.substring(0, 2));
        int endMins = Integer.parseInt(inEnd.substring(3, 5));
;
        // Check whether end date is tomorrow
        if (inStart.compareTo(inEnd) > 0) {
            endHour += 24;
        } // end if

        // Convert to minutes
        int startInMins = (startHour * 60) + startMins;
        int endInMins = (endHour * 60) + endMins;

        // Subtract and format
        // totalMins is a global variable because Java won't let it be a parameter
        totalMins = endInMins - startInMins - breaks;
        int outHours = totalMins / 60;
        int outMins = totalMins % 60;

        String outFormatted;
        if (negative) {
            if (ExcelTime) {
                outFormatted = String.format("TIME(%02d,%02d,0))", outHours, outMins);
            } else {
                outFormatted = String.format("(%02d:%02d)", outHours, outMins);
            } // end ig
            totalMins *= -1;
        } else {
            if (ExcelTime) {
                outFormatted = String.format("TIME(%2d,%2d,0)", outHours, outMins);
            } else {
                outFormatted = String.format(" %02d:%02d ", outHours, outMins);
            } // end  if
        } // end  if

        return (outFormatted);

    } // end calcHours
}
