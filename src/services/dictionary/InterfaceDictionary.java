package services.dictionary;

/**
 * Created by wykwon on 2015-10-28.
 */
public interface InterfaceDictionary {
    /**
     * 기존 내용을 무시하고 사전 항목을 갱신함
     * @param keyword
     * @param contents
     */
    void putContents(String keyword, String... contents);
    /**
     * 기존 내용에 추가함.
     * 기존 내용에 있는 항목은 새로 갱신
     * @param keyword
     * @param contents
     */

    void addContents(String keyword, String... contents);

    /**
     * 해당항목을 지워라
     * @param keyword
     * @param contents
     */
    void deleteContents(String keyword, String... contents);

    String[] search(String keyword);
    String getName();
}
