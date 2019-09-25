package com.cnam.discover.dto;

import com.cnam.discover.interfaces.IServerResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class TestDto implements IServerResponse {

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
}
