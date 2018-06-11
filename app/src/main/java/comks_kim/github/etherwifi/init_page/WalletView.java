package comks_kim.github.etherwifi.init_page;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


import comks_kim.github.etherwifi.MainActivity;
import comks_kim.github.etherwifi.R;
import comks_kim.github.etherwifi.utils.Preference;

public class WalletView extends AppCompatActivity {
    TextView result_tv;
    Button complete_bt;
    Intent intent;
    private String pr_key;
    private String pu_key;
    private String pu_adrs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_view);
        Preference.getInstance().setContext(this);


        Preference.getInstance().putInt("WALLET_NUMBER", 0);

        complete_bt = findViewById(R.id.complete_button);
        result_tv = findViewById(R.id.result);

        complete_bt.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        intent = new Intent(WalletView.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );

        Thread thread = new Thread() {
            public void run() {
                String urlString = "https://api.blockcypher.com/v1/eth/main/addrs?token=c8cfb37c65444a4aae5696b6d3e9df08";

                try {

                    URL url = new URL(urlString);

                    trustAllHosts();

                    HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                    httpsURLConnection.setHostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String s, SSLSession sslSession) {
                            return true;
                        }
                    });


                    HttpURLConnection connection = httpsURLConnection;


                    connection.setRequestMethod("POST");

                    connection.setDoInput(true);

                    connection.setDoOutput(true);


                    List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(3);

                    nameValuePairs.add(new BasicNameValuePair("private", "private_key"));
                    nameValuePairs.add(new BasicNameValuePair("public", "public_key"));
                    nameValuePairs.add(new BasicNameValuePair("address", "public_address"));

                    OutputStream outputStream = connection.getOutputStream();

                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    bufferedWriter.write(getURLQuery(nameValuePairs));
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    connection.connect();


                    StringBuilder responseStringBuilder = new StringBuilder();

                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

                        for (; ; ) {

                            String stringLine = bufferedReader.readLine();

                            if (stringLine == null) break;

                            responseStringBuilder.append(stringLine + '\n');

                        }

                        bufferedReader.close();

                    }


                    connection.disconnect();


                    Log.d("eth11", responseStringBuilder.toString());


                } catch (MalformedURLException e) {

                    e.printStackTrace();

                } catch (IOException e) {

                    e.printStackTrace();

                }


            }
        };


        thread.start();

    }


    private static void trustAllHosts() {

        // Create a trust manager that does not validate certificate chains

        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {

                return new java.security.cert.X509Certificate[]{};

            }
            @Override
            public void checkClientTrusted(

                    java.security.cert.X509Certificate[] chain,

                    String authType)

                    throws java.security.cert.CertificateException {

                // TODO Auto-generated method stub

            }
            @Override

            public void checkServerTrusted(

                    java.security.cert.X509Certificate[] chain,

                    String authType)

                    throws java.security.cert.CertificateException {

                // TODO Auto-generated method stub
            }
        }};
        // Install the all-trusting trust manager

        try {

            SSLContext sc = SSLContext.getInstance("TLS");

            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        } catch (Exception e) {

            e.printStackTrace();

        }

    }


    private String getURLQuery(List<BasicNameValuePair> params) {

        StringBuilder stringBuilder = new StringBuilder();

        boolean first = true;


        for (BasicNameValuePair pair : params)

        {

            if (first) first = false;

            else stringBuilder.append("&");


            try {

                stringBuilder.append(URLEncoder.encode(pair.getName(), "UTF-8"));

                stringBuilder.append("=");

                stringBuilder.append(URLEncoder.encode(pair.getValue(), "UTF-8"));

            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();

            }

        }


        return stringBuilder.toString();
    }


}

