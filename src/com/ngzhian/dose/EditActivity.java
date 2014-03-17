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

public class EditActivity extends Activity {
	ArrayAdapter<CharSequence> adapter;
	Intent i;
	View layout;
	EditText editName, editQuantity;
	Spinner editUnit;
	TimePicker editTimePicker;
	String name, unit;
	int quantity, hour, minute, id, position;
	long time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		layout = findViewById(R.id.editLayout);
		editName = (EditText) layout.findViewById(R.id.editName);
		editQuantity = (EditText) layout.findViewById(R.id.editQuantity);
		editUnit = (Spinner) findViewById(R.id.editUnit);
		editTimePicker = (TimePicker) layout.findViewById(R.id.editTimePicker);
		adapter = ArrayAdapter.createFromResource(this, R.array.dosageTypes,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		editUnit.setAdapter(adapter);
		prefillValues(getIntent().getExtras());
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}

	private void prefillValues(Bundle extras) {
		this.name = extras.getString("name");
		this.quantity = extras.getInt("quantity");
		this.unit = extras.getString("unit");
		this.hour = extras.getInt("hour");
		this.minute = extras.getInt("minute");
		this.position = extras.getInt("position");
		editName.setText(this.name);
		editQuantity.setText(String.valueOf(this.quantity));
		for (int i = 0; i < adapter.getCount(); i++) {
			if (adapter.getItem(i).toString().equals(this.unit)) {
				editUnit.setSelection(i);
			}
		}
		editTimePicker.setCurrentHour(this.hour);
		editTimePicker.setCurrentMinute(this.minute);
	}

	public void deleteDose(View view) {
		Intent returnIntent = new Intent();
		returnIntent.putExtra("position", this.position);
		returnIntent.putExtra("delete", true);
		Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
		setResult(RESULT_OK, returnIntent);
		finish();
	}

	public void editDose(View view) {
		if (!checkValues()) return;
		updateValues();
		Intent returnIntent = new Intent();
		returnIntent.putExtra("name", this.name);
		returnIntent.putExtra("quantity", this.quantity);
		returnIntent.putExtra("unit", this.unit);
		returnIntent.putExtra("hour", this.hour);
		returnIntent.putExtra("minute", this.minute);
		returnIntent.putExtra("time", this.time);
		returnIntent.putExtra("position", this.position);
		Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
		setResult(RESULT_OK, returnIntent);
		finish();
	}

	private boolean checkValues() {
		String q = editQuantity.getText().toString();
		String n = editName.getText().toString();
		if (q.isEmpty()) {
			Toast.makeText(this, "Fill in a quantity", Toast.LENGTH_LONG).show();
			editQuantity.requestFocus();
		} else if (n.isEmpty()) {
			Toast.makeText(this, "Fill in a name", Toast.LENGTH_LONG).show();
			editName.requestFocus();
		} else {
			return true;
		}
		return false;
	}

	protected boolean updateValues() {
		this.name = editName.getText().toString();
		this.quantity = Integer.parseInt(editQuantity.getText().toString());
		this.unit = (String) editUnit.getSelectedItem();
		this.hour = editTimePicker.getCurrentHour();
		this.minute = editTimePicker.getCurrentMinute();
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
