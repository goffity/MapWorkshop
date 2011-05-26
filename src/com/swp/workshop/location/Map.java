package com.swp.workshop.location;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;

public class Map extends MapActivity implements OnCheckedChangeListener,
		LocationListener, OnClickListener {

	MapView mapView;
	MapController mapController;

	FriendItemizedOverlay friendItemizedOverlay;
	RestaurantItemizedOverlay restaurantItemizedOverlay;

	OverlayItem oiFriend1;
	OverlayItem oiFriend2;

	OverlayItem oiRestaurant1;
	OverlayItem oiRestaurant2;
	OverlayItem oiRestaurant3;

	CheckBox cbFriend;
	CheckBox cbRestaurant;
	Button btMe;

	GeoPoint gpFriend1 = new GeoPoint((int) 13.90722059023707E6,
			(int) 100.53127527236938E6); // 13.90722059023707,
	// 100.53127527236938
	GeoPoint gpFriend2 = new GeoPoint((int) 13.907303904886888E6,
			(int) 100.53044378757477E6); // 13.907303904886888,
	// 100.53044378757477

	GeoPoint gpRestaurant1 = new GeoPoint((int) 13.904335801987413E6,
			(int) 100.52936017513275E6); // 13.904335801987413,
	// 100.52936017513275
	GeoPoint gpRestaurant2 = new GeoPoint((int) 13.902976710585888E6,
			(int) 100.53187608718872E6); // 13.902976710585888,
	// 100.53187608718872
	GeoPoint gpRestaurant3 = new GeoPoint((int) 13.905799029402388E6,
			(int) 100.53217649459839E6); // 13.905799029402388,

	LocationManager locManager;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mapView = (MapView) findViewById(R.id.ma_mv_map);
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(true);

		mapController = mapView.getController();

		mapController.animateTo(gpRestaurant1);
		mapController.setZoom(17);

		// init friend
		oiFriend1 = new OverlayItem(gpFriend1, "มานะ", "เพื่อนมานี");
		oiFriend2 = new OverlayItem(gpFriend2, "มาลี", "เพื่อนมานะ");

		friendItemizedOverlay = new FriendItemizedOverlay(getResources()
				.getDrawable(R.drawable.friend));
		friendItemizedOverlay.addOverlayItem(oiFriend1);
		friendItemizedOverlay.addOverlayItem(oiFriend2);
		// TODO for test
		// mapView.getOverlays().add(friendItemizedOverlay);

		// init Restaurant
		oiRestaurant1 = new OverlayItem(gpRestaurant1, "ป.กุ้งเผา",
				"อาหารทะเลสดๆ");
		oiRestaurant2 = new OverlayItem(gpRestaurant2, "ส้มตำป้าน้อย",
				"อาหารอิสานแซ่บๆ");
		oiRestaurant3 = new OverlayItem(gpRestaurant3, "ศิริวรรณหอยทอด",
				"หอยทอด ผัดไทย");

		restaurantItemizedOverlay = new RestaurantItemizedOverlay(
				getResources().getDrawable(R.drawable.restaurant));
		restaurantItemizedOverlay.addOverlayItem(oiRestaurant1);
		restaurantItemizedOverlay.addOverlayItem(oiRestaurant2);
		restaurantItemizedOverlay.addOverlayItem(oiRestaurant3);

		MyLocationOverlay myLocationOverlay = new MyLocationOverlay(this,
				mapView);
		myLocationOverlay.enableMyLocation();
		mapView.getOverlays().add(myLocationOverlay);

		cbFriend = (CheckBox) findViewById(R.id.cb_friend);
		cbRestaurant = (CheckBox) findViewById(R.id.cb_restaurant);
		btMe = (Button) findViewById(R.id.bt_me);

		cbFriend.setOnCheckedChangeListener(this);
		cbRestaurant.setOnCheckedChangeListener(this);
		btMe.setOnClickListener(this);

		initLocationService();

		/**
		 * gof modify version
		 */
		// default check
		cbFriend.setChecked(true);
		onCheckedChanged(cbFriend, true);

		cbRestaurant.setChecked(true);
		onCheckedChanged(cbRestaurant, true);
		/**
		 * 
		 */

	}

	private void initLocationService() {
		locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000,
				10, this);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	class FriendItemizedOverlay extends ItemizedOverlay<OverlayItem> {

		ArrayList<OverlayItem> overlayItemList = new ArrayList<OverlayItem>();

		public FriendItemizedOverlay(Drawable defaultMarker) {
			super(boundCenterBottom(defaultMarker));
		}

		@Override
		protected OverlayItem createItem(int i) {
			return overlayItemList.get(i);
		}

		@Override
		public int size() {
			return overlayItemList.size();
		}

		public void addOverlayItem(OverlayItem item) {
			overlayItemList.add(item);
			populate();
		}

		@Override
		protected boolean onTap(int index) {
			OverlayItem oi = overlayItemList.get(index);
			new AlertDialog.Builder(Map.this).setTitle(oi.getTitle())
					.setMessage(oi.getSnippet()).create().show();
			return super.onTap(index);
		}

	}

	class RestaurantItemizedOverlay extends ItemizedOverlay<OverlayItem> {

		ArrayList<OverlayItem> overlayItemList = new ArrayList<OverlayItem>();

		public RestaurantItemizedOverlay(Drawable defaultMarker) {
			super(boundCenterBottom(defaultMarker));
		}

		@Override
		protected OverlayItem createItem(int i) {
			return overlayItemList.get(i);
		}

		@Override
		public int size() {
			return overlayItemList.size();
		}

		public void addOverlayItem(OverlayItem item) {
			overlayItemList.add(item);
			populate();
		}

		@Override
		protected boolean onTap(int index) {
			OverlayItem oi = overlayItemList.get(index);
			new AlertDialog.Builder(Map.this).setTitle(oi.getTitle())
					.setMessage(oi.getSnippet()).create().show();
			return super.onTap(index);
		}
	}

	public void onClick(View v) {
		Location loc = locManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (loc != null) {
			int lat = (int) (loc.getLatitude() * 1E6);
			int lon = (int) (loc.getLongitude() * 1E6);
			GeoPoint gp = new GeoPoint(lat, lon);
			mapController.animateTo(gp);
		} else {
			Toast.makeText(this, "Cannot get last known location!",
					Toast.LENGTH_LONG).show();
		}
	}

	public void onLocationChanged(Location loc) {
		int lat = (int) (loc.getLatitude() * 1E6);
		int lon = (int) (loc.getLongitude() * 1E6);
		GeoPoint gp = new GeoPoint(lat, lon);
		mapController.animateTo(gp);
	}

	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.cb_friend:
			if (isChecked) {
				mapView.getOverlays().add(friendItemizedOverlay);
			} else {
				mapView.getOverlays().remove(friendItemizedOverlay);
			}
			break;
		case R.id.cb_restaurant:
			if (isChecked) {
				mapView.getOverlays().add(restaurantItemizedOverlay);
			} else {
				mapView.getOverlays().remove(restaurantItemizedOverlay);
			}
			break;
		default:
			break;
		}
		mapView.invalidate();
	}

}