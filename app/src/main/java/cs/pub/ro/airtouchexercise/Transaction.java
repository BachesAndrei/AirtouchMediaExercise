package cs.pub.ro.airtouchexercise;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Currency;

/**
 * Created by andre on 2018-09-16.
 */

public class Transaction implements Parcelable {
//     "sku": "T2006", "amount": "10.00", "currency": "USD"
    private String productName;
    private String currency;
    private float  amount;

    public Transaction() {
        this.productName = "";
        this.currency = "";
        this.amount = 0;
    }

    public Transaction(String productName, float amount, String currency) {
        this.productName = productName;
        this.currency = currency;
        this.amount = amount;
    }

    public String getProductName() {
        return productName;
    }

    public String getCurrency() {
        return currency;
    }

    public float getAmount() {
        return amount;
    }


    protected Transaction(Parcel in) {
        productName = in.readString();
        currency = in.readString();
        amount = in.readFloat();
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productName);
        dest.writeString(currency);
        dest.writeFloat(amount);
    }
}
