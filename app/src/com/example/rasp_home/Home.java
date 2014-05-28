package com.example.rasp_home;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Home extends Activity {

	Button connect;
	Button set;
	
	String DEFAULT_ADDRESS = "mylilraspi.raspctl.com";
	String address = "mylilraspi.raspctl.com";
	int port = 1892;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		connect = (Button) findViewById(R.id.connect);
		connect.setOnClickListener(connect_handler);
		
		set = (Button) findViewById(R.id.set);
		set.setOnClickListener(set_handler);

	}

	View.OnClickListener connect_handler = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			AsyncTaskRunner runner = new AsyncTaskRunner();
			runner.execute();
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
	
	private void showToast(String msg) {
		Toast.makeText(getApplicationContext(), msg,
				Toast.LENGTH_SHORT).show();
	}

	private class AsyncTaskRunner extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {
			String res;
			try {
				String modifiedSentence;
				Socket clientSocket;
				clientSocket = new Socket(address, port);

				BufferedReader inFromServer = new BufferedReader(
						new InputStreamReader(clientSocket.getInputStream()));
				if (inFromServer.equals("")) {
					System.out.println("empty");
				}
				modifiedSentence = inFromServer.readLine();

				System.out.println("FROM SERVER: >" + modifiedSentence + "<");
				clientSocket.close();
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
