package com.ndlonghi.productssearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView productsList;
    ProductAdapter adapter;
    TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productsList = findViewById(R.id.productsList);
        message = findViewById(R.id.activityMessage);
        adapter = new ProductAdapter(this);
        productsList.setAdapter(adapter);
        SearchView productsSearchView = findViewById(R.id.productsSearchView);
        productsSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return true;
            }
        });
    }

    public void search(String terms) {
        if(terms.length() >= 3) {
            setMessage("Buscando...");
            ProductsProvider.getInstance(this.getApplicationContext()).search(
                    terms,
                    new Response.Listener<List<Product>>() {
                        @Override
                        public void onResponse(List<Product> response) {
                            clearMessage();
                            adapter.setProducts(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("API", error.toString());
                            setMessage("Ha ocurrido un error. Intente nuevamente más tarde.");
                        }
                    });
        } else {
            adapter.setProducts(null);
            setMessage("Escribe 3 o más letras para buscar.");
        }

    }

    public void setMessage(String msg) {
        message.setText(msg);
        message.setVisibility(View.VISIBLE);
    }

    public void clearMessage() {
        message.setVisibility(View.GONE);
    }

}
