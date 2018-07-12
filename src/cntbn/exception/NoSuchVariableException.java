package cntbn.exception;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 12. 12. 26
 * Time: 오후 9:24
 * To change this template use File | Settings | File Templates.
 */
public class NoSuchVariableException extends RuntimeException {

    public NoSuchVariableException(String variable) {
        super("name:" + variable + " is not found");
    }

    public NoSuchVariableException(int idx) {
        super("idx:" + idx + " is not found");
    }
}
