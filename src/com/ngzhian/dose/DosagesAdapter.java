package com.ngzhian.dose;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DosagesAdapter extends ArrayAdapter<Dosage> {
	public DosagesAdapter(Context context) {
		super(context, R.layout.dose_row_layout);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Dosage dosage = getItem(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.dose_row_layout, null);
		}
		TextView tvName = (TextView) convertView.findViewById(R.id.rowName);
		TextView tvQuantity = (TextView) convertView
				.findViewById(R.id.rowQuantity);
		TextView tvUnit = (TextView) convertView.findViewById(R.id.rowUnit);
		TextView tvTime = (TextView) convertView.findViewById(R.id.rowTime);
		TextView tvTimeUnit = (TextView) convertView
				.findViewById(R.id.rowTimeUnit);
		tvName.setText(dosage.getName());
		tvQuantity.setText(String.valueOf(dosage.getQuantity()));
		tvUnit.setText(dosage.getUnit());
		String[] timeString = getTimeString(dosage.getTime());
		tvTime.setText(timeString[0]);
		tvTimeUnit.setText(timeString[1]);
		return convertView;
	}

	private String[] getTimeString(long time) {
		String[] s = new String[2];
		long estd = time - System.currentTimeMillis();
		if (estd <= 0) {
			s[0] = "V";
			s[1] = "just now";
		} else if (estd < (60 * DateUtils.SECOND_IN_MILLIS)) {
			s[0] = "< " + String.valueOf(estd / DateUtils.SECOND_IN_MILLIS + 1);
			s[1] = "secs";
		} else if (estd < (60 * DateUtils.MINUTE_IN_MILLIS)) {
			s[0] = "< " + String.valueOf(estd / DateUtils.MINUTE_IN_MILLIS + 1);
			s[1] = "mins";
		} else if (estd < (24 * DateUtils.HOUR_IN_MILLIS)) {
			s[0] = "< " +String.valueOf(estd / DateUtils.HOUR_IN_MILLIS + 1);
			s[1] = "hours";
		} else {
			s[0] = "< " +String.valueOf(estd / DateUtils.DAY_IN_MILLIS + 1);
			s[1] = "days";
		}
		return s;
	}
	
}
