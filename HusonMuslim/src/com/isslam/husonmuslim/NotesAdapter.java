package com.isslam.husonmuslim;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isslam.husonmuslim.controllers.BookChaptersManager;
import com.isslam.husonmuslim.controllers.SemiChapterManager;
import com.isslam.husonmuslim.utils.GlobalConfig;

public class NotesAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;
	TextView content;
	// action 3d context menu
	// action id
	private static final int ID_READ = 1;
	private static final int ID_COPY = 2;
	final QuickAction quickAction;
	String hadithContent = "";
	NotesFragment scope;

	public NotesAdapter(Activity a, ArrayList<HashMap<String, String>> _source,
			NotesFragment _scope) {
		activity = a;
		data = _source;
		scope = _scope;

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		ActionItem nextItem = new ActionItem(ID_READ, activity.getResources()
				.getString(R.string.quick_action_read), activity.getResources()
				.getDrawable(R.drawable.ic_action_read));
		ActionItem prevItem = new ActionItem(ID_COPY, activity.getResources()
				.getString(R.string.quick_action_copy), activity.getResources()
				.getDrawable(R.drawable.ic_action_copy));

		// orientation
		quickAction = new QuickAction(activity, QuickAction.VERTICAL);

		// add action items into QuickAction
		quickAction.addActionItem(nextItem);
		quickAction.addActionItem(prevItem);

		quickAction
				.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
					@Override
					public void onItemClick(QuickAction source, int pos,
							int actionId) {
						ActionItem actionItem = quickAction.getActionItem(pos);

						// here we can filter which action item was clicked with
						// pos or actionId parameter
						if (actionId == ID_READ) {
							BookChaptersManager bookChaptersManager = BookChaptersManager
									.getInstance();
							bookChaptersManager.setselectedChapterId(Integer
									.parseInt(sura.get("chapter_id")));
							SemiChapterManager semiChapterManager = SemiChapterManager
									.getInstance();
							semiChapterManager.setselectedSemiChapterId(Integer
									.parseInt(sura.get("_id")));
							semiChapterManager.SetSemiChaptersList(activity
									.getApplicationContext());
							semiChapterManager.setSelectedContentId(Integer
									.parseInt(sura.get("content_id")));
							// in.putExtra("songIndex", position);
							((com.isslam.husonmuslim.MainActivity) (scope.getActivity()))
									.displayView(1);

						} else if (actionId == ID_COPY) {
							android.content.ClipboardManager clipboard = (android.content.ClipboardManager) activity
									.getSystemService(Context.CLIPBOARD_SERVICE);
							android.content.ClipData clip = android.content.ClipData
									.newPlainText("«·ÕœÌÀ", hadithContent);
							clipboard.setPrimaryClip(clip);
							GlobalConfig.ShowToast(
									activity,
									activity.getResources().getString(
											R.string.copy_success));

						}
					}
				});
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	HashMap<String, String> sura;
	View vi;

	public View getView(int position, View convertView, ViewGroup parent) {
		vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.ly_note_item, null);

		sura = new HashMap<String, String>();
		sura = data.get(position);
		//String chapter_pre = vi.getResources().getString(R.string.chapter);
		String semi_chapter_pre = vi.getResources().getString(
				R.string.semi_chapter);
		TextView note_title = (TextView) vi.findViewById(R.id.note_title);
		TextView note_color_ind = (TextView) vi
				.findViewById(R.id.note_color_ind);

		note_color_ind.setBackgroundColor(Integer.parseInt(sura.get("color")));

		View hr2 = (View) vi.findViewById(R.id.hr2);
		hr2.setBackgroundColor(Integer.parseInt(sura.get("color")));

		TextView semi_chapter_title = (TextView) vi
				.findViewById(R.id.semi_chapter_title);
		note_title.setText(sura.get("title"));

		TextView note_body = (TextView) vi.findViewById(R.id.note_body);

		note_body.setText(sura.get("body"));

		semi_chapter_title.setText( sura.get("chapter_name")
				+ " , " + semi_chapter_pre + " " + sura.get("name"));

		RelativeLayout nafigation_container = (RelativeLayout) vi
				.findViewById(R.id.nafigation_container);
		nafigation_container.setTag(position);
		nafigation_container.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sura = data.get(Integer.parseInt(String.valueOf(v.getTag())));

				BookChaptersManager bookChaptersManager = BookChaptersManager
						.getInstance();
				bookChaptersManager.setselectedChapterId(Integer.parseInt(sura
						.get("chapter_id")));
				SemiChapterManager semiChapterManager = SemiChapterManager
						.getInstance();
				semiChapterManager.setselectedSemiChapterId(Integer
						.parseInt(sura.get("_id")));
				semiChapterManager.SetSemiChaptersList(activity
						.getApplicationContext());
				semiChapterManager.setSelectedContentId(Integer.parseInt(sura
						.get("content_id")));
				// in.putExtra("songIndex", position);
				((com.isslam.husonmuslim.MainActivity) (scope.getActivity()))
						.displayView(1);
			}
		});
		RelativeLayout btn_share = (RelativeLayout) vi
				.findViewById(R.id.btn_share);
		btn_share.setTag(position);
		btn_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sura = data.get(Integer.parseInt(String.valueOf(v.getTag())));
				String share_text = " „‰ √–ﬂ«—  ÿ»Ìﬁ Õ’‰ «·„”·„ , "+sura.get("name") + " \n \n " +sura
						.get("content")
						+ "\n"
						+ "http://play.google.com/store/apps/details?id="
						+ activity.getPackageName();
				sendIntent.putExtra(Intent.EXTRA_TEXT, share_text);
				sendIntent.setType("text/plain");

				activity.startActivity(Intent.createChooser(sendIntent, vi
						.getResources().getText(R.string.share_hadith)));

			}
		});
		RelativeLayout btn_delete = (RelativeLayout) vi
				.findViewById(R.id.btn_delete);
		btn_delete.setTag(position);
		btn_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				DeleteDialog(Integer.parseInt(String.valueOf(v.getTag())));

			}
		});
		RelativeLayout btn_more = (RelativeLayout) vi
				.findViewById(R.id.btn_more);
		btn_more.setTag(position);
		btn_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sura = data.get(Integer.parseInt(String.valueOf(v.getTag())));

				hadithContent = sura.get("content");
				quickAction.show(v);
			}
		});

		content = (TextView) vi.findViewById(R.id.webkit);

		String html = sura.get("content");

		content.setTextColor(GlobalConfig.defaultFColor);
		content.setTextSize(GlobalConfig.defaultFontSize);
		content.setEllipsize(TextUtils.TruncateAt.END);
		content.setMaxLines(4);
		content.setText(html);

		return vi;
	}

	private void DeleteDialog(int _id) {
		final int id = _id;
		new AlertDialog.Builder(activity)
				.setTitle(
						activity.getResources().getString(R.string.note_title))
				.setMessage(
						activity.getResources()
								.getString(R.string.note_deletion))
				.setPositiveButton(
						activity.getResources().getString(R.string.yes),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// continue with delete
								sura = data.get(id);

								// TODO Auto-generated method stub
								GlobalConfig.GetmyDbHelper()
										.delete_note_by_content_id(
												sura.get("content_id"));
								GlobalConfig.ShowToast(
										activity,
										activity.getResources().getString(
												R.string.note_del));
								NotesFragment _activity = (NotesFragment) scope;
								_activity.Refresh();

							}
						})
				.setNegativeButton(
						activity.getResources().getString(R.string.no),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// do nothing
							}
						}).setIcon(android.R.drawable.ic_dialog_alert).show();
	}

}