package com.example.news;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    private QueryUtils() {
    }

    public static List<NewsItem> fetchNewsData(String requestUrl) {

        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("QueryUtils", "Problem making the HTTP request.", e);
        }

        List<NewsItem> newsItemList = extractFeatureFromJson(jsonResponse);
        return newsItemList;
    }

    private static List<NewsItem> extractFeatureFromJson(String newsJSON) {

        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        List<NewsItem> newsItemList = new ArrayList<>();
        try {

            JSONObject rootObject = new JSONObject(newsJSON);
            JSONArray articlesList = rootObject.optJSONArray("articles");

            for (int i = 0; i < articlesList.length(); i++) {

                JSONObject newsArticle = articlesList.optJSONObject(i);

                String title = newsArticle.optString("title");
                String description = newsArticle.optString("description");
                String url = newsArticle.optString("url");
                String urlToImage = newsArticle.optString("urlToImage");

                NewsItem newsItem = new NewsItem(title, description, url, urlToImage);

                newsItemList.add(newsItem);
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return newsItemList;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("QueryUtils", "Problem building the URL ", e);
        }

        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {

                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);

            } else {
                Log.e("QueryUtils", "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e("QueryUtils", "Problem retrieving the news JSON results.", e);
        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }
}
