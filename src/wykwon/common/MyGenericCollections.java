package wykwon.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 7. 15
 * Time: 오후 4:35
 * To change this template use File | Settings | File Templates.
 */
public class MyGenericCollections<T> {
    public List<T> asList(Collection<T> collection){
        List<T> list = new ArrayList<T>();
        for (T t: collection){
            list.add(t);
        }
        return list;

    }

}
