package wykwon.common;


/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 14. 7. 30
 * Time: 오후 4:22
 * To change this template use File | Settings | File Templates.
 */
public class ScoredString implements Comparable<ScoredString> {
    String name;
    Double score;

    public ScoredString(String name, Double score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public Double getScore() {
        return score;
    }

    public String toString() {
        return new String("score" + score + " with =\t" + name);
    }



    @Override
    public int compareTo(ScoredString o) {
        int ret=- score.compareTo(o.score);
        if (ret!=0){
            return ret;
        }
        else {
            return name.compareTo(o.name);
        }
    }
}