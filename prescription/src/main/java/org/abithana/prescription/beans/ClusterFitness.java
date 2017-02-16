package org.abithana.prescription.beans;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by acer on 2/7/2017.
 */
public class ClusterFitness implements Serializable {

    private long blockId;
    private int fitness;

    public ClusterFitness(long blockId, int fitness) {
        this.blockId = blockId;
        this.fitness = fitness;
    }

    public long getBlockId() {
        return blockId;
    }

    public void setBlockId(long blockId) {
        this.blockId = blockId;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public static Comparator<ClusterFitness> fitnessComparator = new Comparator<ClusterFitness>() {
        public int compare(ClusterFitness o1, ClusterFitness o2) {
                /*
                * to compare descending order
                * */
            return (int) (o2.getFitness() - o1.getFitness());
        }
    };
}
