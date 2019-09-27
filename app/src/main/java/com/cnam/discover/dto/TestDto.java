package com.cnam.discover.dto;

import com.cnam.discover.interfaces.IIdentified;

import org.json.JSONException;
import org.json.JSONObject;

public class TestDto implements IIdentified {

    private final String CONFIDENCE = "confidence";
    private final String PREDICTED_LABEL = "predicted_label";

    private double confidence;
    private String predictedLabel;

    public TestDto(JSONObject jsonObject) throws JSONException {
        this.confidence = jsonObject.getDouble(CONFIDENCE);
        this.predictedLabel = jsonObject.getString(PREDICTED_LABEL);
    }

    public double getConfidence() {
        return confidence;
    }

    public String getPredictedLabel() {
        return predictedLabel;
    }

    @Override
    public String getName() {
        return predictedLabel;
    }

    @Override
    public String getProfilePicUrl() {
        return "https://www.biography.com/.image/ar_1:1%2Cc_fill%2Ccs_srgb%2Cg_face%2Cq_auto:good%2Cw_300/MTIzNjEzMTg5MzA5MzY3ODIy/benedict_cumberbatchjpg.jpg";
    }

    @Override
    public String getDescription() {
        return "Actor";
    }
}
