package MtechCommon;

import MtechCommon.MtechDate;

public class WeekNo {

    MtechDate mdBase = null;
    MtechDate mdIn = null;
    MtechDate md = null;

    public WeekNo() {

        mdBase = new MtechDate();
        mdIn = new MtechDate();
        md = new MtechDate();
        mdBase.dte2days(10, "20231002");
    } // end constructor 

    public short GetWeekNo(long daynum) {
        mdIn.setDays(daynum);
        md.setDays(mdIn.finddate(1, '<'));  // find preceding Monday
        long weeks = (md.days - mdBase.days) / 7;
        short weekNo = (short)((weeks % 4) + 1);
        return weekNo;                                                                                                                               
    } // end GetWeekNo

} // end class
