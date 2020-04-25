package com.czb.ccparkinglot;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.czb.ccparkinglot.poi_route.PoiOverlay;
import com.czb.ccparkinglot.record.RecordActivity;
import com.czb.ccparkinglot.util.MyUtil;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    /**
     * 全局变量
     */
    private int isFirstLocate = 2;
    /**
     * UI相关
     */
    private ArrayAdapter<String> mAdapter = null;                //适配器,展示搜索结果
    private Button mSearchBtn;                            //搜索按钮
    private Button showNowLocationBtn;
    private DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;

    /**
     * 百度地图相关
     */
    public LocationClient mLocationClient;                      //定位SDK核心类
    private MapView mMapView;                                   //百度地图控件
    private BaiduMap mBaiduMap;                                 //百度地图对象
    private LatLng mNowLatLng;                                  //当前定位信息
    private BDLocation mCurrentLocation;                        //当前定位信息
    private PoiSearch mPoiSearch;                               //poi搜索模块
    private SuggestionSearch suggestionSearch = null;           //模糊搜索模块
    private MySensorEventListener mySensorEventListener;        //传感器
    private float mLastx = 0.0f;                                //传感器返回的方向


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirstLocate = 2;
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        requestPermissions();
    }

    private void initSensor() {
        //方向传感器监听
        mySensorEventListener = new MySensorEventListener(this);
        //增加监听：orientation listener
        mySensorEventListener.setOnOrientationListener(new MySensorEventListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                //将获取的x轴方向赋值给全局变量
                mLastx = x;
            }
        });
        //开启监听
        mySensorEventListener.start();
    }

    public void initView() {
        MyUtil.makeBarBeautiful(this);

        mNavigationView = findViewById(R.id.activity_main_navigationview);
        mNavigationView.setItemIconTintList(null);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        OnClickListener clickListener = new OnClickListener();
        mSearchBtn = findViewById(R.id.activity_main_button_search);
        mSearchBtn.setOnClickListener(clickListener);
        showNowLocationBtn = findViewById(R.id.activity_main_button_show_now_location);
        showNowLocationBtn.setOnClickListener(clickListener);
        Button createQrCode = findViewById(R.id.activity_main_button_createQrCode);
        createQrCode.setOnClickListener(clickListener);
        Button recordButton = findViewById(R.id.activity_main_button_record);
        recordButton.setOnClickListener(clickListener);
        Button account = findViewById(R.id.activity_main_button_account);
        account.setOnClickListener(clickListener);
    }

    public void initBaiduMap() {
        /**
         * baidu地图模块
         */
        mMapView = findViewById(R.id.activity_main_mapview);
        //隐藏缩放按钮
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();
        /**
         * 定位模块
         */
        mBaiduMap.setMyLocationEnabled(true);
        //初始化Client
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听
        mLocationClient.registerLocationListener(new MyLocationListener());
        //定位的配置信息option
        LocationClientOption option = new LocationClientOption();
        //高精度Mode
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 设置坐标类型,国测局经纬度坐标系:gcj02; 百度墨卡托坐标系:bd09; 百度经纬度坐标系:bd09ll
        option.setCoorType("bd09ll");
        //定位请求间隔1000ms（1秒）
        option.setScanSpan(1000);
        //打开GPS
        option.setOpenGps(true);
        //设备方向
        option.setNeedDeviceDirect(true);
        //地址信息
        option.setIsNeedAddress(true);
        //地址信息语义化信息
        option.setIsNeedLocationDescribe(true);
        //开始定位
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        //定位模式  NORMAL,FOLLOWING,COMPASS;
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null));
        /**
         * POI搜索模块
         */
        mPoiSearch = PoiSearch.newInstance();
        //增加监听：POI搜索结果
        mPoiSearch.setOnGetPoiSearchResultListener(new PoiSearchListener());
    }


    public class OnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.activity_main_button_createQrCode:
                    intent = new Intent(MainActivity.this, QrCodeActivity.class);
                    startActivity(intent);
                    break;
                case R.id.activity_main_button_record:
                    intent = new Intent(MainActivity.this, RecordActivity.class);
                    startActivity(intent);
                    break;
                case R.id.activity_main_button_account:
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                    break;
                case R.id.activity_main_button_search:
                    mPoiSearch.searchNearby(new PoiNearbySearchOption().location(mNowLatLng).radius(1000).keyword("停车场").pageNum(0));
                    break;
                case R.id.activity_main_button_show_now_location:
                    showNowLocation(mCurrentLocation);
                default:
            }
        }
    }


    private class PoiSearchListener implements OnGetPoiSearchResultListener {
        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                Toast.makeText(MainActivity.this, "未找到结果", Toast.LENGTH_SHORT).show();
                return;
            }
            if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
                //成功在传入的搜索city中搜索到POI
                //对result进行一些应用
                mBaiduMap.clear();
                PoiOverlay overlay = new PoiOverlay(mBaiduMap, MainActivity.this, mCurrentLocation);
                mBaiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(poiResult); //图层数据
                overlay.addToMap();         //添加到地图中(添加的都是marker)
                overlay.zoomToSpan();       //保证能显示所有marker
                return;
            }
            if (poiResult.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

                // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
                String strInfo = "在";
                for (CityInfo cityInfo : poiResult.getSuggestCityList()) {
                    strInfo += cityInfo.city;
                    strInfo += ",";
                }
                strInfo += "找到结果";
                Toast.makeText(MainActivity.this, strInfo, Toast.LENGTH_SHORT)
                        .show();
            }
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            if (poiDetailResult == null || poiDetailResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                Toast.makeText(MainActivity.this, "未找到结果", Toast.LENGTH_SHORT).show();
                return;
            }
            if (poiDetailResult.error == SearchResult.ERRORNO.NO_ERROR) {
                //成功在传入的搜索city中搜索到POI
                mBaiduMap.clear();
                mBaiduMap.addOverlay(new MarkerOptions().position(poiDetailResult.location)
                                             .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding))

                );
                //将该POI点设置为地图中心
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(poiDetailResult.location));
                return;
            }
            if (poiDetailResult.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
            }
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
        }
    }

    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            location.setRadius(0);
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
//            Toast.makeText(MainActivity.this, "定位结果编码："+location.getLocType(), Toast.LENGTH_SHORT).show();

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mLastx)//该参数由传感器提供
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            mNowLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            mCurrentLocation = location;
            if (isFirstLocate != 0) {

                MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(mNowLatLng);
                mBaiduMap.animateMapStatus(update);
                update = MapStatusUpdateFactory.zoomTo(16f);
                isFirstLocate--;
                mBaiduMap.animateMapStatus(update);
            }
        }
    }

    private void showNowLocation(BDLocation bdLocation) {
        LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(update);
        update = MapStatusUpdateFactory.zoomTo(16f);
        mBaiduMap.animateMapStatus(update);
    }

    private void requestPermissions() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                                              Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                                              Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                                              Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionList.isEmpty()) {
            initView();
            initBaiduMap();
            initSensor();
        } else {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "你必须同意全部权限才能使用本软件", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    initView();
                    initBaiduMap();
                    initSensor();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        Log.d("MainACtivity", "onResume: ");
    }


    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        Log.d("MainActivity", "onPause: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mMapView.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
        Log.d("MainActivity", "onDestroy: ");
    }
}
