package services.qna.wisenut;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 6. 2
 * Time: 오후 2:21
 * To change this template use File | Settings | File Templates.
 */
public class SpatialConstraints {


    public Set<String> symbolicSpatialConstraints = new HashSet<String>();
    public List<Object> metricSpatialConstraints = new ArrayList<Object>();


    public String toString() {
        StringBuffer strbuf = new StringBuffer();
        if (symbolicSpatialConstraints.size() != 0) {
            strbuf.append(symbolicSpatialConstraints);
        }
        if (metricSpatialConstraints.size() * symbolicSpatialConstraints.size() != 0) {
            strbuf.append("\n");
        }
        if (metricSpatialConstraints.size() != 0) {
            strbuf.append(metricSpatialConstraints + "\t");
        }
        return strbuf.toString();
    }

}
