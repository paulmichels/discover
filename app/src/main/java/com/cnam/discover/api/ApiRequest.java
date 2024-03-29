package com.cnam.discover.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Map;

public class ApiRequest {
    private RequestQueue requestQueue;
    private Context context;
    private static final String baseUrl = "https://discover.nexiz.ovh";

    public static final String GET_IDENTIFICATION = "/identification";

    public ApiRequest(RequestQueue requestQueue, Context context){
        this.requestQueue = requestQueue;
        this.context = context;
    }

    public void apiGetRequest(String method, Map<String, String> parameters, final apiCallback callback){
        String url = baseUrl +  method + parseGetParameters(parameters);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(context, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(context, errorInstance(error));
            }
        });
        requestQueue.add(request);
    }

    public void apiPostRequest(String method, final Map<String, String> parameters, final apiCallback callback){
        String url = baseUrl +  method;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(context, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(context, errorInstance(error));
            }
        }) {
            @Override
            public byte[] getBody() {
                return new JSONObject(parameters).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 20000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 20000;
            }

            @Override
            public void retry(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }

    private String parseGetParameters(Map<String, String> parametersMap){
        StringBuilder parameters = null;
        for (Map.Entry<String, String> entry : parametersMap.entrySet()) {
            if(parameters == null){
                parameters = new StringBuilder("?" + entry.getKey() + "=" + entry.getValue());
            } else {
                parameters.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }
        }
        return parameters != null ? parameters.toString() : null;
    }

    private String errorInstance(VolleyError error){
        String instance;
        if (error instanceof NetworkError) {
            instance = NetworkError.class.getSimpleName();
        } else if (error instanceof ServerError) {
            instance = ServerError.class.getSimpleName();
        } else if (error instanceof AuthFailureError) {
            instance = AuthFailureError.class.getSimpleName();
        } else if (error instanceof ParseError) {
            instance = ParseError.class.getSimpleName();
        } else if (error instanceof TimeoutError) {
            instance = TimeoutError.class.getSimpleName();
        } else {
            instance = "Unknow Error";
        }
        Log.w("API", instance + " : " + error.getMessage());
        return instance;
    }

    public interface apiCallback{
        void onSuccess(Context context, JSONObject jsonObject);
        void onError(Context context, String message);
    }
}
