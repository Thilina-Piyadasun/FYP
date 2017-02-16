package org.abithana.prescription.impl.Redistricting;

import java.util.Comparator;

/**
 * Created by malakaganga on 2/15/17.
 */
public class CensusWithF {

    private double f;

    public CensusTract getCensus() {
        return census;
    }

    public void setCensus(CensusTract census) {
        this.census = census;
    }

    private CensusTract census;

    public double getF() {
        return this.f;
    }

    public void setF(double f) {
        this.f = f;
    }

    public static Comparator<CensusWithF> fComparator = new Comparator<CensusWithF>() {
        public int compare(CensusWithF o1, CensusWithF o2) {
                /*
                * to compare ascending order
                * */
            if ((o1.getF() < o2.getF())) {
                return -1;
            } else if ((o1.getF() == o2.getF())) {
                return 0;
            } else {
                return 1;
            }
        }
    };

    public static Comparator<CensusWithF> fComparatorBack = new Comparator<CensusWithF>() {
        public int compare(CensusWithF o1, CensusWithF o2) {
                /*
                * to compare descending order
                * */
            if ((o1.getF() < o2.getF())) {
                return 1;
            } else if ((o1.getF() == o2.getF())) {
                return 0;
            } else {
                return -1;
            }
        }
    };

}
