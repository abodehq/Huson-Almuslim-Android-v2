package com.isslam.husonmuslim;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class ContactusActivity extends Activity {

	private EditText emailSubject = null;
	private EditText emailBody = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ly_contact_us);
		ActionBar actionbar;
		actionbar = getActionBar();
		actionbar.setTitle(getResources().getString(R.string.setting_contact));
		actionbar.setIcon(R.drawable.ic_action_email);
		emailSubject = (EditText) findViewById(R.id.subject);
		emailBody = (EditText) findViewById(R.id.emailBody);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.contact_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_clear:

			emailBody.setText("");
			emailSubject.setText("");
			break;
		case R.id.menu_send:
			String to = "abed_q@hotmail.com";
			String subject = emailSubject.getText().toString();
			String message = emailBody.getText().toString();

			Intent email = new Intent(Intent.ACTION_SEND);
			email.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
			email.putExtra(Intent.EXTRA_SUBJECT, subject);
			email.putExtra(Intent.EXTRA_TEXT, message);

			// need this to prompts email client only
			email.setType("message/rfc822");

			startActivity(Intent.createChooser(email, "Choose an Email client"));

			break;
		}
		return true;
	}

}
