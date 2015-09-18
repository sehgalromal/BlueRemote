package com.alex.blueremote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.support.v7.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.view.ViewGroup;

public class device_list_activity_2 extends AppCompatActivity {

	BT_global_variables BT_global_variables_1;
	
	BroadcastReceiver bt_device_found_receiver;
	BroadcastReceiver bt_discovery_started_receiver;
	BroadcastReceiver bt_discovery_finished_receiver;
	
	Set<BluetoothDevice> pairedDevices;
	Set<BluetoothDevice> discoveredDevices = new HashSet<BluetoothDevice>();
	
	ExpandableListView elv_1;
	    
    ArrayList<HashMap<String, String>> group_list=new ArrayList<HashMap<String, String>>();
    
    ArrayList<ArrayList<HashMap<String, custom_BluetoothDevice>>> group_child_list=new ArrayList<ArrayList<HashMap<String, custom_BluetoothDevice>>>(); 
    ArrayList<HashMap<String, custom_BluetoothDevice>> paired_device_list = new ArrayList<HashMap<String, custom_BluetoothDevice>>();
    ArrayList<HashMap<String, custom_BluetoothDevice>> unpaired_device_list = new ArrayList<HashMap<String, custom_BluetoothDevice>>();
    
    String[] mGroupFrom;
    int[] mGroupTo;
    
    String[] mChildFrom;
    int[] mChildTo;
    
    SimpleExpandableListAdapter elv_adapter;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dl_layout_2);
		
		elv_1=(ExpandableListView)findViewById(R.id.eList_1);
		BT_global_variables_1 = (BT_global_variables)getApplicationContext();
						
		//*
	    pairedDevices = BT_global_variables_1.getBtAdapter().getBondedDevices();
	    
	    Log.d(BLUETOOTH_SERVICE,"Paired Devices: "+pairedDevices.size());
	    	    
	    if (pairedDevices.size() > 0) 
	    {
	    	for (BluetoothDevice device : pairedDevices) 
	    	{
//	    		Log.d(BLUETOOTH_SERVICE,""+(new custom_BluetoothDevice(device)));
	    		Log.d(BLUETOOTH_SERVICE,device.getName()+" "+device.getAddress());

	    		HashMap<String, custom_BluetoothDevice> temp = new HashMap<String, custom_BluetoothDevice>();
	        	temp.put("group_child_item", new custom_BluetoothDevice(device,false));
	        	paired_device_list.add(temp);
	    	}
	    }
	    //*/
	    	    
//	    HashMap<String, String> temp = new HashMap<String, String>();
//    	temp.put("group_item_1","Paired Devices");
//    	temp.put("group_item_2","Un-Paired Devices");
//	    group_list.add(temp);
	    
	    {
	    	HashMap<String, String> temp = new HashMap<String, String>();
	    	temp.put("group_item","Paired Devices");
		    group_list.add(temp);    
	    }
	    
	    {
	    	HashMap<String, String> temp = new HashMap<String, String>();
			temp.put("group_item","Un-Paired Devices");
		    group_list.add(temp);
	    }

	    group_child_list.add(paired_device_list);
	    group_child_list.add(unpaired_device_list);
	    
	    mGroupFrom=new String[] {"group_item"};
//	    mGroupFrom=new String[] {"group_item_1","group_item_2"};
	    mGroupTo=new int[] { R.id.elv_group };
	    
	    mChildFrom=new String[] {"group_child_item"};
	    mChildTo=new int[] { R.id.elv_group_child};
	    
	    elv_adapter = new SimpleExpandableListAdapter(
	                    this,	
	                    
	                    (List<HashMap<String, String>>)group_list,
	                    R.layout.elv_textview_group,             
	                    mGroupFrom,  			
	                    mGroupTo,    		
	                    
	                    (List<ArrayList<HashMap<String, custom_BluetoothDevice>>>)group_child_list,
	                    R.layout.elv_textview_group_child,        
	                    mChildFrom,      	
	                    mChildTo     		
	                	)
	    {
		    
            @Override
            public View getChildView(int groupPosition, int childPosition,
                    boolean isLastChild, View convertView, ViewGroup parent) {

            	View v;
            	
                if (convertView == null)
                {
                    v = newChildView(isLastChild, parent);
                }
                else 
                {
                    v = convertView;
                }
                
                
                int len = mChildTo.length;

                for (int i = 0; i < len; i++) 
                {
                    TextView tv = (TextView)v.findViewById(mChildTo[i]);
                    
                    if (tv != null) 
                    {
                        tv.setText(group_child_list.get(groupPosition).get(childPosition).get(mChildFrom[i]).toString());
                        
//                        if(groupPosition==1)
//                        {
//                        	tv.setBackgroundColor(Color.rgb(50, 100, 150));
//                        }
                        
                        if(group_child_list.get(groupPosition).get(childPosition).get("group_child_item").isDiscovered == true)
                        {
                        	Log.e("Painted", ""+group_child_list.get(groupPosition).get(childPosition).get("group_child_item").toString()+"\n"
                        						+group_child_list.get(groupPosition).get(childPosition).get("group_child_item").isDiscovered);
                        	
                        	tv.setBackgroundColor(Color.rgb(0x8F,0xE6,0x5B));
                        }
                        
                    }
                }
                
                return v;
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded,
                    View convertView, ViewGroup parent) {
               
                TextView tv = (TextView) super.getGroupView(groupPosition, isExpanded, convertView, parent);
                
                return tv;
            }

	    };
	    
	    elv_1.setAdapter(elv_adapter);
	    
//	    Log.e("GroupCount",elv_1.getCount()+"");
//	    elv_1.expandGroup(0,true);
//	    elv_1.expandGroup(1,true);
	    
	    elv_1.setOnGroupClickListener(new OnGroupClickListener() {

	    	@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
	    		
	    		if(elv_1.isGroupExpanded(groupPosition))
	    		{
	    			elv_1.collapseGroup(groupPosition);
//	    			parent.refreshDrawableState();
//	    			parent.invalidate();
	    		}
	    		else
	    		{
	    			elv_1.expandGroup(groupPosition);
	    			
//	    			elv_adapter.notifyDataSetInvalidated();
//	    			parent.invalidate();
	    		}
	    		
				return true;
			}

         });

	    elv_1.setOnChildClickListener(new OnChildClickListener() {

	    	@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {

//				String mac_address=((TextView)v.findViewById(R.id.elv_group_child)).getText().toString();
//				mac_address=mac_address.substring(mac_address.length()-17);
				
				String mac_address=((custom_BluetoothDevice)group_child_list.get(groupPosition).get(childPosition).get("group_child_item"))
									.BT_Device.getAddress();
				
	    		Log.e("MAC Address",mac_address+" "+group_child_list.get(groupPosition).get(childPosition).get("group_child_item").isDiscovered);
	    		
	            return true;
			}

         });

	    //*
	    bt_device_found_receiver = new BroadcastReceiver() {
	        
			@Override
			public void onReceive(Context context, Intent intent) {
			    
				String action = intent.getAction();

				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            	
	            	Log.d(BLUETOOTH_SERVICE,"New BT Device Found");

	            	BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	                
	                discoveredDevices.add(device);
	                //Log.d(BLUETOOTH_SERVICE,device.getName()+" "+device.getAddress());
	                
	                if(pairedDevices.contains(device)==true)
	                {	                	
	                	for(int count_0=0;count_0<pairedDevices.size();count_0++)
	                	{
	                		if(group_child_list.get(0).get(count_0).get("group_child_item").equals(new custom_BluetoothDevice(device)))
	                		{
	                			group_child_list.get(0).get(count_0).get("group_child_item").setDiscovered(true);
	                			break;
	                		}
	                	}
	                	
//	                	elv_adapter.notifyDataSetInvalidated();
	    				elv_adapter.notifyDataSetChanged();

	                }
	                else
	                {
	                	HashMap<String, custom_BluetoothDevice> temp = new HashMap<String, custom_BluetoothDevice>();
			        	temp.put("group_child_item", new custom_BluetoothDevice(device,true));
			        	unpaired_device_list.add(temp);
			        	
			        	elv_adapter.notifyDataSetChanged();
	                }
	                
	            }
	        	
			}
	    };
	    
	    bt_discovery_started_receiver = new BroadcastReceiver() {
	        
			@Override
			public void onReceive(Context context, Intent intent) {
			    
				String action = intent.getAction();
	            
				if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
	            	
					Log.d(BLUETOOTH_SERVICE,"BT Discovery Started");
	            }
	        	
			}
	    };
	    
	    bt_discovery_finished_receiver = new BroadcastReceiver() {
	        
			@Override
			public void onReceive(Context context, Intent intent) {
			    
				String action = intent.getAction();
	            
				if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
	            	
					Log.d(BLUETOOTH_SERVICE,"BT Discovery Finished");
					Log.d(BLUETOOTH_SERVICE,"Discovered Devices: "+discoveredDevices.size());
				    
					if (discoveredDevices.size() > 0) 
				    {
					    for (BluetoothDevice device : discoveredDevices) 
		    	    	{
		    	    		Log.d(BLUETOOTH_SERVICE,device.getName()+" "+device.getAddress());
		    	    	}
				    }
					
//					if(BtAdapter.isDiscovering());
//					{
//						Log.d(BLUETOOTH_SERVICE,"BT Discovery Cancelled:"+BtAdapter.cancelDiscovery());
//					}
					
				}
	        	
			}
	    };
	    
	    IntentFilter bt_device_found_intent_filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	    registerReceiver(bt_device_found_receiver, bt_device_found_intent_filter); 
	    
	    IntentFilter bt_discovery_started_intent_filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
	    registerReceiver(bt_discovery_started_receiver, bt_discovery_started_intent_filter);
	    
	    IntentFilter bt_discovery_finished_intent_filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
	    registerReceiver(bt_discovery_finished_receiver, bt_discovery_finished_intent_filter);

	    BT_global_variables_1.getBtAdapter().startDiscovery();
//	    Log.d(BLUETOOTH_SERVICE,"BT Discovery Started:"+BtAdapter.startDiscovery());
	    //Log.d(BLUETOOTH_SERVICE,"BT isDiscovering:"+BtAdapter.isDiscovering());
	    //*/
	    
	}
		
	@Override
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
	protected void onDestroy ()
	{
		super.onDestroy();
		
		if(BT_global_variables_1.getBtAdapter().isDiscovering())
		{
			Log.d(BLUETOOTH_SERVICE,"BT Discovery Cancelled:"+BT_global_variables_1.getBtAdapter().cancelDiscovery());
		}
		
		//unregister receivers
		unregisterReceiver(bt_device_found_receiver);
		unregisterReceiver(bt_discovery_started_receiver);
		unregisterReceiver(bt_discovery_finished_receiver);

	}	
}