package com.example.bluetoothapp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button button;
    ListView listView;
    ArrayList devices=new ArrayList();
    ArrayList addresses=new ArrayList();
    ArrayAdapter adapter;

    BluetoothAdapter bluetoothAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=findViewById(R.id.textView);
        button=findViewById(R.id.button);
        listView=findViewById(R.id.listView);

        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

        IntentFilter intentFilter=new IntentFilter();

        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(broadcastReceiver,intentFilter);

        adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
    }

    private final BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                textView.setText("Finished");
                button.setEnabled(true);

            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                String name=device.getName();
                String address=device.getAddress();
                String rssi=Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE));

                String present;
                if (!addresses.contains(address)) {
                    addresses.add(address);
                    if (name == null || name.equals("")) {
                        present = address+" - RSSI : "+rssi;
                    } else {
                        present=name+" - RSSI : "+rssi;
                    }
                    devices.add(present);
                    adapter.notifyDataSetChanged();
                }
            }

        }
    };

    public void searchClicked(View view) {
        button.setEnabled(false);
        textView.setText("Searching...");

        devices.clear();
        addresses.clear();
        bluetoothAdapter.startDiscovery();
    }
}