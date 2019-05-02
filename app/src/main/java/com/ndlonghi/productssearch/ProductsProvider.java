package com.ndlonghi.productssearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductsProvider {
    public static final String TAG = "ProductSearch";
    private static ProductsProvider instance;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private static Context ctx;
    private static final String URL = "https://api.mercadolibre.com/sites/MLA";

    private ProductsProvider(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();

        imageLoader = new ImageLoader(
                requestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                }
        );

    }

    public static synchronized ProductsProvider getInstance(Context context) {
        if (instance == null) {
            instance = new ProductsProvider(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public void search(
            String terms,
            final Response.Listener<List<Product>> listener,
            final Response.ErrorListener errorListener
    ) {
        this.getRequestQueue().cancelAll(TAG);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                URL + "/search?q=" + terms,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onResponse(parseJson(response));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("[API ERROR]: ", error.toString());
                        errorListener.onErrorResponse(error);
                    }
                });
        request.setTag(TAG);
        this.getRequestQueue().add(request);
    }

    public List<Product> parseJson(JSONObject jsonObject) {
        List<Product> products = new ArrayList();
        JSONArray jsonArray = null;

        try {
            jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {

                try {
                    JSONObject object = jsonArray.getJSONObject(i);
                    Product product = new Product(
                            object.getString("id"),
                            object.getString("title"),
                            object.getDouble("price"),
                            object.getString("thumbnail")
                    );
                    products.add(product);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return products;

    }

}
