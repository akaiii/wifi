package com.example.user.myapplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {


private TextView search_result;
private TextView out;
private WifiManager wifi_manager;
private WifiReceiver wifi_receiver;
int num = 0;
Button [] list_button = new Button[num];
LinearLayout linear;
ListView listview;
tArrayAdapter ta;
ArrayList<WifiInfot> wifiarray = new ArrayList<WifiInfot>();
BluetoothAdapter mblue = BluetoothAdapter.getDefaultAdapter();
private UUID uuid = UUID.fromString("00000000-0000-1000-8000-00805F9B34FB");
BluetoothDevice device1;
SimpleAdapter adapter;
Button button = null;


/** Called when the activity is first created. */
	 @Override
	  public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_main);

/**Test ArrayList*/
/*         wifiarray.add(new WifiInfot("yo"));

         ta = new tArrayAdapter(MainActivity.this,R.layout.list_item,wifiarray);
         listview = (ListView) findViewById(R.id.list);
         listview.setAdapter(ta);*/

      /**Register receiver*/
         //IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
         //registerReceiver(filter);

      /**Open Bluetooth*/
      button = (Button)findViewById(R.id.button);
      button.setOnClickListener(new Button.OnClickListener(){
             @Override
             public void onClick(View v){
                 /** checking bluetooth*/
                 if(mblue!=null) {
                     if (!mblue.isEnabled()) {
                         Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                         startActivityForResult(enableIntent, 12);
                         mblue.enable();
                     } else {
                         //mblue.disable();
                     }
                 }
                 else{System.out.print("no");}
                 out = (TextView) findViewById(R.id.text2);
                 out.append(mblue.getName()+"\n"+mblue.getAddress());
                 mblue.startDiscovery();
                 out.append("\nDevices:");
                 Set<BluetoothDevice> devices = mblue.getBondedDevices();
                 //BluetoothDevice device1 ;
                 //String t;
                 for(BluetoothDevice device : devices){
                     out.append("Found device:"+device.getName()+"\t"+device.getAddress());
                     device1 = mblue.getRemoteDevice(device.getAddress());
                     //t = device.getAddress();
                 }
                 //device1 = mblue.getRemoteDevice(t);
                 try{
                     BluetoothSocket client = device1.createInsecureRfcommSocketToServiceRecord(uuid);
                     client.connect();
                 }catch(IOException e){}
             }
         });

         /**Find Server*/
/*         IntentFilter intent = new IntentFilter();
         intent.addAction(BluetoothDevice.ACTION_FOUND);
         intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
         intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
         //registerReceiver();*/



      /** Get Wifi information*/
         search_result = (TextView) findViewById(R.id.text);
         wifi_manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
         wifi_receiver = new WifiReceiver();
         registerReceiver(wifi_receiver, new IntentFilter(
         WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
         wifi_manager.startScan();
         search_result.setText("Now Loading...\n");


         /**Dynamic list of textView*/
         //adapter = new tAdapter();
         //for(int i = 0;i<50;i++){
           //  adapter.addItem("item"+i);
         //}
 }

/*        BroadcastReceiver mr = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };*/

	   class WifiReceiver extends BroadcastReceiver {
          @Override
		  public void onReceive(Context context, Intent intent) {
	         StringBuffer sb = new StringBuffer();
             String s ;
	         List<ScanResult> result_list = wifi_manager.getScanResults();
		     for (int i = 0; i < result_list.size(); i++) {
                sb.append(Integer.toString(i+1) + ".");
                 //creat_button(i,sb.append(result_list.get(i).SSID));
                 sb.append(result_list.get(i).SSID);
                 sb.append(result_list.get(i).BSSID);
	            sb.append((result_list.get(i)).toString());
	            sb.append("\n");
                //wifiarray.add(new WifiInfo(result_list.get(i).SSID));
	         }

              /**Use ArrayList to construct clickable text*/
              /*ta = new tArrayAdapter(MainActivity.this,R.layout.list_item,wifiarray);
                                listview = (ListView) findViewById(R.id.list);
                                listview.setItemsCanFocus(false);
                                listview.setAdapter(ta);*/


	         search_result.setText(sb);
             num = result_list.size();
		  }
	   }


/*        private void creat_button(int i,StringBuffer x){
            linear = (LinearLayout) findViewById(R.id.linear);
                list_button[i] = new Button(this);
                list_button[i].setHeight(50);
                list_button[i].setWidth(50);
                list_button[i].setTag(i);
                list_button[i].setText(x);
                list_button[i].setOnClickListener(buttonListenter);
                linear.addView(list_button[i]);
        }*/

/*        private View.OnClickListener buttonListenter = new View.OnClickListener() {
           @Override
           public void onClick(View v){
               Object tag = v.getTag();
               Toast.makeText(getApplicationContext(),"clicked button",Toast.LENGTH_SHORT).show();
           }
        };*/


/*
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        //UUID uuid = (UUID)tManager.getDeviceId();

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(uuid);
                //mmDevice.getUuids()[0].getUuid()
            } catch (IOException e) {
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            mblue.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            //manageConnectedSocket(mmSocket);
        }

        */
/**
         * Will cancel an in-progress connection, and close the socket
         *//*

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

*/

}


