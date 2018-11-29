package com.lakala.appcomponent.locationmodule.manager;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * 定位
 * Created by dingqiqi on 2016/7/1.
 */
public class LocationManager {

    private static Context mContext;

    private LocationClient mLocationClient;

    private LocationManager() {
        initLocation(mContext);
    }

    public static LocationManager getInstance(Context context) {
        mContext = context.getApplicationContext();
        return LocationInstance.mInstance;
    }

    private static class LocationInstance {
        private static final LocationManager mInstance = new LocationManager();
    }

    /**
     * 初始化定位
     *
     * @param context 上下文
     */
    private void initLocation(Context context) {
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(context);
            LocationClientOption option = new LocationClientOption();
            option.setCoorType("bd09ll");// bd09ll:设置坐标类型，为百度经纬坐标系
            option.setIsNeedAddress(true); //返回的定位结果包含地址信息
            option.setTimeOut(30000);
            option.setOpenGps(true);// 打开GPS定位
            option.setScanSpan(0);// 设置定时定位的时间间隔为1s
            mLocationClient.setLocOption(option);// 设置定位方式
        }
    }

    /**
     * 启动定位
     */
    public void startLocation() {
        if (mLocationClient == null) {
            Log.d("LocationManager", "请初始化定位");
            return;
        }

        if (mLocationClient.isStarted()) {
            mLocationClient.stop();
        }

        mLocationClient.start();
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        if (mLocationClient == null) {
            Log.d("LocationManager", "请初始化定位");
            return;
        }

        mLocationClient.stop();
    }

    /**
     * 注册监听
     */
    public void setLocationListener(BDLocationListener listener) {
        if (mLocationClient == null) {
            Log.d("LocationManager", "请初始化定位");
            return;
        }

        if (listener == null) {
            Log.d("LocationManager", "定位监听不能为空");
            return;
        }

        mLocationClient.registerLocationListener(listener);
    }

    /**
     * 取消监听
     */
    public void removeLocationListener(BDLocationListener listener) {
        if (mLocationClient == null) {
            Log.d("LocationManager", "请初始化定位");
            return;
        }

        if (listener == null) {
            Log.d("LocationManager", "定位监听不能为空");
            return;
        }
        mLocationClient.unRegisterLocationListener(listener);
    }

}
