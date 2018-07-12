package cntbn.terms_factors.tools;

import cntbn.terms_factors.ComplexProductTerm;
import cntbn.terms_factors.ContinuousTerm;
import cntbn.terms_factors.SimpleProductTerm;
import cntbn.terms_factors.SimpleTerm;

/**
 * TemporalTerm을 원래 목적에 맞게 맞춘다. 예를들어 element가 1개뿐인 SimpleProductTerm을
 *
 * @author roland
 */
public class CustomizeTerm {
    public static ContinuousTerm customize(ContinuousTerm term) {
// 만약 SimpleTerm이면 할거없음
        if (term instanceof SimpleTerm) {
            return term;
        } else if (term instanceof SimpleProductTerm) {
            SimpleProductTerm simpleProductTerm = (SimpleProductTerm) term;
            if (simpleProductTerm.getSimpleFactorProduct().size() == 1) {
                return (SimpleTerm) simpleProductTerm
                        .getSimpleFactorProduct().get(0);

            } else if (simpleProductTerm.getSimpleFactorProduct().size() == 0) {
                throw new RuntimeException("EmptyTerm!!!");
            } else {
                return simpleProductTerm;
            }
        } else if (term instanceof ComplexProductTerm) {
            ComplexProductTerm complexProductTerm = (ComplexProductTerm) term;
            if (!complexProductTerm.hasMixture()) {
                return customize(complexProductTerm
                        .convertToSimpleProductTerm());
            } else {
                return term;
            }
        } else {
            throw new RuntimeException("뭐시라, 또 있냐?");
        }
    }
}
