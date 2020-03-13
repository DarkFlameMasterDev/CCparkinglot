package com.czb.ccparkinglot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


import com.czb.ccparkinglot.adapter.RecordsAdapter;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    private List<RecordItem> recordItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init_recordItemList();
        RecyclerView recyclerView = findViewById(R.id.records);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecordsAdapter adapter = new RecordsAdapter(recordItemList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setCheckedItem(R.id.accountMananger);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawerLayout.closeDrawers();
                return true;
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);

        Button homebutton = findViewById(R.id.home_button);
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        Button savecar = findViewById(R.id.savecar);
        savecar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //savecar
            }
        });
        Button pickupcar = findViewById(R.id.pickupcar);
        pickupcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pickupcar
            }
        });
    }

    //初始化recorditemlist
    private void init_recordItemList() {

        RecordItem recordItem = new RecordItem("兰博基尼", "迪拜停车场", "停车中", 123456, 4556);
        for (int i = 0; i < 50; i++) {
            recordItemList.add(recordItem);
        }
    }
}
