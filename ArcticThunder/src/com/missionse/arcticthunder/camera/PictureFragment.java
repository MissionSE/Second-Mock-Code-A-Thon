package com.missionse.arcticthunder.camera;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.missionse.arcticthunder.R;

public class PictureFragment extends Fragment {

	private View contentView;

	public PictureFragment() {

	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.fragment_picture, null);

		return contentView;
	}

	public void setImageBitmap(final Bitmap mImageBitmap) {
		ImageView image = (ImageView) contentView.findViewById(R.id.picture);
		image.setImageBitmap(mImageBitmap);
	}
}
