package com.example.inventoryapp;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UpdateTimeTask extends AsyncTask<Void, Void, String> {
    private View mView;
    private String DateTimeFormat;
    private Handler mHandler;
    private Runnable mRunnable;

    public UpdateTimeTask(View view, Handler handler,String dateTimeFormat) {
        mView = view;
        DateTimeFormat = dateTimeFormat == null ? "yyyy-MM-dd HH:mm:ss" : dateTimeFormat;//Defining optional arguments
        if(handler!=null)
        {
            mHandler = handler;
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    //mHandler.removeCallbacks(this);
                    //UpdateTimeTask.this.execute();

                    new UpdateTimeTask(mView, mHandler, DateTimeFormat).execute();
                    mHandler.postDelayed(this, 1000);
                }
            };
        }

    }

    @Override
    protected String doInBackground(Void... voids) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateTimeFormat, Locale.getDefault());
        return dateFormat.format(new Date());
    }

    @Override
    protected void onPostExecute(String result) {
        if (mView instanceof TextView) {
            ((TextView) mView).setText(result);
        } else if (mView instanceof EditText) {
            ((EditText) mView).setText(result);
        } else if (mView instanceof Button) {
            ((Button) mView).setText(result);
        }
    //mHandler.postDelayed(mRunnable, 1000);
        if(mHandler!=null) {
            mHandler.post(mRunnable);
        }
    }
}
