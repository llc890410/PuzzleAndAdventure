package com.example.PuzzleAndAdventure.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.PuzzleAndAdventure.Adapter.MyListAdapter;
import com.example.PuzzleAndAdventure.MainActivity;
import com.example.PuzzleAndAdventure.R;
import com.example.PuzzleAndAdventure.model.ListData;
import com.google.firebase.database.DatabaseReference;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.service.ArmaRssiFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HomeFragment extends Fragment implements BeaconConsumer{

    private static final String TAG = "Game03"; //log中的String

    //IBeacon 封包格式
    public static final String IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";
    //感興趣的UUID
    private static final String FILTER_UUID = "11d20f4d-4127-47b0-b4e6-75f025783a89";
    private static final long DEFAULT_FOREGROUND_SCAN_PERIOD = 1000L;
    private static final long DEFAULT_FOREGROUND_BETWEEN_SCAN_PERIOD = 1000L;
    private BeaconManager beaconManager;

    protected RecyclerView mRecyclerView;
    public static MyListAdapter myListAdapter;

    private boolean btnStartState = false;

    protected static List<ListData> mListDataSet;

    private final String playerID = MainActivity.playerID;

    private static final int taskNumber = 7;

    private DatabaseReference mDatabase;

    protected SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mListDataSet = new ArrayList<>();
        dataSet();

        //mDatabase = FirebaseDatabase.getInstance().getReference().child("player").child(playerID);

        Button btnStart = root.findViewById(R.id.btnStart);

        mRecyclerView = root.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        myListAdapter = new MyListAdapter(mListDataSet, getApplicationContext());
        mRecyclerView.setAdapter(myListAdapter);

        swipeRefreshLayout = root.findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(()->{
            reMakeRecyclerView();
            swipeRefreshLayout.setRefreshing(false);
        });

        initBeacon();

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!btnStartState){ //尚未開啟掃描
                    Log.e(TAG,"push btnStart");
                    try {
                        //啟動搜索，不然不會調用didRangeBeaconsInRegion method
                        beaconManager.startRangingBeaconsInRegion(
                                new Region("allBeacons", null, null, null));
                        Log.e(TAG,"Start Ranging");
                    } catch (RemoteException e){
                        e.printStackTrace();
                    }
                    btnStart.setText("STOP");
                    btnStartState = true;
                }
                else if (btnStartState){ //正在掃描
                    Log.e(TAG,"push btnStop");
                    try {
                        //停止搜索
                        beaconManager.stopRangingBeaconsInRegion(
                                new Region("allBeacons", null, null, null));
                        Log.e(TAG,"Stop Ranging.");
                    } catch (RemoteException e){
                        e.printStackTrace();
                    }
                    btnStart.setText("START");
                    btnStartState = false;
                }
            }
        });


        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    public static void reMakeRecyclerView() {
        mListDataSet.clear();
        dataSet();
        myListAdapter.notifyDataSetChanged();
    }

    private static void dataSet() {
        //設定初始的list要有哪些

        for (int i = 0; i < taskNumber; i++) {
            mListDataSet.add(new ListData(i+1,"Mission#"+(i+1),
                    MainActivity.playerState[i][0], MainActivity.playerState[i][1]));
        }
    }

    private void initBeacon() {
        //獲取BeaconManager實例對象
        beaconManager = BeaconManager.getInstanceForApplication(getApplicationContext());
        //設置Beacon封包格式
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_FORMAT));
        //设置搜索的时间间隔和周期
        beaconManager.setForegroundBetweenScanPeriod(DEFAULT_FOREGROUND_BETWEEN_SCAN_PERIOD);
        beaconManager.setForegroundScanPeriod(DEFAULT_FOREGROUND_SCAN_PERIOD);

        //ARMA Filter An alternative implementation is the Auto Regressive Moving Average filter
        beaconManager.setRssiFilterImplClass(ArmaRssiFilter.class);

        //beaconManager.setRssiFilterImplClass(RunningAverageRssiFilter.class);
        //RunningAverageRssiFilter.setSampleExpirationMilliseconds(5000l);

        //綁定Activity與BeaconServices
        //準備完成後自動callback下方onBeaconServiceConnect方法
        beaconManager.bind(this);
    }

    public void onBeaconServiceConnect(){
        Log.d(TAG, "Beacon service connected.");
        beaconManager.addRangeNotifier(new RangeNotifier(){
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region){
                Log.i(TAG, "找到了"+collection.size()+"個Beacon訊號");
                //以下對搜尋到的Beacons動作
                for (Beacon beacon: collection){
                    //符合規定UUID
                    if (beacon.getId1().toString().equals(FILTER_UUID)) {
                        //Log.d(TAG,"UUID = "+beacon.getId1().toString());
                        Log.d(TAG,"distance = "+beacon.getDistance());
                        // distance = 2 euqals meter = 1
                        // 5meters = 10distance
                        // 距離大於1m 且 major = 1
                        if(beacon.getDistance() >= 1 && beacon.getId2().toInt() == 1){

                            //Log.i(TAG,"major="+beacon.getId2());
                            //Log.i(TAG,"minor="+beacon.getId3());

                            //change by minor
                            switch (beacon.getId3().toInt()){

                                case 1:
                                    Log.e(TAG,"task = "+beacon.getId3());
                                    if (MainActivity.playerState[0][1] == false){
                                        MainActivity.refPlayer.child("task1").child("isSearched").setValue(true);
                                        Log.e(TAG,"="+MainActivity.playerState[0][1]);
                                    }
                                    break;
                                case 2:
                                    Log.e(TAG,"task = "+beacon.getId3());
                                    if (MainActivity.playerState[1][1] == false){
                                        MainActivity.refPlayer.child("task2").child("isSearched").setValue(true);
                                        Log.e(TAG,"="+MainActivity.playerState[1][1]);
                                    }
                                    break;
                                case 3:
                                    Log.e(TAG,"task = "+beacon.getId3());
                                    if (MainActivity.playerState[2][1] == false){
                                        MainActivity.refPlayer.child("task3").child("isSearched").setValue(true);
                                        Log.e(TAG,"="+MainActivity.playerState[2][1]);
                                    }
                                    break;
                                case 4:
                                    Log.e(TAG,"task = "+beacon.getId3());
                                    if (MainActivity.playerState[3][1] == false){
                                        MainActivity.refPlayer.child("task4").child("isSearched").setValue(true);
                                        Log.e(TAG,"="+MainActivity.playerState[3][1]);
                                    }
                                    break;
                                case 5:
                                    Log.e(TAG,"task = "+beacon.getId3());
                                    if (MainActivity.playerState[4][1] == false){
                                        MainActivity.refPlayer.child("task5").child("isSearched").setValue(true);
                                        Log.e(TAG,"="+MainActivity.playerState[4][1]);
                                    }
                                    break;
                                case 6:
                                    Log.e(TAG,"task = "+beacon.getId3());
                                    if (MainActivity.playerState[5][1] == false){
                                        MainActivity.refPlayer.child("task6").child("isSearched").setValue(true);
                                        Log.e(TAG,"="+MainActivity.playerState[5][1]);
                                    }
                                    break;
                                case 7:
                                    Log.e(TAG,"task = "+beacon.getId3());
                                    if (MainActivity.playerState[6][1] == false){
                                        MainActivity.refPlayer.child("task7").child("isSearched").setValue(true);
                                        Log.e(TAG,"="+MainActivity.playerState[6][1]);
                                    }
                                    break;
                            }
                        }
                    }
                    //Log.i(TAG,"uuid = "+beacon.getId1());
                    //Log.i(TAG,"major = "+beacon.getId2());
                    //Log.i(TAG,"minor = "+beacon.getId3());
                    //Log.i(TAG,"distance = "+beacon.getDistance());
                }
            }
        });

    }

    @Override
    public Context getApplicationContext() {
        return getActivity().getApplicationContext();
    }

    //ALtBeacon interface override
    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        getActivity().unbindService(serviceConnection);
    }

    //ALtBeacon interface override
    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return getActivity().bindService(intent, serviceConnection, i);
    }
}