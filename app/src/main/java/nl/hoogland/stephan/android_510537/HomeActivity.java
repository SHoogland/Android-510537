package nl.hoogland.stephan.android_510537;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.app.AlertDialog;
import android.view.View;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;
import android.net.ConnectivityManager;
import android.content.Context;
import android.net.NetworkInfo;

import nl.hoogland.stephan.android_510537.pojo.MessageListItem;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import nl.hoogland.stephan.android_510537.network.MessagesInterface;
import nl.hoogland.stephan.android_510537.adapters.HomeListAdapter;
import nl.hoogland.stephan.android_510537.pojo.MessageList;

public class HomeActivity extends Activity implements OnScrollListener,
        OnItemClickListener, OnItemLongClickListener {

    public final static String DETAILS_TITLE = "TITLE";
    public final static String DETAILS_TEXT = "TEXT";
    public final static String DETAILS_TIME = "TIME";
    public final static String DETAILS_IMAGE = "IMAGE";

    //network
    private MessagesInterface mMessagesInterface;

    private ListView mList;
    private HomeListAdapter mListAdapter;
    private ArrayList<MessageListItem> mContent;
    private boolean isGettingContent;
    private int lastId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //build retrofit instance
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://wpinholland.azurewebsites.net/API")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        mMessagesInterface = restAdapter.create(MessagesInterface.class);

        mList = (ListView) findViewById(R.id.exercise5_list);
        mContent = new ArrayList<MessageListItem>();
        lastId = 0;
        addContent();

    }

    private void startAdapter(){
        mListAdapter = new HomeListAdapter(this, mContent);
        mList.setAdapter(mListAdapter);
        mList.setOnScrollListener(this);
        mList.setOnItemClickListener(this);
        mList.setOnItemLongClickListener(this);
    }

    private void addContent() {
        isGettingContent= true;
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        final MessageList messageList = new MessageList();
        if (networkInfo != null && networkInfo.isConnected()) {
            //mProgress.setVisibility(View.VISIBLE);

            if(lastId == 0){
                mMessagesInterface.getMessages(new Callback<MessageList>() {
                    @Override
                    public void success(MessageList messages, Response response) {
                        //mProgress.setVisibility(View.GONE);
                        messageList.items.addAll(messages.items);
                        mContent.addAll(messageList.items);
                        if(mListAdapter == null){
                            startAdapter();
                        }
                        else {
                            mListAdapter.notifyDataSetChanged();
                            mList.setSelection(0);
                        }
                        isGettingContent =false;
                        lastId = mContent.get(mContent.size() - 1).ID;
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        new AlertDialog.Builder(HomeActivity.this)
                                .setTitle(R.string.title_activity_home)
                                .setMessage(R.string.error_activity_home).show();
                    }
                });
            } else {
                mMessagesInterface.getMessagesById(lastId, new Callback<MessageList>() {
                    @Override
                    public void success(MessageList messages, Response response) {
                        //mProgress.setVisibility(View.GONE);
                        messageList.items.addAll(messages.items);
                        mContent.addAll(messageList.items);
                        if(mListAdapter == null){
                            startAdapter();
                        }
                        else {
                            mListAdapter.notifyDataSetChanged();
                        }
                        isGettingContent =false;
                        lastId = mContent.get(mContent.size() - 1).ID;
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        new AlertDialog.Builder(HomeActivity.this)
                                .setTitle(R.string.title_activity_home)
                                .setMessage(R.string.error_activity_home).show();
                    }
                });
            }

        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.error_activity_home)
                    .setMessage(R.string.no_connection).show();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (!isGettingContent) {
            if (firstVisibleItem + visibleItemCount == totalItemCount) {
                addContent();
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {
        //create and start a new activity providing details.
        Intent i = new Intent(HomeActivity.this, DetailsActivity.class);
        i.putExtra(DETAILS_TITLE, mListAdapter.getItem(position).Title);
        i.putExtra(DETAILS_TEXT, mListAdapter.getItem(position).Text);
        i.putExtra(DETAILS_TIME, mListAdapter.getItem(position).Timestamp);
        i.putExtra(DETAILS_IMAGE, mListAdapter.getItem(position).ImageUrl);
        startActivity(i);
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                   int position, long arg3) {
        Toast.makeText(
                this,
                getString(R.string.longclick,
                        mListAdapter.getItem(position).Title),
                Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    static final int ADD_MESSAGE_REQUEST = 1;  // The request code
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_add:

                //create and start a new activity providing extras.
                Intent i = new Intent(HomeActivity.this, AddMessageActivity.class);
                startActivityForResult(i, ADD_MESSAGE_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ADD_MESSAGE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                mContent.clear();
                lastId = 0;
                addContent();
            }
        }
    }

}
