package org.me.gcu.rowan_lauren_s1820142;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//Lauren Rowan S1820142

public class InspectFeedItem extends AppCompatActivity {


    TextView title;
    TextView description;
    TextView startDate;
    TextView endDate;
    TextView latLng;
    TextView pubDate;

    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_feed_item);

        title = (TextView) findViewById(R.id.title);
        description = (TextView) findViewById(R.id.description);
        startDate = (TextView) findViewById(R.id.startDate);
        endDate = (TextView) findViewById(R.id.endDate);
        latLng = (TextView) findViewById(R.id.latLng);
        pubDate = (TextView) findViewById(R.id.pubDate);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle receivedBundle = getIntent().getExtras() != null ? getIntent().getExtras() : null;
        if(receivedBundle != null){
            title.setText(receivedBundle.get("title").toString());
            description.setText(receivedBundle.get("description").toString());
            startDate.setText(receivedBundle.get("startDate").toString());
            endDate.setText(receivedBundle.get("endDate").toString());
            latLng.setText(receivedBundle.get("latLng").toString());
            pubDate.setText(receivedBundle.get("pubDate").toString());

        }

    }
}