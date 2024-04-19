package com.example.shopdemo;

import android.util.Log;
import fi.iki.elonen.NanoHTTPD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MyNanoHTTPD extends NanoHTTPD {

    private static final String TAG = "MyNanoHTTPD";
    private DataReceivedListener dataReceivedListener;
    private Set<String> receivedDids = new HashSet<>(); // To store received did values

    public MyNanoHTTPD() {
        super(3000); // Listen on port 3000
    }

    public void setDataReceivedListener(DataReceivedListener listener) {
        this.dataReceivedListener = listener;
    }

    @Override
    public Response serve(IHTTPSession session) {
        String method = session.getMethod().toString();
        String uri = session.getUri();

        // Log incoming request details
        Log.d(TAG, "Method: " + method + ", URI: " + uri);

        // Read request body for POST requests
        if (Method.POST.equals(session.getMethod())) {
            try {
                // Parse incoming POST data as JSON
                Map<String, String> postData = session.getParms();
                session.parseBody(postData);
                String postBody = postData.get("postData"); // Assuming the JSON body is in a parameter named "postData"

                // Parse the JSON string to retrieve "did" parameter
                JSONObject json = new JSONObject(postBody);
                String did = json.getString("did");

                // Log the retrieved "did" value
                Log.d(TAG, "Value of 'did': " + did);

                // Check if the received 'did' is new (not already processed)
                if (!receivedDids.contains(did)) {
                    // Store the new 'did'
                    receivedDids.add(did);

                    // Notify the listener (MainActivity) that data is received
                    if (dataReceivedListener != null) {
                        dataReceivedListener.onDataReceived(did);
                    }
                } else {
                    // 'did' is a duplicate, ignore it
                    Log.d(TAG, "Duplicate 'did' received, ignoring...");
                }

            } catch (IOException | ResponseException | JSONException e) {
                Log.e(TAG, "Error reading or parsing POST data", e);
            }
        }

        // Return a simple response
        String response = "Server received " + method + " request at " + uri;
        return newFixedLengthResponse(response);
    }

    public void startServer() {
        try {
            start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
            Log.i(TAG, "NanoHTTPD server is running on port " + getListeningPort());
        } catch (IOException e) {
            Log.e(TAG, "Error starting NanoHTTPD server", e);
        }
    }

    public void stopServer() {
        stop();
        Log.i(TAG, "NanoHTTPD server stopped");
    }
}
