package cntbn.exception;

public class NoSuchValueException extends RuntimeException {

    public NoSuchValueException(String nodeName, String value) {

        super("node:" + nodeName + ", value=" + value);
    }
}
