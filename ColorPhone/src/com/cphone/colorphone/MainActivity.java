package com.cphone.colorphone;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity 
{
	private EditText ringOne;
	private EditText ringBox;
	private EditText ringOneAdv;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button button = (Button) findViewById(R.id.ring_set);
        ringOne = (EditText) findViewById(R.id.ring_one);
        ringBox = (EditText) findViewById(R.id.ring_box);
        ringOneAdv = (EditText) findViewById(R.id.ring_one_adv);
        button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				PreferenceUtil.putStringFromSharedPreferences(MainActivity.this, PreferenceUtil.PERFER_KEY_COLOR, ringOne.getText().toString());
				PreferenceUtil.putStringFromSharedPreferences(MainActivity.this, PreferenceUtil.PERFER_KEY_BOX, ringBox.getText().toString());
				PreferenceUtil.putStringFromSharedPreferences(MainActivity.this, PreferenceUtil.PERFER_KEY_COLOR_ADV, ringOneAdv.getText().toString());
				Toast.makeText(MainActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
			}
		});
    }
}