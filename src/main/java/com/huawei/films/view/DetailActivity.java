package com.huawei.films.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.films.R;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.film_detail);

        Intent intent = getIntent();
        TextView filmName, filmDescription;

        String id = intent.getStringExtra("filmItemId");

        filmName = (TextView)findViewById(R.id.filmName);
        filmDescription = (TextView)findViewById(R.id.filmDescription);

        filmName.setText(intent.getStringExtra("filmItemName"));
        filmDescription.setText(intent.getStringExtra("filmItemDescription"));

    }
}
