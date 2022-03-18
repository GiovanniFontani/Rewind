package com.example.rewind.connection;

import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
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
import com.example.rewind.button.ConnectionStatusButton;

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
    private final ExecutorService executor = Executors.newFixedThreadPool(8);
    private final View view;
    private double rate = 1;
    private int volume = 0;
    private String state = "undefined";
    private String ip ="";

    public Connection(View view){
        queue = new RequestQueue(new DiskBasedCache(view.getContext().getCacheDir(), 2028 * 2028), new BasicNetwork(new HurlStack()));
        queue.start();
        parser = new VLCParser();
        connected=false;
        this.view=view;
    }

    public boolean isConnected(){
        return connected;
    }

    public boolean isPlaying(){
        return state.equals("playing");
    }

    public void start(String ip, ConnectionStatusButton connectionStatusButton) {

        boolean loopsign = false;
        this.ip=ip;
        executor.execute(()->{
            do{
                String url ="http://"+ip+":8080/requests/status.xml";
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        response -> {
                            try {
                                VLCParser.StatusSnapshot snapshot = parser.parse(new ByteArrayInputStream(response.getBytes()));
                                ((TextView)view.findViewById(R.id.current_time_text)).setText(snapshot.videoCurrentTime);
                                ((TextView)view.findViewById(R.id.total_time_text)).setText(snapshot.videoTime);
                                ((TextView)view.findViewById(R.id.video_player_name)).setText(snapshot.videoName);
                                ((SeekBar)view.findViewById(R.id.video_seekbar)).setMax(Integer.parseInt(snapshot.totalSeconds));
                                ((SeekBar)view.findViewById(R.id.video_seekbar)).setProgress(Integer.parseInt(snapshot.currentSeconds));
                                rate = Double.parseDouble(snapshot.rate);
                                volume = Integer.parseInt(snapshot.volume);
                                state = snapshot.state;
                                ImageButton playButton =  view.findViewById(R.id.play_button);
                                if(state.equals("playing")){
                                    playButton.setActivated(true);
                                    playButton.setBackgroundResource(R.drawable.activated_rounded_button);
                                }else{
                                    playButton.setActivated(false);
                                    playButton.setBackgroundResource(R.drawable.rounded_button);
                                }
                                int newVolumePerc = Math.round(volume*100/256);
                                ((TextView)view.findViewById(R.id.volume_percentage_text_view)).setText(Integer.toString(newVolumePerc) + "%");
                                ((SeekBar)view.findViewById(R.id.volume_seekbar)).setProgress(volume);
                                ImageButton imageVolume =  view.findViewById(R.id.volume_image_button);
                                connected = true;
                                if(!snapshot.loop){
                                    String loopSignal = "http://"+ip+":8080/requests/status.xml?command=pl_loop";
                                    StringRequest loopSignalRequest = new StringRequest(Request.Method.GET, loopSignal,response1->{},error ->{}){
                                        @Override
                                        public Map<String, String> getHeaders() throws AuthFailureError {
                                            HashMap<String, String> params = new HashMap<String, String>();
                                            String creds = String.format("%s:%s","","nevergonnagiveyouup");
                                            String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                                            params.put("Authorization", auth);
                                            return params;
                                        }
                                    };
                                    queue.add(loopSignalRequest);
                                }
                                connectionStatusButton.buttonConnected();
                                view.findViewById(R.id.disconnect_button).setVisibility(View.VISIBLE);
                              } catch (XmlPullParserException | IOException e) {
                                e.printStackTrace();
                            }

                        }, error -> {
                    Toast toast = Toast.makeText(view.getContext(), "Connection Failed, trying again...", Toast.LENGTH_SHORT);
                    toast.show();
                    view.findViewById(R.id.disconnect_button).setVisibility(View.VISIBLE);
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
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while (connected);
        });
    }

    public void play_pause(){
        if(!connected){
            return;
        }
        executor.execute(() ->{
            String url ="http://"+ip+":8080/requests/status.xml?command=pl_pause";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    response -> {

                    }, error -> {
                Toast toast = Toast.makeText(view.getContext(), "Error.", Toast.LENGTH_SHORT);
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
        });
    }

    public void moveTo(String seconds){
        if(!connected){
            return;
        }
        executor.execute(() ->{
            String url ="http://"+ip+":8080/requests/status.xml?command=seek&val=" + seconds;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    response -> {

                    }, error -> {
                Toast toast = Toast.makeText(view.getContext(), "Error.", Toast.LENGTH_SHORT);
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
        });
    }

    public void setVolume(int volume){
        if(!connected){
            return;
        }
        executor.execute(() ->{
            String url ="http://"+ip+":8080/requests/status.xml?command=volume&val=" + volume;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    response -> {

                    }, error -> {
                Toast toast = Toast.makeText(view.getContext(), "Error.", Toast.LENGTH_SHORT);
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
        });
    }

    public void setSpeed(double speed){
        if(!connected){
            return;
        }
        executor.execute(() ->{
            String url ="http://"+ip+":8080/requests/status.xml?command=rate&val=" + (rate+speed);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    response -> {

                    }, error -> {
                Toast toast = Toast.makeText(view.getContext(), "Error.", Toast.LENGTH_SHORT);
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
