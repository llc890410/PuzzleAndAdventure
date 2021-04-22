package com.example.game03.ui.home;

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

import com.example.game03.Adapter.MyListAdapter;
import com.example.game03.R;
import com.example.game03.model.ListData;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HomeFragment extends Fragment implements BeaconConsumer{

    private static final String TAG = "Game03"; //log中的String

    //IBeacon 封包格式
    public static final String IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";
    //感興趣的UUID
    private static final String FILTER_UUID = "FDA50693-A4E2-4FB1-AFCF-C6EB07647825";
    private static final long DEFAULT_FOREGROUND_SCAN_PERIOD = 1000L;
    private static final long DEFAULT_FOREGROUND_BETWEEN_SCAN_PERIOD = 1000L;
    private BeaconManager beaconManager;

    protected RecyclerView mRecyclerView;
    protected MyListAdapter myListAdapter;

    private boolean btnStartState = false;

    protected List<ListData> mListDataSet;

    private static final int taskNumber = 7;

    protected SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        initDataSet();

        Button btnStart = root.findViewById(R.id.btnStart);

        mRecyclerView = root.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        myListAdapter = new MyListAdapter(mListDataSet, getApplicationContext());
        mRecyclerView.setAdapter(myListAdapter);

        swipeRefreshLayout = root.findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(()->{
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

    private void initDataSet() {
        //設定初始的list要有哪些

        mListDataSet = new ArrayList<>();

        /*for (int i = 0; i < taskNumber; i++) {
            mListDataSet.add(new ListData(i+1,"Mission#"+(i+1),false, false));
        }*/

        //test data
        for (int i = 0; i < 2; i++) {
            mListDataSet.add(new ListData(i+1,"Mission#"+(i+1),false, false));
        }
        for (int i = 2; i < 4; i++) {
            mListDataSet.add(new ListData(i+1,"Mission#"+(i+1),false, true));
        }
        for (int i = 4; i < 6; i++) {
            mListDataSet.add(new ListData(i+1,"Mission#"+(i+1),true,false));
        }
        for (int i = 6; i < 7; i++) {
            mListDataSet.add(new ListData(i+1,"Mission#"+(i+1),true,true));
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
                    Log.e(TAG,beacon.getId1().toString());
                    //Tuuid.setText(""+beacon.getId1());
                    //  Tmajor.setText(""+beacon.getId2());
                    // Tminor.setText(""+beacon.getId3());
                    // Trssi.setText(""+beacon.getDistance());
                    Log.i(TAG,"uuid="+beacon.getId1());
                    Log.i(TAG,"major="+beacon.getId2());
                    Log.i(TAG,"minor="+beacon.getId3());
                    Log.i(TAG,"rssi="+beacon.getDistance());
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