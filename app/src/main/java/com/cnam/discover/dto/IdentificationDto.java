package com.cnam.discover.dto;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Objet DTO représentant une personne identifiée
 */

public class IdentificationDto {

    private String name;

    private static final String NAME = "name";

    /**
     * Permet de créer l'objet grâce à la réponse JSON
     * @param jsonObject Objet JSON retourné par le serveur
     * @throws JSONException Si le JSON ne correspond pas
     */
    public IdentificationDto(JSONObject jsonObject) throws JSONException {
        this.name = jsonObject.getString(NAME);
    }
}
