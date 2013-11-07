package com.missionse.arcticthunder;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.missionse.arcticthunder.map.MapsFragment;
import com.missionse.arcticthunder.modelviewer.ModelViewerFragment;
import com.missionse.arcticthunder.modelviewer.ModelViewerFragmentFactory;
import com.missionse.arcticthunder.modelviewer.ObjectLoadedListener;

public class ArcticThunderActivity extends Activity implements ObjectLoadedListener {

	private SlidingMenu navigationDrawer;
	private SlidingMenu filterDrawer;

	private MapsFragment mapsFragment;
	private ModelViewerFragment modelViewerFragment;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mapsFragment = new MapsFragment();
		modelViewerFragment = ModelViewerFragmentFactory.createObjModelFragment(R.raw.lobby_obj);
		modelViewerFragment.registerObjectLoadedListener(this);

		createNavigationMenu();

		createFilterMenu();

		showMap();
	}

	private void createNavigationMenu() {
		navigationDrawer = new SlidingMenu(this);
		navigationDrawer.setMode(SlidingMenu.LEFT);
		navigationDrawer.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		navigationDrawer.setShadowWidthRes(R.dimen.drawer_shadow_width);
		navigationDrawer.setShadowDrawable(R.drawable.shadow_left);
		navigationDrawer.setBehindWidthRes(R.dimen.drawer_width);
		navigationDrawer.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		navigationDrawer.setMenu(R.layout.nav_drawer);

		Fragment leftDrawerFragment;
		FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
		leftDrawerFragment = new NavigationDrawerFragment();
		transaction.replace(R.id.nav_drawer, leftDrawerFragment);
		transaction.commit();
	}

	private void createFilterMenu() {
		filterDrawer = new SlidingMenu(this);
		filterDrawer.setMode(SlidingMenu.RIGHT);
		filterDrawer.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		filterDrawer.setShadowWidthRes(R.dimen.drawer_shadow_width);
		filterDrawer.setShadowDrawable(R.drawable.shadow_right);
		filterDrawer.setBehindWidthRes(R.dimen.drawer_width);
		filterDrawer.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		filterDrawer.setMenu(R.layout.filter_drawer);

		Fragment rightDrawerFragment;
		FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
		rightDrawerFragment = new FilterDrawerFragment();
		transaction.replace(R.id.filter_drawer, rightDrawerFragment);
		transaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * HOOKS FROM MENUS
	 */

	public void showMap() {
		FragmentManager fragmentManager = getFragmentManager();
		//Fragment fragment = fragmentManager.findFragmentById(R.id.content);

		fragmentManager.beginTransaction().replace(R.id.content, mapsFragment).commit();

		//		if (!fragment instanceof) {
		//			fragment = new MapsFragment();
		//			fm.beginTransaction().add(R.id.content, fragment).commit();
		//		}
	}

	public void showAR() {

	}

	public void showCamera() {

	}

	public void showChat() {
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content, modelViewerFragment).commit();

	}

	public void showModelViewer() {
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content, modelViewerFragment).commit();
	}

	/**
	 * CALLBACKS
	 */

	@Override
	public void onObjectLoaded() {
		modelViewerFragment.getController().scale(0.00025f);
		modelViewerFragment.getAnimator().scaleTo(0.045f, 1000);
		modelViewerFragment.getAnimator().rotateTo(-45f, 225f, 0f, 1000);
	}

}
