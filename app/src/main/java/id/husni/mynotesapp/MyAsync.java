package id.husni.mynotesapp;

import java.util.ArrayList;

import id.husni.mynotesapp.entity.NoteModel;

public interface MyAsync {
    void preEksekusi();

    void postEksekusi(ArrayList<NoteModel> noteModels);
}
