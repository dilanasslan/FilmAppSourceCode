package com.huawei.films.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.films.R;
import com.huawei.films.model.CloudDBZoneWrapper;
import com.huawei.films.model.FilmTb;
import com.huawei.films.viewmodel.FilmViewModel;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LifecycleOwner{

    MainActivity context;
    RecyclerView recyclerView;
    private CloudDBZoneWrapper mCloudDBZoneWrapper;
    FilmListAdapter filmAdapter;
    FilmViewModel filmViewModel;

    private static final String TAG = "FilmList ";


    public MainActivity() {
        mCloudDBZoneWrapper = new CloudDBZoneWrapper();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        filmAdapter = new FilmListAdapter(context, null);
        recyclerView = findViewById(R.id.recylerview);
        recyclerView.setHasFixedSize(true);
        filmViewModel = new ViewModelProvider(context).get(FilmViewModel.class);
        filmViewModel.getFilmLiveData().observe(context, filmUpdateObserver);

    }


    Observer<ArrayList<FilmTb>> filmUpdateObserver = new Observer<ArrayList<FilmTb>>() {
        @Override
        public void onChanged(ArrayList<FilmTb> filmItems) {
            filmAdapter = new FilmListAdapter(context, filmItems);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(filmAdapter);
        }
    };


}
