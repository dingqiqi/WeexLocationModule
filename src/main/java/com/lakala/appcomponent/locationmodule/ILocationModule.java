package com.lakala.appcomponent.locationmodule;

import com.taobao.weex.bridge.JSCallback;

/**
 * 定位模块
 * Created by dingqq on 2018/4/17.
 */

public interface ILocationModule {

    //定位
    boolean locale(String params, JSCallback callback);

}
