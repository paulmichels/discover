package com.cnam.discover.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.cnam.discover.api.ApiRequest;
import com.cnam.discover.api.ApiResponseParser;
import com.cnam.discover.api.ApiSingleton;
import com.cnam.discover.dto.TestDto;
import com.cnam.discover.interfaces.IServerResponse;
import com.cnam.discover.service.DiscoverService;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DataUpdateReceiver extends BroadcastReceiver {

    private static final String LOG = "DataUpdateReceiver";
    private boolean isReady = true;
    private IServerResponse serverResponse;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), DiscoverService.DISCOVER_PICTURE) && isReady) {
            isReady = false;
            byte[] data = intent.getByteArrayExtra(DiscoverService.DISCOVER_PICTURE);

            Map<String, String> parameters = new HashMap<>();
            parameters.put("data", Base64.encodeToString(data, Base64.DEFAULT));

            ApiRequest apiRequest = new ApiRequest(ApiSingleton.getInstance(context).getRequestQueue(), context);
            apiRequest.apiPostRequest(ApiRequest.GET_IDENTIFICATION, parameters, new ApiRequest.apiCallback() {
                @Override
                public void onSuccess(Context context, JSONObject jsonObject) {
                    Log.d(LOG, jsonObject.toString());
                    serverResponse = ApiResponseParser.parseTest(jsonObject);
                    isReady = true;
                }

                @Override
                public void onError(Context context, String message) {
                    Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
                    isReady = true;
                }
            });
            Log.d(LOG, Arrays.toString(intent.getByteArrayExtra(DiscoverService.DISCOVER_PICTURE)));
        }
    }

    public IServerResponse getServerResponse() {
        return serverResponse;
    }
}