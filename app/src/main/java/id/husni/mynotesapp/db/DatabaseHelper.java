package id.husni.mynotesapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static id.husni.mynotesapp.db.DatabaseContract.Kolom.NAMA_TABLE;
import static id.husni.mynotesapp.db.DatabaseContract.Kolom._ID;
import static id.husni.mynotesapp.db.DatabaseContract.Kolom.JUDUL;
import static id.husni.mynotesapp.db.DatabaseContract.Kolom.DETAIL;
import static id.husni.mynotesapp.db.DatabaseContract.Kolom.TANGGAL;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final static String NAMA_DATABASE = "noteappdatabase";
    private final static int DATABASE_VERSION = 1;

    private final static String CREATE_QUERY = String.format("CREATE TABLE %s" +
                    "(%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "%s TEXT NOT NULL," +
                    "%s TEXT NOT NULL," +
                    "%s TEXT NOT NULL);",
            NAMA_TABLE,
            _ID,
            JUDUL,
            DETAIL,
            TANGGAL);

    public DatabaseHelper(Context context) {
        super(context, NAMA_DATABASE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NAMA_TABLE);
        onCreate(db);
    }
}
