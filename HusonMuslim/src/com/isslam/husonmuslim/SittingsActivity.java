package com.isslam.husonmuslim;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.isslam.husonmuslim.controllers.SharedPreferencesManager;
import com.isslam.husonmuslim.utils.GlobalConfig;

public class SittingsActivity extends Activity {

	private Context _scope = null;
	int selectedColorOption = 1;

	/** Called when the activity is first created. */
	@TargetApi(14) @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ly_sittings);
		ActionBar actionbar;
		actionbar = getActionBar();
		actionbar.setTitle(getResources().getString(R.string.action_settings));
		actionbar.setIcon(R.drawable.ic_action_settings);
		_scope = this;

		RelativeLayout btn_mail = (RelativeLayout) findViewById(R.id.btn_mail);

		btn_mail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent in = new Intent(getApplicationContext(),
						ContactusActivity.class);
				startActivity(in);

			}
		});
		RelativeLayout btn_share = (RelativeLayout) findViewById(R.id.btn_share);

		btn_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent sharingIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				String shareBody = getString(R.string.share_body) + "\n"
						+ "http://play.google.com/store/apps/details?id="
						+ _scope.getPackageName();
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						getString(R.string.share_title));
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
						shareBody);
				startActivity(Intent.createChooser(sharingIntent,
						getString(R.string.share_title)));

			}
		});

		RelativeLayout btn_apps = (RelativeLayout) findViewById(R.id.btn_apps);

		btn_apps.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri
							.parse("https://play.google.com/store/apps/developer?id=islam+is+the+way+of+life"));
					startActivity(intent);
				} catch (ActivityNotFoundException e) {
					startActivity(new Intent(
							Intent.ACTION_VIEW,
							Uri.parse("http://play.google.com/store/apps/details?id="
									+ _scope.getPackageName())));
				}
			}
		});

		RelativeLayout btn_rate = (RelativeLayout) findViewById(R.id.btn_rate);

		btn_rate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("market://details?id="
						+ _scope.getPackageName());
				Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
				goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
						| Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
						| Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
				try {
					startActivity(goToMarket);
				} catch (ActivityNotFoundException e) {
					startActivity(new Intent(
							Intent.ACTION_VIEW,
							Uri.parse("http://play.google.com/store/apps/details?id="
									+ _scope.getPackageName())));
				}
			}
		});


		RelativeLayout btn_show_automatic_menu = (RelativeLayout) findViewById(R.id.btn_show_automatic_menu);
		final ImageView cb_show_automatic_menu = (ImageView) findViewById(R.id.cb_show_automatic_menu);
	
		
		btn_show_automatic_menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager
						.getInstance(_scope);
				Boolean isChecked = sharedPreferencesManager
						.getBooleanPreferences(
								SharedPreferencesManager.show_menu_always_key,
								false);
				if (!isChecked) {

					sharedPreferencesManager
							.savePreferences(
									SharedPreferencesManager.show_menu_always_key,
									true);
					cb_show_automatic_menu
							.setImageResource(R.drawable.component_style_btn_check_on_focused_holo_dark);

				} else {
					sharedPreferencesManager.savePreferences(
							SharedPreferencesManager.show_menu_always_key,
							false);
					cb_show_automatic_menu
							.setImageResource(R.drawable.component_style_btn_check_off_disabled_focused_holo_dark);

				}

			}
		});
		SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager
				.getInstance(_scope);

		Boolean show_nafigation_key = sharedPreferencesManager
				.getBooleanPreferences(
						SharedPreferencesManager.show_nafigation_key, false);

	
		Boolean show_menu_always_key = sharedPreferencesManager
				.getBooleanPreferences(
						SharedPreferencesManager.show_menu_always_key, false);

		if (show_menu_always_key) {

			cb_show_automatic_menu
					.setImageResource(R.drawable.component_style_btn_check_on_focused_holo_dark);

		} else {

			cb_show_automatic_menu
					.setImageResource(R.drawable.component_style_btn_check_off_disabled_focused_holo_dark);

		}
		final ColorPickerDialog d = new ColorPickerDialog(_scope, 0xffffffff);

		final TextView text_font_size = (TextView) findViewById(R.id.text_font_size);

		final TextView Text_color_bg_ind = (TextView) findViewById(R.id.Text_color_bg_ind);
		final TextView Text_color_font_ind = (TextView) findViewById(R.id.Text_color_font_ind);
		final ImageView image_low_light = (ImageView) findViewById(R.id.cb_low_light);

		if (GlobalConfig.fontColor == -1 && GlobalConfig.bgColor == -16777216) {

			image_low_light
					.setImageResource(R.drawable.component_style_btn_check_on_focused_holo_dark);

		} else {

			image_low_light
					.setImageResource(R.drawable.component_style_btn_check_off_disabled_focused_holo_dark);

		}

		Text_color_bg_ind.setBackgroundColor(GlobalConfig.bgColor);
		Text_color_font_ind.setBackgroundColor(GlobalConfig.fontColor);

		RelativeLayout ly_text_font_color = (RelativeLayout) findViewById(R.id.ly_text_font_color);
		selectedColorOption = 1;
		RelativeLayout ly_text_bg_color = (RelativeLayout) findViewById(R.id.ly_text_bg_color);
		ly_text_bg_color.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectedColorOption = 2;
				d.show();
			}
		});
		RelativeLayout ly_all_settings = (RelativeLayout) findViewById(R.id.ly_all_settings);
		ly_all_settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		RelativeLayout ly_low_light_color = (RelativeLayout) findViewById(R.id.ly_low_light_color);
		ly_low_light_color.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (GlobalConfig.fontColor == -1
						&& GlobalConfig.bgColor == -16777216) {
					GlobalConfig.bgColor = -1;
					GlobalConfig.fontColor = -16777216;
					Text_color_bg_ind.setBackgroundColor(-1);

					Text_color_font_ind.setBackgroundColor(-16777216);
					image_low_light
							.setImageResource(R.drawable.component_style_btn_check_off_disabled_focused_holo_dark);

				} else {
					GlobalConfig.bgColor = -16777216;
					GlobalConfig.fontColor = -1;
					Text_color_bg_ind.setBackgroundColor(-16777216);

					Text_color_font_ind.setBackgroundColor(-1);
					image_low_light
							.setImageResource(R.drawable.component_style_btn_check_on_focused_holo_dark);

				}

				SavePreferences();
			}
		});

		ly_text_font_color.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectedColorOption = 1;
				d.show();
			}
		});
		SeekBar seekBar = (SeekBar) findViewById(R.id.sb_font_size);
		text_font_size.setText(" ÕÃ„ «·Œÿ " + (GlobalConfig.fontSize));
		seekBar.setProgress(GlobalConfig.fontSize - 9);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int progress = 0;

			@Override
			public void onProgressChanged(SeekBar seekBar, int progresValue,
					boolean fromUser) {
				progress = progresValue;
				// Toast.makeText(getApplicationContext(),
				// "Changing seekbar's progress", Toast.LENGTH_SHORT)
				// .show();
				text_font_size.setText(" ÕÃ„ «·Œÿ " + (progresValue + 9) + "");
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// Toast.makeText(getApplicationContext(),
				// "Started tracking seekbar", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// textView.setText("Covered: " + progress + "/"
				// + seekBar.getMax());
				// Toast.makeText(getApplicationContext(),
				// "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
				text_font_size.setText(" ÕÃ„ «·Œÿ " + (progress + 9) + "");
				GlobalConfig.fontSize = progress + 9;
				// pagerAdapter.getFragment(pager.getCurrentItem())
				// .UpdateContent();
				// pagerAdapter.notifyDataSetChanged();
				SavePreferences();

			}
		});
		d.setButton("„Ê«›ﬁ", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				Log.e("---", d.getColor() + "");
				// GlobalConfig.
				if (selectedColorOption == 1) {
					GlobalConfig.fontColor = d.getColor();
					Text_color_font_ind.setBackgroundColor(d.getColor());

				} else {
					GlobalConfig.bgColor = d.getColor();
					Text_color_bg_ind.setBackgroundColor(d.getColor());

				}
				if (GlobalConfig.fontColor == -1
						&& GlobalConfig.bgColor == -16777216) {

					image_low_light
							.setImageResource(R.drawable.component_style_btn_check_on_focused_holo_dark);

				} else {

					image_low_light
							.setImageResource(R.drawable.component_style_btn_check_off_disabled_focused_holo_dark);

				}
				// pagerAdapter.getFragment(pager.getCurrentItem())
				// .UpdateContent();
				// pagerAdapter.notifyDataSetChanged();
				SavePreferences();
				// font_color_btn.setText(String.format("#%06X", (0xFFFFFF &
				// d.getColor())));

				// d.getColor();

			}
		});

		d.setButton2("≈·€«¡", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

	}

	private void SavePreferences() {
		SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager
				.getInstance(_scope);
		sharedPreferencesManager
				.savePreferences(SharedPreferencesManager.font_color_key,
						GlobalConfig.fontColor);
		sharedPreferencesManager.savePreferences(
				SharedPreferencesManager.bg_color_key, GlobalConfig.bgColor);
		sharedPreferencesManager.savePreferences(
				SharedPreferencesManager.font_size_key, GlobalConfig.fontSize);
	}

}
