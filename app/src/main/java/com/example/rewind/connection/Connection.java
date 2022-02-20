package com.example.rewind.connection;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.example.rewind.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Connection {
    private VLCParser parser;
    private final RequestQueue queue;
    private boolean connected;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final View view;

    public Connection(View view){
        queue = new RequestQueue(new DiskBasedCache(view.getContext().getCacheDir(), 2028 * 2028), new BasicNetwork(new HurlStack()));
        queue.start();
        parser = new VLCParser();
        connected=false;
        this.view=view;
    }

    public void start() {
        connected = true;
        executor.execute(()->{
            while(connected){
                String url ="http://192.168.1.7:8080/requests/status.xml";
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        response -> {
                            try {
                                VLCParser.StatusSnapshot snapshot = parser.parse(new ByteArrayInputStream(response.getBytes()));
                                ((TextView)view.findViewById(R.id.current_time_text)).setText(snapshot.videoCurrentTime);
                                ((TextView)view.findViewById(R.id.total_time_text)).setText(snapshot.videoTime);
                                ((TextView)view.findViewById(R.id.video_player_name)).setText(snapshot.videoName);
                                ((SeekBar)view.findViewById(R.id.video_seekbar)).setMax(Integer.parseInt(snapshot.totalSeconds));
                                ((SeekBar)view.findViewById(R.id.video_seekbar)).setProgress(Integer.parseInt(snapshot.currentSeconds));
                                Log.d("NEVERGONNAGIVEYOUUP", response +snapshot.videoName+ snapshot.position + " " + snapshot.volume +" "+ snapshot.videoTime +" "+ snapshot.videoCurrentTime);
                            } catch (XmlPullParserException | IOException e) {
                                e.printStackTrace();
                            }

                        }, error -> {
                    Log.d("NEVERGONNALETYOUDOWN", "cazzo");
                    Toast toast = Toast.makeText(view.getContext(), "Connection Failed, trying again...", Toast.LENGTH_SHORT);
                    toast.show();

                }){
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
        ((TextView)view.findViewById(R.id.current_time_text)).setText(R.string.undefined_time);
        ((TextView)view.findViewById(R.id.total_time_text)).setText(R.string.undefined_time);
        ((TextView)view.findViewById(R.id.video_player_name)).setText("---");
        ((SeekBar)view.findViewById(R.id.video_seekbar)).setProgress(0);

    }

    public void close(){
        queue.stop();
    }
}
