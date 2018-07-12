package cntbn.common;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 13. 2. 12
 * Time: 오후 5:10
 * To change this template use File | Settings | File Templates.
 */
public class KeyMap {
    int numOfNodes;
    int cardinalities[];

    public KeyMap(int numOfNodes, int cardinalities[]) {
        this.numOfNodes = numOfNodes;
        this.cardinalities = cardinalities.clone();
    }


}
