package com.example.ggg.receivedataandroidmatong;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class DeviceListActivity extends AppCompatActivity {

    private static final String TAG = "DeviceListActivity";
    private static final boolean D = true;



    // EXTRA string to send on to mainactivity
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    // Member fields
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private String address;
    private boolean connected = false;
    Button reconnect;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);


    }


    protected  void onRestart() {

        super.onRestart();
        checkBTState();
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // Get a set of currently paired devices and append to 'pairedDevices'
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                // Get the device MAC address, which is the last 17 chars in the View
                address = device.getAddress();
            }
            if (address != null && connected == false) {
                // Make an intent to start next activity while taking an extra which is the MAC address.
                Intent i = new Intent(DeviceListActivity.this, MainActivity.class);
                i.putExtra(EXTRA_DEVICE_ADDRESS, address);
                startActivity(i);
                connected = true;
            }
        } else {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }
    }

    protected void onResume() {

        super.onResume();
        checkBTState();
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // Get a set of currently paired devices and append to 'pairedDevices'
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                // Get the device MAC address, which is the last 17 chars in the View
                address = device.getAddress();
            }
            if (address != null && connected == false) {
                // Make an intent to start next activity while taking an extra which is the MAC address.
                Intent i = new Intent(DeviceListActivity.this, MainActivity.class);
                i.putExtra(EXTRA_DEVICE_ADDRESS, address);
                startActivity(i);
                connected = true;
            }
        } else {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }
    }




    private void checkBTState() {
        // Check device has Bluetooth and that it is turned on
        mBtAdapter = BluetoothAdapter.getDefaultAdapter(); // CHECK THIS OUT THAT IT WORKS!!!
        if (mBtAdapter == null) {
            Toast.makeText(getBaseContext(), "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (mBtAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth ON...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);

            }
        }
    }




    @Override

    protected void onStop() {

        super.onStop();
        connected = false;
        new ConnectBT().execute();
    }


    private BluetoothSocket btSocket = null;
    private PendingIntent pi;
    private AlarmManager am;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    private boolean isBtConnected = false;
    private boolean awake = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(DeviceListActivity.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
                try {
                    btSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                finish();
            }
            else
            {
                isBtConnected = true;
                if (awake == false) {
                    awake = true;
                    Intent intent = new Intent(DeviceListActivity.this, AlarmReceiver.class);
                    pi = PendingIntent.getActivity(DeviceListActivity.this, 2, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    am = (AlarmManager) getSystemService(ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP, 0, pi);
                }
            }
            progress.dismiss();
        }

    }
}




