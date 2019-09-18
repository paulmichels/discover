package com.cnam.discover;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.cnam.discover.api.ApiRequest;
import com.cnam.discover.api.ApiResponseParser;
import com.cnam.discover.dto.IdentificationDto;
import com.cnam.discover.service.CameraService;

import org.json.JSONException;
import org.json.JSONObject;

public class DataUpdateReceiver extends BroadcastReceiver {

    private ApiRequest apiRequest;
    private RequestQueue requestQueue;

    public DataUpdateReceiver(ApiRequest apiRequest, RequestQueue requestQueue) {
        this.apiRequest = apiRequest;
        this.requestQueue = requestQueue;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(CameraService.REFRESH_DATA_INTENT)) {
            String image = intent.getStringExtra(CameraService.BASE_64_BITMAP);
            apiRequest.apiPostImage(ApiRequest.GET_IDENTIFICATION, image, new ApiRequest.apiCallback() {
                @Override
                public void onSuccess(Context context, JSONObject jsonObject) {
                    IdentificationDto identificationDto = ApiResponseParser.parseIdentification(jsonObject);
                }

                @Override
                public void onError(Context context, String message) {
                    Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}