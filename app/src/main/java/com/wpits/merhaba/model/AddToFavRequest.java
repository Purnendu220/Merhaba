package com.wpits.merhaba.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class AddToFavRequest extends JSONObject {
    String subscriber_id;

    String top_content_id;

    public AddToFavRequest(String subscriber_id, String top_content_id) {
        this.subscriber_id = subscriber_id;
        this.top_content_id = top_content_id;
    }

    public String getSubscriber_id() {
        return subscriber_id;
    }

    public void setSubscriber_id(String subscriber_id) {
        this.subscriber_id = subscriber_id;
    }

    public String getTop_content_id() {
        return top_content_id;
    }

    public void setTop_content_id(String top_content_id) {
        this.top_content_id = top_content_id;
    }
}
