package kienme.react.content;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ravikiran on 20/1/17.
 *
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String TBL_NAME = "favourites";
    public static final String COL_ID = "_id";

    public static final String COL_TITLE = "title";
    public static final String COL_URL = "url";

    private static final String DB_NAME = "favourites.DB";
    private static final int DB_VERSION = 1;

    private static final String CREATE_DB = "CREATE TABLE " + TBL_NAME +
            " (" + COL_ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_TITLE + " TEXT, " + COL_URL + " TEXT);";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL_NAME);
        onCreate(sqLiteDatabase);
    }
}
