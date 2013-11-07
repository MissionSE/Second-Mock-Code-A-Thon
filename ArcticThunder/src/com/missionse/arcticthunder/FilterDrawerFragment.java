package com.missionse.arcticthunder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.missionse.arcticthunder.model.AssetType;

public class FilterDrawerFragment extends Fragment {

	private final List<String> menuItems = new ArrayList<String>();
	private ListView listView;
	private HashMap<String, CheckBox> checkboxes = new HashMap<String, CheckBox>();

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.filter_drawer_list, null);
		listView = (ListView) view.findViewById(R.id.filter_list);
		return view;
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		createMenu();
	}

	private void createMenu() {
		menuItems.add("FRIENDS");
		menuItems.add("ENEMY ASSETS");
		menuItems.add("Enemy Vehicles");
		menuItems.add("Enemy Buildings");
		menuItems.add("Enemy Watch Stands");
		menuItems.add("Roaming Troops");
		menuItems.add("TOWN ASSETS");
		menuItems.add("Church");
		menuItems.add("Schools");
		menuItems.add("Malls");
		menuItems.add("PHOTOS/VIDEOS");
		menuItems.add("HOTSPOTS");
		menuItems.add("POSSIBLE THREATS");

		listView.setAdapter(new FilterAdapter(getActivity(), R.layout.filter_drawer_item, menuItems));
	}

	public void onCheckboxStateChange(final String item, final boolean isChecked) {
		ArcticThunderActivity activity = (ArcticThunderActivity) getActivity();

		if (item.equals("FRIENDS")) {
			activity.setAssetShown(AssetType.FRIEND, isChecked);
		} else if (item.equals("ENEMY ASSETS")) {
			activity.setAssetShown(AssetType.ENEMY_BUILDING, isChecked);
			activity.setAssetShown(AssetType.ENEMY_ROAMING_TROOP, isChecked);
			activity.setAssetShown(AssetType.ENEMY_VEHICLE, isChecked);
			activity.setAssetShown(AssetType.ENEMY_WATCH_STAND, isChecked);
		} else if (item.equals("Enemy Vehicles")) {
			activity.setAssetShown(AssetType.ENEMY_VEHICLE, isChecked);
		} else if (item.equals("Enemy Buildings")) {
			activity.setAssetShown(AssetType.ENEMY_BUILDING, isChecked);
		} else if (item.equals("Enemy Watch Stands")) {
			activity.setAssetShown(AssetType.ENEMY_WATCH_STAND, isChecked);
		} else if (item.equals("Roaming Troops")) {
			activity.setAssetShown(AssetType.ENEMY_ROAMING_TROOP, isChecked);
		} else if (item.equals("TOWN ASSETS")) {
			activity.setAssetShown(AssetType.TOWN_CHURCH, isChecked);
			activity.setAssetShown(AssetType.TOWN_MALL, isChecked);
			activity.setAssetShown(AssetType.TOWN_SCHOOL, isChecked);
		} else if (item.equals("Church")) {
			activity.setAssetShown(AssetType.TOWN_CHURCH, isChecked);
		} else if (item.equals("Schools")) {
			activity.setAssetShown(AssetType.TOWN_SCHOOL, isChecked);
		} else if (item.equals("Malls")) {
			activity.setAssetShown(AssetType.TOWN_MALL, isChecked);
		} else if (item.equals("PHOTOS/VIDEOS")) {
			activity.setAssetShown(AssetType.PHOTO, isChecked);
			activity.setAssetShown(AssetType.VIDEO, isChecked);
		} else if (item.equals("HOTSPOTS")) {
			activity.setAssetShown(AssetType.WIFI, isChecked);
		} else if (item.equals("POSSIBLE THREATS")) {
			activity.setAssetShown(AssetType.POSSIBLE_THREAT, isChecked);
		} else if (item.equals("POSSIBLE THREATS")) {
			activity.setAssetShown(AssetType.POSSIBLE_THREAT, isChecked);
		}

		updateCheckboxes(activity);
	}

	private void updateCheckboxes(final ArcticThunderActivity activity) {
		for (int index = 0; index < menuItems.size(); index++) {
			String item = (String) listView.getAdapter().getItem(index);

			if (checkboxes.get(item) != null) {
				if (item.equals("Enemy Vehicles")) {
					checkboxes.get(item).setChecked(activity.isAssetShown(AssetType.ENEMY_VEHICLE));
				} else if (item.equals("Enemy Buildings")) {
					checkboxes.get(item).setChecked(activity.isAssetShown(AssetType.ENEMY_BUILDING));
				} else if (item.equals("Enemy Watch Stands")) {
					checkboxes.get(item).setChecked(activity.isAssetShown(AssetType.ENEMY_WATCH_STAND));
				} else if (item.equals("Roaming Troops")) {
					checkboxes.get(item).setChecked(activity.isAssetShown(AssetType.ENEMY_ROAMING_TROOP));
				} else if (item.equals("Church")) {
					checkboxes.get(item).setChecked(activity.isAssetShown(AssetType.TOWN_CHURCH));
				} else if (item.equals("Schools")) {
					checkboxes.get(item).setChecked(activity.isAssetShown(AssetType.TOWN_SCHOOL));
				} else if (item.equals("Malls")) {
					checkboxes.get(item).setChecked(activity.isAssetShown(AssetType.TOWN_MALL));
				}
			}
		}

	}

	private class FilterAdapter extends ArrayAdapter<String> {

		private int listItemResource;
		private List<String> filters;

		public FilterAdapter(final Context context, final int resource, final List<String> filters) {
			super(context, resource, filters);
			listItemResource = resource;
			this.filters = filters;
		}

		@Override
		public View getView(final int position, View convertView, final ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(listItemResource, null);
			}

			String item = filters.get(position);
			if (item != null) {
				ImageView icon = (ImageView) convertView.findViewById(R.id.filter_drawer_icon);
				CheckBox text = (CheckBox) convertView.findViewById(R.id.filter_drawer_text);

				LinearLayout.LayoutParams leftPadding = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				leftPadding.setMargins(30, 0, 0, 0);

				if (icon != null && text != null) {
					if (item.equals("FRIENDS")) {
						icon.setImageResource(R.drawable.friendly_icon);
					} else if (item.equals("ENEMY ASSETS")) {
						icon.setVisibility(View.GONE);
					} else if (item.equals("Enemy Vehicles")) {
						icon.setImageResource(R.drawable.enemy_icon);
					} else if (item.equals("Enemy Buildings")) {
						icon.setImageResource(R.drawable.enemy_icon);
					} else if (item.equals("Enemy Watch Stands")) {
						icon.setImageResource(R.drawable.enemy_icon);
					} else if (item.equals("Roaming Troops")) {
						icon.setImageResource(R.drawable.enemy_icon);
					} else if (item.equals("TOWN ASSETS")) {
						icon.setVisibility(View.GONE);
					} else if (item.equals("Church")) {
						icon.setImageResource(R.drawable.church_icon);
					} else if (item.equals("Schools")) {
						icon.setImageResource(R.drawable.school_icon);
					} else if (item.equals("Malls")) {
						icon.setImageResource(R.drawable.mall_icon);
					} else if (item.equals("PHOTOS/VIDEOS")) {
						icon.setImageResource(R.drawable.camera_icon);
					} else if (item.equals("HOTSPOTS")) {
						icon.setImageResource(R.drawable.wifi_icon);
					} else if (item.equals("POSSIBLE THREATS")) {
						icon.setImageResource(R.drawable.ic_launcher);
					}
					text.setText(item);
					text.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
							FilterDrawerFragment.this.onCheckboxStateChange(buttonView.getText().toString(), isChecked);
						}
					});

					checkboxes.put(item, text);
				}
			}
			return convertView;
		}
	}
}
