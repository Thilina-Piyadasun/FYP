package org.abithana.prescription.impl.Redistricting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

/**
 * Created by malakaganga on 1/2/17.
 */
public class Cluster implements Serializable {

    private long clusterId;
    private long x;

    private long h = 0;
    private double g = 0;
    private double f = 0;
    private long population = 0;

    private double minLongitude = 0;
    private double minLatitude = 0;
    private double maxLongitude = 0;
    private double maxLatitude = 0;
    private HashSet<Long> censusIds = new HashSet<Long>();
    public HashSet<CensusTract> cencusTracts = new HashSet<CensusTract>();

    public CensusTract bestPoly;

    public double getF() {
        return this.f;
    }

    public void setF(double f) {
        this.f = f;
    }

    public HashSet<Long> getCensusIds() {
        return censusIds;
    }

    public void setCensusIds(long censusId) {
        this.censusIds.add(censusId);
    }

    public static Comparator<Cluster> perimeterComparator = new Comparator<Cluster>() {
        public int compare(Cluster o1, Cluster o2) {
                /*
                * to compare ascending order
                * */
            if( (o1.getG() < o2.getG()) ) {
                return -1;
            } else if((o1.getG() == o2.getG()) ) {
                return 0;
            } else {
                return 1;
            }
        }
    };

    public static Comparator<Cluster> perimeterComparatorBack = new Comparator<Cluster>() {
        public int compare(Cluster o1, Cluster o2) {
                /*
                * to compare descending order
                * */
            if( (o1.getG() < o2.getG()) ) {
                return 1;
            } else if((o1.getG() == o2.getG()) ) {
                return 0;
            } else {
                return -1;
            }
        }
    };


    public static Comparator<Cluster> fComparator = new Comparator<Cluster>() {
        public int compare(Cluster o1, Cluster o2) {
                /*
                * to compare ascending order
                * */
            if( (o1.getF() < o2.getF()) ) {
                return -1;
            } else if((o1.getF() == o2.getF()) ) {
                return 0;
            } else {
                return 1;
            }
        }
    };

    public static Comparator<Cluster> fComparatorBack = new Comparator<Cluster>() {
        public int compare(Cluster o1, Cluster o2) {
                /*
                * to compare descending order
                * */
            if( (o1.getF() < o2.getF()) ) {
                return 1;
            } else if((o1.getF() == o2.getF()) ) {
                return 0;
            } else {
                return -1;
            }
        }
    };

    /*
    *
    * Comparators to compare clusters according to Id, Latitudes and Longitudes in ascending and descending order
    *
    * */

    public static Comparator<Cluster> populationComparator = new Comparator<Cluster>() {
        public int compare(Cluster o1, Cluster o2) {
                /*
                * to compare ascending order
                * */
            return (int) (o1.getPopulation() - o2.getPopulation());
        }
    };

    public static Comparator<Cluster> populationComparatorBack = new Comparator<Cluster>() {
        public int compare(Cluster o1, Cluster o2) {
                /*
                * to compare descending order
                * */
            return (int) (o2.getPopulation() - o1.getPopulation());
        }
    };

    public static Comparator<Cluster> latitudeComparator = new Comparator<Cluster>() {
        public int compare(Cluster o1, Cluster o2) {
            if ((o1.getMinLatitude() < o2.getMinLatitude())) {
                return -1;
            } else if ((o1.getMinLatitude() == o2.getMinLatitude())) {
                return 0;
            } else {
                return 1;
            }
        }
    };
    public static Comparator<Cluster> latitudeComparatorMax = new Comparator<Cluster>() {
        public int compare(Cluster o1, Cluster o2) {
            if ((o1.getMaxLatitude() < o2.getMaxLatitude())) {
                return -1;
            } else if ((o1.getMaxLatitude() == o2.getMaxLatitude())) {
                return 0;
            } else {
                return 1;
            }
        }
    };
    public static Comparator<Cluster> longitudeComparator = new Comparator<Cluster>() {
        public int compare(Cluster o1, Cluster o2) {
            if ((o1.getMinLongitude() < o2.getMinLongitude())) {
                return -1;
            } else if ((o1.getMinLongitude() == o2.getMinLongitude())) {
                return 0;
            } else {
                return 1;
            }
        }
    };
    public static Comparator<Cluster> longitudeComparatorMax = new Comparator<Cluster>() {
        public int compare(Cluster o1, Cluster o2) {
            if ((o1.getMaxLongitude() < o2.getMaxLongitude())) {
                return -1;
            } else if ((o1.getMaxLongitude() == o2.getMaxLongitude())) {
                return 0;
            } else {
                return 1;
            }
        }
    };

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public long getH() {
        return h;
    }

    public void setH(long h) {
        this.h = h;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public long getClusterId() {
        return clusterId;
    }

    public void setClusterId(long clusterId) {
        this.clusterId = clusterId;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public double getMinLongitude() {
        return minLongitude;
    }

    public void setMinLongitude(double minLongitude) {
        this.minLongitude = minLongitude;
    }

    public double getMinLatitude() {
        return minLatitude;
    }

    public void setMinLatitude(double minLatitude) {
        this.minLatitude = minLatitude;
    }

    public double getMaxLongitude() {
        return maxLongitude;
    }

    public void setMaxLongitude(double maxLongitude) {
        this.maxLongitude = maxLongitude;
    }

    public double getMaxLatitude() {
        return maxLatitude;
    }

    public void setMaxLatitude(double maxLatitude) {
        this.maxLatitude = maxLatitude;
    }

}
