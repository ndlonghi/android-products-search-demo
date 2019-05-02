package com.ndlonghi.productssearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.NumberFormat;

public class ProductDetail extends AppCompatActivity {
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        product = (Product) getIntent().getExtras().getSerializable("product");
        TextView title = findViewById(R.id.productDetailTitle);
        title.setText(product.getTitle());
        TextView price = findViewById(R.id.productDetailPrice);
        price.setText(NumberFormat.getCurrencyInstance().format(product.getPrice()));
    }
}
