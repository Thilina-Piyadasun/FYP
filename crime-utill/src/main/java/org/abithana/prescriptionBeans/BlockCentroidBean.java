package org.abithana.prescriptionBeans;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Thilina on 1/4/2017.
 */
public class BlockCentroidBean implements Serializable {

    private double lat;
    private double lon;
    private long blockID;
    private int work;

    public BlockCentroidBean(double lat, double lon, long blockID) {
        this.lat = lat;
        this.lon = lon;
        this.blockID = blockID;
    }

    public BlockCentroidBean(double lat, double lon, long blockID, int work) {
        this.lat = lat;
        this.lon = lon;
        this.blockID = blockID;
        this.work = work;
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

    public long getBlockID() {
        return blockID;
    }

    public void setBlockID(long blockID) {
        this.blockID = blockID;
    }

    public int getWork() {
        return work;
    }

    public void setWork(int work) {
        this.work = work;
    }

    public static Comparator<BlockCentroidBean> workComparator = new Comparator<BlockCentroidBean>() {
        public int compare(BlockCentroidBean o1, BlockCentroidBean o2) {
                /*
                * to compare ascending order
                * */
            return (int) (o2.getWork() - o1.getWork());
        }
    };



    public static Comparator<BlockCentroidBean> latComparator = new Comparator<BlockCentroidBean>() {
        public int compare(BlockCentroidBean o1, BlockCentroidBean o2) {
                /*
                * to compare ascending order
                * */
            if ((o1.getLat() < o2.getLat())) {
                return -1;
            } else if ((o1.getLat() == o2.getLat())) {
                return 0;
            } else {
                return 1;
            }
        }
    };

    public static Comparator<BlockCentroidBean> lonComparator = new Comparator<BlockCentroidBean>() {
        public int compare(BlockCentroidBean o1, BlockCentroidBean o2) {
            if ((o1.getLon() < o2.getLon())) {
                return -1;
            } else if ((o1.getLon() == o2.getLon())) {
                return 0;
            } else {
                return 1;
            }
        }
    };
}
