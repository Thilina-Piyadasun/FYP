package org.abithana.prescription.impl.patrolBeats;

import akka.dispatch.Foreach;
import com.graphhopper.GraphHopper;
import org.abithana.prescriptionBeans.*;
import org.abithana.utill.Config;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * Created by Thilina on 1/4/2017.
 */
public class TractCentroid implements Serializable{

    private int toalWork=0;
    static GraphHopper hopper=Routing.getRoute();
    Routing routing=new Routing();

    HashMap<Long,List<LocationWeightBean>> locationWeightBeansHashMap =new HashMap<>();

    private Config instance=Config.getInstance();

    public List<BlockCentroidBean> getAllBlockCentroids(String tableName) {

        List<Row> list = instance.getSqlContext().sql("select blockID,categoryWeight,lat,lon from " + tableName).collectAsList();

        for(Row row:list){

            Long blockId = row.getAs("blockID");
            LocationWeightBean locationWeightBean = new LocationWeightBean(row.getAs("categoryWeight"), row.getAs("lat"), row.getAs("lon"));
            if(locationWeightBeansHashMap.get(blockId)==null){
                List<LocationWeightBean> locationWeightBeanList=new ArrayList<>();
                locationWeightBeanList.add(locationWeightBean);
                locationWeightBeansHashMap.put(blockId,locationWeightBeanList);
            }
            else
                locationWeightBeansHashMap.get(blockId).add(locationWeightBean);
        }

        System.out.println("========================================================");
        System.out.println( "CALCULATING BEST CRIME CENTRODIS OF INDIVIDUAL Blocks");
        System.out.println("========================================================");

        List<BlockCentroidBean> bestLocationsOfBlocks = new ArrayList<>();
        for(long blockID:locationWeightBeansHashMap.keySet()){
            bestLocationsOfBlocks.add(getBestLocation(blockID));
        }

        return bestLocationsOfBlocks;
    }


    public BlockCentroidBean getBestLocation(long blockID){

        List<LocationWeightBean> locations=locationWeightBeansHashMap.get(blockID);
        int size=locations.size();

        System.out.println("blockID :"+ blockID+" No of crime locations : "+size+ " .Calculating distance between each pair...");

        if(size==1){
            return new BlockCentroidBean(locations.get(0).getLat(),locations.get(0).getLon(),blockID,locations.get(0).getCategoryWeight());
        }
        else {
            int[][] weightMatrix=new int[size][size];
            IntStream.range(0, size).parallel().forEach(i ->
            //for(int i=0;i<size;i++)
            {
                for (int j = i; j < size; j++) {

                    if (i == j) {
                        weightMatrix[i][j] = 0;
                    } else {
                        double x1 = locations.get(i).getLat();
                        double y1 = locations.get(i).getLon();
                        double x2 = locations.get(j).getLat();
                        double y2 = locations.get(j).getLon();
                        int distance = 0;
                        if ((x1 != x2) || (y2 != y1)) {
                            distance = (int)calcRoadDistance(x1, y1, x2, y2)[0];
                        } else {
                            distance = 1;
                        }
                        weightMatrix[i][j] = distance * locations.get(j).getCategoryWeight();
                        weightMatrix[j][i] = distance * locations.get(i).getCategoryWeight();
                    }
                }
            });

            int minWork=Integer.MAX_VALUE;
            int bestIndex=0;

            //System.out.println("caculating best index for tractID" +tractID);
            for(int i=0;i<size;i++){

                int sum=0;
                for(int j=0;j<size;j++){
                    sum=sum+weightMatrix[i][j];
                }
                if(sum<minWork){
                    minWork=sum;
                    bestIndex=i;
                }
            }
            incrementTotalWork(minWork);
            return new BlockCentroidBean(locations.get(bestIndex).getLat(),locations.get(bestIndex).getLon(),blockID,minWork);
        }

    }

    public void incrementTotalWork(int val){
        toalWork=toalWork+val;
    }

    public int getToalWork() {
        return toalWork;
    }

    public long[] calcRoadDistance( Double latFrom, Double  lonFrom, Double latTo, Double lonTo ) {

        return routing.calc( hopper,latFrom, lonFrom, latTo, lonTo);
    }

}
