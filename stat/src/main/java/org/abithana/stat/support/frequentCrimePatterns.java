package org.abithana.stat.support;

import java.io.Serializable;
import java.lang.String;import java.util.List;

/**
 * Created by Thilina on 8/7/2016.
 */
public class frequentCrimePatterns implements Serializable{

    private List<String> Antecedents;
    private List<String> Consequent;
    private double conf;

    public frequentCrimePatterns(List<String> antecedents, List<String> consequent, double conf) {
        Antecedents = antecedents;
        Consequent = consequent;
        this.conf = conf;
    }

    public List<String> getAntecedents() {
        return Antecedents;
    }

    public void setAntecedents(List<String> antecedents) {
        Antecedents = antecedents;
    }

    public double getConf() {
        return conf;
    }

    public void setConf(double conf) {
        this.conf = conf;
    }

    public List<String> getConsequent() {
        return Consequent;
    }

    public void setConsequent(List<String> consequent) {
        Consequent = consequent;
    }
}
