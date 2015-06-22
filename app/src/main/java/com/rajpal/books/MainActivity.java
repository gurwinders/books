package com.rajpal.books;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerlayout;
    private NavigationView navigationView;
    private FloatingActionButton floatingActionButton;

    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initToolbar();


    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu_white);
        setTitle("Android Support Design Sample");
    }

    private void initViews() {
        drawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout);

        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.floating_btn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSnackBar(v, "Hello ! I am SnackBar !");
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.rv_sample);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);
        switch (menuItem.getItemId()) {
            case R.id.navi_item_1:
                Intent ii = new Intent(MainActivity.this, NewFragment.class);
                startActivity(ii);
                drawerlayout.closeDrawers();
        }


        //       Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerlayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSnackBar(View view, final String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction("Action", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        }).show();
    }

}
