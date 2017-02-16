package org.abithana.prescription.beans;

import java.util.Comparator;

/**
 * Created by acer on 2/8/2017.
 */
public class DistanceBean {

    private long blockId;
    private int distance;

    public DistanceBean(long blockId, int distance) {
        this.blockId = blockId;
        this.distance = distance;
    }

    public long getBlockId() {
        return blockId;
    }

    public void setBlockId(long blockId) {
        this.blockId = blockId;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public static Comparator<DistanceBean> distanceComparator = new Comparator<DistanceBean>() {
        public int compare(DistanceBean o1, DistanceBean o2) {
                /*
                * to compare descending order
                * */
            return (int) (o1.getDistance() - o2.getDistance());
        }
    };
}
