package com.isslam.husonmuslim;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class LoadingFragment extends Fragment {

	public LoadingFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.ly_loading,
				container, false);
		final ImageView myImage = (ImageView) rootView
				.findViewById(R.id.imageView1);
		final Animation myRotation = AnimationUtils.loadAnimation(getActivity()
				.getApplicationContext(), R.anim.rotator);
		myImage.startAnimation(myRotation);
		return rootView;
	}
}
