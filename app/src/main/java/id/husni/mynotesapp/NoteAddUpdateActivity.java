package id.husni.mynotesapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import id.husni.mynotesapp.db.NoteHelper;
import id.husni.mynotesapp.entity.NoteModel;

import static id.husni.mynotesapp.db.DatabaseContract.Kolom.JUDUL;
import static id.husni.mynotesapp.db.DatabaseContract.Kolom.DETAIL;
import static id.husni.mynotesapp.db.DatabaseContract.Kolom.TANGGAL;

public class NoteAddUpdateActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_CODE_ADD = 100;
    public static final int RESULT_CODE_ADD = 101;
    public static final int REQUEST_CODE_UPDATE = 200;
    public static final int RESULT_CODE_UPDATE = 201;
    public static final int RESULT_CODE_DELETE = 301;

    public static final String EXTRA_NOTE = "extra_note";
    public static final String EXTRA_POSITION = "extra_position";

    EditText edtJudul, edtDetail;
    Button btnSubmit;
    private boolean isEdit = false;
    NoteHelper helper;
    NoteModel noteModel;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add_update);

        helper = NoteHelper.getInstance(getApplicationContext());
        noteModel = getIntent().getParcelableExtra(EXTRA_NOTE);

        if (noteModel != null) {
            position = getIntent().getIntExtra(EXTRA_POSITION,0);
            isEdit = true;
        } else {
            noteModel = new NoteModel();
        }

        edtJudul = findViewById(R.id.edt_title);
        edtDetail = findViewById(R.id.edt_description);
        btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);

        String actBar, btnTitle;
        if (isEdit) {
            actBar = "Edit";
            btnTitle = "Update";

            if (noteModel != null) {
                edtJudul.setText(noteModel.getJudul());
                edtDetail.setText(noteModel.getDetail());
            }
        } else {
            actBar = "Tambah";
            btnTitle = "Submit";
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(actBar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        btnSubmit.setText(btnTitle);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_submit) {
            String judul = edtJudul.getText().toString();
            String detail = edtDetail.getText().toString();

            if (TextUtils.isEmpty(judul)) {
                edtJudul.setError("Harus Di Isi");
                return;
            }

            noteModel.setJudul(judul);
            noteModel.setDetail(detail);

            Intent intent = new Intent();
            intent.putExtra(EXTRA_NOTE, noteModel);
            intent.putExtra(EXTRA_POSITION, position);

            ContentValues values = new ContentValues();
            values.put(JUDUL, judul);
            values.put(DETAIL, detail);

            if (isEdit) {
                long result = helper.updateData(String.valueOf(noteModel.getId()), values);
                if (result > 0) {
                    setResult(RESULT_CODE_UPDATE, intent);
                    finish();
                } else {
                    Toast.makeText(this, "Gagal Update", Toast.LENGTH_SHORT).show();
                }
            } else {
                noteModel.setTanggal(tanggalSet());
                values.put(TANGGAL,tanggalSet());
                long result = helper.insertData(values);
                if (result > 0) {
                    noteModel.setId((int) result);
                    setResult(RESULT_CODE_ADD, intent);
                    finish();
                } else {
                    Toast.makeText(this, "Gagal Menambahkan", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String tanggalSet() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        Date tanggal = new Date();
        return sdf.format(tanggal);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        showConfirmDialog(DIALOG_CLOSE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdit) {
            getMenuInflater().inflate(R.menu.menu_delete,menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    private static final int DIALOG_DELETE = 80;
    private static final int DIALOG_CLOSE = 90;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionDelete:
                showConfirmDialog(DIALOG_DELETE);
                break;
            case android.R.id.home:
                showConfirmDialog(DIALOG_CLOSE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    String dialogJudul, dialogPesan;

    private void showConfirmDialog(int type) {
        final boolean isClose = type == DIALOG_CLOSE;
        if (isClose) {
            dialogJudul = "Batal";
            dialogPesan = "Apakah Anda Ingin Membatalkan ?";
        } else {
            dialogJudul = "Hapus";
            dialogPesan = "Apakah Anda Ingin Menghapus Item ?";
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle(dialogJudul)
                .setMessage(dialogPesan)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isClose) {
                            finish();
                        } else {
                            long result = helper.deleteData(String.valueOf(noteModel.getId()));
                            if (result > 0) {
                                Intent intent = new Intent();
                                intent.putExtra(EXTRA_POSITION, position);
                                setResult(RESULT_CODE_DELETE, intent);
                                finish();
                            } else {
                                Toast.makeText(NoteAddUpdateActivity.this, "Gagal Hapus Data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
    }
}
