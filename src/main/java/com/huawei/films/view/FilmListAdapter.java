package com.huawei.films.view;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.huawei.films.R;
import com.huawei.films.model.FilmTb;

import java.util.ArrayList;

public class FilmListAdapter extends RecyclerView.Adapter<FilmListAdapter.MyViewHolder> {
    ArrayList<FilmTb> mFilm;
    LayoutInflater inflater;
    Context context = null;

    public FilmListAdapter(Context context, ArrayList<FilmTb> films) {
        this.mFilm = films;
        this.context = context;
    }

    @Override
    public FilmListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_card, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FilmTb selectedFilm= mFilm.get(position);
        holder.setData(selectedFilm, position);
    }

    @Override
    public int getItemCount() {
        return mFilm.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView filmName, filmDescription;
        Button detail_button;

        public MyViewHolder(View view) {
            super(view);
            filmName = (TextView) view.findViewById(R.id.film_name);
            filmDescription = (TextView) view.findViewById(R.id.film_description);
            detail_button = (Button) view.findViewById(R.id.film_detail_button);

            // on item click
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                // get position
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    // check if item still exists
                    if (pos != RecyclerView.NO_POSITION) {
                        FilmTb clickedFilmItem = mFilm.get(pos);
                        Toast.makeText(v.getContext(), "clicked clickedFilmItem id: " + clickedFilmItem.getId() + " name: " +
                                clickedFilmItem.getName(), Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(context, DetailActivity.class);
                        i.putExtra("filmItemId", clickedFilmItem.getId());
                        i.putExtra("filmItemName", clickedFilmItem.getName());
                        i.putExtra("filmItemDescription", clickedFilmItem.getDescription());
                        context.startActivity(i);

                    }
                }
            });
        }


        public void setData(FilmTb selectedFilm, int position) {
            this.filmName.setText(selectedFilm.getName());
            this.filmDescription.setText(selectedFilm.getDescription());
        }
    }
}
