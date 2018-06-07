package wrh.learning.simpleorm.record;

import wrh.learning.simpleorm.SimpleRecord;

/**
 * @author bruce.wu
 * @date 2018/6/7
 */
public class StringRecord extends SimpleRecord {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
