package com.example.myapplication;

import android.util.Log;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VModel extends androidx.lifecycle.ViewModel {

        OkHttpClient client = new OkHttpClient();
        String url = "https://catfact.ninja/fact";
        private static final String TAG_JSON_EXAMPLE = "TAG_JSON_EXAMPLE";
        TextView textView;


        private MutableLiveData<String> currentName;
        private MutableLiveData<List<CatFact>> factsLiveData = new MutableLiveData<List<CatFact>>();
        CatFactApi api = initRetrofit();

        public VModel() {

            getFacts();
        }

        public MutableLiveData<String> getCurrentName() {
            if (currentName == null) {
                currentName = new MutableLiveData<String>();
            }
            return currentName;
        }

        public LiveData<List<CatFact>> getFactsLiveData() {
            return factsLiveData;
        }

        private CatFactApi initRetrofit() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://catfact.ninja/fact")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return retrofit.create(CatFactApi.class);
        }

        private void getFacts() {                                                          // Error with okhhttp Impossible to modify the signature of onResponse and onFailure
            api.getFacts().enqueue(new Callback<List<CatFact>>() {
                @Override
                public void onResponse(@NotNull Call<List<CatFact>> call, @NotNull Response<List<CatFact>> response) throws IOException {
                    List<CatFact> facts = response.body();
                    factsLiveData.setValue(facts);
                }

                @Override
                public void onFailure(@NotNull Call<List<CatFact>> call, @NotNull IOException e) {
                    factsLiveData.setValue(new ArrayList<CatFact>());
                }

            });


    }




    public void getFact(final OnFactGetListener listener) {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    factsLiveData.setValue(new ArrayList<CatFact>());
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



