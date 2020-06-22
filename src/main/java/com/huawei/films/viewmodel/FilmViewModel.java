package com.huawei.films.viewmodel;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.huawei.films.model.CloudDBZoneWrapper;
import com.huawei.films.model.FilmTb;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FilmViewModel extends ViewModel implements CloudDBZoneWrapper.UiCallBack {

    private CloudDBZoneWrapper mCloudDBZoneWrapper;
    private static final String TAG = "Film List ";
    MutableLiveData<ArrayList<FilmTb>> filmLiveData;
    ArrayList<FilmTb> filmList = new ArrayList<FilmTb>();
    Handler handler = new Handler();
    Timer timer = new Timer();
    TimerTask timerTask;



    public FilmViewModel(){
        mCloudDBZoneWrapper = new CloudDBZoneWrapper();
        filmLiveData = new MutableLiveData<>();
        init();
    }

    public MutableLiveData<ArrayList<FilmTb>> getFilmLiveData() {
        return filmLiveData;
    }

    public void init(){
        setFilmLiveData();
        filmLiveData.setValue(filmList);
    }


    public void setFilmLiveData(){
        getFilms();
    }


    public void getFilms() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.postDelayed(new Runnable() {
                    public void run() {
                        mCloudDBZoneWrapper.addCallBacks(FilmViewModel.this);
                        mCloudDBZoneWrapper.createObjectType();
                        mCloudDBZoneWrapper.openCloudDBZone();
                        mCloudDBZoneWrapper.getAllFilms();
                    }
                }, 500);
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, (1*60 * 500));

    }

    @Override
    public void updateUiOnError(String errorMessage) {
    }


    @Override
    public void onAddOrQueryFilmList(List<FilmTb> filmListTmp) {
        filmList.addAll(filmListTmp);
        for(int i=0; i<filmList.size(); i++){
            Log.w(TAG, String.valueOf(filmList.get(i)));
        }
        filmLiveData.setValue(filmList);
    }
    @Override
    public void onSubscribeFilmList(List<FilmTb> filmList) {

    }
    @Override
    public void onDeleteFilmList(List<FilmTb> filmList) {

    }


}
