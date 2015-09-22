package com.btellez.solidandroid.network;

import android.net.Uri;
import android.support.v4.util.Pair;
import android.util.Base64;

import com.btellez.solidandroid.model.ApiKeys;
import com.btellez.solidandroid.utility.Strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Based on OAuth Implementation on TheNounProject Api Explorer:
 * http://api.thenounproject.com/lib/oauth-1.0a.js
 */
public class NounProjectOAuth {

    public enum RequestType {GET, POST}

    private static final String AMP = "&";
    private static final String EQ = "=";

    private ApiKeys keys;
    private String endpoint;
    private RequestType requestType;

    public NounProjectOAuth(ApiKeys keys) {
        this.keys = keys;
    }

    public NounProjectOAuth withEnpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public NounProjectOAuth withRequestType(RequestType requestType) {
        this.requestType = requestType;
        return this;
    }

    public String getOAuthHeader() {
        assertReady(endpoint, requestType);
        List<Pair<String, String>> oAuthHeaderList = new ArrayList<>();
        oAuthHeaderList.add(new Pair<>("oauth_signature_method", "HMAC-SHA1"));
        oAuthHeaderList.add(new Pair<>("oauth_version", "1.0"));
        oAuthHeaderList.add(new Pair<>("oauth_consumer_key", keys.getKey()));
        oAuthHeaderList.add(new Pair<>("oauth_nonce", getNounce(32)));
        oAuthHeaderList.add(new Pair<>("oauth_timestamp", String.valueOf(System.currentTimeMillis()/1000)));

        String signature = computeSignature(sortOAuthHeaderParams(oAuthHeaderList));
        oAuthHeaderList.add(new Pair<>("oauth_signature", encode(signature)));
        return "OAuth "+ getOAuthheaderString(sortOAuthHeaderParams(oAuthHeaderList));
    }

    private void assertReady(String endpoint, RequestType requestType) {
        if (Strings.isEmpty(endpoint) || requestType == null) {
            throw new IllegalArgumentException("Endpoint and Request type are both required fields.");
        }
    }

    private String getOAuthheaderString(List<Pair<String, String>> headerList) {
        StringBuffer sb = new StringBuffer();
        for (Pair<String, String> headers : headerList) {
            sb.append(String.format("%s=\"%s\", ", headers.first, headers.second));
        }
        return sb.subSequence(0, sb.length()-2).toString(); // trim extra space and comma.;
    }

    protected String computeSignature(List<Pair<String, String>> headerList) {
        String baseString = requestType.name().toUpperCase() + AMP;
        baseString += encode(endpoint) + AMP;
        baseString += encode(getParamString(headerList));
        return calculateHMACSHA1(getSignatureKey(), baseString);
    }

    private String getParamString(List<Pair<String, String>> headerList) {
        StringBuffer paramsString = new StringBuffer();
        for (Pair<String, String> set : headerList) {
            paramsString.append(encode(set.first)).append(EQ)
                        .append(encode(set.second)).append(AMP);
        }
        return paramsString.subSequence(0, paramsString.length() - 1).toString(); // trim extra ampersand
    }

    // Utility Methods

    private String getSignatureKey() {
        return encode(keys.getSecret()) + AMP;
    }

    private String getNounce(int length) {
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder result = new StringBuilder(32);

        for(int i = 0; i < length; i++) {
            result.append(alphabet.charAt((int) Math.floor(Math.random() * alphabet.length())));
        }
        return result.toString();
    }

    private String encode(String s) {
        return Uri.encode(s);
    }

    private List<Pair<String, String>> sortOAuthHeaderParams(List<Pair<String, String>> headerList) {
        Collections.sort(headerList, new Comparator<Pair<String, String>>() {
            @Override
            public int compare(Pair<String, String> lhs, Pair<String, String> rhs) {
                return lhs.first.compareTo(rhs.first);
            }
        });
        return headerList;
    }

    public String calculateHMACSHA1(String key, String data)
    {
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec secret = new SecretKeySpec(key.getBytes("UTF-8"), mac.getAlgorithm());
            mac.init(secret);
            byte[] digest = mac.doFinal(data.getBytes());
            return Base64.encodeToString(digest, Base64.NO_WRAP);
        } catch (Exception e) {
            return "";
        }
    }
}
