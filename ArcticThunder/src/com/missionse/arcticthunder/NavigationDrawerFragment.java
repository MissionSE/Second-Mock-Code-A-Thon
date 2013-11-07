package com.missionse.arcticthunder;

import java.util.ArrayList;
import java.util.List;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NavigationDrawerFragment extends ListFragment {
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.nav_drawer_list, null);
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		createMenu();
	}

	private void createMenu() {
		final List<String> menuItems = new ArrayList<String>();
		menuItems.add("MAP");
		menuItems.add("AR");
		//menuItems.add("CAMERA");
		menuItems.add("CHAT");

		setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.nav_drawer_item, R.id.nav_drawer_text,
				menuItems));
	}

	@Override
	public void onListItemClick(final ListView listView, final View view, final int position, final long id) {
		ArcticThunderActivity activity = (ArcticThunderActivity) getActivity();

		String selectedItem = (String) listView.getAdapter().getItem(position);
		if (selectedItem.equals("MAP")) {
			activity.showMap();
		} else if (selectedItem.equals("CAMERA")) {
			activity.showCamera();
		} else if (selectedItem.equals("AR")) {
			activity.showAR();
		} else if (selectedItem.equals("CHAT")) {
			activity.showChat();
		}
	}
}
