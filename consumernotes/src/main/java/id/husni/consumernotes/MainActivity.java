package id.husni.consumernotes;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import id.husni.consumernotes.adapter.NoteAdapter;
import id.husni.consumernotes.db.MappingHelper;
import id.husni.consumernotes.entity.NoteModel;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FlipInLeftYAnimator;

import static id.husni.consumernotes.db.DatabaseContract.Kolom.CONTENT_URI;

public class MainActivity extends AppCompatActivity implements MyAsync {
    RecyclerView recyclerView;
    NoteAdapter adapter;
    ProgressBar progressBar;

    private static final String EXTRA_STATE = "extra_state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Consumer Catatan");
        }

        recyclerView = findViewById(R.id.rv_notes);
        progressBar = findViewById(R.id.progressbar);

        HandlerThread handlerThread = new HandlerThread("NotesDataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        NotesDataObserver notesDataObserver = new NotesDataObserver(handler,this);
        getContentResolver().registerContentObserver(CONTENT_URI,true,notesDataObserver);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new NoteAdapter(this);
        FlipInLeftYAnimator flipInLeftYAnimator = new FlipInLeftYAnimator();
        flipInLeftYAnimator.setAddDuration(400);
        flipInLeftYAnimator.setRemoveDuration(400);
        recyclerView.setItemAnimator(flipInLeftYAnimator);
        recyclerView.setAdapter(new ScaleInAnimationAdapter(adapter));

        if (savedInstanceState == null) {
            new LoadNoteAsync(this, this).execute();
        } else {
            ArrayList<NoteModel> noteModels = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (noteModels != null) {
                adapter.setNoteModels(noteModels);
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE,adapter.getNoteModels());
    }

    @Override
    public void preEksekusi() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postEksekusi(ArrayList<NoteModel> noteModels) {
        if (noteModels.size() > 0) {
            adapter.setNoteModels(noteModels);
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            showSnake("Data Belum Ada");
        }
    }

    private static class LoadNoteAsync extends AsyncTask<Void, Void, ArrayList<NoteModel>> {

        private final WeakReference<Context> contextWeakReference;
        private final WeakReference<MyAsync> asyncWeakReference;

        public LoadNoteAsync(Context context, MyAsync myAsync) {
            contextWeakReference = new WeakReference<>(context);
            asyncWeakReference = new WeakReference<>(myAsync);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            asyncWeakReference.get().preEksekusi();
        }

        @Override
        protected ArrayList<NoteModel> doInBackground(Void... voids) {
            Context context = contextWeakReference.get();
            Cursor dataCursor = context.getContentResolver().query(CONTENT_URI, null, null, null, null);
            return MappingHelper.mapCursorToArray(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<NoteModel> noteModels) {
            super.onPostExecute(noteModels);
            asyncWeakReference.get().postEksekusi(noteModels);
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void showSnake(String pesan) {
        Snackbar.make(recyclerView,pesan,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class NotesDataObserver extends ContentObserver {
        Context context;
        public NotesDataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadNoteAsync(context, (MyAsync) context).execute();
        }
    }
}
