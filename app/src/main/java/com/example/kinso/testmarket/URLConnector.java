package com.example.kinso.testmarket;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by kinso on 2018-12-26.
 */

public class URLConnector extends Thread {
    private  String result;
    private  String URL;

    public URLConnector(String url){
        URL =url;
    }

    @Override
    public void run(){
        final String output = request(URL);
        result = output;
    }

    public String getResult(){
        return result;
    }

    private String request(String urlStr){
        StringBuilder output = new StringBuilder();
        /*try{
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            if(con !=null){
                con.setConnectTimeout(10000);
                con.setRequestMethod("GET");
                con.setDoInput(true);
                con.setDoOutput(true);

                int resCode = con.getResponseCode();
                if(resCode == HttpURLConnection.HTTP_OK){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String line = null;
                    while(true){
                        line = reader.readLine();
                        if(line ==null){
                            break;
                        }
                        output.append(line +"\n");
                    }
                    reader.close();
                    con.disconnect();
                }
            }

        }
        catch (Exception ex){
            Log.e("SampleHTTP", "Exception in processing response", ex);
            ex.printStackTrace();
        }*/

        return output.toString();
    }

}
