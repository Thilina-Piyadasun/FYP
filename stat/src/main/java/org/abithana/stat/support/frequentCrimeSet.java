package org.abithana.stat.support;

import java.io.Serializable;
import java.lang.String;import java.util.List;

/**
 * Created by Thilina on 8/6/2016.
 */
public class frequentCrimeSet implements Serializable {

    private List<String> frequentCrimePatterns;
    private long frequency;

    public frequentCrimeSet(List<String> frequentCrimePatterns, long frequency) {
        this.frequentCrimePatterns = frequentCrimePatterns;
        this.frequency = frequency;
    }

    public List<String> getFrequentCrimePatterns() {
        return frequentCrimePatterns;
    }

    public void setFrequentCrimePatterns(List<String> frequentCrimePatterns) {
        this.frequentCrimePatterns = frequentCrimePatterns;
    }

    public long getFrequency() {
        return frequency;
    }

    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }
}
