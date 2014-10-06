package nl.hoogland.stephan.android_510537;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import nl.hoogland.stephan.android_510537.pojo.MessageListItem;


public class DetailsActivity extends Activity {

    private TextView mTitle;
    private TextView mText;
    private TextView mTime;
    private ImageView mImage;

    private ShareActionProvider mShareActionProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mTitle = (TextView) findViewById(R.id.details_title);
        mText = (TextView) findViewById(R.id.details_text);
        mTime = (TextView) findViewById(R.id.details_time);
        mImage = (ImageView) findViewById(R.id.imageView);

        //get the extras
        Intent intent = getIntent();
        String title = intent.getStringExtra(HomeActivity.DETAILS_TITLE);
        String text = intent.getStringExtra(HomeActivity.DETAILS_TEXT);
        long timeLong = intent.getLongExtra(HomeActivity.DETAILS_TIME, 0);
        String image = intent.getStringExtra(HomeActivity.DETAILS_IMAGE);

        mTitle.setText(title);
        mText.setText(text);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String time = sdf.format(new Date(timeLong*1000));
        mTime.setText(time);

        if(image != null) {
            Picasso.with(getApplicationContext()).load(image).into((android.widget.ImageView) mImage);
        } else {
            Picasso.with(getApplicationContext()).load("http://www.financiereguizot.com/wp-content/themes/twentyeleven/images/img-not-found_600_600.jpg").into((android.widget.ImageView) mImage);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details, menu);

        // Set up ShareActionProvider's default share intent
        mShareActionProvider = (ShareActionProvider) menu.findItem(R.id.action_share).getActionProvider();
        mShareActionProvider.setShareIntent(getDefaultIntent());

        return true;
    }

    private Intent getDefaultIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, mTitle.getText());
        intent.putExtra(Intent.EXTRA_TEXT, mText.getText());
        return intent;
    }

}
