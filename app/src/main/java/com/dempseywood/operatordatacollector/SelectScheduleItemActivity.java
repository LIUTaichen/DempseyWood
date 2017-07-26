package com.dempseywood.operatordatacollector;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SelectScheduleItemActivity extends ListActivity {

    static final String[] scheduleItems = new String[] { "2.12 Supply and place straw mulch to temporary stabilise earthworks areas",
            "2.13 Hydro-seeding to temporarily stabilise earthworks areas",
            "2.30 1.0mm HDPE liner with welded joints to a depth of 1.0m for sediment retention ponds.  Include for preparation of smooth base perimeter fixing and ballast/weighting as required.",
            "3.6 R Strip topsoil 200mm thick to temporary stockpile (measured in the cut)",
            "3.8 Undercut gully material to waste off-site (measured in the cut)",
            "3.9 Approved imported hardfill backfill, as specified to gully undercut (measured solid in place)"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_schedule_item);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        getListView().setSelector(android.R.color.darker_gray);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.schedule_item_layout, R.id.listText, scheduleItems);
        setListAdapter(adapter);
    }
}
