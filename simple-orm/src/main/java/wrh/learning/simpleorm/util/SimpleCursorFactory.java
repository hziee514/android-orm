package wrh.learning.simpleorm.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

import static wrh.learning.simpleorm.SimpleContext.SQL_TAG;
import static wrh.learning.simpleorm.SimpleContext.isDebugEnabled;

/**
 * @author bruce.wu
 * @date 2018/6/7
 */
public class SimpleCursorFactory implements SQLiteDatabase.CursorFactory {

    public SimpleCursorFactory() {

    }

    @Override
    public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery, String editTable, SQLiteQuery query) {
        if (isDebugEnabled()) {
            Log.d(SQL_TAG, query.toString());
        }
        return new SQLiteCursor(db, masterQuery, editTable, query);
    }

}
