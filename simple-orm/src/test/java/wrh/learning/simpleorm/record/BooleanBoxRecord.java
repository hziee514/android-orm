package wrh.learning.simpleorm.record;

import wrh.learning.simpleorm.SimpleRecord;

/**
 * @author bruce.wu
 * @date 2018/6/7
 */
public class BooleanBoxRecord extends SimpleRecord {

    private Boolean boolValue;

    public Boolean getBoolValue() {
        return boolValue;
    }

    public void setBoolValue(Boolean boolValue) {
        this.boolValue = boolValue;
    }
}
