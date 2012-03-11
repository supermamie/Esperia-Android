package net.esperia.application;

import android.app.Activity;
import android.os.Bundle;

public class EsperiaActivity extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        /*Intent intent = new Intent(this, VoteActivity.class);
        startActivity(intent);
        finish();*/
    }
    
    
}