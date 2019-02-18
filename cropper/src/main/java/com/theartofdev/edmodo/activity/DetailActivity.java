package com.theartofdev.edmodo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.chrisbanes.VertexData;
import com.theartofdev.edmodo.cropper.R;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ArrayList<VertexData> vertexData = getIntent().getParcelableArrayListExtra("vertexData");


        TextView text = findViewById(R.id.text);
        text.setText("  Article Detail Of "+vertexData.get(0).getArticleID());

    }


}
