package id.husni.mynotesapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import id.husni.mynotesapp.entity.NoteModel;

import static id.husni.mynotesapp.db.DatabaseContract.Kolom.NAMA_TABLE;
import static id.husni.mynotesapp.db.DatabaseContract.Kolom._ID;
import static id.husni.mynotesapp.db.DatabaseContract.Kolom.JUDUL;
import static id.husni.mynotesapp.db.DatabaseContract.Kolom.DETAIL;
import static id.husni.mynotesapp.db.DatabaseContract.Kolom.TANGGAL;

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

    public ArrayList<NoteModel> getAllNoteData() {
        ArrayList<NoteModel> noteModels = new ArrayList<>();
        Cursor cursor = database.query(NAMA_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        NoteModel note;
        if (cursor.getCount() > 0) {
            do {
                note = new NoteModel();
                note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                note.setJudul(cursor.getString(cursor.getColumnIndexOrThrow(JUDUL)));
                note.setDetail(cursor.getString(cursor.getColumnIndexOrThrow(DETAIL)));
                note.setTanggal(cursor.getString(cursor.getColumnIndexOrThrow(TANGGAL)));

                noteModels.add(note);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return noteModels;
    }
    public long insertData(NoteModel note) {
        ContentValues values = new ContentValues();
        values.put(JUDUL, note.getJudul());
        values.put(DETAIL, note.getDetail());
        values.put(TANGGAL, note.getTanggal());
        return database.insert(NAMA_TABLE, null, values);
    }

    public int updateData(NoteModel note) {
        ContentValues values = new ContentValues();
        values.put(JUDUL, note.getJudul());
        values.put(DETAIL, note.getDetail());
        values.put(TANGGAL, note.getTanggal());
        return database.update(NAMA_TABLE, values, _ID + "='" + note.getId() + "'", null);
    }

    public int deleteData(int id) {
        return database.delete(NAMA_TABLE, _ID + "='" + id + "'", null);
    }
}
