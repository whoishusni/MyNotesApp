package id.husni.mynotesapp.db;

import android.database.Cursor;

import java.util.ArrayList;

import id.husni.mynotesapp.entity.NoteModel;

import static id.husni.mynotesapp.db.DatabaseContract.Kolom.*;

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
}
