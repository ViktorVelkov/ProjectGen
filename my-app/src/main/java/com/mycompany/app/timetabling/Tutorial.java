package com.mycompany.app.timetabling;

public class Tutorial {

        private String sName;
        private int iSmallOrRargeGT;
        private int iDuration;
        private String sLocation;
        private int iTimeStart;
        private int iDate;
        private int iMonth;
        private int iYear;

        public Tutorial(){}

        public Tutorial(String sName, int iDuration, String sLocation, int iTimeStart, int iDate, int iMonth, int iYear) {
            this.sName = sName;
            this.iDuration = iDuration;
            this.sLocation = sLocation;
            this.iTimeStart = iTimeStart;
            this.iDate = iDate;
            this.iMonth = iMonth;
            this.iYear = iYear;
        }

        public String getsName() {
            return sName;
        }

        public void setsName(String sName) {
            this.sName = sName;
        }

        public int getiDuration() {
            return iDuration;
        }

        public int getiSmallOrRargeGT() {
            return iSmallOrRargeGT;
        }

        public void setiSmallOrRargeGT(int iSmallOrRargeGT) {
            this.iSmallOrRargeGT = iSmallOrRargeGT;
        }

    public void setiDuration(int iDuration) {
            this.iDuration = iDuration;
        }

        public String getsLocation() {
            return sLocation;
        }

        public void setsLocation(String sLocation) {
            this.sLocation = sLocation;
        }

        public int getiTimeStart() {
            return iTimeStart;
        }

        public void setiTimeStart(int iTimeStart) {
            this.iTimeStart = iTimeStart;
        }

        public int getiDate() {
            return iDate;
        }

        public void setiDate(int iDate) {
            this.iDate = iDate;
        }

        public int getiMonth() {
            return iMonth;
        }

        public void setiMonth(int iMonth) {
            this.iMonth = iMonth;
        }

        public int getiYear() {
            return iYear;
        }

        public void setiYear(int iYear) {
            this.iYear = iYear;
        }




        @Override
        public String toString() {
            return "Tutorial{" +
                    "sName='" + sName + '\'' +
                    ", iDuration=" + iDuration +
                    ", sLocation='" + sLocation + '\'' +
                    ", iTimeStart=" + iTimeStart +
                    ", iDate=" + iDate +
                    ", iMonth=" + iMonth +
                    ", iYear=" + iYear +
                    '}';
        }


}


