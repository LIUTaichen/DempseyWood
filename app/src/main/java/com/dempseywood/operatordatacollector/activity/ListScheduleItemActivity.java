package com.dempseywood.operatordatacollector.activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.dempseywood.operatordatacollector.R;


/**
 * Created by musing on 25/07/2017.
 */

public class ListScheduleItemActivity extends ListActivity {

    static final String[] scheduleItems = new String[] { "2.12 Supply and place straw mulch to temporary stabilise earthworks areas",
            "2.13 Hydro-seeding to temporarily stabilise earthworks areas",
            "2.30 1.0mm HDPE liner with welded joints to a depth of 1.0m for sediment retention ponds.  Include for preparation of smooth base perimeter fixing and ballast/weighting as required.",
            "3.6 R Strip topsoil 200mm thick to temporary stockpile (measured in the cut)",
            "3.8 Undercut gully material to waste off-site (measured in the cut)",
            "3.9 Approved imported hardfill backfill, as specified to gully undercut (measured solid in place)"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // no more this
        // setContentView(R.layout.list_fruit);

       // setListAdapter(new ArrayAdapter<String>(this, R.list,scheduleItems));
        setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_main, scheduleItems));
        ListView listView = getListView();
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                Toast.makeText(getApplicationContext(),
                        ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
