package com.cnam.discover;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.volley.RequestQueue;
import com.cnam.discover.api.ApiRequest;
import com.cnam.discover.service.DiscoverService;

public class DataUpdateReceiver extends BroadcastReceiver {

    private ApiRequest apiRequest;
    private RequestQueue requestQueue;

    public DataUpdateReceiver(ApiRequest apiRequest, RequestQueue requestQueue) {
        this.apiRequest = apiRequest;
        this.requestQueue = requestQueue;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DiscoverService.REFRESH_DATA_INTENT)) {
            //
        }
    }
}