package id.husni.mynotesapp.db;

import android.provider.BaseColumns;

public class DatabaseContract {
    public static class Kolom implements BaseColumns {
        public static final String NAMA_TABLE = "catatan";
        public static final String JUDUL = "judul";
        public static final String DETAIL = "detail";
        public static final String TANGGAL = "tanggal";

    }
}
