/* Calorie Intake Calculator
 * Copyright (C) 2009 Bryan Emmanuel
 * 
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  
 *  Bryan Emmanuel piusvelte@gmail.com
 */
package com.piusvelte.calorieintakecalculatorpro;

import com.piusvelte.calorieintakecalculatorpro.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class UI extends Activity implements OnClickListener, TextWatcher {
	private Button mBtn_gender;
	private String mGender = "M";
	private EditText mFld_age;
	private Button mBtn_weight;
	private EditText mFld_weight;
	private Button mBtn_height;
	private EditText mFld_height;
	private Button mBtn_activity;
	private double mActivity = 1.55;
	private Button mBtn_goal;
	private int mTarget = 0;
	private Button mBtn_bmr;
	private EditText mFld_bmr;
	private Button mBtn_tdee;
	private EditText mFld_tdee;
	private EditText mFld_total_calories;
	private EditText mFld_chg_calories;
	private Button mBtn_calculate;
	private double mWeight_conversion = 2.2;
	private double mHeight_conversion = 2.54;
	private static final int ABOUT_ID = 1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mBtn_gender = (Button) findViewById(R.id.btn_gender);
        mFld_age = (EditText) findViewById(R.id.fld_age);
        mBtn_weight = (Button) findViewById(R.id.btn_weight);
        mFld_weight = (EditText) findViewById(R.id.fld_weight);
        mBtn_height = (Button) findViewById(R.id.btn_height);
        mFld_height = (EditText) findViewById(R.id.fld_height);
        mBtn_activity = (Button) findViewById(R.id.btn_activity);
        mBtn_goal = (Button) findViewById(R.id.btn_goal);
        mBtn_bmr = (Button) findViewById(R.id.btn_bmr);
        mFld_bmr = (EditText) findViewById(R.id.fld_bmr);
        mBtn_tdee = (Button) findViewById(R.id.btn_tdee);
        mFld_tdee = (EditText) findViewById(R.id.fld_tdee);
        mFld_total_calories = (EditText) findViewById(R.id.fld_total_calories);
        mFld_chg_calories = (EditText) findViewById(R.id.fld_chg_calories);
        mBtn_calculate = (Button) findViewById(R.id.btn_calculate);
        mFld_age.addTextChangedListener(this);
        mBtn_gender.setOnClickListener(this);
        mBtn_weight.setOnClickListener(this);
        mFld_weight.addTextChangedListener(this);
        mBtn_height.setOnClickListener(this);
        mFld_height.addTextChangedListener(this);
        mBtn_activity.setOnClickListener(this);
        mBtn_goal.setOnClickListener(this);
        mBtn_bmr.setOnClickListener(this);
        mBtn_tdee.setOnClickListener(this);
        mBtn_calculate.setOnClickListener(this);
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, ABOUT_ID, 0, R.string.lbl_about).setIcon(android.R.drawable.ic_menu_more);
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ABOUT_ID:
			(new AlertDialog.Builder(this))
			.setMessage(R.string.about)
			.setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			})
			.show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onClick(View v) {
		if (v == mBtn_gender) {
			(new AlertDialog.Builder(this))
			.setItems(R.array.gender_entries, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					mBtn_gender.setText(getResources().getStringArray(R.array.gender_entries)[which]);
					mGender = getResources().getStringArray(R.array.gender_values)[which];
					recalculate();
				}
			})
			.show();
		} else if (v == mBtn_weight) {
			(new AlertDialog.Builder(this))
			.setItems(R.array.weight_unit_entries, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					mBtn_weight.setText(getResources().getStringArray(R.array.weight_unit_entries)[which]);
					mWeight_conversion = Double.parseDouble(getResources().getStringArray(R.array.weight_unit_values)[which]);
					recalculate();
				}
			})
			.show();
		} else if (v == mBtn_height) {
			(new AlertDialog.Builder(this))
			.setItems(R.array.height_unit_entries, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					mBtn_height.setText(getResources().getStringArray(R.array.height_unit_entries)[which]);
					mHeight_conversion = Double.parseDouble(getResources().getStringArray(R.array.height_unit_values)[which]);
					recalculate();
				}
			})
			.show();
		} else if (v == mBtn_activity) {
			(new AlertDialog.Builder(this))
			.setItems(R.array.activity_entries, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					mBtn_activity.setText(getResources().getStringArray(R.array.activity_entries)[which]);
					mActivity = Double.parseDouble(getResources().getStringArray(R.array.activity_values)[which]);
					recalculate();
				}
			})
			.show();
		} else if (v == mBtn_goal) {
			(new AlertDialog.Builder(this))
			.setItems(R.array.target_entries, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					mBtn_goal.setText(getResources().getStringArray(R.array.target_entries)[which]);
					mTarget = Integer.parseInt(getResources().getStringArray(R.array.target_values)[which]);
					recalculate();
				}
			})
			.show();
		} else if (v == mBtn_bmr) {
			(new AlertDialog.Builder(this))
			.setMessage(R.string.def_bmr)
			.setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			})
			.show();
		} else if (v == mBtn_tdee) {
			(new AlertDialog.Builder(this))
			.setMessage(R.string.def_tdee)
			.setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			})
			.show();
		} else if (v == mBtn_calculate) {
			recalculate();
		}
	}
	
	private void recalculate() {
		long bmr = 0;
		double age = 0;
		double weight = 0;
		double height = 0;
		String value = mFld_age.getText().toString();
		if ((value != null) && (value.length() > 0)) {
			age = Double.parseDouble(value);
		}
		value = mFld_weight.getText().toString();
		if ((value != null) && (value.length() > 0)) {
			weight = Double.parseDouble(value) / mWeight_conversion;
		}
		value = mFld_height.getText().toString();
		if ((value != null) && (value.length() > 0)) {
			height = Double.parseDouble(value) * mHeight_conversion;
		}
		if (mGender.equals("M")) {
			bmr = Math.round(66 + (13.7 * weight) + (5 * height) - (6.8 * age));
		} else {
			bmr = Math.round(655 + (9.6 * weight) + (1.8 * height) - (4.7 * age));
		}
		mFld_bmr.setText(Long.toString(bmr));
		long tdee = Math.round(bmr * mActivity);
		mFld_tdee.setText(Long.toString(tdee));
		long chg = Math.round(tdee * .2 * mTarget);
		mFld_total_calories.setText(Long.toString(tdee + chg));
		mFld_chg_calories.setText(Long.toString(chg));
	}

	public void afterTextChanged(Editable arg0) {
		recalculate();
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}
}