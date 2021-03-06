package com.example.PuzzleAndAdventure;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PuzzleAndAdventure.ui.home.HomeFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    //新增開啟定位

    private AppBarConfiguration mAppBarConfiguration;
    private static final String TAG = "Game03";

    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
    private static final int REQUEST_ENABLE_BLUETOOTH = 2;

    public static String playerID; //玩家id
    public static boolean playerState [][] = new boolean[][]{{false, false}, {false, false}, {false, false},
            {false, false}, {false, false}, {false, false}, {false, false}};

    public static DatabaseReference refPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getPlayerID();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navPlayerID = headerView.findViewById(R.id.navPlayerID);
        navPlayerID.setText("Player ID: "+playerID);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_help)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Log.d(TAG,"requestLocationPermissions()");
        requestLocationPermissions();
        enableBluetooth();
        enableLocation();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        refPlayer = database.getReference().child("player").child(playerID);
        getDatabaseData();
    }

    private void getDatabaseData() {
        Log.e(TAG,"getDatabaseData");

        refPlayer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Log.d(TAG,"onDataChange");

                MainActivity.playerState [0][0] = Boolean.parseBoolean(snapshot.child("task1").child("isFinished").getValue().toString());
                MainActivity.playerState [0][1] = Boolean.parseBoolean(snapshot.child("task1").child("isSearched").getValue().toString());
                MainActivity.playerState [1][0] = Boolean.parseBoolean(snapshot.child("task2").child("isFinished").getValue().toString());
                MainActivity.playerState [1][1] = Boolean.parseBoolean(snapshot.child("task2").child("isSearched").getValue().toString());
                MainActivity.playerState [2][0] = Boolean.parseBoolean(snapshot.child("task3").child("isFinished").getValue().toString());
                MainActivity.playerState [2][1] = Boolean.parseBoolean(snapshot.child("task3").child("isSearched").getValue().toString());
                MainActivity.playerState [3][0] = Boolean.parseBoolean(snapshot.child("task4").child("isFinished").getValue().toString());
                MainActivity.playerState [3][1] = Boolean.parseBoolean(snapshot.child("task4").child("isSearched").getValue().toString());
                MainActivity.playerState [4][0] = Boolean.parseBoolean(snapshot.child("task5").child("isFinished").getValue().toString());
                MainActivity.playerState [4][1] = Boolean.parseBoolean(snapshot.child("task5").child("isSearched").getValue().toString());
                MainActivity.playerState [5][0] = Boolean.parseBoolean(snapshot.child("task6").child("isFinished").getValue().toString());
                MainActivity.playerState [5][1] = Boolean.parseBoolean(snapshot.child("task6").child("isSearched").getValue().toString());
                MainActivity.playerState [6][0] = Boolean.parseBoolean(snapshot.child("task7").child("isFinished").getValue().toString());
                MainActivity.playerState [6][1] = Boolean.parseBoolean(snapshot.child("task7").child("isSearched").getValue().toString());

                HomeFragment.reMakeRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });
    }

    private void getPlayerID() {
        //取得傳過來的Bundle
        Bundle bundle = getIntent().getExtras();
        playerID = bundle.getString("ID");
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void requestLocationPermissions(){

        if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            //建立Dialog提醒用戶需開啟定位權限
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("這個app需要定位權限");
            builder.setMessage("請允許定位權限，才可偵測Beacon訊號");
            builder.setPositiveButton(android.R.string.ok, null);
            //請求權限的Dialog並設置監聽
            builder.setOnDismissListener(new DialogInterface.OnDismissListener(){
                @Override
                public void onDismiss(DialogInterface dialog) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSION_REQUEST_FINE_LOCATION);
                }
            });
            builder.show();
        }
    }

    private void enableBluetooth(){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Log.e(TAG,"藍芽功能未開啟");
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH);
        }
    }

    private void enableLocation(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Log.e(TAG,"定位功能未開啟");
            Intent enableLocationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(enableLocationIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults){
        switch (requestCode){

            case PERMISSION_REQUEST_FINE_LOCATION:{
                //獲得權限
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "FINE LOCATION permission granted");
                }
                //未獲得權限
                else{
                    Log.d(TAG,"FINE LOCATION permission not granted");
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("此app功能受到限制");
                    builder.setMessage("如果定位權限未被允許，會無法偵測Beacon訊號");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener(){
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            //再次請求權限
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    PERMISSION_REQUEST_FINE_LOCATION);
                        }
                    });
                    builder.show();
                }

            }
        }
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){

            case REQUEST_ENABLE_BLUETOOTH:{
                switch (resultCode){
                    case RESULT_OK:
                        Log.d(TAG,"藍芽開啟成功");
                        Toast.makeText(getApplicationContext(),"藍芽功能開啟",Toast.LENGTH_SHORT).show();
                        break;
                    case RESULT_CANCELED:
                        Log.d(TAG,"藍芽開啟失敗");
                        break;
                }
            }

        }
    }

    //禁用返回鍵 也就是說按了會回手機主畫面
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}