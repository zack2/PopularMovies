package com.zack_olivier.zackpopularmoviesstage2.api;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;


public class CustomJSONRequest extends Request<JSONObject> {

    private final Response.Listener<JSONObject> listener;
    private Map<String, String> params;

    public CustomJSONRequest(int method, String uri, Map<String, String> params,
                             Response.Listener<JSONObject> reponseListener, Response.ErrorListener listener) {
        super(method, uri, listener);
        this.listener = reponseListener;
        this.params = params;
    }

    public CustomJSONRequest(String uri, Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener) {
        super(Method.GET, uri, errorListener);
        this.listener = reponseListener;
    }

    @Override
    protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
        return params;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String jsonString = new String(networkResponse.data,
                    HttpHeaderParser.parseCharset(networkResponse.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        listener.onResponse(response);
    }
}
