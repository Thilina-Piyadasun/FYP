package org.abithana.prescriptionBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Thilina on 1/5/2017.
 */
public class LeaderBean implements Serializable {

    private double lat;
    private double lon;
    private long LeaderBlock;
    private int leaderWork;
    private List<Long> followers=new ArrayList<>();
    private List<BlockCentroidBean> followerBeans=new ArrayList<>();

    public LeaderBean(double lat, double lon, long leaderBlock, int leaderWork) {
        this.lat = lat;
        this.lon = lon;
        LeaderBlock = leaderBlock;
        this.leaderWork = leaderWork;
    }



    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public long getLeaderBlock() {
        return LeaderBlock;
    }

    public void setLeaderBlock(int leaderBlock) {
        LeaderBlock = leaderBlock;
    }

    public int getLeaderWork() {
        return leaderWork;
    }

    public void setLeaderWork(int leaderWork) {
        this.leaderWork = leaderWork;
    }

    public void incrementLeaderWork(int followerWork) {
        leaderWork=leaderWork+followerWork;
    }
    public List<Long> getFollowers() {
        return followers;
    }

    public void addFollower(long follower) {
        followers.add(follower);
    }

    public List<BlockCentroidBean> getFollowerBeans() {
        return followerBeans;
    }

    public void addFollowerBean(BlockCentroidBean follower) {
        followerBeans.add(follower);
    }

    public static Comparator<LeaderBean> leaderWorkComparator = new Comparator<LeaderBean>() {
        public int compare(LeaderBean o1, LeaderBean o2) {
                /*
                * to compare ascending order
                * */
            return (int) (o1.getLeaderWork() - o2.getLeaderWork());
        }
    };
}
