package com.cnam.discover.api;

import com.cnam.discover.dto.FacebookDto;
import com.cnam.discover.dto.InstagramDto;
import com.cnam.discover.dto.TestDto;
import com.cnam.discover.interfaces.IIdentified;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApiResponseParser {

    private static final String DATA = "data";

    public static List<IIdentified> parseInstagram(JSONObject jsonObject){
        List<IIdentified> identifiedList = new ArrayList<>();
        try {
            JSONArray data = jsonObject.getJSONArray(DATA);
            for (int i = 0; i <data.length(); i++){
                identifiedList.add(new InstagramDto(data.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return identifiedList;
    }

    public static List<IIdentified> parseTest(JSONObject jsonObject){
        List<IIdentified> identifiedList = new ArrayList<>();
        try {
            JSONArray data = jsonObject.getJSONArray(DATA);
            for (int i = 0; i <data.length(); i++){
                identifiedList.add(new TestDto(data.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return identifiedList;
    }

    public static List<IIdentified> parseFacebook(JSONObject jsonObject){
        List<IIdentified> identifiedList = new ArrayList<>();
        try {
            JSONArray data = jsonObject.getJSONArray(DATA);
            for (int i = 0; i <data.length(); i++){
                identifiedList.add(new FacebookDto(data.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return identifiedList;
    }
}
