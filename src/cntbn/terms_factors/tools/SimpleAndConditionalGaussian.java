package cntbn.terms_factors.tools;

import cntbn.terms_factors.SimpleGaussian;
import cntbn.terms_factors.TDGaussian;
import javolution.util.FastList;

import java.util.List;


public class SimpleAndConditionalGaussian {
    public TDGaussian clg;
    public List<SimpleGaussian> simpleGaussianList = new FastList<SimpleGaussian>();


    public void addClg(TDGaussian clg) {
        this.clg = clg;
    }

    public void addSimpleGaussian(SimpleGaussian sg) {
        simpleGaussianList.add(sg);
    }

    public TDGaussian getCLG() {
        return clg;
    }

    public List<SimpleGaussian> getSimpleGaussianList() {
        return simpleGaussianList;
    }

    public String toString() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append("Temporal Difference Gaussian :");
        strbuf.append(clg);
        strbuf.append("   Simple Gaussian:");
        strbuf.append(simpleGaussianList);
        return strbuf.toString();

    }

}