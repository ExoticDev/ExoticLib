package net.exoticdev.api.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class WebConnection {

    public static String getInputFromURL(URL url) {
        try {
            URLConnection connection = url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String input = reader.readLine();

            reader.close();

            return input;
        } catch (Exception e) {
            return "NULL";
        }
    }
}