package ca.ualberta.cs.views;

import ca.ualberta.cs.R;
import ca.ualberta.cs.R.layout;
import ca.ualberta.cs.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class LocationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location, menu);
		return true;
	}

}
