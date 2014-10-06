package nl.hoogland.stephan.android_510537;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import nl.hoogland.stephan.android_510537.network.MessagesInterface;
import nl.hoogland.stephan.android_510537.pojo.MessagePostDataReturn;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;


public class AddMessageActivity extends Activity implements View.OnClickListener {

    private EditText mTitle;
    private EditText mText;
    private Button mSend;
    private Intent intent;

    private ProgressBar mProgress;

    //network
    private MessagesInterface mMessagesInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_message);


        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        //build retrofit instance
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://wpinholland.azurewebsites.net/API")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .build();
        mMessagesInterface = restAdapter.create(MessagesInterface.class);

        intent = getIntent();

        mProgress = (ProgressBar) findViewById(R.id.addmessage_progress);
        mTitle = (EditText) findViewById(R.id.title);
        mText = (EditText) findViewById(R.id.text);
        mSend = (Button) findViewById(R.id.send);
        mSend.setEnabled(true);
        mSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //Check which view is clicked.
        if (v == mSend) {

            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                mProgress.setVisibility(View.VISIBLE);

                mMessagesInterface.postMessage(mText.getText().toString(), mTitle.getText().toString(), new Callback<MessagePostDataReturn>() {
                    @Override
                    public void success(MessagePostDataReturn responseID, Response response) {
                        mProgress.setVisibility(View.GONE);
                        if (getParent() == null) {
                            setResult(Activity.RESULT_OK, intent);
                        } else {
                            getParent().setResult(Activity.RESULT_OK, intent);
                        }
                        finish();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        mProgress.setVisibility(View.GONE);
                        if (getParent() == null) {
                            setResult(Activity.RESULT_OK, intent);
                        } else {
                            getParent().setResult(Activity.RESULT_OK, intent);
                        }
                        finish();
                    }
                });


            } else {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.error_activity_home)
                        .setMessage(R.string.no_connection).show();
            }
            //end with

        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.add_message, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
