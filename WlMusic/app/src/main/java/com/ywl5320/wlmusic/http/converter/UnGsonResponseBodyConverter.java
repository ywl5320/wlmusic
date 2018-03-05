package com.ywl5320.wlmusic.http.converter;

import com.google.gson.Gson;
import com.ywl5320.wlmusic.log.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by ywl on 2017/5/23.
 */

public class UnGsonResponseBodyConverter<T extends Object> implements Converter<ResponseBody, T> {

    private final Gson gson;
    private final Type type;

    public UnGsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody valuer) throws IOException {


        String json = valuer.string();
        String result;
        MyLog.d("response content: " + json);
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator<String> iterator = jsonObject.keys();
            String data = "";
            String message = "";
            String status = "";
            while(iterator.hasNext())
            {
                String key = iterator.next();
                if(!key.equals("message") && !key.equals("status"))
                {
                    String value = jsonObject.getString(key);
                    if(value.startsWith("[") || value.startsWith("{")) {
                        data = value;
                    }
                    else
                    {
                        data = value;
                    }
                }
                else if(key.equals("message"))
                {
                    message = "\"message\":\"" + jsonObject.getString(key) + "\"";
                }
                else if(key.equals("status"))
                {
                    status = "\"status\":" + jsonObject.getString(key);
                }
            }

            if(data.equals(""))
            {
                int dataType = -1;//0：字符串 1：整数 2：Boolean
                Type[] params = ((ParameterizedType) type).getActualTypeArguments();
                String dType=params[0].toString().replace("class ", "");
                if (dType.equals("java.lang.String")) {
                    dataType = 0;
                } else if (dType.equals("java.lang.Integer")) {
                    dataType = 1;
                } else if (dType.equals("java.lang.Boolean")) {
                    dataType = 2;
                } else if (dType.equals("java.lang.Double")) {
                    dataType = 3;
                } else if (dType.equals("java.lang.Float")) {
                    dataType = 4;
                } else {
                    dataType = -1;
                }
                if(dataType == -1)//需要的是对象
                {
                    data = "null";
                }
            }

            data = "\"data\":" + data + ",";
            result = "{" + data + message + "," + status + "}";
            MyLog.d( "result:" + result);

        } catch (JSONException e) {
            e.printStackTrace();
            result = "{\n" +
                    "    \"status\": 400,\n" +
                    "    \"message\": \"gson format wrong\"\n" +
                    "}";
        }
        T t = gson.fromJson(result, type);
        return t;
    }
}
