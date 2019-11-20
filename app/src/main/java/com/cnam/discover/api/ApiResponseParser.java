package com.cnam.discover.api;

import com.cnam.discover.IPerson;
import com.cnam.discover.dto.TestDto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApiResponseParser {

    private static final String DATA = "data";


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

}
