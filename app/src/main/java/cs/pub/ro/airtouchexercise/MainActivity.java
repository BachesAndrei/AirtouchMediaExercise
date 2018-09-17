package cs.pub.ro.airtouchexercise;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements ProductsRecyclerViewAdapter.AdapterCallback{


    private static RecyclerView productsRecyclerView;
    private static ProductsRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static ProgressBar progressBar;

    //stores all transactions related to a given product
    private static HashMap<String, ArrayList<Transaction>> productTransactions;
    //stores all known conversion rates for each currency
    private static HashMap<String, HashMap<String, String>> currencyRates;

    private static Context ctx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productsRecyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        productsRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        productsRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ProductsRecyclerViewAdapter(this);
        productsRecyclerView.setAdapter(mAdapter);

        try {
            //Starting asynchronous tasks to download and process the remote data
            WebServerCommunications.doGetTransactionsRequest(Constants.TRANSACTIONS_HTTP_ADDRESS);
            WebServerCommunications.doGetRatesRequest(Constants.RATES_HTTP_ADDRESS);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ctx = getApplicationContext();
    }




    public static void setTransactionsData(final ArrayList<String> products,
                                           final HashMap<String, ArrayList<Transaction>> productTransactions) {
        //Handler used to set data and objects on the main thread
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                mAdapter.setProducts(products);
                mAdapter.notifyDataSetChanged();
                productsRecyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                MainActivity.productTransactions = productTransactions;
            }
        });
    }

    //Determines conversion rates between all currencies using a breadth search algorithm
    //For every currency finds new "links" to other currencies and stores the computed rate
    public static void setRatesData(HashMap<String, HashMap<String, String>> rates) {
        Set<String> keys = rates.keySet();
        for(String key: keys){
            if (!rates.get(key).containsKey("EUR")) {
                Stack<Pair<String, String>> stack = new Stack<>();
                Set<String> newKeys = rates.get(key).keySet();
                for(String newKey: newKeys){
                    stack.push(new Pair(newKey, rates.get(key).get(newKey)));
                }

                while (!stack.empty()) {
                    Pair<String, String> popedPair = stack.pop();

                    newKeys = rates.get(popedPair.first).keySet();
                    for(String newKey: newKeys){
                        if (!rates.get(key).containsKey(newKey) && !newKey.equals(key)) {
                            BigDecimal firstValue = new BigDecimal(popedPair.second);
                            BigDecimal secondValue = new BigDecimal(rates.get(popedPair.first).get(newKey));
                            BigDecimal newValue = firstValue.multiply(secondValue).setScale(2, RoundingMode.HALF_EVEN);

                            stack.push(new Pair(newKey, newValue.toString()));
                            rates.get(key).put(newKey, newValue.toString());
                        }
                    }
                }
            }
        }

        final HashMap<String, HashMap<String, String>> finalRates = rates;

        //Handler used to pass data from background thread to main thread
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                MainActivity.currencyRates = finalRates;
            }
        });
    }


    public static void httpRequestsFailed(final String errorMessage) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.ctx, errorMessage,
                        Toast.LENGTH_LONG).show();
            }
        });
    }



    @Override
    public void onAdapterCallback(String productName) {
        final Intent intent = new Intent(MainActivity.this, ProductDetailsActivity.class);
        intent.putParcelableArrayListExtra("transactions", productTransactions.get(productName));
        intent.putExtra( "rates", currencyRates);
        startActivity(intent);

    }


}
