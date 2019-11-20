package com.cnam.discover.dto;

import android.annotation.SuppressLint;
import android.os.Parcel;

import com.cnam.discover.IPerson;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("ParcelCreator")
public class TestDto implements IPerson{

    private final String CONFIDENCE = "confidence";
    private final String PREDICTED_LABEL = "predicted_label";

    private double confidence;
    private String predictedLabel;
    private String profilePic;
    private String lastName;
    private String description;
    private String age;
    private String gender;

    public TestDto() {
    }

    public TestDto(JSONObject jsonObject) throws JSONException {
        this.confidence = jsonObject.getDouble(CONFIDENCE);
        this.predictedLabel = jsonObject.getString(PREDICTED_LABEL);
    }

    public double getConfidence() {
        return confidence;
    }

    public void setPredictedLabel(String firstName) {
        this.predictedLabel = firstName;
    }

    @Override
    public String getFirstName() {
        return predictedLabel.toUpperCase();
    }

    @Override
    public String getProfilePicUrl() {
        return profilePic;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getAge() {
        return age;
    }

    @Override
    public String getGender() {
        return gender;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(confidence);
        dest.writeString(predictedLabel);
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public int compareTo(IPerson o) {
        if(o.getFirstName().equals(this.getFirstName())){
            return 0;
        }
        return 1;
    }
}
