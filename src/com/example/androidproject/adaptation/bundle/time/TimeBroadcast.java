package com.example.androidproject.adaptation.bundle.time;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TimeBroadcast extends BroadcastReceiver {

	private long diff;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		this.diff = intent.getExtras().getLong("diff");
		
		System.out.println("[diff]received intend msg : " + diff);
	}
	
	public long getDiff() {
		return diff;
	}
}
