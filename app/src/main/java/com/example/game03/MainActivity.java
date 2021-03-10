package com.example.game03;

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
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;

public class MainActivity extends AppCompatActivity {

    //新增開啟定位

    private AppBarConfiguration mAppBarConfiguration;
    private static final String TAG = "Game03";

    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
    private static final int REQUEST_ENABLE_BLUETOOTH = 2;
    private static final int REQUEST_ENABLE_LOCATION = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
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
            startActivityForResult(enableLocationIntent, REQUEST_ENABLE_LOCATION);
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
            case REQUEST_ENABLE_LOCATION:{
                switch (resultCode){
                    case RESULT_OK:
                        Log.d(TAG,"定位開啟成功");
                        Toast.makeText(getApplicationContext(),"定位功能開啟",Toast.LENGTH_SHORT).show();
                        break;
                    case RESULT_CANCELED:
                        Log.d(TAG,"藍芽開啟失敗");
                        break;
                }
            }
        }
    }

}