package id.husni.consumernotes.db;

import android.database.Cursor;

import java.util.ArrayList;

import id.husni.consumernotes.entity.NoteModel;

import static android.provider.BaseColumns._ID;
import static id.husni.consumernotes.db.DatabaseContract.Kolom.*;
import static id.husni.consumernotes.db.DatabaseContract.Kolom.DETAIL;
import static id.husni.consumernotes.db.DatabaseContract.Kolom.JUDUL;
import static id.husni.consumernotes.db.DatabaseContract.Kolom.TANGGAL;

public class MappingHelper {
    public static ArrayList<NoteModel> mapCursorToArray(Cursor cursor) {
        ArrayList<NoteModel> noteModels = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(JUDUL));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DETAIL));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(TANGGAL));
            noteModels.add(new NoteModel(id,title,description,date));
        }
        return noteModels;
    }

    public static NoteModel mapCursorToObject(Cursor cursor) {
        cursor.moveToFirst();
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(JUDUL));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(DETAIL));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(TANGGAL));
        return new NoteModel(id, title, description, date);
    }
}
