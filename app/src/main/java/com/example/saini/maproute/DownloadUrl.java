package com.example.saini.maproute;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadUrl {
    public String readUrl(String myurl) throws IOException {
        String data= "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;

        try {
            URL url = new URL(myurl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();

            String line = "";
            while((line = bf.readLine())!=null) {
                sb.append(line);
            }
            data = sb.toString();
            bf.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        finally {
            inputStream.close();
            httpURLConnection.disconnect();
        }
        return  data;

    }

}
