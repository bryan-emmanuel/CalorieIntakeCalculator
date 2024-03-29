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
package com.piusvelte.calorieintakecalculator.core;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class UI extends Activity implements OnClickListener, TextWatcher {
	private static final String TAG = "UI";
	public static final String PRO = "pro";
	private static final String GOOGLE_AD_ID = "a14ee17a6b89258";
	private Button mBtn_gender;
	private String mGender = "M";
	private EditText mFld_age;
	private Button mBtn_weight;
	private EditText mFld_weight;
	private Button mBtn_height;
	private EditText mFld_height;
	private Button mBtn_activity;
	private double mActivity = 1.55;
	private Button mBtn_additional;
	private Button mBtn_goal;
	private double mGoal = 1;
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
	private ArrayList<Double> mAdditional = new ArrayList<Double>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		if (!getPackageName().toLowerCase().contains(PRO)) {
			AdView adView = new AdView(this, AdSize.BANNER, GOOGLE_AD_ID);
			((LinearLayout) findViewById(R.id.ad)).addView(adView);
			adView.loadAd(new AdRequest());
		}
		mBtn_gender = (Button) findViewById(R.id.btn_gender);
		mFld_age = (EditText) findViewById(R.id.fld_age);
		mBtn_weight = (Button) findViewById(R.id.btn_weight);
		mFld_weight = (EditText) findViewById(R.id.fld_weight);
		mBtn_height = (Button) findViewById(R.id.btn_height);
		mFld_height = (EditText) findViewById(R.id.fld_height);
		mBtn_activity = (Button) findViewById(R.id.btn_activity);
		mBtn_additional = (Button) findViewById(R.id.btn_additional);
		mBtn_goal = (Button) findViewById(R.id.btn_goal);
		mBtn_bmr = (Button) findViewById(R.id.btn_bmr);
		mFld_bmr = (EditText) findViewById(R.id.fld_bmr);
		mBtn_tdee = (Button) findViewById(R.id.btn_tdee);
		mFld_tdee = (EditText) findViewById(R.id.fld_tdee);
		mFld_total_calories = (EditText) findViewById(R.id.fld_total_calories);
		mFld_chg_calories = (EditText) findViewById(R.id.fld_chg_calories);
		mBtn_calculate = (Button) findViewById(R.id.btn_calculate);
		// restore last session
		SharedPreferences sp = getSharedPreferences(getString(R.string.key_preferences), MODE_PRIVATE);
		String age = sp.getString(getString(R.string.key_age), "0");
		if (!age.equals("0")) {
			mFld_age.setText(age);
		}
		mGender = sp.getString(getString(R.string.key_gender), "M");
		if (!mGender.equals("M")) {
			String genders[] = getResources().getStringArray(R.array.gender_values);
			for (int i = 0, l = genders.length; i < l; i++) {
				if (mGender.equals(genders[i])) {
					mBtn_gender.setText(getResources().getStringArray(R.array.gender_entries)[i]);
					break;
				}
			}
		}
		String weight = sp.getString(getString(R.string.key_weight), "0");
		if (!weight.equals("0")) {
			mFld_weight.setText(weight);
		}
		String height = sp.getString(getString(R.string.key_height), "0");
		if (!height.equals("0")) {
			mFld_height.setText(height);
		}
		String activity = sp.getString(getString(R.string.key_activity), "1.55");
		if (!activity.equals("1.55")) {
			mActivity = Double.parseDouble(activity);
			String activities[] = getResources().getStringArray(R.array.activity_values);
			for (int i = 0, l = activities.length; i < l; i++) {
				if (activity.equals(activities[i])) {
					mBtn_activity.setText(getResources().getStringArray(R.array.activity_entries)[i]);
					break;
				}
			}
		}
		String goal = sp.getString(getString(R.string.key_goal), "1");
		if (!goal.equals("1")) {
			mGoal = Double.parseDouble(goal);
			String goals[] = getResources().getStringArray(R.array.goal_values);
			for (int i = 0, l = goals.length; i < l; i++) {
				if (goal.equals(goals[i])) {
					mBtn_goal.setText(getResources().getStringArray(R.array.goal_entries)[i]);
					break;
				}
			}
		}
		String weight_conversion = sp.getString(getString(R.string.key_weight_unit), "2.2");
		if (!weight_conversion.equals("2.2")) {
			mWeight_conversion = Double.parseDouble(weight_conversion);
			String weight_conversions[] = getResources().getStringArray(R.array.weight_unit_values);
			for (int i = 0, l = weight_conversions.length; i < l; i++) {
				if (weight_conversion.equals(weight_conversions[i])) {
					mBtn_weight.setText(getResources().getStringArray(R.array.weight_unit_entries)[i]);
					break;
				}
			}
		}
		String height_conversion = sp.getString(getString(R.string.key_height_unit), "2.54");
		if (!height_conversion.equals("2.2")) {
			mHeight_conversion = Double.parseDouble(height_conversion);
			String height_conversions[] = getResources().getStringArray(R.array.height_unit_values);
			for (int i = 0, l = height_conversions.length; i < l; i++) {
				if (height_conversion.equals(height_conversions[i])) {
					mBtn_height.setText(getResources().getStringArray(R.array.height_unit_entries)[i]);
					break;
				}
			}
		}
		recalculate();
		mFld_age.addTextChangedListener(this);
		mBtn_gender.setOnClickListener(this);
		mBtn_weight.setOnClickListener(this);
		mFld_weight.addTextChangedListener(this);
		mBtn_height.setOnClickListener(this);
		mFld_height.addTextChangedListener(this);
		mBtn_activity.setOnClickListener(this);
		mBtn_additional.setOnClickListener(this);
		mBtn_goal.setOnClickListener(this);
		mBtn_bmr.setOnClickListener(this);
		mBtn_tdee.setOnClickListener(this);
		mBtn_calculate.setOnClickListener(this);
	}

	@Override
	protected void onPause() {
		SharedPreferences sp = getSharedPreferences(getString(R.string.key_preferences), MODE_PRIVATE);
		SharedPreferences.Editor spe = sp.edit();
		spe.putString(getString(R.string.key_age), Double.toString(getFieldValue(mFld_age)));
		spe.putString(getString(R.string.key_gender), mGender);
		spe.putString(getString(R.string.key_weight), Double.toString(getFieldValue(mFld_weight)));
		spe.putString(getString(R.string.key_height), Double.toString(getFieldValue(mFld_height)));
		spe.putString(getString(R.string.key_activity), Double.toString(mActivity));
		spe.putString(getString(R.string.key_goal), Double.toString(mGoal));
		spe.commit();
		super.onPause();
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
		} else if (v == mBtn_additional) {
			// select an activity
			(new AlertDialog.Builder(this))
			.setItems(R.array.activities_entries, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// launch form for duration
					final double met = Double.parseDouble(getResources().getStringArray(R.array.activities_values)[which]);
					if (met > 0) {
						final EditText fld_duration = new EditText(UI.this);
						fld_duration.setInputType(InputType.TYPE_CLASS_NUMBER);
						(new AlertDialog.Builder(UI.this))
						.setTitle(R.string.lbl_duration)
						.setView(fld_duration)
						.setPositiveButton(R.string.lbl_add, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								double duration = getFieldValue(fld_duration);
								if (duration > 0) {
									mAdditional.add(duration * met);
								}
								recalculate();
							}
						})
						.show();
					} else {
						mAdditional.clear();
						recalculate();
					}
				}
			})
			.show();
		} else if (v == mBtn_goal) {
			(new AlertDialog.Builder(this))
			.setItems(R.array.goal_entries, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					mBtn_goal.setText(getResources().getStringArray(R.array.goal_entries)[which]);
					mGoal = Double.parseDouble(getResources().getStringArray(R.array.goal_values)[which]);
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

	private double getFieldValue(EditText fld) {
		double value = 0;
		String value_str = fld.getText().toString();
		if ((value_str != null) && (value_str.length() > 0)) {
			value = Double.parseDouble(value_str);
		}
		return value;
	}

	private void recalculate() {
		long bmr = 0;
		double age = getFieldValue(mFld_age);
		double weight = getFieldValue(mFld_weight);
		if (weight > 0) {
			weight = weight / mWeight_conversion;
		}
		double height = getFieldValue(mFld_height);
		if (height > 0) {
			height = height * mHeight_conversion;
		}
		if (mGender.equals("M")) {
			bmr = Math.round(66 + (13.7 * weight) + (5 * height) - (6.8 * age));
		} else {
			bmr = Math.round(655 + (9.6 * weight) + (1.8 * height) - (4.7 * age));
		}
		mFld_bmr.setText(Long.toString(bmr));
		long tdee = Math.round(bmr * mActivity);
		long additional = 0;
		if (!mAdditional.isEmpty()) {
			Iterator<Double> itr = mAdditional.iterator();
			while (itr.hasNext()) {
				additional += Math.round(itr.next() * 3.5 * weight / 200);
			}
			tdee += additional;
		}
		mBtn_additional.setText(Long.toString(additional));
		mFld_tdee.setText(Long.toString(tdee));
		long target = Math.round(tdee * mGoal);
		long chg = target - tdee;
		mFld_total_calories.setText(Long.toString(target));
		mFld_chg_calories.setText(Long.toString(chg));
	}

	public void afterTextChanged(Editable arg0) {
		recalculate();
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}
	
	@SuppressWarnings("rawtypes")
	protected static Class getPackageClass(Context context, Class cls) {
		try {
			return Class.forName(context.getPackageName() + "." + cls.getSimpleName());
		} catch (ClassNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return cls;
	}
	
	@SuppressWarnings("rawtypes")
	protected static Intent getPackageIntent(Context context, Class cls) {
		return new Intent(context, getPackageClass(context, cls));
	}
}
