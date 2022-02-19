package com.example.rewind.connection;

import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Connection {
    private VLCParser parser;
    private final RequestQueue queue;
    private boolean connected;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public Connection(View view){
        queue = new RequestQueue(new DiskBasedCache(view.getContext().getCacheDir(), 2028 * 2028), new BasicNetwork(new HurlStack()));
        queue.start();
        connected=false;
    }

    public void start(){
        connected = true;
        ExecutorService executor = Executors.newFixedThreadPool(4);
        executor.execute(()->{
            while(connected){
                String url ="http://192.168.1.5:8080/requests/status.xml?command=pl_pause";
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        response -> {
                            Log.d("NEVERGONNAGIVEYOUUP", response); //PREVISIONE: C'è da tradurre l'xml
                        }, error -> Log.d("NEVERGONNALETYOUDOWN" ,"cazzo")){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<String, String>();
                        String creds = String.format("%s:%s","","nevergonnagiveyouup");
                        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                        params.put("Authorization", auth);
                        return params;
                    }
                };
                queue.add(stringRequest);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void stop(){
        connected = false;
    }

    public void close(){
        queue.stop();
    }
}
