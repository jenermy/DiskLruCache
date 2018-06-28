package com.example.administrator.review;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

/**
 * Created by Administrator on 2018/6/28.
 */

public class XmlRequest extends Request<XmlPullParser> {
    private Response.Listener<XmlPullParser> mListener;
    public XmlRequest(int method, String url, Response.Listener listener, Response.ErrorListener errorListener){
        super(method,url,errorListener);
        mListener = listener;
    }
    public XmlRequest(String url, Response.Listener listener, Response.ErrorListener errorListener){
        this(Method.GET,url,listener,errorListener);
    }

    @Override
    protected void deliverResponse(XmlPullParser response) {
        if (mListener != null) {
            mListener.onResponse(response);
        }
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            String xmlString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlString));
            return Response.success(parser,HttpHeaderParser.parseCacheHeaders(response));
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }
}
