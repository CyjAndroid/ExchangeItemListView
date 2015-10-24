package com.itheima.demo;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends Activity {

	private ArrayList<String> list = new ArrayList<String>();
	private ListView lv;
	private ListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);
		for (int i = 0; i < 30; i++) {
			list.add("test---" + i);
		}
		lv = (ListView) findViewById(R.id.lv);
		adapter = new ListAdapter(list,lv,this);
		lv.setAdapter(adapter);

	}



}
