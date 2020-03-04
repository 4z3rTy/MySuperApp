package com.example.myapplication;

import android.util.Log;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Vm extends ViewModel {

        OkHttpClient client = new OkHttpClient();
        String url = "https://catfact.ninja/fact";
        private static final String TAG_JSON_EXAMPLE = "TAG_JSON_EXAMPLE";
        TextView textView;


        // Create a LiveData with a String
        private MutableLiveData<String> currentName;

        public MutableLiveData<String> getCurrentName() {
            if (currentName == null) {
                currentName = new MutableLiveData<String>();
            }
            return currentName;
        }




    public void getFact(final OnFactGetListener listener) {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                public String parseJSONWithJSONObject(String jsonString)
                {
                    StringBuffer retBuf = new StringBuffer();

                    try {
                        JSONObject jsonObject = new JSONObject(jsonString);

                        for(int i = 0; i < jsonObject.length()-1; i++)
                        {

                            // Get each json filed value.
                            String fact = jsonObject.getString("fact");
                            String length = jsonObject.getString("length");

                            retBuf.append("\r\nfact = " + fact);
                            retBuf.append("\r\nlength = " + length);
                            retBuf.append("\r\n**************************");
                        }
                    }catch(JSONException ex)
                    {
                        Log.e(TAG_JSON_EXAMPLE, ex.getMessage(), ex);
                        retBuf.append(ex.getMessage());
                    }finally {
                        //p.setVisibility(View.INVISIBLE);
                        return retBuf.toString();
                    }
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws
                        IOException {
                    if (response.isSuccessful()) {
                        final String myResponse = response.body().string();
                        final String parsedString = parseJSONWithJSONObject(myResponse);
                        //List<String> facts=response.body().string();
                        //

                        listener.onFactGet(parsedString);
                    }
                }
            });




        }

    interface OnFactGetListener {
        public void onFactGet(String fact);
    }

}



