package cs.pub.ro.airtouchexercise;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by andre on 2018-09-16.
 */

public class WebServerCommunications {

    static void doGetTransactionsRequest(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        // send error to main thread
                        MainActivity.httpRequestsFailed(Constants.TRANSACTIONS_REQUEST_FAILURE);
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        String res = response.body().string();
                        try {
                            JSONArray jsonArray = new JSONArray(res);
                            ArrayList<String> products = new ArrayList<>();
                            HashMap<String, ArrayList<Transaction>> productTransactions = new HashMap<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Transaction transaction = new Transaction(jsonObject.getString("sku"),
                                        Float.valueOf(jsonObject.getString("amount")),
                                        jsonObject.getString("currency"));

                                String productName = transaction.getProductName();
                                ArrayList<Transaction> transactionsForProduct = productTransactions.get(productName);
                                if (transactionsForProduct == null) {
                                    products.add(transaction.getProductName());
                                    transactionsForProduct = new ArrayList<>();
                                    transactionsForProduct.add(transaction);
                                    productTransactions.put(productName, transactionsForProduct);
                                } else {
                                    transactionsForProduct.add(transaction);
                                }


                            }
                            MainActivity.setTransactionsData(products, productTransactions);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    static void doGetRatesRequest(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        // send error to main thread
                        MainActivity.httpRequestsFailed(Constants.RATES_REQUEST_FAILURE);
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        String res = response.body().string();
                        try {
                            JSONArray jsonArray = new JSONArray(res);
                            HashMap<String, HashMap<String, String>> rates = new HashMap<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);


                                String firstCurrency = jsonObject.getString("from");
                                String secondCurrency = jsonObject.getString("to");
                                String rate = jsonObject.getString("rate");

                                HashMap<String, String> firstCurrencyRates = rates.get(firstCurrency);

                                if (firstCurrencyRates == null) {
                                    firstCurrencyRates = new HashMap<>();
                                }

                                firstCurrencyRates.put(secondCurrency, rate);
                                rates.put(firstCurrency, firstCurrencyRates);

                            }
                            MainActivity.setRatesData(rates);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
