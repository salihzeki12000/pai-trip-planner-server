package cntbn.terms_factors.tools;

import cntbn.common.NodeDictionary;
import cntbn.exception.NoSuchVariableException;
import cntbn.terms_factors.*;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Convert a mixture factor with one elements to a simple factor
 */
public class CustomizeFactor {
    public static ContinuousFactor customize(ContinuousFactor factor) {
        // 만약 SimpleTerm이면 할거없음
        if (factor instanceof SimpleFactor) {
            return factor;
        } else if (factor instanceof SimpleMixtureFactor) {
            SimpleMixtureFactor simpleMixtureFactor = (SimpleMixtureFactor) factor;
            List<SimpleTerm> termList = simpleMixtureFactor
                    .getMixtureOfSimpleTerm();
            if (termList.size() == 0) {
                throw new RuntimeException("EmptyFactor!!!");
            } else if (termList.size() == 1) {
                return (SimpleFactor) (termList.get(0));
            } else {
                return factor;
            }

        } else if (factor instanceof ComplexMixtureFactor) {
            ComplexMixtureFactor complexMixtureFactor = (ComplexMixtureFactor) factor;
            if (!complexMixtureFactor.hasProductTerm()) {
                return customize(complexMixtureFactor
                        .convertToSimpleMixtureFactor());
            } else {
                return factor;
            }
        } else {
            throw new RuntimeException("뭐시라, 또 있냐?");
        }
    }

    public static void main(String args[]) {
        NodeDictionary.getInstance().putValues("X", "true", "false");
        ComplexMixtureFactor m = new ComplexMixtureFactor();
        try {
            m.addSimpleTerm(new SimpleGaussian(1.0, NodeDictionary.getInstance().nodeIdx("X")));
        } catch (NoSuchVariableException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Logger.getRootLogger().info(m);
        Logger.getRootLogger().info(CustomizeFactor.customize(m));
    }
}
