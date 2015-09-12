package com.squgeim.squremote;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class Main extends Activity {

	private Socket socket;
	private int SERVERPORT;
	private String SERVER_IP;
	SharedPreferences sharedPref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		findViewById(R.id.play).setOnTouchListener(new TouchListener());
     	findViewById(R.id.stop).setOnTouchListener(new TouchListener());   	
    	findViewById(R.id.prev).setOnTouchListener(new TouchListener());
    	findViewById(R.id.next).setOnTouchListener(new TouchListener());
    	findViewById(R.id.seek_left).setOnTouchListener(new TouchListener());
    	findViewById(R.id.seek_right).setOnTouchListener(new TouchListener());	
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		initialize();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    		case R.id.eject:
    			proc("ejct");
    			return true;
    		case R.id.abt:
    			AlertDialog.Builder abt_builder = new AlertDialog.Builder(this);
    			abt_builder.setIcon(R.drawable.about)
    				.setMessage("squRemote is a small utility that lets you work around the house with media playing on your computer.\n\nby squGEIm\nsqugeim@outlook.com")
    				.setTitle("About squRemote")
    				.setCancelable(false)
    				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {}
					});
    			AlertDialog about=abt_builder.create();
    			about.show();
    			return true;
    		case R.id.action_settings:
    			Intent intent = new Intent(this, SettingsActivity.class);
    			startActivity(intent);
    			return true;
    		default:
    			return super.onOptionsItemSelected(item);
    	}
    }
	
	private void initialize() {
    	sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    	SERVER_IP = sharedPref.getString("ip", "192.168.0.102");
    	SERVERPORT = Integer.parseInt(sharedPref.getString("port", "9999"));
    }

    class TouchListener implements OnTouchListener {
    	@Override
    	public boolean onTouch(View v, MotionEvent event) {
    		if(event.getAction()==MotionEvent.ACTION_DOWN) {
    			findViewById(v.getId()).setBackgroundColor(0xFF006666);
    		}
    		else if (event.getAction()==MotionEvent.ACTION_UP) {
    			findViewById(v.getId()).setBackgroundColor(0xFF333333);
    			proc((String) v.getTag());
    		}
    		return true;
    	}
    }
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
    	switch(event.getKeyCode()) {
	    	case KeyEvent.KEYCODE_VOLUME_UP:
	    		if(event.getAction()==KeyEvent.ACTION_UP){
	    			proc("vup");
	    		}
	    		return true;
	    	case KeyEvent.KEYCODE_VOLUME_DOWN:
	    		if(event.getAction()==KeyEvent.ACTION_DOWN){
	    			proc("vdwn");
	    		}
	    		return true;
	    	default:
	    		return super.dispatchKeyEvent(event);
    	}
    }
     
    private void proc(String val) {
    	Boolean def = sharedPref.getBoolean("def", true);
    	if(def) new AsyncSend().execute(val);
    	else {
    		String key = sharedPref.getString(val, "def");
    		//Toast.makeText(getBaseContext(), key, Toast.LENGTH_SHORT).show();
    		new AsyncSend().execute((!key.equals("def"))?key:val);
    	}
    }
   
    private class AsyncSend extends AsyncTask<String, Void, String> {
    	
    	@Override
		protected String doInBackground(String... parm) {
    		String val=parm[0];
			try {
	    		InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
				socket = new Socket(serverAddr, SERVERPORT);
				
				try {
					PrintWriter out = new PrintWriter(new BufferedWriter(
							new OutputStreamWriter(socket.getOutputStream())),
							true);
					out.println(val);
					return null;
				} catch (UnknownHostException e) {
					return "Unknown Host Error";
				} catch (IOException e) {
					return "IO Error";
				} catch (Exception e) {
					return "Error";
				}
	
			} catch (UnknownHostException e1) {
				return "The Computer is not ready.";
			} catch (IOException e1) {
				return "The computer is not ready.";
			}
		}
    	
    	@Override
    	protected void onPostExecute(String error){
    		if(error!=null) {
    			Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
    		}
    	}
    }
    
}
