package com.wpits.merhaba.model.favResponse;

import com.wpits.merhaba.model.album.Song;

import java.io.Serializable;

public class DataDTO implements Serializable {
	private Song topContentId;
	private SubscriberIdDTO subscriberId;

	public void setTopContentId(Song topContentId){
		this.topContentId = topContentId;
	}

	public Song getTopContentId(){
		return topContentId;
	}

	public void setSubscriberId(SubscriberIdDTO subscriberId){
		this.subscriberId = subscriberId;
	}

	public SubscriberIdDTO getSubscriberId(){
		return subscriberId;
	}

	@Override
 	public String toString(){
		return 
			"DataDTO{" + 
			"topContentId = '" + topContentId + '\'' + 
			",subscriberId = '" + subscriberId + '\'' + 
			"}";
		}
}