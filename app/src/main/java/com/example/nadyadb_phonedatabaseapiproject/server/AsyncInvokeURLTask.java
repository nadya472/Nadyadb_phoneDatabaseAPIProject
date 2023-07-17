package com.example.nadyadb_phonedatabaseapiproject.server;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.util.Pair;


public class AsyncInvokeURLTask extends AsyncTask<Void, Void, String>
{
    public String mNoteItWebUrl = "www.smartneasy.com";
    private ArrayList<Pair<String, String>> mParams;
    private OnPostExecuteListener mPostExecuteListener = null;
    private ProgressDialog dialog;
    public boolean showdialog =false;
    public String message ="Proses Data";
    //Harus diganti dengan IP Server yang digunakan
    public String url_server ="http://www.bibitbagus.id/xphone/";
    public Context applicationContext;
    public static interface OnPostExecuteListener{
        void onPostExecute(String result);
    }
    public AsyncInvokeURLTask(
            ArrayList<Pair<String, String>> nameValuePairs,
            OnPostExecuteListener postExecuteListener) throws Exception
    {
        mParams = nameValuePairs;
        mPostExecuteListener = postExecuteListener;
        if (mPostExecuteListener == null)
            throw new Exception("Param cannot be null.");
    }
    @Override
    public void onPreExecute() {
        if (showdialog)
            this.dialog =
                    ProgressDialog.show(applicationContext,message, "Silakan Menunggu...",
                            true);
    }
    @Override
    public String doInBackground(Void... params) {
        String result = "timeout";
        // Create a new HttpClient and Post Header
        OkHttpClient client = new OkHttpClient();
        // Membuat body permintaan POST
        RequestBody requestBody = new FormBody.Builder()
                .add("param1", "value1")
                .add("param2", "value2")
                .build();

        // Membuat objek Request dengan header khusus
        Request request = new Request.Builder()
                .url("http://example.com/api/endpoint")
                .post(requestBody)
                .addHeader("Authorization", "Bearer your_token_here")
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            // Melakukan permintaan HTTP
            Response response = client.newCall(request).execute();

            // Mendapatkan respons body
            ResponseBody responseBody = response.body();

            if (responseBody != null) {
                // Mendapatkan konten dari respons body
                String responseData = responseBody.string();

                // Lakukan sesuatu dengan responseData
                System.out.println(responseData);
            }

            // Jangan lupa menutup respons body setelah digunakan
            responseBody.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
    @Override
        public void onPostExecute(String result) {
            if (mPostExecuteListener != null){
                try {
                    //JSONObject json = new JSONObject(result);
                    if (showdialog)this.dialog.dismiss();
                    mPostExecuteListener.onPostExecute(result);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        private static String convertStreamToString(InputStream is){
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }
    }