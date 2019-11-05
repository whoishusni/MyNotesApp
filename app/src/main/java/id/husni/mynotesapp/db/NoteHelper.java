package id.husni.mynotesapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static id.husni.mynotesapp.db.DatabaseContract.Kolom.NAMA_TABLE;
import static id.husni.mynotesapp.db.DatabaseContract.Kolom._ID;

public class NoteHelper {

    private static SQLiteDatabase database;
    private static DatabaseHelper databaseHelper;
    private static NoteHelper INSTANCE;

    public static NoteHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NoteHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public NoteHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }
    public void close() {
        database.close();
        if (database.isOpen()) {
            database.close();
        }
    }

    public Cursor getAllNoteData() {
        return database.query(NAMA_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID + " ASC");
    }

    public Cursor getDataById(String id) {
        return database.query(NAMA_TABLE,
                null,
                _ID + "= ?", new String[]{id},
                null,
                null,
                null);
    }
    public long insertData(ContentValues values) {
        return database.insert(NAMA_TABLE, null, values);
    }

    public int updateData(String id, ContentValues contentValues) {
        return database.update(NAMA_TABLE, contentValues, _ID + "= ?", new String[]{id});
    }

    public int deleteData(String id) {
        return database.delete(NAMA_TABLE, _ID + "= ?", new String[]{id});
    }
}
