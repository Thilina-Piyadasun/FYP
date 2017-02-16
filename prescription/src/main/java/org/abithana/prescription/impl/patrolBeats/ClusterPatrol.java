package org.abithana.prescription.impl.patrolBeats;
import org.abithana.prescription.beans.CensusBlock;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Created by malakaganga on 1/23/17.
 */
public class ClusterPatrol implements Serializable {
    private long clusterId;
    private HashSet<CensusBlock> cencusTracts = new HashSet<CensusBlock>();
    private HashSet<Long> censusIds = new HashSet<Long>();

    public HashSet<Long> getCensusIds() {
        return censusIds;
    }

    public void setCensusIds(long censusId) {
        this.censusIds.add(censusId);
    }

    public long getClusterId() {
        return clusterId;
    }

    public void setClusterId(long clusterId) {
        this.clusterId = clusterId;
    }

    public HashSet<CensusBlock> getCencusTracts() {
        return cencusTracts;
    }

    public void setCencusTracts(HashSet<CensusBlock> cencusTracts) {
        this.cencusTracts = cencusTracts;
    }
}
