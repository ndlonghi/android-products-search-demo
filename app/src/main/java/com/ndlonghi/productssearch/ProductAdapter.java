package com.ndlonghi.productssearch;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.text.NumberFormat;
import java.util.List;

public class ProductAdapter extends ArrayAdapter {

    List<Product> products;

    public ProductAdapter(
            Context context
    ) {
        super(context, 0);
    }

    @Override
    public int getCount() {
        return products != null ? products.size() : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItemView;
        listItemView = null == convertView ? layoutInflater.inflate(R.layout.product_layout, parent, false) : convertView;
        final Product product = products.get(position);
        TextView textTitle = listItemView.findViewById(R.id.productTitle);
        TextView textPrice = listItemView.findViewById(R.id.productPrice);
        final ImageView imageThumbnail = listItemView.findViewById(R.id.productThumbnail);
        textTitle.setText(product.getTitle());
        textPrice.setText(NumberFormat.getCurrencyInstance().format(product.getPrice()));
        imageThumbnail.setImageBitmap(null);
        ProductsProvider.getInstance(getContext()).getImageLoader().get(
                product.getImage(),
                new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        Log.d("IMAGE OK: ", "");
                        imageThumbnail.setImageBitmap(response.getBitmap());
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("IMAGE ERROR: ", error.toString());
                        // TODO asignar imagen por defecto
                    }
                }
        );
        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productDetail = new Intent(getContext(), ProductDetail.class);
                productDetail.putExtra("product", product);
                getContext().startActivity(productDetail);
            }
        });
        return listItemView;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

}
