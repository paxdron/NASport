package com.paxdron.neurodynaussiesport;


import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.security.cert.Certificate;

import javax.net.ssl.*;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {
    /**
     * Called when the activity is first created.
     */

    private EditText mText;
    private Button mSend;
    private TextView mResponse;
    private EditText mIPaddress;
    private EditText mPort;

    // port to use
    private String ip_address;
    private int port = 9998;
    private SSLSocket socket = null;
    private BufferedWriter out = null;
    private BufferedReader in = null;
    private final String TAG = "TAG";
    private char keystorepass[] = "Kinnov2016".toCharArray();
    private char keypassword[] = "2016Kinnov".toCharArray();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mText = (EditText) findViewById(R.id.editext);
        mIPaddress = (EditText) findViewById(R.id.ip_address);
        mPort = (EditText) findViewById(R.id.port);
        mSend = (Button) findViewById(R.id.send_button);
        mResponse = (TextView) findViewById(R.id.server_response);

        mSend.setClickable(true);
        mSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mIPaddress.getText().toString().equals(null) || mPort.getText().toString().equals(null)) {
                    Toast.makeText(v.getContext(), "Please enter an IP address or Port number", Toast.LENGTH_LONG).show();
                } else {
                    String temp = mText.getText().toString();
                    if (temp == null) {
                        temp = "No text was entered";
                    }

                    Log.i(TAG, "makes it to here");

                    port = Integer.parseInt(mPort.getText().toString());
                    ip_address = mIPaddress.getText().toString();

                    try {
                        KeyStore trustStore = KeyStore.getInstance("BKS");
                        InputStream trustStoreStream = getApplicationContext().getResources().openRawResource(R.raw.kinnov_key);
                        trustStore.load(trustStoreStream, keystorepass);
                        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                        trustManagerFactory.init(trustStore);
                        SSLContext sslContext = SSLContext.getInstance("SSL");
                        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
                        SSLSocketFactory factory = sslContext.getSocketFactory();
                        socket = (SSLSocket) factory.createSocket();
                        socket.connect(new InetSocketAddress(ip_address, port), 9000);
                        socket.startHandshake();

                        //printServerCertificate(socket);
                        //printSocketInfo(socket);

                        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        //chat(temp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }

    /**
     * AsyncTask which handles the commiunication with clients
     */
    class ServerAsyncTask extends AsyncTask<SSLSocket, Void, String> {
        //Background task which serve for the client
        @Override
        protected String doInBackground(SSLSocket... params) {
            String result = null;
            //Get the accepted socket object
            SSLSocket mySocket = params[0];
            try {
                //Get the data input stream comming from the client
                InputStream is = mySocket.getInputStream();
                //Get the output stream to the client
                PrintWriter out = new PrintWriter(
                        mySocket.getOutputStream(), true);
                //Write data to the data output stream
                out.println("Hello from server");
                //Buffer the data input stream
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is));
                //Read the contents of the data buffer
                result = br.readLine();
                //Close the client connection
                mySocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

    }


}