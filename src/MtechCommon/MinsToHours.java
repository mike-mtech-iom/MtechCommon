package MtechCommon;

  public class MinsToHours{
       public String convert(long inMins) {

        long outHours = inMins / 60;
        long outMins = inMins % 60;

        String outFormatted = String.format("%02d:%02d", outHours, outMins);
        return (outFormatted);

    } // end convert
} //end class