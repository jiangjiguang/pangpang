package com.pangpang.util.webservice;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by jiangjg on 2016/9/20.
 */
public class HttpClientUtils {
    public static HttpClientResult execPostMethod(String url, List<NameValuePair> params) {
        try {
            // HttpClient 4.3 Fluent API
            StatusLine status = Request.Post(url).bodyForm(params, Charset.forName("UTF-8"))
                    .connectTimeout(5000)
                    .socketTimeout(10000)
                    .execute().returnResponse().getStatusLine();

            //
            if (status.getStatusCode() != 200) {
                return new HttpClientResult(status.getStatusCode(), status.getReasonPhrase());
            } else {
                return new HttpClientResult(200, "SUCCESS");
            }
        } catch (Exception ex) {
            return new HttpClientResult(503, ex.toString());
        }
    }

    public static HttpClientResult execute(String url, String content){
        try {
            HttpEntity httpEntity = new StringEntity(content, ContentType.create("application/json", "UTF-8"));
            Response response = Request.Post(url).body(httpEntity).execute();
            HttpResponse httpResponse = response.returnResponse();
            StatusLine status = httpResponse.getStatusLine();
            HttpEntity httpEntityResponse = httpResponse.getEntity();

            if (status.getStatusCode() != 200) {
                return new HttpClientResult(status.getStatusCode(), status.getReasonPhrase());
            } else {
                return new HttpClientResult(200, "SUCCESS", EntityUtils.toString(httpEntityResponse, Charset.forName("UTF-8")));
            }
        }catch (Exception ex){
            return new HttpClientResult(503, ExceptionUtils.getFullStackTrace(ex));
        }
    }
}
