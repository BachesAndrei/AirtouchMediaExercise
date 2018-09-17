package cs.pub.ro.airtouchexercise;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private TextView totalPriceTextView;

    ArrayList<Transaction> productTransactions;
    private static HashMap<String, HashMap<String, String>> currencyRates;

    private static RecyclerView transactionsRecyclerView;
    private static TransactionRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productTransactions = getIntent().getParcelableArrayListExtra("transactions");
        currencyRates = (HashMap<String, HashMap<String, String>> ) getIntent().getSerializableExtra("rates");

        transactionsRecyclerView = findViewById(R.id.transactionsRecyclerView);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        transactionsRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new TransactionRecyclerViewAdapter(productTransactions);
        transactionsRecyclerView.setAdapter(mAdapter);

        //new background thread in case the computation is too slow (too many transactions)
        Thread workingThread = new Thread(){
            public void run(){
                BigDecimal total = new BigDecimal(0);
                for (Transaction transaction: productTransactions) {
                    BigDecimal newSum;
                    if (transaction.getCurrency().equals("EUR")) {
                        newSum = new BigDecimal(transaction.getAmount());
                    } else {
                        BigDecimal amount = new BigDecimal(transaction.getAmount());
                        BigDecimal rate = new BigDecimal(currencyRates.get(transaction.getCurrency()).get("EUR"));
                        newSum = amount.multiply(rate).setScale(2, RoundingMode.HALF_EVEN);
                    }
                    total = total.add(newSum).setScale(2, RoundingMode.HALF_EVEN);
                }
                final BigDecimal finalTotal = total;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        totalPriceTextView.setText(String.valueOf(finalTotal.doubleValue()) + " EUR");
                    }
                });
            }
        };
        workingThread.start();
    }

}
