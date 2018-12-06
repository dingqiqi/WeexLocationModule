package com.lakala.appcomponent.locationmodule;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.lakala.appcomponent.locationmodule.manager.LocationManager;
import com.lakala.appcomponent.permissionManager.PermissionsManager;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 定位模块
 * Created by dingqq on 2018/4/24.
 */

public class LocationModule extends WXModule implements ILocationModule {

    //可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
    private String mCoorType = "gcj02";

    @JSMethod
    @Override
    public boolean locale(String params, final JSCallback callback) {

        final Context context = mWXSDKInstance.getContext();

        if (context == null) {
            return false;
        }

        if (!TextUtils.isEmpty(params)) {
            try {
                JSONObject jsonObject = new JSONObject(params);
                if (jsonObject.has("coorType")) {
                    mCoorType = jsonObject.getString("coorType");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        PermissionsManager.getInstance().requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                , new PermissionsManager.requestPermissionListener() {
                    @Override
                    public void onSuccess() {
                        final LocationManager locationManager = LocationManager.getInstance(context, mCoorType);

                        locationManager.setLocationListener(new BDLocationListener() {
                            @Override
                            public void onReceiveLocation(BDLocation location) {
                                String provide = location.getProvince(); //省
                                String city = location.getCity();// 城市
                                String district = location.getDistrict();// 区（或县）
                                String street = location.getStreet();// 街道
                                String streetNumber = location.getStreetNumber();// 街道号
                                double latitude = location.getLatitude();// 纬度
                                double longitude = location.getLongitude();// 经度

                                String address = "";

                                if (!TextUtils.isEmpty(provide)) {
                                    //上送信息
                                    address = provide +
                                            "|" +
                                            city +
                                            "|" +
                                            district +
                                            "|" +
                                            street +
                                            "|" +
                                            streetNumber;
                                }

                                Map<String, Object> map = new HashMap<>();
                                map.put("address", address);
                                map.put("latitude", latitude);
                                map.put("longitude", longitude);

                                if (callback != null) {
                                    callback.invoke(map);
                                }

                                locationManager.stopLocation();
                                locationManager.removeLocationListener(this);
                            }
                        });

                        locationManager.startLocation();
                    }

                    @Override
                    public void onFail() {
                    }
                }, "请开启定位权限");

        return true;
    }
}
