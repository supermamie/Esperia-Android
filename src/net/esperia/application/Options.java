package net.esperia.application;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Options extends PreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.options);

		//SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		

	}


}
