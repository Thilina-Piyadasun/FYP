package org.abithana.prescription.impl.patrolBeats;

import com.graphhopper.GraphHopper;
import org.abithana.prescriptionBeans.BlockCentroidBean;
import org.abithana.prescriptionBeans.LeaderBean;
import org.abithana.utill.Config;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import java.io.Serializable;
import java.util.*;

/**
 * Created by Thilina on 1/5/2017.
 */
public class PatrolBoundry3 implements Serializable {
/*

    private final double PI = Math.PI;
    double minDistanceApart=0.0;
    private int threashold;
    private int toalWork;
    int boundries;
    private String lat;
    private String lon;
    private Config instance=Config.getInstance();
    private List<LeaderBean> leaderList=new ArrayList<>();
    private List<BlockCentroidBean> follwers=new ArrayList<>();
    static GraphHopper hopper=Routing.getRoute();
    Routing routing=new Routing();

    HashMap<Long,Set<Long>> AllLeaderNeighbours=new HashMap<Long,Set<Long>>();
    Checker checker=new Checker();

    public List<LeaderBean> getLearders(List<BlockCentroidBean> list,int noOfPatrols){

        String tableName="blockCentroidTable";
        DataFrame dataFrame=instance.getSqlContext().createDataFrame(list,BlockCentroidBean.class);
        dataFrame.registerTempTable(tableName);
        double minLat=getMaxMinLatLonValues("min", "lat", tableName);
        double maxLat=getMaxMinLatLonValues("max", "lat", tableName);
        double minLon=getMaxMinLatLonValues("min", "lon", tableName);
        double maxLon=getMaxMinLatLonValues("max", "lon", tableName);


        double d1=  getMaxHorizontalDistance(maxLat,minLat,maxLon,minLon);
        double d2 = getMaxVerticalDistance(maxLat,minLat,maxLon,minLon);

        double recArea = d1 * d2;

        minDistanceApart= Math.sqrt(recArea/(noOfPatrols * PI));
        instance.getSqlContext().sql("select * from "+tableName+ " order by work desc").show(50);
        List<Row> workOrderList=instance.getSqlContext().sql("select * from "+tableName+ " order by work desc").collectAsList();

        for(Row row:workOrderList){
            if(leaderList.size()<noOfPatrols) {
                if (isMinDistanceApart(row.getAs("lat"), row.getAs("lon"))) {
                    LeaderBean leaderBean=new LeaderBean(row.getAs("lat"), row.getAs("lon"),row.getAs("blockID"),row.getAs("work"));
                    leaderList.add(leaderBean);
                }
                else {
                    BlockCentroidBean centroidBean=new BlockCentroidBean(row.getAs("lat"), row.getAs("lon"),row.getAs("blockID"),row.getAs("work"));
                    follwers.add(centroidBean);
                }
            }
            else {
                BlockCentroidBean centroidBean=new BlockCentroidBean(row.getAs("lat"), row.getAs("lon"),row.getAs("blockID"),row.getAs("work"));
                follwers.add(centroidBean);
            }
        }

        System.out.println("=================Intial Leader list SIZE IS =====================");
        System.out.println(leaderList.size());

        while (leaderList.size()<noOfPatrols){
            BlockCentroidBean bean=follwers.remove(0);
            LeaderBean lb=new LeaderBean(bean.getLat(),bean.getLon(),bean.getBlockID(),bean.getWork());
            leaderList.add(lb);
            System.out.println("add bean to leader list.New Size is "+leaderList.size() );
        }
        System.out.println("=================Follwer list SIZE IS =====================");
        System.out.println(follwers.size());

        instance.getSqlContext().dropTempTable(tableName);
        return leaderList;

    }
    public boolean isMinDistanceApart(double lat,double lon){
        if(leaderList.isEmpty()){
            return true;
        }
        else{
            for(LeaderBean leaderBean:leaderList){
                double distance=distanceInMeters(leaderBean.getLat(),lat,leaderBean.getLon(),lon);
                if(distance<minDistanceApart){
                    return false; //if one point is close by not consider it as a leader
                }
            }
        }
        return true;
    }

    private void seperateFolowers(List<BlockCentroidBean> list){

        System.out.println("=========================Before remove==============");
        System.out.println( list.size());

        for(LeaderBean lb:leaderList){
            for(BlockCentroidBean tb:list){
                if(lb.getLeaderBlock()==tb.getBlockID()){
                    list.remove(tb);
                    break;
                }
            }
        }
        System.out.println("=========================After remove==============");
        System.out.println( list.size());

        System.out.println("=========================Leaderlist Size==============");
        System.out.println( leaderList.size());

        for(LeaderBean l:leaderList){
            System.out.print(l.getLeaderBlock() + "  ");
        }
        System.out.println();
        follwers=list;
    }

    public double getMaxMinLatLonValues(String minorMax,String latOrLong,String tableName){

        String minMaxLatLong=minorMax+"("+latOrLong+")";
        instance.getSqlContext().sql("select "+minMaxLatLong+" from "+tableName).show(20);
        Row[] row=instance.getSqlContext().sql("select "+minMaxLatLong+" from "+tableName).collect();
        double d=0;
        try {
            d=row[0].getDouble(0);
        }catch (Exception e){
            e.printStackTrace();
        }
        return d;
    }

    public double getMaxHorizontalDistance(double maxLat,double minLat,double maxLong,double minLong){

        double val1=distanceInMeters(maxLat,maxLat,maxLong,minLong);
        double val2=distanceInMeters(minLat,minLat,maxLong,minLong);
        if(val1>=val2)
            return val1;
        else
            return val2;
    }

    public double getMaxVerticalDistance(double maxLat,double minLat,double maxLong,double minLong){

        double val1=distanceInMeters(maxLat,minLat,maxLong,maxLong);
        double val2=distanceInMeters(maxLat,minLat,minLong,minLong);
        if(val1>=val2)
            return val1;
        else
            return val2;
    }

    private double distanceInMeters(double lat1, double lat2, double lon1,
                                    double lon2) {

        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        //return Math.sqrt(distance);
        return distance;
    }

    public void calcThreashold(int Totalwork,int n){
        //TODO method implement
        toalWork=Totalwork;
        boundries=n;
        System.out.println("===============================");
        System.out.println("Total Work "+ Totalwork);
        threashold=Totalwork/n;
        System.out.println("threasholdl Work "+ threashold);
    }

    public int getcalcThreashold(){
        return toalWork/boundries;
    }

    public void addWork(int work){
        this.toalWork=toalWork+work;
    }

    public void findPatrolBoundries(){

        System.out.println("========================================================");
        System.out.println( "AGGREGATING Blocks TOGETHER");
        System.out.println("========================================================");

        for (int i = 0; i < leaderList.size(); i++) {
            Set<Long> foll=new HashSet<>();
            foll= collectAllNeighbours(leaderList.get(i));
            */
/*for(long l:leaderList.get(i).getFollowers()){
                foll.add(l);
            }*//*

            AllLeaderNeighbours.put(leaderList.get(i).getLeaderBlock(),foll);
        }

        while(follwers.size()>=1) {
            for (int j = follwers.size() - 1; j >= 0; j--) {
                BlockCentroidBean centroidBean = follwers.get(j);

                int min = Integer.MAX_VALUE;
                int leader = -1;

                for (int i = 0; i < leaderList.size(); i++) {

                    LeaderBean leaderBean = leaderList.get(i);
               //  Set<Long> neighbours = collectAllNeighbours(leaderBean);

                    //do not calculate list everytime keep it as hash set.
                    if (AllLeaderNeighbours.get(leaderBean.getLeaderBlock()).contains(centroidBean.getBlockID())) {

                        int distance = calcRoadDistance(leaderBean.getLat(), leaderBean.getLon(), centroidBean.getLat(), centroidBean.getLon());

                      //  if(leaderBean.getLeaderWork() < 150*getcalcThreashold()/100) {
                            if (distance < min) {
                                leader = i;
                                min = distance;
                            }
                      //  }

                    }
                }
                if (leader == -1) {
                    System.out.println("*******************************************************************" + centroidBean.getBlockID());
                }
                if (leader != -1) {
                    leaderList.get(leader).addFollower(centroidBean.getBlockID());
                    leaderList.get(leader).incrementLeaderWork(centroidBean.getWork() + min);
                   //newly
                    addWork(min);
                    AllLeaderNeighbours.put((long)leader, getNeighbours(centroidBean.getBlockID()));
                    follwers.remove(centroidBean);
                }

            }
        }

        for(LeaderBean leaderBean:leaderList){
            System.out.println("================Leader "+leaderBean.getLeaderBlock()+" Total leader work : "+leaderBean.getLeaderWork()+"=================");
            for(long i:leaderBean.getFollowers()){
                System.out.print(i + "  ");
            }
            System.out.println();
        }
    }

    public HashMap<Integer,ArrayList<Long>> getBoundryTractids()
    {
        HashMap<Integer,ArrayList<Long>> allset=new HashMap<>();
        try{
            int i=0;
            for(LeaderBean leaderBean:leaderList){
                ArrayList<Long> tractidList=new ArrayList<>();
                if(leaderBean.getFollowers().size()!=0)
                    tractidList.addAll(leaderBean.getFollowers()) ;
                tractidList.add(leaderBean.getLeaderBlock());
                allset.put(i,tractidList);
                i++;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return allset;
    }
    */
/*
    * Check isNeighbour
    * *//*

    public boolean isNeighbour(LeaderBean leaderBean,long tractID){

        return leaderBean.getFollowers().contains(tractID);
    }
    public int calcRoadDistance(Double latFrom, Double  lonFrom, Double latTo, Double lonTo){
        return routing.calc( hopper,latFrom, lonFrom, latTo, lonTo);
    }
*/
/*
* COllect all neighbours of current cluster lead by parameter leaderBean
* *//*

    public Set<Long> collectAllNeighbours(LeaderBean leaderBean){

        Set<Long> allNeighbours=new HashSet<>();

        allNeighbours.addAll(getNeighbours(leaderBean.getLeaderBlock()));

        if(leaderBean.getFollowers().size()>0) {
            for (long i : leaderBean.getFollowers()) {
                allNeighbours.addAll(getNeighbours(i));
            }
        }
        return allNeighbours;

    }

    public Set<Long> getNeighbours(long tractID){

       return checker.getNeighbours(tractID);
    }

*/

}
