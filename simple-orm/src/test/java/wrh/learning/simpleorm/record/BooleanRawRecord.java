package wrh.learning.simpleorm.record;

import wrh.learning.simpleorm.SimpleRecord;

/**
 * @author bruce.wu
 * @date 2018/6/7
 */
public class BooleanRawRecord extends SimpleRecord {

    private boolean boolValue;

    public boolean isBoolValue() {
        return boolValue;
    }

    public void setBoolValue(boolean boolValue) {
        this.boolValue = boolValue;
    }
}
