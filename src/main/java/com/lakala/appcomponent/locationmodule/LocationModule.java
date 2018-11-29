package com.lakala.appcomponent.locationmodule;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.lakala.appcomponent.locationmodule.manager.LocationManager;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

import java.util.HashMap;
import java.util.Map;

/**
 * 定位模块
 * Created by dingqq on 2018/4/24.
 */

public class LocationModule extends WXModule implements ILocationModule {

    @JSMethod
    @Override
    public boolean locale(final JSCallback callback) {

        Context context = mWXSDKInstance.getContext();

        if (context == null) {
            return false;
        }

        final LocationManager locationManager = LocationManager.getInstance(context);

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

        return true;
    }
}
