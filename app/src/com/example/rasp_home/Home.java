package com.example.rasp_home;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Locale;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends Activity implements ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
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
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			Fragment fragment = null;
			switch(position){
				case 0:		
					fragment = HomeFragment.newInstance(position + 1);
					break;
				case 1:
					fragment = Log1Fragment.newInstance(position + 1);
					break;
				case 2:
					fragment = Log2Fragment.newInstance(position + 1);
					break;
					}
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A fragment containing the home view.
	 */
	public static class HomeFragment extends Fragment {

		private ImageButton dose1;
		private ImageButton dose2;
		private ImageButton dose3;
		private ImageButton set;
		private ImageButton restart;
		private ImageButton refresh;
		private TextView status;
		
		private static final String DEFAULT_ADDRESS = "mylilraspi.raspctl.com";
		private String address = "mylilraspi.raspctl.com";
		private static final int port = 1892;
		
		private View rootView;
		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static HomeFragment newInstance(int sectionNumber) {
			HomeFragment fragment = new HomeFragment();
			return fragment;
		}

		public HomeFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_home, container,
					false);
			
			dose1 = (ImageButton) rootView.findViewById(R.id.dose1);
			dose1.setOnClickListener(dose1_handler);
			
			dose2 = (ImageButton) rootView.findViewById(R.id.dose2);
			dose2.setOnClickListener(dose2_handler);
			
			dose3 = (ImageButton) rootView.findViewById(R.id.dose3);
			dose3.setOnClickListener(dose3_handler);
			
			set = (ImageButton) rootView.findViewById(R.id.save_button);
			set.setOnClickListener(set_handler);
			
			refresh = (ImageButton) rootView.findViewById(R.id.refresh);
			refresh.setOnClickListener(refresh_handler);
			
			restart = (ImageButton) rootView.findViewById(R.id.restart);
			restart.setOnClickListener(restart_handler);

			status = (TextView) rootView.findViewById(R.id.status);
			
			return rootView;
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
				address = ((EditText)rootView.findViewById(R.id.ipaddress)).getText().toString();	
				if(address.equals("")) {
					address = DEFAULT_ADDRESS;
				};
				showToast(address);
			}
			
		};
		
		View.OnClickListener refresh_handler = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AsyncTaskRunner runner = new AsyncTaskRunner();
				runner.execute("101");
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
			Toast.makeText(getActivity(), msg,
					Toast.LENGTH_SHORT).show();
		}

		private class AsyncTaskRunner extends AsyncTask<String, String, String> {
			@Override
			protected String doInBackground(String... params) {
				String res;
				try {
					String read_msg;
					Socket socket;
					socket = new Socket();
					
					SocketAddress sockaddr = new InetSocketAddress(address, port);
					
					try{
						socket.connect(sockaddr, 500);
					} catch (SocketTimeoutException e){
						
					}
					
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
					
					res = params[0];
				} catch (UnknownHostException e) {
					System.out.println("UnknownHostException");
					if(params[0].equals("101")){
						res = "101_Error";
					} else {
						res = "Error";
					}
				} catch (IOException e) {
					System.out.println("IOException");
					if(params[0].equals("101")){
						res = "101_Error";
					} else {
						res = "Error";
					}
				}
				return res;
			}

			protected void onPostExecute(String result) {
				if(!result.equals("101") && !result.equals("101_Error") && !result.equals("Error")){
					System.out.println(result);
					showToast("Success");
				} else {
					if(result.equals("101_Error") || result.equals("Error")){
						status.setText("offline");
					} else {
						status.setText("online");
					}
				}
			}
		}
	}

	/**
	 * A fragment containing the Log1 view.
	 */
	public static class Log1Fragment extends Fragment {

		private String address = "mylilraspi.raspctl.com";
		private static final int port = 1892;
		private TextView log1;
		private String log_msg = "";
		private Boolean running = false;
		private final static int LOGLEN = 25; 
		
		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static Log1Fragment newInstance(int sectionNumber) {
			Log1Fragment fragment = new Log1Fragment();
			return fragment;
		}

		public Log1Fragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_log1, container,
					false);
			
			log1 = (TextView) rootView.findViewById(R.id.log1);
			
			AsyncTaskRunner runner = new AsyncTaskRunner();
			if(!running) {
				runner.execute("100");
				running = true;
			}
			return rootView;
		}
		
		@Override
		public void onResume(){
			System.out.println("log1 onresume");
			running = false;
			super.onResume();
		}
		
		private class AsyncTaskRunner extends AsyncTask<String, String, String> {
			@Override
			protected String doInBackground(String... params) {
				String res;
				try {
					String read_msg;
					Socket socket;
					socket = new Socket();
					
					SocketAddress sockaddr = new InetSocketAddress(address, port);
					
					try{
						socket.connect(sockaddr, 500);
					} catch (SocketTimeoutException e){
						
					}
					
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					out.println(params[0]);
			        out.flush();
			        
					BufferedReader inFromServer = new BufferedReader(
							new InputStreamReader(socket.getInputStream()));
					if (inFromServer.equals("")) {
						System.out.println("empty");
					}
					
					
					read_msg = inFromServer.readLine();
					
					String log1 = "";
					
					for(int i=0; i<LOGLEN; i++){
						log1 = log1 + inFromServer.readLine() + System.getProperty("line.separator");;		
					}
					log1 = log1 + inFromServer.readLine();
					//System.out.println("log1: " + log1);
					log_msg = log1;
					
					System.out.println("FROM SERVER: '" + read_msg + "'");
					
					socket.close();
					
					res = params[0];
				} catch (UnknownHostException e) {
					System.out.println("UnknownHostException");
					if(params[0].equals("101")){
						res = "101_Error";
					} else {
						res = "Error";
					}
				} catch (IOException e) {
					System.out.println("IOException");
					if(params[0].equals("101")){
						res = "101_Error";
					} else {
						res = "Error";
					}
				}
				return res;
			}

			protected void onPostExecute(String result) {
				if(!result.equals("101") && !result.equals("101_Error") && !result.equals("Error")){
					log1.setText(log_msg);
				} 
			}
		}
	}
	
	/**
	 * A fragment containing the Log2 view.
	 */
	public static class Log2Fragment extends Fragment {

		private String address = "mylilraspi.raspctl.com";
		private static final int port = 1892;
		private TextView log2;
		private String log_msg = "";
		private Boolean running = false;
		private final static int LOGLEN = 29; 
		
		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static Log2Fragment newInstance(int sectionNumber) {
			Log2Fragment fragment = new Log2Fragment();
			return fragment;
		}

		public Log2Fragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_log2, container,
					false);
			
			log2 = (TextView) rootView.findViewById(R.id.log2);
			
			AsyncTaskRunner runner = new AsyncTaskRunner();
			System.out.println(running);
			if(!running){
				runner.execute("110");
				running = true;
			}
			return rootView;
		}
		
		@Override
		public void onResume(){
			System.out.println("log2 onresume");
			running = false;
			super.onResume();
		}
		
		private class AsyncTaskRunner extends AsyncTask<String, String, String> {
			@Override
			protected String doInBackground(String... params) {
				String res;
				try {
					String read_msg;
					Socket socket;
					socket = new Socket();
					
					SocketAddress sockaddr = new InetSocketAddress(address, port);
					
					try{
						socket.connect(sockaddr, 500);
					} catch (SocketTimeoutException e){
						
					}
					
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					out.println(params[0]);
			        out.flush();
			        
					BufferedReader inFromServer = new BufferedReader(
							new InputStreamReader(socket.getInputStream()));
					if (inFromServer.equals("")) {
						System.out.println("empty");
					}
					
					
					read_msg = inFromServer.readLine();
					
					String log2 = "";
					
					for(int i=0; i<LOGLEN; i++){
						log2 = log2 + inFromServer.readLine() + System.getProperty("line.separator");;		
					}
					log2 = log2 + inFromServer.readLine();
					//System.out.println("log2: " + log2);
					log_msg = log2;
					
					System.out.println("FROM SERVER: '" + read_msg + "'");
					
					socket.close();
					
					res = params[0];
				} catch (UnknownHostException e) {
					System.out.println("UnknownHostException");
					if(params[0].equals("101")){
						res = "101_Error";
					} else {
						res = "Error";
					}
				} catch (IOException e) {
					System.out.println("IOException");
					if(params[0].equals("101")){
						res = "101_Error";
					} else {
						res = "Error";
					}
				}
				return res;
			}

			protected void onPostExecute(String result) {
				if(!result.equals("101") && !result.equals("101_Error") && !result.equals("Error")){
					log2.setText(log_msg);
				} 
			}
		}
	}

}
