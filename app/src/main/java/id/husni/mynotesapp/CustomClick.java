package id.husni.mynotesapp;

import android.view.View;

public class CustomClick implements View.OnClickListener {

    OnItemClick onItemClick;
    int position;

    public CustomClick(int position,OnItemClick onItemClick) {
        this.position = position;
        this.onItemClick = onItemClick;
    }

    @Override
    public void onClick(View v) {
        onItemClick.onItemKliked(v,position);
    }

    public interface OnItemClick {
        void onItemKliked(View v, int position);
    }

}
