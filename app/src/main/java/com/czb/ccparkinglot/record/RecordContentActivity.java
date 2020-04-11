package com.czb.ccparkinglot.record;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.czb.ccparkinglot.R;

public class RecordContentActivity extends AppCompatActivity {

    public static void actionStart(Context context, RecordItem recordItem) {
        Intent intent = new Intent(context, RecordContentActivity.class);
        intent.putExtra("record_item_data", recordItem);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_content);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(Color.WHITE);

        RecordItem recordItem = (RecordItem) getIntent().getSerializableExtra("record_item_data");
        RecordContentFragment recordContentFragment = (RecordContentFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_recvord_content_record_fragment);
        recordContentFragment.refresh(recordItem);

    }
}
