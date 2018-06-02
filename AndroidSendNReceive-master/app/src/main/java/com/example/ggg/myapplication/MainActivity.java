package com.example.ggg.myapplication;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rm.rmswitch.RMSwitch;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

public class MainActivity extends Activity implements ControlContract.view,View.OnClickListener {
    Button batlockbtn;

    Handler bluetoothIn;
    TextView battery_vol, battery_temp, battery_current, battery_balance, battery_cycle, battery_serial, VIN_number, Milleage, speed, rangemeter;
    final int handlerState = 0;                        //used to identify handler message
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();
    private ConnectedThread mConnectedThread;
    private boolean isConnected = false;
    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // String for MAC address
    private static String address;
    static final String LOG_TAG = "ScreenOffActivity";
    private TextView tv_date;
    private TextView tv_time;
    private ImageView imgBackward,  imgPlug, imgWarning,  imgForward, imgInfo, imghorn, imgRFID, imgkey, seatlockbtn, highbeambtn, lock_icon;
    private ControlContract.presenter presenter;
//    private SeekArc seekArc2,seekArc1;
    private RMSwitch Ignition ;
    private SeekBar sb;

    //for test toast
    String a,b,c,d;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                lock_icon.setVisibility(View.GONE);
                Ignition.setEnabled(true);
                Toast.makeText(getBaseContext(), "Device is now connected", Toast.LENGTH_SHORT).show();
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action) && isConnected) {
                turnScreenOffAndExit();
                lock_icon.setVisibility(View.VISIBLE);
                Ignition.setEnabled(false);
                isConnected = false;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set language
        String languageToLoad  = "en_US"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale.ENGLISH; //set locale language to english
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.activity_main);

        //Link the buttons and textViews to respective views
        initView();

        //set onclick
        initInstances();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(mReceiver, filter);

        // Set up onClick listeners for buttons to send 1 or 0 to turn on/off LED

//        batlockbtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mConnectedThread.write("b");    // Send "b" via Bluetooth to turn off LED number1
//            }
//        });

        //I send a character when resuming.beginning transmission to check device is connected
        //If it is not an exception will be thrown in the write method and finish() will be called


    }

    private void initView() {
//        seatlockbtn = findViewById(R.id.seatlockbtn);
//        batlockbtn = findViewById(R.id.batlockbtn);
//        highbeambtn = findViewById(R.id.highbeambtn);
//
//        sensorView0 = findViewById(R.id.sensorView0);
//        sensorView1 = findViewById(R.id.sensorView1);
//        sensorView2 = findViewById(R.id.sensorView2);
//        sensorView3 = findViewById(R.id.sensorView3);
//        sensorView4 = findViewById(R.id.sensorView4);
//        sensorView5 = findViewById(R.id.sensorView5);
//        sensorView6 = findViewById(R.id.sensorView6);
//        sensorView7 = findViewById(R.id.sensorView7);
//        sensorView8 = findViewById(R.id.sensorView8);
//        sensorView9 = findViewById(R.id.sensorView9);
//        sensorView10 = findViewById(R.id.sensorView10);
//        sensorView11 = findViewById(R.id.sensorView11);
//        sensorView12 = findViewById(R.id.sensorView12);
//        sensorView13 = findViewById(R.id.sensorView13);
//        sensorView14 = findViewById(R.id.sensorView14);
//        sensorView15 = findViewById(R.id.sensorView15);
//        sensorView16 = findViewById(R.id.sensorView16);
//        sensorView17 = findViewById(R.id.sensorView17);
//        sensorView18 = findViewById(R.id.sensorView18);

//        seekArc1 = findViewById(R.id.seekArc);
//        seekArc2 = findViewById(R.id.seekArc2);
//        seekArc1.setEnabled(false);
//        seekArc2.setEnabled(false);
//
//        seekArc1.setProgress(0);
//        seekArc2.setProgress(0);
        speed = findViewById(R.id.speed);
        battery_vol = findViewById(R.id.battery_vol);
        battery_temp = findViewById(R.id.battery_temp);
        battery_current = findViewById(R.id.battery_current);
        battery_balance = findViewById(R.id.battery_balance);
        battery_cycle = findViewById(R.id.battery_cycle);
        battery_serial= findViewById(R.id.battery_serial);
        VIN_number = findViewById(R.id.VIN_number);
        Milleage = findViewById(R.id.Milleage);
        rangemeter = findViewById(R.id.rangemeter);

        tv_date = findViewById(R.id.tv_date);
        tv_time = findViewById(R.id.tv_time);
        imgBackward = findViewById(R.id.imgBackward);
        seatlockbtn = findViewById(R.id.imgMocycle);
        imgPlug = findViewById(R.id.imgPlug);
        imgWarning = findViewById(R.id.imgWarning);
        highbeambtn = findViewById(R.id.imgLight);
        imgForward = findViewById(R.id.imgForward);
        imgInfo = findViewById(R.id.imgInfo);
        imghorn = findViewById(R.id.horn);
        imgRFID = findViewById(R.id.memory);
        imgkey = findViewById(R.id.key);
        lock_icon = findViewById(R.id.lock_icon);
        Ignition = findViewById(R.id.your_id2);
        //set on click Ignition(switch)
        Ignition.setEnabled(false);
        Ignition.addSwitchObserver(new RMSwitch.RMSwitchObserver() {
            @Override
            public void onCheckStateChange(RMSwitch switchView, boolean isChecked) {
                if (isChecked){
                    mConnectedThread.write("h");

                }else {
                    mConnectedThread.write("g");

                }
            }
        });
//        mSwitch = findViewById(R.id.your_id);
//        //set on click switch
//        mSwitch.setEnabled(false);
//        mSwitch.addSwitchObserver(new RMSwitch.RMSwitchObserver() {
//            @Override
//            public void onCheckStateChange(RMSwitch switchView, boolean isChecked) {
//
//                if (isChecked){
//                    mConnectedThread.write("f");
//
//                }else {
//                    mConnectedThread.write("c");
//
//                }
//            }
//        });

        sb = findViewById(R.id.myseek);
        sb.setProgress(50);

        

        ViewTreeObserver vto = sb.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                Resources res = getResources();
                Drawable thumb = res.getDrawable(R.drawable.ic_n);
                int h = (int) (sb.getMeasuredHeight() * 0.75); // 8 * 1.5 = 12
                int w = h;
                Bitmap bmpOrg = ((BitmapDrawable)thumb).getBitmap();
                Bitmap bmpScaled = Bitmap.createScaledBitmap(bmpOrg, w, h, true);
                Drawable newThumb = new BitmapDrawable(res, bmpScaled);
                newThumb.setBounds(0, 0, newThumb.getIntrinsicWidth(), newThumb.getIntrinsicHeight());
                sb.setThumb(newThumb);

                sb.getViewTreeObserver().removeOnPreDrawListener(this);

                return true;
            }
        });

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if (seekBar.getProgress() > 0 & seekBar.getProgress() < 20) {
                    ViewTreeObserver vto = sb.getViewTreeObserver();
                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        public boolean onPreDraw() {
                            Resources res = getResources();
                            Drawable thumb = res.getDrawable(R.drawable.ic_r);
                            int h = (int) (sb.getMeasuredHeight() * 0.75); // 8 * 1.5 = 12
                            int w = h;
                            Bitmap bmpOrg = ((BitmapDrawable)thumb).getBitmap();
                            Bitmap bmpScaled = Bitmap.createScaledBitmap(bmpOrg, w, h, true);
                            Drawable newThumb = new BitmapDrawable(res, bmpScaled);
                            newThumb.setBounds(0, 0, newThumb.getIntrinsicWidth(), newThumb.getIntrinsicHeight());
                            sb.setThumb(newThumb);

                            sb.getViewTreeObserver().removeOnPreDrawListener(this);

                            return true;
                        }
                    });
                    sb.setProgress(0);

                }
                if (seekBar.getProgress() > 40 & seekBar.getProgress() < 60) {
                    ViewTreeObserver vto = sb.getViewTreeObserver();
                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        public boolean onPreDraw() {
                            Resources res = getResources();
                            Drawable thumb = res.getDrawable(R.drawable.ic_n);
                            int h = (int) (sb.getMeasuredHeight() * 0.75); // 8 * 1.5 = 12
                            int w = h;
                            Bitmap bmpOrg = ((BitmapDrawable)thumb).getBitmap();
                            Bitmap bmpScaled = Bitmap.createScaledBitmap(bmpOrg, w, h, true);
                            Drawable newThumb = new BitmapDrawable(res, bmpScaled);
                            newThumb.setBounds(0, 0, newThumb.getIntrinsicWidth(), newThumb.getIntrinsicHeight());
                            sb.setThumb(newThumb);

                            sb.getViewTreeObserver().removeOnPreDrawListener(this);

                            return true;
                        }
                    });
                    sb.setProgress(50);

                }if (seekBar.getProgress() > 80 & seekBar.getProgress() <= 100) {
                    ViewTreeObserver vto = sb.getViewTreeObserver();
                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        public boolean onPreDraw() {
                            Resources res = getResources();
                            Drawable thumb = res.getDrawable(R.drawable.ic_d);
                            int h = (int) (sb.getMeasuredHeight() * 0.75); // 8 * 1.5 = 12
                            int w = h;
                            Bitmap bmpOrg = ((BitmapDrawable)thumb).getBitmap();
                            Bitmap bmpScaled = Bitmap.createScaledBitmap(bmpOrg, w, h, true);
                            Drawable newThumb = new BitmapDrawable(res, bmpScaled);
                            newThumb.setBounds(0, 0, newThumb.getIntrinsicWidth(), newThumb.getIntrinsicHeight());
                            sb.setThumb(newThumb);

                            sb.getViewTreeObserver().removeOnPreDrawListener(this);

                            return true;
                        }
                    });
                    sb.setProgress(100);

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (seekBar.getProgress() > 0 & seekBar.getProgress() < 20) {
                    ViewTreeObserver vto = sb.getViewTreeObserver();
                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        public boolean onPreDraw() {
                            Resources res = getResources();
                            Drawable thumb = res.getDrawable(R.drawable.ic_r);
                            int h = (int) (sb.getMeasuredHeight() * 0.75); // 8 * 1.5 = 12
                            int w = h;
                            Bitmap bmpOrg = ((BitmapDrawable)thumb).getBitmap();
                            Bitmap bmpScaled = Bitmap.createScaledBitmap(bmpOrg, w, h, true);
                            Drawable newThumb = new BitmapDrawable(res, bmpScaled);
                            newThumb.setBounds(0, 0, newThumb.getIntrinsicWidth(), newThumb.getIntrinsicHeight());
                            sb.setThumb(newThumb);

                            sb.getViewTreeObserver().removeOnPreDrawListener(this);

                            return true;
                        }
                    });
                    sb.setProgress(0);
                    mConnectedThread.write("R");

                }
                if (seekBar.getProgress() > 40 & seekBar.getProgress() < 60) {
                    ViewTreeObserver vto = sb.getViewTreeObserver();
                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        public boolean onPreDraw() {
                            Resources res = getResources();
                            Drawable thumb = res.getDrawable(R.drawable.ic_n);
                            int h = (int) (sb.getMeasuredHeight() * 0.75); // 8 * 1.5 = 12
                            int w = h;
                            Bitmap bmpOrg = ((BitmapDrawable)thumb).getBitmap();
                            Bitmap bmpScaled = Bitmap.createScaledBitmap(bmpOrg, w, h, true);
                            Drawable newThumb = new BitmapDrawable(res, bmpScaled);
                            newThumb.setBounds(0, 0, newThumb.getIntrinsicWidth(), newThumb.getIntrinsicHeight());
                            sb.setThumb(newThumb);

                            sb.getViewTreeObserver().removeOnPreDrawListener(this);

                            return true;
                        }
                    });
                    sb.setProgress(50);
                    mConnectedThread.write("N");

                }if (seekBar.getProgress() > 80 & seekBar.getProgress() <= 100) {
                    ViewTreeObserver vto = sb.getViewTreeObserver();
                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        public boolean onPreDraw() {
                            Resources res = getResources();
                            Drawable thumb = res.getDrawable(R.drawable.ic_d);
                            int h = (int) (sb.getMeasuredHeight() * 0.75); // 8 * 1.5 = 12
                            int w = h;
                            Bitmap bmpOrg = ((BitmapDrawable)thumb).getBitmap();
                            Bitmap bmpScaled = Bitmap.createScaledBitmap(bmpOrg, w, h, true);
                            Drawable newThumb = new BitmapDrawable(res, bmpScaled);
                            newThumb.setBounds(0, 0, newThumb.getIntrinsicWidth(), newThumb.getIntrinsicHeight());
                            sb.setThumb(newThumb);

                            sb.getViewTreeObserver().removeOnPreDrawListener(this);

                            return true;
                        }
                    });
                    sb.setProgress(100);
                    mConnectedThread.write("D");
                }

            }
        });

        presenter = new ControlPresenter(this,this);
        presenter.initTime();
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }

    @Override
    public void onResume() {
        super.onResume();
        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {                                        //if message is what we want
                    String readMessage = (String) msg.obj;
//                    Log.e("data", readMessage);
                    // msg.arg1 = bytes from connect thread
                    recDataString.append(readMessage);
//                    Log.e("recDataString", recDataString.toString());//keep appending to string until ~
                    int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                    if (endOfLineIndex > 0) {                                           // make sure there data before ~

                        if (recDataString.charAt(0) == '#')                                //if it starts with # we know it is what we are looking for
                        {
//                            Log.e("recDataString", recDataString.toString());
                            String[] removeSharp = recDataString.toString().split(Pattern.quote("#"));
                            String[] sensors = removeSharp[1].split(Pattern.quote("+"));
//                            sensorView0.setText("Seat Lock: " + sensors[0]);    //update the textviews with sensor values
//                            sensorView1.setText("Battery Lock: " + sensors[1]);
//                            sensorView2.setText("BMS Status: " + sensors[2]);
//                            sensorView3.setText("RTDS: " + sensors[3]);
//                            sensorView4.setText("RFID: " + sensors[5]);
//                            sensorView5.setText("High Beam: " + sensors[7]);
//                            sensorView6.setText("Turning Signal: " + sensors[8]);
//                            sensorView7.setText("Horn: " + sensors[9]);
//                            int speedprogree = Integer.parseInt(sensors[15]);
//                            speedprogree = (speedprogree*100)/180;
//                            if (speedprogree > 100){
//                                seekArc1.setProgress(100);
//                            }
//                            else {
//                                seekArc1.setProgress(speedprogree);
//                            }
//                            int battery = Integer.parseInt(sensors[19]);
//                            if (battery > 100){
//                                seekArc2.setProgress(100);
//                            }
//                            else {
//                                seekArc2.setProgress(battery);
//                            }
                            speed.setText(sensors[15]);
                            battery_vol.setText("Battery Voltage: " + sensors[18]);
//                            sensorView10.setText("Battery Percentage: " + sensors[19]);
                            battery_current.setText("Battery Current: " + sensors[20]);
                            battery_balance.setText("Battery Off Bal: " + sensors[21]);
                            battery_temp.setText("Battery Temperature: " + sensors[22]);
                            rangemeter.setText(sensors[23]);
                            battery_serial.setText("Battery Serial: " + sensors[25]);
                            battery_cycle.setText("Battery Cycle: " + sensors[26]);
                            VIN_number.setText("VIN Number: " + sensors[27]);
                            Milleage.setText("Vehicle Mileage: " + sensors[28]);


                            if (sensors[0].equals("1.00") || sensors[0].equals("1")) {
                                if (!seatlockbtn.isSelected()){
                                    seatlockbtn.setSelected(true);
                                }
                            }
                            if (sensors[1].equals("1.00") || sensors[1].equals("1")) {
                                if (!imgPlug.isSelected()){
                                    imgPlug.setSelected(true);
                                }
                            }
                            if (sensors[5].equals("1.00") || sensors[5].equals("1")) {
                                if (!imgRFID.isSelected()){
                                    imgRFID.setSelected(true);
                                }
                            }
                            if (sensors[7].equals("1.00") || sensors[7].equals("1")) {
                                if (!highbeambtn.isSelected()){
                                    highbeambtn.setSelected(true);
                                }
                            }

                        }
                        recDataString.delete(0, recDataString.length());                    //clear all string data
                    }
                }
            }
        };

        Intent intent = getIntent();
        //Get the MAC address from the DeviceListActivty via EXTRA
        address = intent.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();

        //create device and set the MAC address
        BluetoothDevice device = btAdapter.getRemoteDevice(address);
        Log.e("onResume", "onStop " + address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("onResume", " BT " + btSocket.isConnected());
        Log.e("address", "address " + address);
        // Establish the Bluetooth socket connection.
            new Thread() {
                @Override
                public void run() {
                    try {
                        btSocket.connect();
                        isConnected = true;
                        Log.e("onResume", "connect");

                    } catch (IOException e) {
                        e.printStackTrace();


                        if (btSocket != null) //If the btSocket is busy
                        {
                            try {
                                btSocket.close();
                                Log.e("onResume", "close");
                            } catch (IOException e2) {
                                //insert code to deal with this
                            }
                        }
                    }
                }
            }.start();


        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("onStop", "onStop " + address);
        Log.e("onStop", " BT " + btSocket.isConnected());
        try {
            btSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        finish();

//        Log.e("onPause", "onPause");
//        try {
//            btSocket.close();
//            isConnected = false;
//            Log.e("onPause", "close socket");
//            BluetoothDevice device = btAdapter.getRemoteDevice(address);
//            try {
//                btSocket = createBluetoothSocket(device);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    while (true) {
//
//                        try {
//                            Thread.sleep(3000);
//
//                            try {
//                                btSocket.connect();
//
//                                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
//                                            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
//                                            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
//                                            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
//                                    break;
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                }
//            });
//        } catch (IOException e2) {
//            e2.printStackTrace();
//        }


    }



    //Checks that the Android device Bluetooth is available and prompts to be turned on if off
    private void checkBTState() {

        if (btAdapter == null) {
            Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == imgBackward){
            setActive(!imgBackward.isSelected(),imgBackward);
        }
        if (v == seatlockbtn){
            setActive(!seatlockbtn.isSelected(),seatlockbtn);
            mConnectedThread.write("a");
        }
        if (v == imgPlug){
            setActive(!imgPlug.isSelected(),imgPlug);
            mConnectedThread.write("b");

        }
        if (v == imgWarning){
            setActive(!imgWarning.isSelected(),imgWarning);
        }
        if (v == highbeambtn){
            setActive(!highbeambtn.isSelected(),highbeambtn);
            mConnectedThread.write("d");

        }
        if (v == imgForward){
            setActive(!imgForward.isSelected(),imgForward);
        }
        if (v == imgInfo){
            setActive(!imgInfo.isSelected(),imgInfo);
        }
        if (v == imghorn){
            setActive(!imghorn.isSelected(),imghorn);
        }
        if (v == imgRFID){
            setActive(!imgRFID.isSelected(),imgRFID);
            mConnectedThread.write("c");
        }
        if (v == imgkey){
            setActive(!imgkey.isSelected(),imgkey);
        }
    }

    @Override
    public void setTime(String date, String time) {
        tv_date.setText(date);
        tv_time.setText(time);

    }

    @Override
    public void setActive(boolean isActive, ImageView imageView) {
        imageView.setSelected(isActive);

    }

    private void initInstances() {
        //For manual tap icon
        imgBackward.setOnClickListener(this);
        seatlockbtn.setOnClickListener(this);
        imgPlug.setOnClickListener(this);
        imgWarning.setOnClickListener(this);
        highbeambtn.setOnClickListener(this);
        imgForward.setOnClickListener(this);
        imgInfo.setOnClickListener(this);
        imghorn.setOnClickListener(this);
        imgRFID.setOnClickListener(this);
        imgkey.setOnClickListener(this);
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
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    if (isConnected) //If the btSocket is busy
                    {
                        bytes = mmInStream.read(buffer);
                        String readMessage = new String(buffer, 0, bytes);
                        bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                    }
                } catch (IOException e) {

                    break;
                }
            }
        }

        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application1
                Toast.makeText(getBaseContext(), "Connection Failure. Please re-connect.", Toast.LENGTH_LONG).show();
                destroy();
            }
        }
    }

    private void turnScreenOffAndExit() {
        // first lock screen
        turnScreenOff(getApplicationContext());

        // then provide feedback
        ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(50);

        // schedule end of activity
    }

    static void turnScreenOff(final Context context) {
        DevicePolicyManager policyManager = (DevicePolicyManager) context
                .getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName adminReceiver = new ComponentName(context,
                ScreenOffAdminReceiver.class);
        boolean admin = policyManager.isAdminActive(adminReceiver);
        if (admin) {
            Log.i(LOG_TAG, "Going to sleep now.");
            policyManager.lockNow();
        } else {
            Log.i(LOG_TAG, "Not an admin");
            Toast.makeText(context, R.string.device_admin_not_enabled,
                    Toast.LENGTH_LONG).show();
        }
    }
}