package com.example.user.myapplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;

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
import android.os.Message;
import android.support.v7.app.ActionBarActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
ListView listview;
tArrayAdapter ta;
ArrayList<WifiInfot> wifiarray = new ArrayList<WifiInfot>();
BluetoothAdapter mblue = BluetoothAdapter.getDefaultAdapter();
private UUID uuid = UUID.fromString("00000000-0000-1000-8000-00805F9B34FB");
BluetoothDevice device1;
Button button = null;
final int MESSAGE_READ = 1;
public Handler mHandler;
private static final int DISCOVER_DURATION = 300;
private static final int REQEST_BLU = 1;
String path = "/storage/emulated/0/Download/test.txt";
int fre = 1;
String ch = "no";
String[] value = new String[]{};
ArrayList<String> list = new ArrayList<String>();
ArrayList<String> bssid = new ArrayList<String>();
ArrayList<String> channel = new ArrayList<String>();
int set=1000;
ArrayAdapter arrayAdapter;

/** Called when the activity is first created. */
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);



      /** Get Wifi information*/
         //search_result = (TextView) findViewById(R.id.text);

         wifi_manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
         wifi_receiver = new WifiReceiver();
         registerReceiver(wifi_receiver, new IntentFilter(
                 WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
         wifi_manager.startScan();
         //search_result.setText("Now Loading...\n");

    listview = (ListView) findViewById(R.id.list);
    arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,list);
    listview.setAdapter(arrayAdapter);
    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            set = position;
            sendFile(view);
        }
    });


 }

	   class WifiReceiver extends BroadcastReceiver {
          @Override
		  public void onReceive(Context context, Intent intent) {

	         List<ScanResult> result_list = wifi_manager.getScanResults();

		     for (int i = 0; i < result_list.size(); i++) {
                 list.add(result_list.get(i).SSID);
                 listview.setAdapter(arrayAdapter);
                 bssid.add(result_list.get(i).BSSID);
                 int count = 0;
                 for(int j=2412;j<=result_list.get(i).frequency;j+=5){count++;}
                 if(count>13){channel.add("We only support 2.4G");}
                 else{channel.add(Integer.toString(count));}
	         }
		  }
	   }

    public void sendFile(View v){
        BluetoothAdapter bAdatapter = BluetoothAdapter.getDefaultAdapter();
        if (bAdatapter == null){
            Toast.makeText(this,"not support",Toast.LENGTH_LONG).show();
        }else{
            enableBuletooth();
        }
    }

    public void enableBuletooth(){
        Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,DISCOVER_DURATION);
        startActivityForResult(discoveryIntent, REQEST_BLU);
    }

   // @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == DISCOVER_DURATION && requestCode == REQEST_BLU && set != 1000){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");

            File f = new File("/storage/emulated/0/Download","test.txt");

            try {
                FileWriter fw = new FileWriter(path, false);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(list.get(set).toString());
                bw.newLine();
                bw.write(bssid.get(set).toString());
                bw.newLine();
                bw.write(channel.get(set).toString());
                bw.close();
            }catch (IOException e){}

            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));

            PackageManager pm = getPackageManager();
            List<ResolveInfo> appList = pm.queryIntentActivities(intent,0);

            if(appList.size() > 0){
                String packName = null;
                String className = null;
                boolean found = false;

                for(ResolveInfo info : appList){
                    packName = info.activityInfo.packageName;
                    if(packName.equals("com.android.bluetooth")){
                        className = info.activityInfo.name;
                        found = true;
                        break;
                    }

                }
                if(!found){
                    Toast.makeText(this,"Bluetooth havn't been found",Toast.LENGTH_SHORT).show();
                }else{
                    intent.setClassName(packName,className);

                    startActivity(intent);
                }
            }
            else{
                Toast.makeText(this,"Bluetooth is cancelled",Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*public void connect(View v){
        *//** checking bluetooth*//*
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
        String t = new String();
        for(BluetoothDevice device : devices){
            out.append("Found device:"+device.getName()+"\t"+device.getAddress());
            t = device.getAddress();
        }
        device1 = mblue.getRemoteDevice(t);
        //out.append(t);
        try {
            FileOutputStream output = new FileOutputStream(path);
        }catch (IOException e){out.setText("create error");}


*//*                String s = "Hello";
                 try {
                     connectedThread = new ConnectedThread(device1.createInsecureRfcommSocketToServiceRecord(uuid));
                     //connectedThread.run();
                     out.append("\nyo");
                     //connectedThread.write(s.getBytes());
                 }catch (IOException e){
                     connectedThread.cancel();
                 }*//*
        //connectThread.run();

        //BluetoothSocket client ;
*//*                 try{
                     client = device1.createInsecureRfcommSocketToServiceRecord(uuid);

                    // client.connect();
                 }catch(IOException e){
                     //client.close();
                     out.append("\nConnect error");
                 }*//*
    }*/


    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        //TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
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

        /*** Will cancel an in-progress connection, and close the socket*/

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {out.setText("write error"); }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }


}


