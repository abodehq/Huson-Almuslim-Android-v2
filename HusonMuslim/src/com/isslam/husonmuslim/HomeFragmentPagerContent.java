package com.isslam.husonmuslim;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.isslam.husonmuslim.utils.GlobalConfig;

public class HomeFragmentPagerContent extends Fragment {

	int mCurrentPage;
	String html = "";
	String footer = "";
	HomeFragment homeFragment;
	HomeFragmentPagerContent _context;

	@TargetApi(14) @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/** Getting the arguments to the Bundle object */
		Bundle data = getArguments();

		/** Getting integer data of the key current_page from the bundle */
		mCurrentPage = data.getInt("current_page", 0);
		html = data.getString("content", "");
		footer = data.getString("footer", "");
		_context = this;

	}

	public TextView content;
	public TextView txt_footer;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.ly_main_fragment_content, container, false);
		content = (TextView) v.findViewById(R.id.webkit);
		txt_footer= (TextView) v.findViewById(R.id.txt_footer);
		UpdateContent();

		return v;
	}

	public void UpdateContent() {

		content.setText(html);
		txt_footer.setText(footer);
		content.setTextColor(GlobalConfig.fontColor);
		if(!GlobalConfig.showBg)
		content.setBackgroundColor(GlobalConfig.bgColor);
		content.setTextSize(GlobalConfig.fontSize);
	}
}
