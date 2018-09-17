package cs.pub.ro.airtouchexercise;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by andre on 2018-09-16.
 */

public class ProductsRecyclerViewAdapter extends RecyclerView.Adapter<ProductsRecyclerViewAdapter.ProductViewHolder> {

    private static ArrayList<String> products;
    private static AdapterCallback mAdapterCallback;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView productNameTextView;

        public ProductViewHolder(View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                         mAdapterCallback.onAdapterCallback(productNameTextView.getText().toString());


                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ProductsRecyclerViewAdapter(Context context) {
        this.mAdapterCallback = (AdapterCallback) context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent,
                                                int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View productView = inflater.inflate(R.layout.item_product, parent, false);

        // Return a new holder instance
        ProductViewHolder viewHolder = new ProductViewHolder(productView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.productNameTextView.setText(products.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setProducts(ArrayList<String> products) {
        this.products = products;
    }


    public static interface AdapterCallback {
        void onAdapterCallback(String productName);
    }
}
