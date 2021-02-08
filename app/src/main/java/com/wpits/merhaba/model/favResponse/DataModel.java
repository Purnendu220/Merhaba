package com.wpits.merhaba.model.favResponse;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class DataModel implements Serializable {

	@SerializedName("topContentId")
	private TopContentIdModel topContentId;

	@SerializedName("subscriberId")
	private SubscriberIdModel subscriberId;

	public void setTopContentId(TopContentIdModel topContentId){
		this.topContentId = topContentId;
	}

	public TopContentIdModel getTopContentId(){
		return topContentId;
	}

	public void setSubscriberId(SubscriberIdModel subscriberId){
		this.subscriberId = subscriberId;
	}

	public SubscriberIdModel getSubscriberId(){
		return subscriberId;
	}
}