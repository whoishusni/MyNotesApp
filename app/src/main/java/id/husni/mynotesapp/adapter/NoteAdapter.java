package id.husni.mynotesapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import id.husni.mynotesapp.CustomClick;
import id.husni.mynotesapp.NoteAddUpdateActivity;
import id.husni.mynotesapp.R;
import id.husni.mynotesapp.entity.NoteModel;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    ArrayList<NoteModel> noteModels = new ArrayList<>();
    Activity activity;

    public NoteAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<NoteModel> getNoteModels() {
        return noteModels;
    }

    public void setNoteModels(ArrayList<NoteModel> items) {
        if (noteModels.size() >0) {
            noteModels.clear();
        }
        noteModels.addAll(items);
        notifyDataSetChanged();
    }

    public void insertItem(NoteModel note) {
        this.noteModels.add(note);
        notifyItemInserted(noteModels.size() - 1);
    }

    public void updateItem(int position, NoteModel note) {
        this.noteModels.set(position, note);
        notifyItemChanged(position, note);
    }

    public void deleteItem(int position) {
        this.noteModels.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position, noteModels.size());
    }

    @NonNull
    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_note, viewGroup, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.NoteViewHolder noteViewHolder, final int i) {
        noteViewHolder.tvJudul.setText(noteModels.get(i).getJudul());
        noteViewHolder.tvDetail.setText(noteModels.get(i).getDetail());
        noteViewHolder.tvTanggal.setText(noteModels.get(i).getTanggal());
        noteViewHolder.cvNote.setOnClickListener(new CustomClick(i, new CustomClick.OnItemClick() {
            @Override
            public void onItemKliked(View v, int position) {
                Intent intent = new Intent(activity, NoteAddUpdateActivity.class);
                intent.putExtra(NoteAddUpdateActivity.EXTRA_NOTE, noteModels.get(position));
                intent.putExtra(NoteAddUpdateActivity.EXTRA_POSITION, position);
                activity.startActivityForResult(intent, NoteAddUpdateActivity.REQUEST_CODE_UPDATE);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return noteModels.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        public TextView tvJudul;
        public TextView tvDetail;
        public TextView tvTanggal;
        public CardView cvNote;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudul = itemView.findViewById(R.id.tv_item_title);
            tvDetail = itemView.findViewById(R.id.tv_item_description);
            tvTanggal = itemView.findViewById(R.id.tv_item_date);
            cvNote = itemView.findViewById(R.id.cv_item_note);
        }
    }
}
