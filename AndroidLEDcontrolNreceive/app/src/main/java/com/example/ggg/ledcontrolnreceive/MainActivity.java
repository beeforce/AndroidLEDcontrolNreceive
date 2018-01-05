package com.example.ggg.ledcontrolnreceive;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    Button btnOn1,btnOn2,btnOn3, btnOn4, btnOn5, btnOff1,btnOff2,btnOff3, btnOff4, btnOff5;
    TextView sensorView0, sensorView1, sensorView2, sensorView3, sensorView4, sensorView5, sensorView6, sensorView7, sensorView8, sensorView9,
            sensorView10, sensorView11, sensorView12, sensorView13, sensorView14, sensorView15, sensorView16, sensorView17, sensorView18, sensorView19;
    Handler bluetoothIn;
    final int handlerState = 0;        				 //used to identify handler message
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();

    private ConnectedThread mConnectedThread;
    public Thread sleepCheck;
    public Thread t;
    private boolean runningThread = false;
    private boolean runningThreadSleep = false;
    private PendingIntent pi;
    private AlarmManager am;
    private boolean isBtConnected = false;
    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //Link the buttons and textViews to respective views
        btnOn1 = (Button) findViewById(R.id.buttonOn1);
        btnOn2 = (Button) findViewById(R.id.buttonOn2);
        btnOn3 = (Button) findViewById(R.id.buttonOn3);
        btnOn4 = (Button) findViewById(R.id.buttonOn4);
        btnOn5 = (Button) findViewById(R.id.buttonOn5);
        btnOff1 = (Button) findViewById(R.id.buttonOff1);
        btnOff2 = (Button) findViewById(R.id.buttonOff2);
        btnOff3 = (Button) findViewById(R.id.buttonOff3);
        btnOff4 = (Button) findViewById(R.id.buttonOff4);
        btnOff5 = (Button) findViewById(R.id.buttonOff5);
        sensorView0 = (TextView) findViewById(R.id.sensorView0);
        sensorView1 = (TextView) findViewById(R.id.sensorView1);
        sensorView2 = (TextView) findViewById(R.id.sensorView2);
        sensorView3 = (TextView) findViewById(R.id.sensorView3);
        sensorView4 = (TextView) findViewById(R.id.sensorView4);
        sensorView5 = (TextView) findViewById(R.id.sensorView5);
        sensorView6 = (TextView) findViewById(R.id.sensorView6);
        sensorView7 = (TextView) findViewById(R.id.sensorView7);
        sensorView8 = (TextView) findViewById(R.id.sensorView8);
        sensorView9 = (TextView) findViewById(R.id.sensorView9);
        sensorView10 = (TextView) findViewById(R.id.sensorView10);
        sensorView11 = (TextView) findViewById(R.id.sensorView11);
        sensorView12 = (TextView) findViewById(R.id.sensorView12);
        sensorView13 = (TextView) findViewById(R.id.sensorView13);
        sensorView14 = (TextView) findViewById(R.id.sensorView14);
        sensorView15 = (TextView) findViewById(R.id.sensorView15);
        sensorView16 = (TextView) findViewById(R.id.sensorView16);
        sensorView17 = (TextView) findViewById(R.id.sensorView17);
        sensorView18 = (TextView) findViewById(R.id.sensorView18);
        sensorView19 = (TextView) findViewById(R.id.sensorView19);




        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState(); // check Bluetooth state

        // Get a set of currently paired devices and append to 'pairedDevices'
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                // Get the device MAC address, which is the last 17 chars in the View
                address = device.getAddress();
            }
        }

        //create device and set the MAC address
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
        }

        // Establish the Bluetooth socket connection.
        try {
            btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                //insert code to deal with this
            }
        }
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();

        //I send a character when resuming.beginning transmission to check device is connected
        //If it is not an exception will be thrown in the write method and finish() will be called
        mConnectedThread.write("x");




        btnOn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mConnectedThread.writeString("a");    // Send "a" via Bluetooth to turn on LED number 1
            }
        });
        btnOff1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mConnectedThread.writeString("b");    // Send "b" via Bluetooth to turn off LED number1
            }
        });
        btnOn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnectedThread.writeString("c");    // Send "c" via Bluetooth to turn on LED number2
            }
        });

        btnOff2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnectedThread.writeString("d");    // Send "d" via Bluetooth to turn off LED number2
            }
        });
        btnOn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnectedThread.writeString("e");    // Send "e" via Bluetooth to turn on LED number3
            }
        });
        btnOff3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnectedThread.writeString("f");    // Send "f" via Bluetooth to turn off LED number3
            }
        });
        btnOn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnectedThread.writeString("g");   // Send "g" via Bluetooth to turn off LED number3
            }
        });
        btnOff4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnectedThread.writeString("h");   // Send "h" via Bluetooth to turn off LED number3
            }
        });
        btnOn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnectedThread.writeString("i");   // Send "i" via Bluetooth to turn off LED number3
            }
        });
        btnOff5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnectedThread.writeString("j");   // Send "j" via Bluetooth to turn off LED number3
            }
        });
    }


    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }



    protected void onResume() {

        super.onResume();
        runningThread = false;
        restoreScreenOffTimeOut();      //restore timeout to sleep of device to 10 minute
        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {										//if message is what we want
                    String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
                    recDataString.append(readMessage);      								//keep appending to string until ~
                    int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                    if (endOfLineIndex > 0) {                                           // make sure there data before ~

                        if (recDataString.charAt(0) == '#')								//if it starts with # we know it is what we are looking for
                        {
                            String[] removeSharp = recDataString.toString().split(Pattern.quote("#"));
                            String[] sensors = removeSharp[1].split(Pattern.quote("+"));
                            sensorView0.setText(" Sensor 1 " + sensors[0]);	//update the textviews with sensor values
                            sensorView1.setText(" Sensor 2 " + sensors[1]);
                            sensorView2.setText(" Sensor 3 " +  sensors[2]);
                            sensorView3.setText(" Sensor 4 " +  sensors[3]);
                            sensorView4.setText(" Sensor 5 " +  sensors[4]);
                            sensorView5.setText(" Sensor 6 " + sensors[5]);
                            sensorView6.setText(" Sensor 7 " +  sensors[6]);
                            sensorView7.setText(" Sensor 8 " +  sensors[7]);
                            sensorView8.setText(" Sensor 9 " +  sensors[8]);
                            sensorView9.setText(" Sensor 10 " +  sensors[9]);
                            sensorView10.setText(" Sensor 11 " +  sensors[10]);
                            sensorView11.setText(" Sensor 12 " +  sensors[11]);
                            sensorView12.setText(" Sensor 13 " +  sensors[12]);
                            sensorView13.setText(" Sensor 14 " +  sensors[13]);
                            sensorView14.setText(" Sensor 15 " +  sensors[14]);
                            sensorView15.setText(" Sensor 16 " +  sensors[15]);
                            sensorView16.setText(" Sensor 17 " +  sensors[16]);
                            sensorView17.setText(" Sensor 18 " +  sensors[17]);
                            sensorView18.setText(" Sensor 19 " +  sensors[18]);
                            sensorView19.setText(" Sensor 20 " +  sensors[19]);
                        }
                        recDataString.delete(0, recDataString.length()); 					//clear all string data
                    }
                }
            }
        };

        t = new Thread(){       //create new Thread
            @Override
            public void run(){
                while (!runningThread){
                    try {
                        Thread.sleep(5000);     //run every 5 sec (1000 = 1 sec)

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                BluetoothDevice device = btAdapter.getRemoteDevice(address);

                                try {
                                    btSocket = createBluetoothSocket(device);
                                } catch (IOException e) {
                                }

                                // Establish the Bluetooth socket connection.
                                try {
                                    btSocket.connect();
                                    mConnectedThread = new ConnectedThread(btSocket);
                                    mConnectedThread.start();

                                    //I send a character when resuming.beginning transmission to check device is connected
                                    //If it is not an exception will be thrown in the write method and finish() will be called
                                    mConnectedThread.writeCheck("x");
                                } catch (IOException e) {
                                    try {
                                        btSocket.close();
                                        mConnectedThread.writeCheck("x");
                                    } catch (IOException e2) {
                                        //insert code to deal with this
                                    }
                                }
                            }
                        });

                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }

            }
        };
        t.start();      //start Thread

    }

    protected void onPause() {

        super.onPause();
        restoreScreenOffTimeOut();      //restore timeout to sleep of device to 10 minute
        runningThread = true;
        try {
            btSocket.close();       //close the BluetoothSocket when user doesn't use the app
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onStop() {

        super.onStop();
        runningThread = true;
        sleepCheck = new Thread(){
            @Override
            public void run(){
                while (!runningThreadSleep){
                    try {
                        Thread.sleep(10000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                BluetoothDevice device = btAdapter.getRemoteDevice(address);

                                try {
                                    btSocket = createBluetoothSocket(device);
                                } catch (IOException e) {
                                }

                                // Establish the Bluetooth socket connection.
                                try {
                                    btSocket.connect();
                                    mConnectedThread = new ConnectedThread(btSocket);
                                    mConnectedThread.start();

                                    //I send a character when resuming.beginning transmission to check device is connected
                                    //If it is not an exception will be thrown in the write method and finish() will be called
                                    mConnectedThread.writeSleep("x");
                                } catch (IOException e) {
                                    try {
                                        btSocket.close();
                                    } catch (IOException e2) {
                                        //insert code to deal with this
                                    }
                                }
                                    Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                                    pi = PendingIntent.getActivity(MainActivity.this, 2, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                                    am = (AlarmManager) getSystemService(ALARM_SERVICE);
                                    am.set(AlarmManager.RTC_WAKEUP, 5000, pi);
                            }
                        });

                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }

            }
        };
            sleepCheck.start();

    }

    protected void onRestart() {

        super.onRestart();
        runningThreadSleep = true;      //Stop a Thread
        restoreScreenOffTimeOut();      //Restore timeout to sleep of device

    }



    //Checks that the Android device Bluetooth is available and prompts to be turned on if off
    private void checkBTState() {

        if(btAdapter==null) {
            Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    //create new class for connect thread
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }


        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);        	//read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);
                isBtConnected = true;//write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                isBtConnected = false;
                runningThreadSleep = false;
                runningThread = false;

            }
        }
        //write method
        public void writeString(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);
                isBtConnected = true;//write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application

            }
        }
        //write method
        public void writeCheck(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);
                isBtConnected = true;//write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                setScreenOffTimeOut();      //set timeout to sleep of device to 5 sec
                isBtConnected = false;

            }
        }
        //write method when device sleeping
        public void writeSleep(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);
                isBtConnected = true;
                btSocket.close();//write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application

            }
        }


    }





    private static final int SCREEN_OFF_TIME_OUT = 5000;
    private int mSystemScreenOffTimeOut;
    private void setScreenOffTimeOut() {
        try {
            mSystemScreenOffTimeOut = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, SCREEN_OFF_TIME_OUT);
        } catch (Exception e) {

        }
    }
    private void restoreScreenOffTimeOut() {
        try {
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 600000);
        } catch (Exception e) {
        }
    }

}



