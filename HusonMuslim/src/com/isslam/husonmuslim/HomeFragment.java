package com.isslam.husonmuslim;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.isslam.husonmuslim.controllers.SemiChapterManager;
import com.isslam.husonmuslim.controllers.SharedPreferencesManager;
import com.isslam.husonmuslim.utils.GlobalConfig;
import com.isslam.husonmuslim.utils.SlidingPanel;

public class HomeFragment extends Fragment {

	MainActivity _FragmentActivity;
	HomeFragmentPagerAdapter pagerAdapter;
	ViewPager pager;
	ImageView img_fav;
	TextView txt_repeat;
	// action id
	private static final int ID_READ = 1;
	private static final int ID_COPY = 2;
	private static final int ID_NOTE = 3;
	QuickAction quickAction;
	SlidingPanel popup;
	RelativeLayout controls;
	public HomeFragment() {

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_FragmentActivity = (MainActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.ly_home_fragment, container,
				false);
		

		ArrayList<HashMap<String, String>> containerList = GlobalConfig
				.GetmyDbHelper().get_content_by_id(
						GlobalConfig.getSemiChapterList(_FragmentActivity)
								.get(0).get("hadith_id"));
		String chapter_txt = getResources().getString(R.string.chapter) + " "
				+ containerList.get(0).get("chapter_name");
		String semi_chapter_txt = getResources().getString(
				R.string.semi_chapter)
				+ " " + containerList.get(0).get("semi_chapter_name");
		
		TextView semi_chapter_title = (TextView) rootView
				.findViewById(R.id.semi_chapter_title);
		 txt_repeat = (TextView) rootView
				.findViewById(R.id.txt_repeat);
		
		semi_chapter_title.setText(semi_chapter_txt);
		/** Getting a reference to the ViewPager defined the layout file */
		pager = (ViewPager) rootView.findViewById(R.id.pager);
		/** Getting fragment manager */
		pager.setOffscreenPageLimit(100);

		FragmentManager fm = _FragmentActivity.getSupportFragmentManager();

		/** Instantiating FragmentPagerAdapter */
		pagerAdapter = new HomeFragmentPagerAdapter(fm, this);

		/** Setting the pagerAdapter to the pager object */

		pager.setAdapter(pagerAdapter);
		img_fav = (ImageView) rootView.findViewById(R.id.img_fav);
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				SemiChapterManager semiChapterManager = SemiChapterManager
						.getInstance();
				semiChapterManager.setSelectedContentId(Integer
						.parseInt(GlobalConfig
								.getSemiChapterList(_FragmentActivity)
								.get(pager.getCurrentItem()).get("hadith_id")));
				ToggleFav();
				

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		
		
		
		 controls = (RelativeLayout) rootView
		.findViewById(R.id.controls);

		controls.setOnClickListener(new OnClickListener() {

	@Override
	public void onClick(View v) {
		pager.setCurrentItem(pager.getCurrentItem()+1);
	}
});

		RelativeLayout ry_next = (RelativeLayout) rootView
				.findViewById(R.id.ry_next);

		ry_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SemiChapterManager semiChapterManager = SemiChapterManager
						.getInstance();
				semiChapterManager.SetNextSemiChapter();
				LoadSemiChapter(semiChapterManager.getselectedSemiChapterId());
			}
		});

		RelativeLayout ry_show = (RelativeLayout) rootView
				.findViewById(R.id.ry_show);

		ry_show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager
						.getInstance(_FragmentActivity);
				if (popup.getVisibility() == View.VISIBLE) {
					ToggleMenu(true, v);
					sharedPreferencesManager.savePreferences(
							SharedPreferencesManager.show_menu_key, false);
				} else {
					ToggleMenu(false, v);
					sharedPreferencesManager.savePreferences(
							SharedPreferencesManager.show_menu_key, true);
				}
			}
		});
		RelativeLayout ry_prev = (RelativeLayout) rootView
				.findViewById(R.id.ry_prev);

		ry_prev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SemiChapterManager semiChapterManager = SemiChapterManager
						.getInstance();
				semiChapterManager.SetPrevSemiChapter();
				LoadSemiChapter(semiChapterManager.getselectedSemiChapterId());
			}
		});

		RelativeLayout nafigation_container = (RelativeLayout) rootView
				.findViewById(R.id.nafigation_container);

		SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager
				.getInstance(_FragmentActivity);
		Boolean showNavigation = sharedPreferencesManager
				.getBooleanPreferences(
						SharedPreferencesManager.show_nafigation_key, true);
		if (!showNavigation) {

			nafigation_container.setVisibility(View.GONE);
			GlobalConfig.ShowLongToast(_FragmentActivity, chapter_txt + "\n"
					+ semi_chapter_txt);
		}

		RelativeLayout btn_m_fav = (RelativeLayout) rootView
				.findViewById(R.id.btn_m_fav);

		btn_m_fav.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ArrayList<HashMap<String, String>> containerList = GlobalConfig
						.GetmyDbHelper().get_content_by_id(
								GlobalConfig
										.getSemiChapterList(_FragmentActivity)
										.get(pager.getCurrentItem())
										.get("hadith_id"));
				GlobalConfig.GetmyDbHelper().toggle_fav(
						containerList.get(0).get("_id"));
				if (ToggleFav()) {
					GlobalConfig.ShowToast(_FragmentActivity, getResources()
							.getString(R.string.fav_add));
				} else {
					GlobalConfig.ShowToast(_FragmentActivity, getResources()
							.getString(R.string.fav_del));
				}

			}
		});

		RelativeLayout btn_m_font_settings = (RelativeLayout) rootView
				.findViewById(R.id.btn_m_font_settings);

		btn_m_font_settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ShowFontSettings();
			}
		});

		RelativeLayout btn_m_more = (RelativeLayout) rootView
				.findViewById(R.id.btn_m_more);

		btn_m_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				quickAction.show(v);
			}
		});
		
		RelativeLayout nafigation__semi_chapter = (RelativeLayout) rootView
				.findViewById(R.id.nafigation__semi_chapter);

		nafigation__semi_chapter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				//Intent i = new Intent();
				//i.setClass(_FragmentActivity, BookTabsActivity.class);
				//i.putExtra("selectedTab", "1");
				//startActivityForResult(i, 1);
				_FragmentActivity.displayView(2);
			}
		});
		RelativeLayout btn_m_share = (RelativeLayout) rootView
				.findViewById(R.id.btn_m_share);

		btn_m_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ArrayList<HashMap<String, String>> containerList = GlobalConfig
						.GetmyDbHelper().get_content_by_id(
								GlobalConfig
										.getSemiChapterList(_FragmentActivity)
										.get(pager.getCurrentItem())
										.get("hadith_id"));
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				String share_text = " „‰ √–ﬂ«—  ÿ»Ìﬁ Õ’‰ «·„”·„ , "+containerList.get(0).get("semi_chapter_name") + " \n \n " +containerList.get(0)
						.get("content")
						+ "\n"
						+ "http://play.google.com/store/apps/details?id="
						+ _FragmentActivity.getPackageName();
				sendIntent.putExtra(Intent.EXTRA_TEXT,share_text );
				sendIntent.setType("text/plain");

				_FragmentActivity.startActivity(Intent.createChooser(
						sendIntent,
						_FragmentActivity.getResources().getText(
								R.string.share_hadith)));
			}
		});
		ToggleFav();

		// ActionItem nextItem = new ActionItem(ID_READ,
		// getResources().getString(
		// R.string.quick_action_read), _FragmentActivity.getResources()
		// .getDrawable(R.drawable.ic_action_book));
		ActionItem prevItem = new ActionItem(ID_COPY, getResources().getString(
				R.string.quick_action_copy), _FragmentActivity.getResources()
				.getDrawable(R.drawable.ic_action_copy));
		ActionItem noteItem = new ActionItem(ID_NOTE, getResources().getString(
				R.string.quick_action_add_note), _FragmentActivity
				.getResources().getDrawable(R.drawable.ic_action_note));

		// orientation
		quickAction = new QuickAction(_FragmentActivity, QuickAction.VERTICAL);

		// add action items into QuickAction
		// quickAction.addActionItem(nextItem);
		quickAction.addActionItem(prevItem);
		quickAction.addActionItem(noteItem);
		quickAction
				.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
					@Override
					public void onItemClick(QuickAction source, int pos,
							int actionId) {
						ActionItem actionItem = quickAction.getActionItem(pos);

						// here we can filter which action item was clicked with
						// pos or actionId parameter
						if (actionId == ID_READ) {

						} else if (actionId == ID_COPY) {

							ArrayList<HashMap<String, String>> containerList = GlobalConfig
									.GetmyDbHelper()
									.get_content_by_id(
											GlobalConfig
													.getSemiChapterList(
															_FragmentActivity)
													.get(pager.getCurrentItem())
													.get("hadith_id"));
							android.content.ClipboardManager clipboard = (android.content.ClipboardManager) _FragmentActivity
									.getSystemService(Context.CLIPBOARD_SERVICE);
							android.content.ClipData clip = android.content.ClipData
									.newPlainText("«·ÕœÌÀ", containerList
											.get(0).get("content"));
							clipboard.setPrimaryClip(clip);
							GlobalConfig.ShowToast(
									_FragmentActivity,
									getResources().getString(
											R.string.copy_success));

						} else if (actionId == ID_NOTE) {
							ShowNewNoteDialog();
						}
					}
				});
		SemiChapterManager semiChapterManager = SemiChapterManager
				.getInstance();
		pager.setCurrentItem(semiChapterManager.getPageCurrentItem());
		popup = (SlidingPanel) rootView.findViewById(R.id.menu_slider);

		popup.setVisibility(View.GONE);

		Boolean show_menu_always_key = sharedPreferencesManager
				.getBooleanPreferences(
						SharedPreferencesManager.show_menu_always_key, false);

		if (!show_menu_always_key) {
			Boolean show_menu_key = sharedPreferencesManager
					.getBooleanPreferences(
							SharedPreferencesManager.show_menu_key, true);
			ToggleMenu(!show_menu_key, rootView);
		} else {
			popup.setVisibility(View.VISIBLE);
			ImageView img_show = (ImageView) rootView
					.findViewById(R.id.img_show);
			img_show.setVisibility(View.GONE);

		}
		return rootView;
	}

	Boolean ToggleFav() {
		ArrayList<HashMap<String, String>> containerList = GlobalConfig
				.GetmyDbHelper().get_content_by_id(
						GlobalConfig.getSemiChapterList(_FragmentActivity)
								.get(pager.getCurrentItem()).get("hadith_id"));
		if(pager.getCurrentItem()==pagerAdapter.getCount()-1)
			controls.setVisibility(View.GONE);
		else
			controls.setVisibility(View.VISIBLE);
		boolean result = GlobalConfig.GetmyDbHelper().get_fav_count(
				containerList.get(0).get("_id"));
		 txt_repeat.setText(containerList.get(0).get("repeat"));
		if (result) {
			img_fav.setImageResource(R.drawable.ic_action_favorite_selected);

		} else {
			img_fav.setImageResource(R.drawable.ic_action_favorite_light);

		}
		return result;
	}

	int selectedColorOption = 1;

	private void ShowFontSettings() {
		final Dialog custom = new Dialog(_FragmentActivity);
		custom.requestWindowFeature(Window.FEATURE_NO_TITLE);
		custom.setContentView(R.layout.ly_font_settings);
		ArrayAdapter adapter = ArrayAdapter.createFromResource(
				_FragmentActivity, R.array.nav_drawer_items,
				R.layout.spinner_item);
		adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
		final ColorPickerDialog d = new ColorPickerDialog(_FragmentActivity,
				0xffffffff);

		final TextView text_font_size = (TextView) custom
				.findViewById(R.id.text_font_size);

		final TextView Text_color_bg_ind = (TextView) custom
				.findViewById(R.id.Text_color_bg_ind);
		final TextView Text_color_font_ind = (TextView) custom
				.findViewById(R.id.Text_color_font_ind);
		final ImageView image_low_light = (ImageView) custom
				.findViewById(R.id.cb_low_light);

		final ImageView cb_hide_bg = (ImageView) custom
				.findViewById(R.id.cb_hide_bg);

		
		
		
		if (GlobalConfig.fontColor == -1 && GlobalConfig.bgColor == -16777216) {

			image_low_light
					.setImageResource(R.drawable.component_style_btn_check_on_focused_holo_dark);

		} else {

			image_low_light
					.setImageResource(R.drawable.component_style_btn_check_off_disabled_focused_holo_dark);

		}

		Text_color_bg_ind.setBackgroundColor(GlobalConfig.bgColor);
		Text_color_font_ind.setBackgroundColor(GlobalConfig.fontColor);

		RelativeLayout ly_text_font_color = (RelativeLayout) custom
				.findViewById(R.id.ly_text_font_color);
		selectedColorOption = 1;
		final RelativeLayout ly_text_bg_color = (RelativeLayout) custom
				.findViewById(R.id.ly_text_bg_color);
		ly_text_bg_color.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectedColorOption = 2;
				d.show();
			}
		});
		if (GlobalConfig.showBg) {

			cb_hide_bg
					.setImageResource(R.drawable.component_style_btn_check_on_focused_holo_dark);
			ly_text_bg_color.setVisibility(View.GONE);

		} else {
			ly_text_bg_color.setVisibility(View.VISIBLE);
			cb_hide_bg
					.setImageResource(R.drawable.component_style_btn_check_off_disabled_focused_holo_dark);

		}
		RelativeLayout ly_all_settings = (RelativeLayout) custom
				.findViewById(R.id.ly_all_settings);
		ly_all_settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();

				i.setClass(_FragmentActivity, SittingsActivity.class);
				custom.cancel();
				startActivity(i);
			}
		});
		
		
		RelativeLayout ly_hide_bg = (RelativeLayout) custom
				.findViewById(R.id.ly_hide_bg);
		ly_hide_bg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (GlobalConfig.showBg == false) {
					GlobalConfig.showBg = true;
					cb_hide_bg
							.setImageResource(R.drawable.component_style_btn_check_on_focused_holo_dark);
					ly_text_bg_color.setVisibility(View.GONE);

				} else {
					GlobalConfig.showBg = false;
					ly_text_bg_color.setVisibility(View.VISIBLE);
					cb_hide_bg
							.setImageResource(R.drawable.component_style_btn_check_off_disabled_focused_holo_dark);

				}
				pagerAdapter.getFragment(pager.getCurrentItem())
						.UpdateContent();
				pagerAdapter.notifyDataSetChanged();
				SavePreferences();
			}
		});

		
		
		

		RelativeLayout ly_low_light_color = (RelativeLayout) custom
				.findViewById(R.id.ly_low_light_color);
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
					GlobalConfig.showBg = false;
					ly_text_bg_color.setVisibility(View.VISIBLE);
					cb_hide_bg
							.setImageResource(R.drawable.component_style_btn_check_off_disabled_focused_holo_dark);

				}
				
				
				
				pagerAdapter.getFragment(pager.getCurrentItem())
						.UpdateContent();
				pagerAdapter.notifyDataSetChanged();
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

		d.setButton(getResources().getString(R.string.color_picker_ok),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

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
						pagerAdapter.getFragment(pager.getCurrentItem())
								.UpdateContent();
						pagerAdapter.notifyDataSetChanged();
						SavePreferences();

					}
				});

		d.setButton2(getResources().getString(R.string.color_picker_cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});

		SeekBar seekBar = (SeekBar) custom.findViewById(R.id.sb_font_size);
		text_font_size.setText(getResources().getString(
				R.string.setting_font_size)
				+ (GlobalConfig.fontSize));
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
				text_font_size.setText(getResources().getString(
						R.string.setting_font_size)
						+ (progresValue + 9) + "");
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// Toast.makeText(getApplicationContext(),
				// "Started tracking seekbar", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				text_font_size.setText(getResources().getString(
						R.string.setting_font_size)
						+ (progress + 9) + "");
				GlobalConfig.fontSize = progress + 9;
				pagerAdapter.getFragment(pager.getCurrentItem())
						.UpdateContent();
				pagerAdapter.notifyDataSetChanged();
				SavePreferences();

			}
		});

		custom.show();

	}

	String selected_color;

	private void ShowNewNoteDialog() {
		final Dialog custom = new Dialog(_FragmentActivity);
		custom.requestWindowFeature(Window.FEATURE_NO_TITLE);
		custom.setContentView(R.layout.ly_new_note);
		final TextView text_title = (TextView) custom
				.findViewById(R.id.text_title);
		final TextView text_content = (TextView) custom
				.findViewById(R.id.text_content);
		final TextView Text_color_note_ind = (TextView) custom
				.findViewById(R.id.Text_color_note_ind);
		selected_color = "-16777216";

		final ColorPickerDialog d = new ColorPickerDialog(_FragmentActivity,
				0xff000000);
		d.setButton(getResources().getString(R.string.color_picker_ok),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						selected_color = d.getColor() + "";
						Text_color_note_ind.setBackgroundColor(d.getColor());

					}
				});

		d.setButton2(getResources().getString(R.string.color_picker_cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		RelativeLayout ly_note_color = (RelativeLayout) custom
				.findViewById(R.id.ly_note_color);
		ly_note_color.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				d.show();
			}
		});
		RelativeLayout ly_save_note = (RelativeLayout) custom
				.findViewById(R.id.ly_save_note);
		ly_save_note.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ArrayList<HashMap<String, String>> containerList = GlobalConfig
						.GetmyDbHelper().get_content_by_id(
								GlobalConfig
										.getSemiChapterList(_FragmentActivity)
										.get(pager.getCurrentItem())
										.get("hadith_id"));
				GlobalConfig.GetmyDbHelper().insert_note(
						containerList.get(0).get("_id"),
						text_title.getText().toString(),
						text_content.getText().toString(), selected_color,
						"10-10-2012");
				GlobalConfig.ShowToast(_FragmentActivity, getResources()
						.getString(R.string.note_add));
				custom.cancel();
			}
		});

		custom.show();

	}

	private void SavePreferences() {
		SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager
				.getInstance(_FragmentActivity);
		sharedPreferencesManager
				.savePreferences(SharedPreferencesManager.font_color_key,
						GlobalConfig.fontColor);
		sharedPreferencesManager.savePreferences(
				SharedPreferencesManager.bg_color_key, GlobalConfig.bgColor);
		sharedPreferencesManager.savePreferences(
				SharedPreferencesManager.font_size_key, GlobalConfig.fontSize);
		sharedPreferencesManager.savePreferences(
				SharedPreferencesManager.show_bg, GlobalConfig.showBg);
	}

	private void LoadSemiChapter(int _position) {
		SemiChapterManager semiChapterManager = SemiChapterManager
				.getInstance();
		semiChapterManager.SetSemiChaptersList(_FragmentActivity);

		_FragmentActivity.displayView(1);
	}

	private void ToggleMenu(Boolean showMenu, View v) {

		if (showMenu) {
			Animation animHide = AnimationUtils.loadAnimation(
					_FragmentActivity, R.anim.popup_hide);
			popup.startAnimation(animHide);
			popup.setVisibility(View.GONE);
			ImageView img_show = (ImageView) v.findViewById(R.id.img_show);
			img_show.setImageResource(R.drawable.ic_action_collapse);

		} else {
			Animation animShow = AnimationUtils.loadAnimation(
					_FragmentActivity, R.anim.popup_show);

			popup.setVisibility(View.VISIBLE);
			popup.startAnimation(animShow);
			ImageView img_show = (ImageView) v.findViewById(R.id.img_show);
			img_show.setImageResource(R.drawable.ic_action_expand);

		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check which request we're responding to

		if (requestCode == 1) {

			// Make sure the request was successful
			if (resultCode == Activity.RESULT_OK) {

				_FragmentActivity.displayView(1);
			}
		}
	}
}
