package com.example.rasp_home;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Home extends Activity {

	ImageButton dose1;
	ImageButton dose2;
	ImageButton dose3;
	ImageButton set;
	ImageButton restart;
	
	String DEFAULT_ADDRESS = "mylilraspi.raspctl.com";
	String address = "mylilraspi.raspctl.com";
	int port = 1892;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		dose1 = (ImageButton) findViewById(R.id.dose1);
		dose1.setOnClickListener(dose1_handler);
		
		dose2 = (ImageButton) findViewById(R.id.dose2);
		dose2.setOnClickListener(dose2_handler);
		
		dose3 = (ImageButton) findViewById(R.id.dose3);
		dose3.setOnClickListener(dose3_handler);
		
		set = (ImageButton) findViewById(R.id.set);
		set.setOnClickListener(set_handler);
		
		restart = (ImageButton) findViewById(R.id.restart);
		restart.setOnClickListener(restart_handler);
		

	}

	View.OnClickListener dose1_handler = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			AsyncTaskRunner runner = new AsyncTaskRunner();
			runner.execute("000");
		}
	};
	
	View.OnClickListener dose2_handler = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			AsyncTaskRunner runner = new AsyncTaskRunner();
			runner.execute("001");
		}
	};
	
	View.OnClickListener dose3_handler = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			AsyncTaskRunner runner = new AsyncTaskRunner();
			runner.execute("010");
		}
	};
	
	View.OnClickListener set_handler = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			address = ((EditText)findViewById(R.id.ipaddress)).getText().toString();	
			if(address.equals("")) {
				address = DEFAULT_ADDRESS;
			};
			showToast(address);
		}
		
	};
	
	View.OnClickListener restart_handler = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			AsyncTaskRunner runner = new AsyncTaskRunner();
			runner.execute("111");
		}
	};
	
	private void showToast(String msg) {
		Toast.makeText(getApplicationContext(), msg,
				Toast.LENGTH_SHORT).show();
	}

	private class AsyncTaskRunner extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {
			String res;
			try {
				String read_msg;
				Socket socket;
				socket = new Socket(address, port);
								
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				out.println(params[0]);
		        out.flush();
		        
				BufferedReader inFromServer = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				if (inFromServer.equals("")) {
					System.out.println("empty");
				}
				read_msg = inFromServer.readLine();

				System.out.println("FROM SERVER: '" + read_msg + "'");
				
				socket.close();
				
				res = "Success";
			} catch (UnknownHostException e) {
				System.out.println("UnknownHostException");
				res = "Error";
			} catch (IOException e) {
				System.out.println("IOException");
				res = "Error";
			}
			return res;
		}

		protected void onPostExecute(String result) {
			showToast(result);
		}
	}
}
