package com.example.rewind.connection;

import android.util.Xml;

import androidx.annotation.NonNull;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

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

        private StatusSnapshot(String position, String videoTime, String videoCurrentTime, String volume) {
            this.videoTime = videoTime;
            this.videoCurrentTime = videoCurrentTime;
            this.position = position;
            this.volume = volume;
        }
    }


    private StatusSnapshot readStatusSnapshot(@NonNull XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "root");
        String videoCurrentTime = null;
        String videoTime = null;
        String position= null;
        String volume = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "time":
                    videoCurrentTime = readVideoCurrentTime(parser);
                    break;
                case "length":
                    videoTime = readVideoTime(parser);
                    break;
                case "position":
                    position = readPosition(parser);
                    break;
                case "volume":
                    volume = readVolume(parser);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        return new StatusSnapshot(position, videoTime,videoCurrentTime, volume);
    }

    private String readVideoTime(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "length");
        long length = Integer.parseInt(readText(parser));
        long hours = (length - (length%3600))/3600;
        long remaining = length%3600;
        long minutes = (remaining - (remaining%60))/60;
        long seconds = remaining%60;
        parser.require(XmlPullParser.END_TAG, ns, "length");
        return Long.toString(hours) +":"+ Long.toString(minutes) +":"+ Long.toString(seconds);
    }

    private String readVideoCurrentTime(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "time");
        long time = Integer.parseInt(readText(parser));
        long hours = (time - (time%3600))/3600;
        long remaining = time%3600;
        long minutes = (remaining - (remaining%60))/60;
        long seconds = remaining%60;
        parser.require(XmlPullParser.END_TAG, ns, "time");
        return Long.toString(hours) +":"+ Long.toString(minutes) +":"+ Long.toString(seconds);
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
