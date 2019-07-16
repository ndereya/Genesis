package com.example.njukiandrew.genesis;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Switch security,bedroom,sittingrm;
    Button BTconnect;

    TextView txtview4;

    String address = null,name = null;

    BluetoothAdapter myBTAdapter = null;
    BluetoothSocket bluetoothSocket =  null;
    Set<BluetoothDevice> pairedDevices;


    static  final UUID myUUID =UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bedroom=(Switch)findViewById(R.id.switch_bedroom);
        sittingrm=(Switch)findViewById(R.id.switch_siitingroom);
        BTconnect=(Button)findViewById(R.id.buttonBT);



        try{setw();}catch (Exception e){}
    }

    private void setw(){

        txtview4 =(TextView)findViewById(R.id.textView4);
        try {
            bluetooth_connect_device();
        } catch (IOException e) {
            e.printStackTrace();
        }

        security=(Switch)findViewById(R.id.switch_secutity);
        security.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (security.isChecked()) {
                    security_led_on("b");
                    Toast.makeText(context, "Security lights turned On", Toast.LENGTH_SHORT).show();

                }else{

                security_led_on("f");
                Toast.makeText(context,"Security lights turned off",Toast.LENGTH_SHORT).show();

            }
            }
        });





    }

    private  void bluetooth_connect_device() throws IOException{
        try{
            myBTAdapter = BluetoothAdapter.getDefaultAdapter();
            address=myBTAdapter.getAddress();
            pairedDevices= myBTAdapter.getBondedDevices();
            if (pairedDevices.size()>0){
                for (BluetoothDevice bt:pairedDevices){
                    address=bt.getAddress().toString();name=bt.getName().toString();
                    Toast.makeText(getApplicationContext(),"connected",Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception we){}
            myBTAdapter=BluetoothAdapter.getDefaultAdapter();//get mobile bt device
            BluetoothDevice dispositivo =myBTAdapter.getRemoteDevice(address);
            bluetoothSocket=dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
            bluetoothSocket.connect();

            try{
                txtview4.setText("BT:"+name+"\n"+address);
                BTconnect.setText("Connected");
                BTconnect.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }catch (Exception e){}

        }


    private void security_led_on(String i){
        try{
            if (bluetoothSocket!=null){
                bluetoothSocket.getOutputStream().write(i.toString().getBytes());
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }



}
