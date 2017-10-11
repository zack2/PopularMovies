package com.zack_olivier.zackpopularmoviesstage2.api;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.Map;


public class CustomStringRequest extends Request<String> {

    private final Response.Listener<String> response;
    private Map<String, String> params;

    public CustomStringRequest(String url, Response.Listener<String> response, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.response = response;
    }

    public CustomStringRequest(int method, String url, Map<String, String> params, Response.Listener<String> response,
                               Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.response = response;
        this.params = params;
    }

    @Override
    protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
        return params;
    }

    @Override
    protected void deliverResponse(String s) {
        response.onResponse(s);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(jsonString,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }
}
