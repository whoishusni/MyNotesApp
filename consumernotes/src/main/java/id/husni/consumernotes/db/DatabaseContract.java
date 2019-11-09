package id.husni.consumernotes.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static class Kolom implements BaseColumns {
        public static final String NAMA_TABLE = "catatan";
        public static final String JUDUL = "judul";
        public static final String DETAIL = "detail";
        public static final String TANGGAL = "tanggal";

        private static final String SCHEME = "content" ;
        public static final String AUTHORITY = "id.husni.mynotesapp" ;
        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(NAMA_TABLE)
                .build();

    }
}
