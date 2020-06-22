package com.huawei.films.model;

import android.content.Context;
import android.util.Log;

import com.huawei.agconnect.cloud.database.AGConnectCloudDB;
import com.huawei.agconnect.cloud.database.CloudDBZone;
import com.huawei.agconnect.cloud.database.CloudDBZoneConfig;
import com.huawei.agconnect.cloud.database.CloudDBZoneObjectList;
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery;
import com.huawei.agconnect.cloud.database.CloudDBZoneSnapshot;
import com.huawei.agconnect.cloud.database.CloudDBZoneTask;
import com.huawei.agconnect.cloud.database.ListenerHandler;
import com.huawei.agconnect.cloud.database.OnFailureListener;
import com.huawei.agconnect.cloud.database.OnSuccessListener;
import com.huawei.agconnect.cloud.database.exceptions.AGConnectCloudDBException;

import java.util.ArrayList;
import java.util.List;

public class CloudDBZoneWrapper {

    private AGConnectCloudDB mCloudDB;
    private CloudDBZone mCloudDBZone;
    private ListenerHandler mRegister;
    private CloudDBZoneConfig mConfig;
    private UiCallBack mUiCallBack = UiCallBack.DEFAULT;
    private static final String TAG = "DB_Zone_Wrapper";


    public CloudDBZoneWrapper() {
        mCloudDB = AGConnectCloudDB.getInstance();
    }


    public static void initAGConnectCloudDB(Context context) {
        AGConnectCloudDB.initialize(context);
        Log.w(TAG, "initAGConnectCloudDB");
    }


    public void createObjectType() {
        try {
            mCloudDB.createObjectType(ObjectTypeInfoHelper.getObjectTypeInfo());
            Log.w(TAG, "createObjectTypeSuccess ");
        } catch (AGConnectCloudDBException e) {
            Log.w(TAG, "createObjectTypeError: " + e.getMessage());
        }
    }

    /**
     * Call AGConnectCloudDB.openCloudDBZone to open a cloudDBZone.
     * We set it with cloud cache mode, and data can be store in local storage
     */
    public void openCloudDBZone() {
        mConfig = new CloudDBZoneConfig("FilmDB",
                CloudDBZoneConfig.CloudDBZoneSyncProperty.CLOUDDBZONE_CLOUD_CACHE,
                CloudDBZoneConfig.CloudDBZoneAccessProperty.CLOUDDBZONE_PUBLIC);
        mConfig.setPersistenceEnabled(true);
        Log.w(TAG, "openCloudDBZoneSuccess ");
        try {
            mCloudDBZone = mCloudDB.openCloudDBZone(mConfig, true);
        } catch (AGConnectCloudDBException e) {
            Log.w(TAG, "openCloudDBZoneError: " + e.getMessage());
        }
    }

    /**
     * Call AGConnectCloudDB.closeCloudDBZone
     */
    public void closeCloudDBZone() {
        try {
            mRegister.remove();
            mCloudDB.closeCloudDBZone(mCloudDBZone);
            Log.w(TAG, "closeCloudDBZoneSuccess ");
        } catch (AGConnectCloudDBException e) {
            Log.w(TAG, "closeCloudDBZoneError: " + e.getMessage());
        }
    }


    /**
     * Call AGConnectCloudDB.deleteCloudDBZone
     */
    public void deleteCloudDBZone() {
        try {
            mCloudDB.deleteCloudDBZone(mConfig.getCloudDBZoneName());
            Log.w(TAG, "deleteCloudBZoneSuccess");
        } catch (AGConnectCloudDBException e) {
            Log.w(TAG, "deleteCloudDBZone: " + e.getMessage());
        }
    }

    public void getAllFilms() {
        if (mCloudDBZone == null) {
            Log.w(TAG, "GET FILM DETAIL : CloudDBZone is null, try re-open it");
            return;
        }
        CloudDBZoneTask<CloudDBZoneSnapshot<FilmTb>> queryTask = mCloudDBZone.executeQuery(
                CloudDBZoneQuery.where(FilmTb.class),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY);
        queryTask.addOnSuccessListener(new OnSuccessListener<CloudDBZoneSnapshot<FilmTb>>() {
            @Override
            public void onSuccess(CloudDBZoneSnapshot<FilmTb> snapshot) {
                filmListResult (snapshot);
                Log.w(TAG, "GET FILM DETAIL : GoResults: ");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (mUiCallBack != null) {
                    mUiCallBack.updateUiOnError("GET FILM DETAIL : Query film list from cloud failed");
                }
            }
        });
    }

    private void filmListResult (CloudDBZoneSnapshot<FilmTb> snapshot) {
        CloudDBZoneObjectList<FilmTb> filmCursor = snapshot.getSnapshotObjects();
        List<FilmTb> filmInfoList = new ArrayList<>();
        try {
            while (filmCursor.hasNext()) {
                FilmTb film = filmCursor.next();
                filmInfoList.add(film);
                Log.w(TAG, "FILM DETAIL RESULT : processQueryResult: ");
            }
        } catch (AGConnectCloudDBException e) {
            Log.w(TAG, "FILM DETAIL RESULT : processQueryResult: " + e.getMessage());
        }
        snapshot.release();
        if (mUiCallBack != null) {
            mUiCallBack.onAddOrQueryFilmList(filmInfoList);
        }
    }


    /**
     * Call back to update ui in MainActivity
     */
    public interface UiCallBack {
        UiCallBack DEFAULT = new UiCallBack() {

            /**
             *
             * @param filmList
             */
            @Override
            public void onAddOrQueryFilmList(List<FilmTb> filmList) {
                Log.w(TAG, "Using default onAddOrQuery");
            }

            @Override
            public void onSubscribeFilmList(List<FilmTb> filmList) {
                Log.w(TAG, "Using default onSubscribe");
            }

            @Override
            public void onDeleteFilmList(List<FilmTb> filmList) {
                Log.w(TAG, "Using default onDelete");
            }

            @Override
            public void updateUiOnError(String errorMessage) {
                Log.w(TAG, "Using default updateUiOnError");
            }


        };

        /**
         * Call
         * @param filmList
         */
        void onAddOrQueryFilmList(List<FilmTb> filmList);
        void onSubscribeFilmList(List<FilmTb> filmList);
        void onDeleteFilmList(List<FilmTb> filmList);
        void updateUiOnError(String errorMessage);

    }

    /**
     * Add a callback to update book info list
     *
     * @param uiCallBack callback to update book list
     */
    public void addCallBacks(CloudDBZoneWrapper.UiCallBack uiCallBack) {
        mUiCallBack = uiCallBack;
    }

}
