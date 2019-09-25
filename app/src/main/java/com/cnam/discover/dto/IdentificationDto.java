package com.cnam.discover.dto;

import com.cnam.discover.interfaces.IServerResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Objet DTO représentant une personne identifiée
 */

public class IdentificationDto implements IServerResponse {

    private int id;
    private String firstName;
    private String lastName;
    private String profilePic;
    private String gender;
    private String hometown;
    private List<String> music;
    private List<String> event;

    private static final String FACEBOOK_ID = "facebook_id";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String PROFILE_PIC = "profile_pic";
    private static final String GENDER = "gender";
    private static final String HOMETOWN = "hometown";
    private static final String MUSIC = "music";
    private static final String EVENT = "event";

    public IdentificationDto(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getInt(FACEBOOK_ID);
        this.firstName = jsonObject.getString(FIRST_NAME);
        this.lastName = jsonObject.getString(LAST_NAME);
        this.profilePic = jsonObject.getString(PROFILE_PIC);
        this.gender = jsonObject.getString(GENDER);
        this.hometown = jsonObject.getString(HOMETOWN);
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getGender() {
        return gender;
    }
}
