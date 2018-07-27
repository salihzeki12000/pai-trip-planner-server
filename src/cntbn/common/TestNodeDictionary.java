package cntbn.common;

public class TestNodeDictionary {
    public static NodeDictionary makeSample() {
        NodeDictionary dict = NodeDictionary.getInstance();
        dict.putValues("X", "true", "false");
        dict.putValues("Y", "true", "false");
        dict.putValues("Z", "true", "false");
        return dict;
    }
}
