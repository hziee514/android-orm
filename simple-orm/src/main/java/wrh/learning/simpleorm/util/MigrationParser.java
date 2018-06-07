package wrh.learning.simpleorm.util;

/**
 * @author bruce.wu
 * @date 2018/6/7
 */
public class MigrationParser {

    private String content;

    public MigrationParser(String content) {
        // ignore comments and empty lines
        this.content = content.replaceAll("(\\/\\*([\\s\\S]*?)\\*\\/)|(--(.)*)|(\n)","");
    }

    public String[] getStatements() {
        return this.content.split(";");
    }

}
