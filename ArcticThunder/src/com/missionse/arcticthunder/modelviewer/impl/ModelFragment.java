package com.missionse.arcticthunder.modelviewer.impl;

import com.missionse.arcticthunder.modelviewer.ModelParser;
import com.missionse.arcticthunder.modelviewer.ModelViewerFragment;
import com.missionse.arcticthunder.modelviewer.ModelViewerRenderer;

public class ModelFragment extends ModelViewerFragment {

	@Override
	protected ModelViewerRenderer createRenderer(final int modelID, final ModelParser parser) {
		return new ModelRenderer(getActivity(), this, modelID, parser);
	}
}
