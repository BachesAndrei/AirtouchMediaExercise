package cs.pub.ro.airtouchexercise;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by andre on 2018-09-16.
 */

public class TransactionRecyclerViewAdapter  extends RecyclerView.Adapter<TransactionRecyclerViewAdapter.TransactionViewHolder>{

    ArrayList<Transaction> transactions;

    // Provide a suitable constructor (depends on the kind of dataset)
    public TransactionRecyclerViewAdapter(ArrayList<Transaction> productTransactions) {
        this.transactions = productTransactions;
    }


    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View productView = inflater.inflate(R.layout.item_transaction, parent, false);

        // Return a new holder instance
        TransactionViewHolder viewHolder = new TransactionViewHolder(productView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.amountTextView.setText(Float.toString(transaction.getAmount()));
        holder.currencyTextView.setText(transaction.getCurrency());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {

        public TextView currencyTextView;
        public TextView amountTextView;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            currencyTextView = itemView.findViewById(R.id.currencyTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
        }
    }
}
