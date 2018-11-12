package com.abdur.asynctask;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class fetchBook extends AsyncTask<String,Void,String> {
    private WeakReference<TextView> mTitleText;
    private WeakReference<TextView> mAuthorText;
    private WeakReference<TextView> mPriceText;

    public fetchBook(TextView titleText, TextView authorText, TextView priceText) {
        this.mTitleText = new WeakReference<>(titleText);
        this.mAuthorText = new WeakReference<>(authorText);
        this.mPriceText = new WeakReference<>(priceText);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            int i = 0;
            String title = null;
            String authors = null;
            String price = null;
            String currency = null;
            while (i<itemsArray.length() && (authors == null && title == null)){
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");
                JSONObject saleInfo = book.getJSONObject("saleInfo");
                try {
                    title = volumeInfo.getString("title");
                    authors = volumeInfo.getString("authors");
                    JSONObject getPrice = saleInfo.getJSONObject("listPrice");
                    currency = getPrice.getString("currencyCode");
                    price = getPrice.getString("amount");
                    if (title != null && authors != null){
                        mTitleText.get().setText(title);
                        mAuthorText.get().setText(authors);
                        mPriceText.get().setText(currency+" "+price);
                    } else {
                        mTitleText.get().setText(R.string.no_results);
                        mAuthorText.get().setText("");
                        mPriceText.get().setText("");
                    }
                } catch (Exception e){
                    mTitleText.get().setText(R.string.no_results);
                    mAuthorText.get().setText("");
                    mPriceText.get().setText("");
                    e.printStackTrace();
                }
                i++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        return NetworkUtils.getBookInfo(strings[0]);
    }
}