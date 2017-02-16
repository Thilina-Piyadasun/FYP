package org.abithana.prescription.impl.patrolBeats;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import io.netty.util.internal.ConcurrentSet;
import org.abithana.prescription.beans.CensusBlock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by malakaganga on 1/23/17.
 */
public class Checker implements Serializable{

    private ArrayList<CensusBlock> censusTracts = new ArrayList<CensusBlock>(); // to hold census tracts

    public HashMap<Long, CensusBlock> getCensusMap() {
        return censusMap;
    }

    public void setCensusMap(HashMap<Long, CensusBlock> censusMap) {
        this.censusMap = censusMap;
    }

    private HashMap<Long, CensusBlock> censusMap = new HashMap<Long, CensusBlock>();


    public Checker() {
        extractDataFromBoundry();
    }

    public double getArea(List<Long> idList) {
        double area = 0;
        for (long id : idList) {
            CensusBlock mainBlock = censusMap.get((Long) id);
            area += mainBlock.getBlockPolygon().getArea();
        }
        return area * 100 * 98;
    }

    public long polygonChecker(double[] point) {


        long polygonID = 0;
        double pointLong = point[0];
        double pointLat = point[1];

        for (CensusBlock censusTract : censusTracts) {

            int count = 0;
            ArrayList<Double> latPoints = censusTract.getPolygonLatPoints();
            ArrayList<Double> longPoints = censusTract.getPolygonLonPoints();

            for (int i = 0; i < latPoints.size(); i++) {
                int j = (i + 1) % latPoints.size();
                double latUp = latPoints.get(i);
                double longUp = longPoints.get(i);
                double latDown = latPoints.get(j);
                double longDown = longPoints.get(j);

                if (latUp == latDown) {
                    continue;
                } else {
                    double upPoint = (latUp > latDown)? latUp : latDown;
                    double lowPoint = (latDown < latUp)? latDown : latUp;
                    if (pointLat > upPoint || pointLat < lowPoint) {
                        continue;
                    }
                    if (pointLong <= breakLong(latUp,longUp,latDown,longDown,pointLat)) {
                        count ++;
                    }
                }

            }
            if ((count % 2) == 1 ) {
                return censusTract.getCensusId();
            }


        }

        return polygonID;
    }

    public HashMap<Long, ClusterPatrol> convertToCluster(HashMap<Integer,ArrayList<Long>> idMap) {

        long i = 0;
        HashMap<Long, ClusterPatrol> finishedClusters = new HashMap<Long, ClusterPatrol>();

        for (ArrayList<Long> idList : idMap.values()) {
            long clusterId = i;
            ClusterPatrol clusterPatrol = new ClusterPatrol();
            clusterPatrol.setClusterId(clusterId);
            i++;
            for (Long cenId : idList) {
                clusterPatrol.setCensusIds(cenId);
            }
            finishedClusters.put(clusterId, clusterPatrol);

        }


        for (ClusterPatrol cl : finishedClusters.values()) {
            for (long id : cl.getCensusIds()) {
                cl.getCencusTracts().add(censusMap.get(id));
            }
        }


        return finishedClusters;


    }
    public List<Long> getNeighbours(long id) {
        List<Long> neighbours = new ArrayList<>();
        CensusBlock mainBlock = censusMap.get((Long) id);
        for (CensusBlock cb : censusMap.values()) {
            if( mainBlock.getBlockPolygon().touches(cb.getBlockPolygon())) {
                neighbours.add(cb.getCensusId());
            }
        }
        return neighbours;
    }
    private double breakLong(double latUp, double longUp, double latDown, double longDown, double pointLat) {
        double breakLong = longUp - ((latUp - pointLat)*(longUp - longDown))/(latUp - latDown);
        return breakLong;
    }

    private void extractDataFromBoundry() {

        CsvReader csvReader = new CsvReader(); // to read the Census_2010_Tracts.csv file
        Integer[] wantedColumns = {0, 5, 13, 14}; // indicated wanted columns to read
        List<String[]> allRows = csvReader.readCsv("./tl_2010_06075_tabblock10.csv",
                wantedColumns);


        for (int i = 1; i < allRows.size(); i++) {

            String[] columns = allRows.get(i);
            //Define two array lists to keep latitudes and longitudes of each boundary point
            ArrayList<Double> latitudes = new ArrayList<Double>();
            ArrayList<Double> longitudes = new ArrayList<Double>();

            // to keep id of cencustract
            long id = 0;

            //New census tract object
            CensusBlock censusTract = new CensusBlock();

            //To keep mid points
            double midLong = 0, midLat = 0;

            for (int j = 0; j < columns.length; j++) {

                if (j == 0) { // Boundary points column

                    int x = columns[j].indexOf(")");
                    String points = columns[j].substring(16, x); // get the string with lats
                    // and longs

                    String[] individualPoints = points.split(", "); // get individual points into a string array

                    for (int k = 0; k < individualPoints.length; k++) {

                        String[] latNlong = individualPoints[k].split(" ");
                        longitudes.add(Double.parseDouble(latNlong[0]));
                        latitudes.add(Double.parseDouble(latNlong[1]));

                    }
                } else if (j >= columns.length - 2) {
                    double point = Double.parseDouble(columns[j]);
                    if (point < 0) {
                        midLong = point;
                    } else {
                        midLat = point;
                    }
                } else {
                    id = Long.parseLong(columns[j]);
                }

            }
            //Add details to the created census tract
            censusTract.setCensusId(id);
            censusTract.setPolygonLonPoints(longitudes);
            censusTract.setPolygonLatPoints(latitudes);
            censusTract.setMidLongitude(midLong);
            censusTract.setMidLatitude(midLat);
            GeometryFactory fact = new GeometryFactory();
            Coordinate cor;
            Coordinate[] coordinates = new Coordinate[longitudes.size()];
            for(int j = 0; j < longitudes.size(); j++) {
                cor = new Coordinate(longitudes.get(j), latitudes.get(j));
                coordinates[j] = cor;
            }
            LinearRing linear = new GeometryFactory().createLinearRing(coordinates);
            Polygon poly = new Polygon(linear, null, fact);

            //add created polygon into block
            censusTract.setBlockPolygon(poly);

            //add census tract to list of tracts and map of tracts

            censusTracts.add(censusTract);
            censusMap.put(id, censusTract);


        }
    }
}
