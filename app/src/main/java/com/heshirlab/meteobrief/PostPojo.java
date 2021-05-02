package com.heshirlab.meteobrief;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.util.Map;

public class PostPojo {

    @SerializedName("name")
    @Expose
    private String cityName;

    public String getCityName() {
        return cityName;
    }

    @SerializedName("main")
    @Expose
    private Map<String, Float> mainParams;

    public Map<String, Float> getMainParams() {
        return mainParams;
    }

    @SerializedName("wind")
    @Expose
    private Map<String, Float> WindParams;

    public Map<String, Float> getWindParams() {
        return WindParams;
    }
}

