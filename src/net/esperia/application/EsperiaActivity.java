package net.esperia.application;

import net.esperia.application.listConnected.ListConnected;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class EsperiaActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		/*Intent intent = new Intent(this, VoteActivity.class);
        startActivity(intent);
        finish();*/

		Button list = (Button)findViewById(R.id.buttonlisteco);
		Button vote = (Button)findViewById(R.id.buttonvote);


		list.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EsperiaActivity.this, ListConnected.class);
		        startActivity(intent);
			}
		});
		
		vote.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EsperiaActivity.this, VoteActivity.class);
		        startActivity(intent);
			}
		});
	}


}