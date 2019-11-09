package id.husni.consumernotes;

import java.util.ArrayList;

import id.husni.consumernotes.entity.NoteModel;

public interface MyAsync {
    void preEksekusi();

    void postEksekusi(ArrayList<NoteModel> noteModels);
}
