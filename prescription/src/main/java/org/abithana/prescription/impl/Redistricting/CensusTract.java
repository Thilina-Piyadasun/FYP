package org.abithana.prescription.impl.Redistricting;

import com.vividsolutions.jts.geom.Polygon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * Created by malakaganga on 1/2/17.
 */
public class CensusTract implements Serializable {

    private long censusId;
    private long population;
    private double minLongitude;
    private double minLatitude;
    private double maxLongitude;
    private double maxLatitude;
    private double midLatitude;
    private double midLongitude;
    private double perimeter;

    private ArrayList<Double> polygonLatPoints = new ArrayList<Double>();
    private ArrayList<Double> polygonLonPoints = new ArrayList<Double>();
    private TreeSet<Double> sortedLatPoints = new TreeSet<Double>();
    private TreeSet<Double> sortedLongPoints = new TreeSet<Double>();
    private HashSet<Long> neighbourSet = new HashSet<Long>();

    public Polygon getBlockPolygon() {
        return blockPolygon;
    }

    public void setBlockPolygon(Polygon blockPolygon) {
        this.blockPolygon = blockPolygon;
    }

    private Polygon blockPolygon;

    public HashSet<Long> getNeighbourSet() {
        return neighbourSet;
    }

    public void setNeighbourSet(HashSet<Long> neighbourSet) {
        this.neighbourSet = neighbourSet;
    }

    public ArrayList<Double> getPolygonLatPoints() {
        return polygonLatPoints;
    }

    public void setPolygonLatPoints(ArrayList<Double> polygonLatPoints) {
        this.polygonLatPoints = polygonLatPoints;
        sortedLatPoints.addAll(polygonLatPoints);
    }

    public ArrayList<Double> getPolygonLonPoints() {
        return polygonLonPoints;
    }

    public void setPolygonLonPoints(ArrayList<Double> polygonLonPoints) {
        this.polygonLonPoints = polygonLonPoints;
        sortedLongPoints.addAll(polygonLonPoints);
    }

    public TreeSet<Double> getSortedLatPoints() {
        return sortedLatPoints;
    }

    public TreeSet<Double> getSortedLongPoints() {
        return sortedLongPoints;
    }

    public double getPerimeter() {
        return perimeter;
    }

    public void setPerimeter() {
        DistanceCalculator distanceCalculator = new DistanceCalculator();
        /*double perimeter = 0;
        for (int i = 1; i < polygonLatPoints.size(); i++) {
            perimeter += distanceCalculator.distance(polygonLatPoints.get(i - 1), polygonLonPoints.get(i - 1), polygonLatPoints.get(i),
                    polygonLonPoints.get(i), "K");
        }

*/
        double distanceOfSSide = distanceCalculator.distance(minLatitude, minLongitude, maxLatitude,
                minLongitude, "K");
        double distanceOfLSide = distanceCalculator.distance(minLatitude, minLongitude, minLatitude,
                maxLongitude, "K");

        double recPerimeter = (distanceOfSSide ) *  (distanceOfLSide);

        this.perimeter = recPerimeter;
    }

    public double getMidLatitude() {
        return midLatitude;
    }

    public void setMidLatitude(double midLatitude) {
        this.midLatitude = midLatitude;
    }

    public double getMidLongitude() {
        return midLongitude;
    }

    public void setMidLongitude(double midLongitude) {
        this.midLongitude = midLongitude;
    }

    public static Comparator<CensusTract> perimeterComparator = new Comparator<CensusTract>() {
        public int compare(CensusTract o1, CensusTract o2) {
                /*
                * to compare ascending order
                * */
            if ((o1.getPerimeter() < o2.getPerimeter())) {
                return -1;
            } else if ((o1.getPerimeter() == o2.getPerimeter())) {
                return 0;
            } else {
                return 1;
            }
        }
    };

    public static Comparator<CensusTract> perimeterComparatorBack = new Comparator<CensusTract>() {
        public int compare(CensusTract o1, CensusTract o2) {
                /*
                * to compare descending order
                * */
            if ((o1.getPerimeter() < o2.getPerimeter())) {
                return 1;
            } else if ((o1.getPerimeter() == o2.getPerimeter())) {
                return 0;
            } else {
                return -1;
            }
        }
    };


    /*
    *
    * Comparators to compare tracts according to Id, Latitudes and Longitudes in ascending and descending order
    *
    * */

    public static Comparator<CensusTract> populationComparator = new Comparator<CensusTract>() {
        public int compare(CensusTract o1, CensusTract o2) {
                /*
                * to compare ascending order
                * */
            return (int) (o1.getPopulation() - o2.getPopulation());
        }
    };

    public static Comparator<CensusTract> populationComparatorBack = new Comparator<CensusTract>() {
        public int compare(CensusTract o1, CensusTract o2) {
                /*
                * to compare descending order
                * */
            return (int) (o2.getPopulation() - o1.getPopulation());
        }
    };

    public static Comparator<CensusTract> latitudeComparator = new Comparator<CensusTract>() {
        public int compare(CensusTract o1, CensusTract o2) {
            if ((o1.getMinLatitude() < o2.getMinLatitude())) {
                return -1;
            } else if ((o1.getMinLatitude() == o2.getMinLatitude())) {
                return 0;
            } else {
                return 1;
            }
        }
    };
    public static Comparator<CensusTract> latitudeComparatorMax = new Comparator<CensusTract>() {
        public int compare(CensusTract o1, CensusTract o2) {
            if ((o1.getMaxLatitude() < o2.getMaxLatitude())) {
                return -1;
            } else if ((o1.getMaxLatitude() == o2.getMaxLatitude())) {
                return 0;
            } else {
                return 1;
            }
        }
    };
    public static Comparator<CensusTract> longitudeComparator = new Comparator<CensusTract>() {
        public int compare(CensusTract o1, CensusTract o2) {
            if ((o1.getMinLongitude() < o2.getMinLongitude())) {
                return -1;
            } else if ((o1.getMinLongitude() == o2.getMinLongitude())) {
                return 0;
            } else {
                return 1;
            }
        }
    };
    public static Comparator<CensusTract> longitudeComparatorMax = new Comparator<CensusTract>() {
        public int compare(CensusTract o1, CensusTract o2) {
            if ((o1.getMaxLongitude() < o2.getMaxLongitude())) {
                return -1;
            } else if ((o1.getMaxLongitude() == o2.getMaxLongitude())) {
                return 0;
            } else {
                return 1;
            }
        }
    };

    public long getCensusId() {
        return censusId;
    }

    public void setCensusId(long censusId) {
        this.censusId = censusId;
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

    public void setMinLongitude() {
       this.minLongitude = sortedLongPoints.first();
    }

    public double getMinLatitude() {
        return minLatitude;
    }

    public void setMinLatitude() {
        this.minLatitude = sortedLatPoints.first();
    }

    public double getMaxLongitude() {
        return maxLongitude;
    }

    public void setMaxLongitude() {
        this.maxLongitude = sortedLongPoints.last();
    }

    public double getMaxLatitude() {
        return maxLatitude;
    }

    public void setMaxLatitude() {
        this.maxLatitude = sortedLatPoints.last();
    }

}
