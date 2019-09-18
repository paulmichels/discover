package com.cnam.discover.dto;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Objet DTO représentant une personne identifiée
 */

public class IdentificationDto {

    private int id;
    private String name;
    private String firstName;
    private String lastName;
    private String profilePic;
    private String locale;
    private int timezone;
    private String gender;

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String PROFILE_PIC = "profile_pic";
    private static final String LOCALE = "locale";
    private static final String TIMEZONE = "timezone";
    private static final String GENDER = "gender";


    /**
     * Permet de créer l'objet grâce à la réponse JSON
     * @param jsonObject Objet JSON retourné par le serveur
     * @throws JSONException Si le JSON ne correspond pas
     */

    public IdentificationDto(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getInt(ID);
        this.name = jsonObject.getString(NAME);
        this.firstName = jsonObject.getString(FIRST_NAME);
        this.lastName = jsonObject.getString(LAST_NAME);
        this.profilePic = jsonObject.getString(PROFILE_PIC);
        this.locale = jsonObject.getString(LOCALE);
        this.timezone = jsonObject.getInt(TIMEZONE);
        this.gender = jsonObject.getString(GENDER);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public String getLocale() {
        return locale;
    }

    public int getTimezone() {
        return timezone;
    }

    public String getGender() {
        return gender;
    }
}
