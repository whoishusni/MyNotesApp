package id.husni.mynotesapp.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import id.husni.mynotesapp.db.NoteHelper;

import static id.husni.mynotesapp.db.DatabaseContract.Kolom.AUTHORITY;
import static id.husni.mynotesapp.db.DatabaseContract.Kolom.CONTENT_URI;
import static id.husni.mynotesapp.db.DatabaseContract.Kolom.NAMA_TABLE;

public class NotesProvider extends ContentProvider {
    private final static int NOTE = 1;
    private final static int NOTE_WITH_ID = 2;
    private final static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private NoteHelper noteHelper;
    static {
        uriMatcher.addURI(AUTHORITY, NAMA_TABLE, NOTE);
        uriMatcher.addURI(AUTHORITY, NAMA_TABLE + "/#", NOTE_WITH_ID);
    }
    public NotesProvider() {
    }

    @Override
    public boolean onCreate() {
        noteHelper = NoteHelper.getInstance(getContext());
        noteHelper.open();
        return true;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case NOTE:
                cursor = noteHelper.getAllNoteData();
                break;
            case NOTE_WITH_ID:
                cursor = noteHelper.getDataById(uri.getLastPathSegment());
                break;
                default:
                    cursor = null;
                    break;
        }
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long added;
        switch (uriMatcher.match(uri)) {
            case NOTE:
                added = noteHelper.insertData(values);
                break;
                default:
                    added = 0;
                    break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int update;
        switch (uriMatcher.match(uri)) {
            case NOTE_WITH_ID:
                update = noteHelper.updateData(uri.getLastPathSegment(), values);
                break;
                default:
                    update = 0;
                    break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return update;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deleted;
        switch (uriMatcher.match(uri)) {
            case NOTE_WITH_ID:
                deleted = noteHelper.deleteData(uri.getLastPathSegment());
                break;
                default:
                    deleted = 0;
                    break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return deleted;
    }
}
