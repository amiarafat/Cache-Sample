package com.arafat.cachesample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvUserList;

    private LinearLayoutManager linearLayoutManager;
    private List<Users> userList;
    private RecyclerView.Adapter adapter;

    private String url ="http://dummy.restapiexample.com/api/v1/employees";

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intView();

        //getData();
        getDataWithCache();
    }

    private void getDataWithCache(){

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        RequestQueue queue =  Volley.newRequestQueue(getApplicationContext());
        Cache cache =  queue.getCache();
        Cache.Entry entry = cache.get(url);
        if(entry != null){

            try {

                String data = new String(entry.data ,"UTF-8");
                parseDeliveryList(data);

            }catch (Exception e){
                e.printStackTrace();
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        }else {
            getData();
        }

    }

    private void parseDeliveryList(String data) {

        try{

            JSONArray jArray =  new JSONArray(data);

            for (int i = 0 ; i< jArray.length(); i++){

                JSONObject jObj =  jArray.getJSONObject(i);

                Users users =  new Users();
                users.setUserName(jObj.getString("employee_name"));
                users.setUserEmail(jObj.getString("employee_age"));
                users.setUserPhone(jObj.getString("employee_salary"));

                userList.add(users);
            }

        }catch (Exception e){

            e.printStackTrace();
        }


        adapter.notifyDataSetChanged();
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }


    private void getData() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    Log.d("res:",response.get(1).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        Users users =  new Users();
                        users.setUserName(jsonObject.getString("employee_name"));
                        users.setUserEmail(jsonObject.getString("employee_age"));
                        users.setUserPhone(jsonObject.getString("employee_salary"));

                        userList.add(users);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }
                }



                adapter.notifyDataSetChanged();
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Volley", error.toString());
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        }){/*
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                try {

                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }

                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;

                    headerValue = response.headers.get("Date");

                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONArray(jsonString), cacheEntry);

                }catch (Exception e){

                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(JSONArray response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }*/
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);



    }

    private void intView() {
        rvUserList = findViewById(R.id.mRecyclerView);

        userList = new ArrayList<>();
        adapter = new UserAdapter(getApplicationContext(),userList);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvUserList.setHasFixedSize(true);
        rvUserList.setLayoutManager(linearLayoutManager);
        rvUserList.setAdapter(adapter);
    }
}
