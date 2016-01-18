package com.example.wenda.tarucnfc.Activitys;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wenda.tarucnfc.R;

public class PinEntryActivity extends BaseActivity {

	String userEntered;
	String userPin="8888";
	
	final int PIN_LENGTH = 4;
	boolean keyPadLockedFlag = false;
	Context appContext;
	
	TextView titleView;
	
	TextView pinBox0;
	TextView pinBox1;
	TextView pinBox2;
	TextView pinBox3;
	
	TextView [] pinBoxArray;
	
	TextView statusView;

	TextView mTextView0;
	TextView mTextView1;
	TextView mTextView2;
	TextView mTextView3;
	TextView mTextView4;
	TextView mTextView5;
	TextView mTextView6;
	TextView mTextView7;
	TextView mTextView8;
	TextView mTextView9;

	Button buttonExit;
	Button buttonDelete;

	public static final String KEY_PAYMENT = "selected";
	private String selectType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		appContext = this;
		userEntered = "";
		
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_pin_entry);
		
		//buttonExit = (Button) findViewById(R.id.buttonExit);
		//buttonExit.setOnClickListener(new View.OnClickListener() {
		//    public void onClick(View v) {
		    
		    	//Exit app
		//    	Intent i = new Intent();
		//    	i.setAction(Intent.ACTION_MAIN);
		//    	i.addCategory(Intent.CATEGORY_HOME);
		//    	appContext.startActivity(i);
		//    	finish();
		        
		//    }
		    
		//    }
		//);
		//buttonExit.setTypeface(xpressive);
		
		
		buttonDelete = (Button) findViewById(R.id.deleteButton);
		buttonDelete.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	if (keyPadLockedFlag == true) {
		    		return;
		    	}
		    	if (userEntered.length()>0) {
		    		userEntered = userEntered.substring(0,userEntered.length()-1);
		    		pinBoxArray[userEntered.length()].setText("");
		    	}
		    }
		    }
		);
		
		titleView = (TextView)findViewById(R.id.titleBox);
		//titleView.setTypeface(xpressive);
		
		pinBox0 = (TextView)findViewById(R.id.pinBox0);
		pinBox1 = (TextView)findViewById(R.id.pinBox1);
		pinBox2 = (TextView)findViewById(R.id.pinBox2);
		pinBox3 = (TextView)findViewById(R.id.pinBox3);
		
		pinBoxArray = new TextView[PIN_LENGTH];
		pinBoxArray[0] = pinBox0;
		pinBoxArray[1] = pinBox1;
		pinBoxArray[2] = pinBox2;
		pinBoxArray[3] = pinBox3;
		
		
		
		statusView = (TextView) findViewById(R.id.statusMessage);
		//statusView.setTypeface(xpressive);
	
		View.OnClickListener pinButtonHandler = new View.OnClickListener() {
		    public void onClick(View v) {
		    	if (keyPadLockedFlag == true) {
		    		return;
		    	}

		    	TextView getNumber = (TextView)v;

		    	if (userEntered.length()<PIN_LENGTH) {
		    		userEntered = userEntered + getNumber.getText();
		    		Log.v("PinView", "User entered="+userEntered);
		    		
		    		//Update pin boxes
		    		pinBoxArray[userEntered.length()-1].setText("8");
		    		
		    		if (userEntered.length()==PIN_LENGTH) {
		    			//Check if entered PIN is correct
		    			if (userEntered.equals(userPin)) {
		    				statusView.setTextColor(Color.GREEN);
		    				statusView.setText("Correct PIN Code");
		    				Log.v("PinView", "Correct PIN");

							selectType = getIntent().getStringExtra(KEY_PAYMENT);
							if (selectType.equals("payment")) {

							} else if (selectType.equals("topUp")) {

							} else if (selectType.equals("transfer")) {

							}

		    			} else {
		    				statusView.setTextColor(Color.RED);
		    				statusView.setText("Wrong PIN Code!");
		    				keyPadLockedFlag = true;
		    				Log.v("PinView", "Wrong PIN");
		    				
		    				new LockKeyPadOperation().execute("");
		    			}
		    		}	
		    	} else {
		    		//Roll over
		    		pinBoxArray[0].setText("");
		    		pinBoxArray[1].setText("");
		    		pinBoxArray[2].setText("");
		    		pinBoxArray[3].setText("");
		    		
		    		userEntered = "";
		    		
		    		statusView.setText("");
		    		
		    		userEntered = userEntered + getNumber.getText();
		    		Log.v("PinView", "User entered="+userEntered);
		    		
		    		//Update pin boxes
		    		pinBoxArray[userEntered.length()-1].setText("8");
		    		
		    	}
		    }
		  };
		
		
		mTextView0 = (TextView) findViewById(R.id.num0);
		mTextView0.setOnClickListener(pinButtonHandler);

		mTextView1 = (TextView) findViewById(R.id.num1);
		mTextView1.setOnClickListener(pinButtonHandler);

		mTextView2 = (TextView) findViewById(R.id.num2);
		mTextView2.setOnClickListener(pinButtonHandler);

		mTextView3 = (TextView) findViewById(R.id.num3);
		mTextView3.setOnClickListener(pinButtonHandler);

		mTextView4 = (TextView) findViewById(R.id.num4);
		mTextView4.setOnClickListener(pinButtonHandler);

		mTextView5 = (TextView) findViewById(R.id.num5);
		mTextView5.setOnClickListener(pinButtonHandler);

		mTextView6 = (TextView) findViewById(R.id.num6);
		mTextView6.setOnClickListener(pinButtonHandler);

		mTextView7 = (TextView) findViewById(R.id.num7);
		mTextView7.setOnClickListener(pinButtonHandler);

		mTextView8 = (TextView) findViewById(R.id.num8);
		mTextView8.setOnClickListener(pinButtonHandler);

		mTextView9 = (TextView) findViewById(R.id.num9);
		mTextView9.setOnClickListener(pinButtonHandler);

		buttonDelete = (Button) findViewById(R.id.deleteButton);
		
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		//App not allowed to go back to Parent activity until correct pin entered.
		return;
		//super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_pin_entry, menu);
		return true;
	}

	
	private class LockKeyPadOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
              for(int i=0;i<2;i++) {
                  try {
                      Thread.sleep(1000);
                  } catch (InterruptedException e) {
                      // TODO Auto-generated catch block
                      e.printStackTrace();
                  }
              }

              return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
                statusView.setText("");

            //Roll over
            pinBoxArray[0].setText("");
                pinBoxArray[1].setText("");
                pinBoxArray[2].setText("");
                pinBoxArray[3].setText("");

                userEntered = "";

                keyPadLockedFlag = false;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
  }
	
	
}
