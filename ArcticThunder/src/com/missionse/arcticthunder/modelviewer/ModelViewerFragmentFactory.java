package com.missionse.arcticthunder.modelviewer;

import android.os.Bundle;

import com.missionse.arcticthunder.modelviewer.impl.ModelFragment;
import com.missionse.arcticthunder.modelviewer.impl.ModelGestureListener;
import com.missionse.arcticthunder.modelviewer.impl.ObjModelParser;

public final class ModelViewerFragmentFactory
{
	private ModelViewerFragmentFactory() {
	}

	public static ModelViewerFragment createObjModelFragment(final int modelID) {
		return createObjModelFragment(modelID, new ModelGestureListener());
	}

	public static ModelViewerFragment createObjModelFragment(final int modelID, final ModelViewerGestureListener gestureListener) {
		ModelViewerFragment fragment = new ModelFragment();
		fragment.setModelParser(new ObjModelParser());
		fragment.setGestureListener(gestureListener);

		Bundle arguments = new Bundle();
		arguments.putInt(ModelViewerFragment.ARG_MODEL_ID, modelID);
		fragment.setArguments(arguments);

		return fragment;
	}
}
