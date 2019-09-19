package com.cnam.discover.api;

import com.cnam.discover.dto.IdentificationDto;
import com.cnam.discover.dto.TestDto;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiResponseParser {

    private static final String DATA = "data";

    public static IdentificationDto parseIdentification(JSONObject jsonObject){
        try {
            JSONObject data = jsonObject.getJSONObject(DATA);
            return new IdentificationDto(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static TestDto parseTest(JSONObject jsonObject){
        try {
            JSONObject data = jsonObject.getJSONObject(DATA);
            return new TestDto(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
