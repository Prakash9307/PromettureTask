package com.example.machinetest;

import android.app.Activity;
import android.app.ProgressDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DownloadData
{

    Activity activity;

    private OnDataDownloadComplete listener;

    public DownloadData(Activity activity, OnDataDownloadComplete listener)
    {
        this.activity = activity;
        this.listener = listener;
    }

    public void downloadDetails()
    {


        final ProgressDialog pd = new ProgressDialog(activity);
        pd.setMessage("Downloading details...");
        pd.show();

        ArrayList<Model> arrayList = new ArrayList<>();
        arrayList.clear();
        String url = "https://api.coindesk.com/v1/bpi/currentprice.json";

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest strReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject root = new JSONObject(response);
                            JSONObject time = root.getJSONObject("time");
                            JSONObject bpi = root.getJSONObject("bpi");
                            JSONObject usd = bpi.getJSONObject("USD");
                            JSONObject gbp = bpi.getJSONObject("GBP");
                            JSONObject eur = bpi.getJSONObject("EUR");
                            Model model = new Model();
                            model.setId("");
                            model.setUpdated(time.getString("updated"));
                            model.setUpdatedISO(time.getString("updatedISO"));
                            model.setUpdateduk(time.getString("updateduk"));
                            model.setUsd_code(usd.getString("code"));
                            model.setUsd_rate(usd.getString("rate"));
                            model.setUsd_rate_float(Float.parseFloat(usd.getString("rate_float")));
                            model.setGbp_code(gbp.getString("code"));
                            model.setGbp_rate(gbp.getString("rate"));
                            model.setGbp_rate_float(Float.parseFloat(gbp.getString("rate_float")));
                            model.setEur_code(eur.getString("code"));
                            model.setEur_rate(eur.getString("rate"));
                            model.setEur_rate_float(Float.parseFloat(eur.getString("rate_float")));

                            arrayList.add(model);

                            listener.downloadDownloadComplete(arrayList);
                            pd.dismiss();

                        }catch (Exception e)
                        {
                            listener.downloadDownloadFailed();
                            pd.dismiss();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        System.out.println("ERROR=>"+error);
                        listener.downloadDownloadFailed();
                        pd.dismiss();
                    }
                }
        );

        requestQueue.add(strReq);
    }



    public interface OnDataDownloadComplete
    {
        void downloadDownloadComplete(ArrayList<Model>arrayList);
        void downloadDownloadFailed();
    }

}
