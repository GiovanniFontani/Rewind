package com.example.rewind.connection;

import android.content.res.XmlResourceParser;
import android.util.Log;
import android.util.Xml;

import androidx.annotation.NonNull;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class VLCParser {
    private static final String ns = null;

    public VLCParser(){

    }

    public StatusSnapshot parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readStatusSnapshot(parser);
        } finally {
            in.close();
        }
    }

    public class StatusSnapshot {
        public final String videoTime;
        public final String videoCurrentTime;
        public final String position;
        public final String volume;
        public final String videoName;
        public final String totalSeconds;
        public final String currentSeconds;
        public final String rate;
        public final String state;
        public final boolean loop;

        private StatusSnapshot(String videoName,String position,
                               String videoTime, String videoCurrentTime,
                               String volume, String totalSeconds,
                               String currentSeconds, String rate,
                               String state, boolean loop) {
            this.videoTime = videoTime;
            this.videoCurrentTime = videoCurrentTime;
            this.position = position;
            this.volume = volume;
            this.videoName=videoName;
            this.totalSeconds=totalSeconds;
            this.currentSeconds=currentSeconds;
            this.rate=rate;
            this.state = state;
            this.loop=loop;
        }
    }


    private StatusSnapshot readStatusSnapshot(@NonNull XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "root");
        String videoCurrentTime = null;
        String videoTime = null;
        String position= null;
        String volume = null;
        String videoName = null;
        String currentSeconds = null;
        String totalSeconds = null;
        String rate = null;
        String state = null;
        boolean loop = false;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "time":
                    ArrayList<String> current = readVideoCurrentTime(parser);
                    videoCurrentTime =current.get(0);
                    currentSeconds = current.get(1);
                    break;
                case "length":
                    ArrayList<String> total = readVideoTime(parser);
                    videoTime = total.get(0);
                    totalSeconds =  total.get(1);
                    break;
                case "position":
                    position = readPosition(parser);
                    break;
                case "volume":
                    volume = readVolume(parser);
                    break;
                case "information":
                    videoName = readInformation(parser);
                    break;
                case "rate":
                    rate = readRate(parser);
                    break;
                case "state":
                    state = readState(parser);
                    break;
                case "loop":
                    loop = readLoop(parser);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        return new StatusSnapshot(videoName,position, videoTime,videoCurrentTime, volume,totalSeconds,currentSeconds, rate, state,loop);
    }

    private boolean readLoop(XmlPullParser parser)  throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "loop");
        boolean loop = Boolean.parseBoolean(readText(parser));
        parser.require(XmlPullParser.END_TAG, ns, "loop");
        return loop;
    }

    private String readState(XmlPullParser parser)  throws IOException, XmlPullParserException{
        parser.require(XmlPullParser.START_TAG, ns, "state");
        String state = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "state");
        return state;
    }

    private String readRate(XmlPullParser parser)  throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "rate");
        String rate = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "rate");
        return rate;
    }

    private String readInformation(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "information");
        String videoName = null;
        int event = parser.getEventType();
        boolean found = false;
        while (event != XmlPullParser.END_DOCUMENT && !found) {
            String tag = parser.getName();
            switch (event) {
                case XmlPullParser.START_TAG:
                    if (tag.equals("info")) {
                        String filename = parser.getAttributeValue(0);
                        if(parser.getAttributeValue(0).equals("filename")){
                            videoName = readText(parser);
                            found = true;
                        }
                    }
                    break;
                default:
                    break;
            }
            event = parser.next();
        }
        return videoName;
    }
    private ArrayList<String> readVideoTime(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "length");
        String totalSeconds = readText(parser);
        long length = Integer.parseInt(totalSeconds);
        long hours = (length - (length%3600))/3600;
        long remaining = length%3600;
        long minutes = (remaining - (remaining%60))/60;
        long seconds = remaining%60;
        parser.require(XmlPullParser.END_TAG, ns, "length");
        ArrayList<String> result = new ArrayList<>();
        String secs = seconds < 10? "0" + Long.toString(seconds) : Long.toString(seconds) ;
        String mins = minutes < 10? "0" + Long.toString(minutes):Long.toString(minutes);
        String hrs = hours < 10? "0" + Long.toString(hours):Long.toString(hours);
        result.add(hrs+":"+ mins+":"+ secs);
        result.add(totalSeconds);
        return result;
    }

    private ArrayList<String> readVideoCurrentTime(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "time");
        String currentSeconds = readText(parser);
        long time = Integer.parseInt(currentSeconds);
        long hours = (time - (time%3600))/3600;
        long remaining = time%3600;
        long minutes = (remaining - (remaining%60))/60;
        long seconds = remaining%60;
        parser.require(XmlPullParser.END_TAG, ns, "time");
        ArrayList<String> result = new ArrayList<>();
        String secs = seconds < 10? "0" + Long.toString(seconds) : Long.toString(seconds) ;
        String mins = minutes < 10? "0" + Long.toString(minutes):Long.toString(minutes);
        String hrs = hours < 10? "0" + Long.toString(hours):Long.toString(hours);
        result.add(hrs+":"+ mins+":"+ secs);
        result.add(currentSeconds);
        return result;
    }

    private String readPosition(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "position");
        String position = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "position");
        return position;
    }

    private String readVolume(XmlPullParser parser) throws IOException, XmlPullParserException{
        parser.require(XmlPullParser.START_TAG, ns, "volume");
        String volume = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "volume");
        return volume;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
