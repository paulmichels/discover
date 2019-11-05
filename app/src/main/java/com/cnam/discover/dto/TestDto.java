package com.cnam.discover.dto;

import android.os.Parcel;

import com.cnam.discover.interfaces.IPerson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TestDto implements IPerson {

    private final String CONFIDENCE = "confidence";
    private final String PREDICTED_LABEL = "predicted_label";

    private String confidence;
    private String predictedLabel;


    public TestDto() {
    }

    public TestDto(JSONObject jsonObject) throws JSONException {
        this.confidence = jsonObject.getString(CONFIDENCE);
        this.predictedLabel = jsonObject.getString(PREDICTED_LABEL);
    }

    public String getConfidence() {
        return confidence;
    }

    @Override
    public String getFirstName() {
        return predictedLabel.toUpperCase();
    }

    @Override
    public String getProfilePicUrl() {
        return "https://scontent-cdg2-1.xx.fbcdn.net/v/t1.0-9/61243115_107739237144932_3995934312594669568_n.jpg?_nc_cat=103&_nc_oc=AQnVpCLSF5l0vuEAx4raKmPXJyiA5mR63h9evVVFdRUMsJN4VAnTJaZmppeEvF6xSKQ&_nc_ht=scontent-cdg2-1.xx&oh=20d9b37eb2ac63b8b06c18c0d2e52c37&oe=5E3B094A";
    }

    @Override
    public String getLastName() {
        return "MICHELS";
    }

    @Override
    public String getDescription() {
        return "Petit ingénieur";
    }

    @Override
    public String getAge() {
        return "25";
    }

    @Override
    public String getGender() {
        return "Homme";
    }

    public void setPredictedLabel(String predictedLabel) {
        this.predictedLabel = predictedLabel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
