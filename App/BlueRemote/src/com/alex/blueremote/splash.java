package com.alex.blueremote;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class splash extends AppCompatActivity {
	
	BlueRemote_global_variables global_variables_object;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		this.getSupportActionBar().hide();
		
//		ImageView imageView = (ImageView) findViewById(R.id.iv);
//		imageView.setImageResource(R.drawable.logo);
		
		global_variables_object = (BlueRemote_global_variables)getApplicationContext();
		global_variables_object.setBtAdapter(BluetoothAdapter.getDefaultAdapter());
		
		if(global_variables_object.getBtAdapter() == null) 
		{
		    Toast.makeText(getApplicationContext(), "No BlueTooth Hardware Detected.\nApp Exited", Toast.LENGTH_LONG).show();
		    
		    finish();
		}
		
		//*
		//Direct Turn On BT by App 
		if (global_variables_object.getBtAdapter().isEnabled()==false) 
		{
			global_variables_object.getBtAdapter().enable();
		}

		new Thread(){
			
			public void run()
			{
				try
				{
					sleep(1000);
					
					while(global_variables_object.getBtAdapter().getState()==BluetoothAdapter.STATE_TURNING_ON)
					{
						//Log.d(BLUETOOTH_SERVICE, "Bluetooth turning On.");
					}
					Log.d(BLUETOOTH_SERVICE, "Bluetooth turned On.");
				    
					startActivity(new Intent(".MainActivity"));
//					startActivity(new Intent(".HexBoard"));
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();		
				}
			}
		}.start();
			    
	    //*/
	    
	    /*
		//Request User to turn on BT 
	    //Can not turned off
	    if (!global_variables_object.getBtAdapter().isEnabled()) 
		{
			Log.d(BLUETOOTH_SERVICE, "Bluetooth is Off.");
			Log.d(BLUETOOTH_SERVICE, "Turning on Bluetoooth.");
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, BT_turn_on_fragment_request_code);
		    //startActivity(enableBtIntent);
		    
		    while(global_variables_object.getBtAdapter().getState()==BluetoothAdapter.STATE_TURNING_ON)
			{
				//Log.d(BLUETOOTH_SERVICE, "Bluetooth turning On.");
			}		
		}
		else
		{
			Log.d(BLUETOOTH_SERVICE, "Bluetooth is On.");
		}
		//*/
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		this.finish();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed ()
	{
		finish();
	}
	
	@Override
	protected void onDestroy ()
	{
		super.onDestroy();
		Log.e("Splash", "Splashing out");
	}
	
}