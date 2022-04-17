package org.me.gcu.rowan_lauren_s1820142;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;

import org.me.gcu.rowan_lauren_s1820142.Models.FeedItem;

import java.util.ArrayList;
import java.util.Map;

//Lauren Rowan S1820142

public class TabActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    //Sources
    String currentRoadworksSrc = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    String plannedRoadworksSrc = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    String currentIncidentsSrc = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";

    public ArrayList<FeedItem> currentRoadworksItems = new ArrayList<>();
    public ArrayList<FeedItem> plannedRoadworksItems = new ArrayList<>();
    public ArrayList<FeedItem> currentIncidentsItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new ListFragment(), "List");
        adapter.addFragment(new MapFragment(), "Map");
        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(1);

        //Load the roadworks from the xml
        new RSSReader(currentRoadworksSrc, new RSSParsingDone() {
            @Override
            public void rssParsingDone(ArrayList<FeedItem> result) {
                currentRoadworksItems = result;

                for(int i = 0; i < currentRoadworksItems.size(); i++){
                    Log.i("RSS Reader", currentRoadworksItems.get(i).toString());
                }
            }
        }, "CURRENT_ROADWORKS");

        //Load the planned roadworks from the xml
        new RSSReader(plannedRoadworksSrc, new RSSParsingDone() {
            @Override
            public void rssParsingDone(ArrayList<FeedItem> result) {
                plannedRoadworksItems = result;

                for(int i = 0; i < plannedRoadworksItems.size(); i++){
                    Log.i("RSS Reader", plannedRoadworksItems.get(i).toString());
                }
            }
        }, "PLANNED_ROADWORKS");

        //Load the current incidents from the xml
        new RSSReader(currentIncidentsSrc, new RSSParsingDone() {
            @Override
            public void rssParsingDone(ArrayList<FeedItem> result) {
                currentIncidentsItems = result;

                for(int i = 0; i < currentIncidentsItems.size(); i++){
                    Log.i("RSS Reader", currentIncidentsItems.get(i).toString());
                }
            }
        }, "INCIDENT");
    }
}