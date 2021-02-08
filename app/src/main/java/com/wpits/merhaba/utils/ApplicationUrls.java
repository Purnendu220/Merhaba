package com.wpits.merhaba.utils;

public class ApplicationUrls {

    public static final String category="http://182.74.113.60:8090/contents/category.json";
    public static final String otp="https://www.marhaba.com.ly:18084/crbt/api/subscriber/ivr/sendOtp/";
    public static final String validateOtp="https://www.marhaba.com.ly:18084/crbt/api/subscriber/ivr/verifyOtp/";
    /*
    this.http.get('https://www.marhaba.com.ly:18084/crbt/api/subscriber/ivr/verifyOtp/'+ this.userInputMobileNumber+"/"+otp , { headers:otp_header } )
     */
    public static final String subscribe = "https://www.marhaba.com.ly:18084/crbt/api/subscriber/ivr/subscription/";
    public static final String gift = "https://www.marhaba.com.ly/gift.jsp";

}
