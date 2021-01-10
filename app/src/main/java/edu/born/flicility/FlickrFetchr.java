package edu.born.flicility;

import android.net.Uri;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.reactivex.rxjava3.core.Observable;

// 134331dec6e8d89991a909472dd0d802
// 9ade8b70414e727c
public class FlickrFetchr {

    private static final String API_KEY = "134331dec6e8d89991a909472dd0d802";

    private static final String FETCH_RECENTS_METHOD = "flickr.photos.getRecent";
    private static final String SEARCH_METHOD = "flickr.photos.search";

    private static final Uri ENDPOINT = Uri
            .parse("https://www.flickr.com/services/rest/")
            .buildUpon()
            .appendQueryParameter("api_key", API_KEY)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .appendQueryParameter("extras", "url_s")
            .build();

    public static final String TAG = FlickrFetchr.class.getCanonicalName();

    public byte[] getBytesByUrl(String strUrl) throws IOException {
        URL url = new URL(strUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage());
            }

            int lenBytes = 0;
            byte[] buffer = new byte[1024];
            while ((lenBytes = in.read(buffer)) > 0) {
                out.write(buffer, 0, lenBytes);
            }
            in.close();
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getStringByUrl(String strUrl) throws IOException {
        return new String(getBytesByUrl(strUrl));
    }

    /*public FlickrResponse fetchItems(String url) {

        FlickrResponse response = null;

        try {
            String json = getStringByUrl(url);
            response = parseItems(json);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public FlickrResponse fetchRecentPhotos(int page) {
        return fetchItems(buildUrl(FETCH_RECENTS_METHOD, null, page));
    }

    public Observable<FlickrResponse> fetchRecentPhotosObs(int page) {
        return Observable.create(subscriber ->
                subscriber.onNext(fetchItems(buildUrl(FETCH_RECENTS_METHOD, null, page)))
        );
    }

    public Observable<FlickrResponse> searchPhotosObs(String query, int page) {
        return Observable.create(subscriber ->
                subscriber.onNext(fetchItems(buildUrl(SEARCH_METHOD, query, page)))
        );
    }

    public FlickrResponse searchPhotos(String query, int page) {
        return fetchItems(buildUrl(SEARCH_METHOD, query, page));
    }

    private String buildUrl(String method, String query, int page) {

        Uri.Builder uriBuilder = ENDPOINT.buildUpon()
                .appendQueryParameter("method", method)
                .appendQueryParameter("page", String.valueOf(page <= 0 ? 1 : page));

        if (method.equals(SEARCH_METHOD)) {
            uriBuilder.appendQueryParameter("text", query);
        }

        return uriBuilder.build().toString();
    }

    private FlickrResponse parseItems(String jsonBody) {

        Gson gson = new Gson().newBuilder()
                .registerTypeAdapter(FlickrResponse.PhotoWrapper.class, new PhotoWrapperDeserializer())
                .create();

        return gson.fromJson(jsonBody, FlickrResponse.class);
    }*/

}
