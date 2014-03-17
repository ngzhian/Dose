package com.ngzhian.dose;

import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddActivity extends Activity {
	ArrayAdapter<CharSequence> adapter;
	Intent i;
	View layout;
	EditText addName, addQuantity;
	Spinner addUnit;
	TimePicker addTimePicker;
	String name, unit;
	int quantity, hour, minute, id;
	long time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		layout = findViewById(R.id.addLayout);
		addName = (EditText) layout.findViewById(R.id.addName);
		addQuantity = (EditText) layout.findViewById(R.id.addQuantity);
		addUnit = (Spinner) findViewById(R.id.addUnit);
		addTimePicker = (TimePicker) layout.findViewById(R.id.addTimePicker);
		adapter = ArrayAdapter.createFromResource(this, R.array.dosageTypes,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		addUnit.setAdapter(adapter);
	}

	public void addDose(View view) {
		if (!checkValues()) {
			return;
		}
		updateValues();
		Intent returnIntent = new Intent();
		returnIntent.putExtra("name", this.name);
		returnIntent.putExtra("quantity", this.quantity);
		returnIntent.putExtra("unit", this.unit);
		returnIntent.putExtra("hour", this.hour);
		returnIntent.putExtra("minute", this.minute);
		returnIntent.putExtra("time", this.time);
		Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
		setResult(RESULT_OK, returnIntent);
		finish();
	}

	private boolean checkValues() {
		String n = addName.getText().toString();
		String q = addQuantity.getText().toString();
		if (n.isEmpty()) {
			Toast.makeText(this, "Fill in a name", Toast.LENGTH_SHORT).show();
			addName.requestFocus();
		} else if (q.isEmpty()) {
			Toast.makeText(this, "Fill in a quantity", Toast.LENGTH_SHORT).show();
			addQuantity.requestFocus();
		} else {
			return true;
		}
		return false;
	}

	protected boolean updateValues() {
		this.name = addName.getText().toString();
		this.quantity = Integer.parseInt(addQuantity.getText().toString());
		this.unit = (String) addUnit.getSelectedItem();
		this.hour = addTimePicker.getCurrentHour();
		this.minute = addTimePicker.getCurrentMinute();
		this.time = getMillis();
		return true;
	}

	protected long getMillis() {
		GregorianCalendar now = new GregorianCalendar();
		GregorianCalendar set = new GregorianCalendar(
				now.get(GregorianCalendar.YEAR),
				now.get(GregorianCalendar.MONTH),
				now.get(GregorianCalendar.DAY_OF_MONTH), this.hour, this.minute);
		long time = set.getTimeInMillis();
		if (now.getTimeInMillis() > time)
			time += DateUtils.DAY_IN_MILLIS;
		return time;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
