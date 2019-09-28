package com.cnam.discover.api;

import com.cnam.discover.dto.FacebookDto;
import com.cnam.discover.dto.InstagramDto;
import com.cnam.discover.dto.TestDto;
import com.cnam.discover.interfaces.IPerson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApiResponseParser {

    private static final String DATA = "data";

    public static List<IPerson> parseInstagram(JSONObject jsonObject){
        List<IPerson> people = new ArrayList<>();
        try {
            JSONArray data = jsonObject.getJSONArray(DATA);
            for (int i = 0; i <data.length(); i++){
                people.add(new InstagramDto(data.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return people;
    }
/*
    public static List<IPerson> parseTest(JSONObject jsonObject){
        List<IPerson> people = new ArrayList<>();
        try {
            JSONArray data = jsonObject.getJSONArray(DATA);
            for (int i = 0; i <data.length(); i++){
                people.add(new TestDto(data.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return people;
    }

 */

    public static IPerson parseTest(JSONObject jsonObject){
        IPerson person = null;
        try {
            person = new TestDto(jsonObject.getJSONObject(DATA));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return person;
    }

    public static List<IPerson> parseFacebook(JSONObject jsonObject){
        List<IPerson> people = new ArrayList<>();
        try {
            JSONArray data = jsonObject.getJSONArray(DATA);
            for (int i = 0; i <data.length(); i++){
                people.add(new FacebookDto(data.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return people;
    }
}
