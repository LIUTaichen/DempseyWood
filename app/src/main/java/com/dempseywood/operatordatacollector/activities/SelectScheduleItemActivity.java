package com.dempseywood.operatordatacollector.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.models.DataHolder;
import com.dempseywood.operatordatacollector.models.ScheduleItem;
import com.dempseywood.operatordatacollector.listeners.ScheduleItemOnClickListener;

import java.util.ArrayList;
import java.util.Arrays;


public class SelectScheduleItemActivity extends AppCompatActivity {

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


        ArrayList<String> itemList = new ArrayList<String>(Arrays.asList(scheduleItems));
        ArrayList<ScheduleItem> items = new ArrayList<ScheduleItem>();
        for(int i=0; i < scheduleItems.length; i++
                ){
            ScheduleItem item = new ScheduleItem();
            item.setItemDescription(scheduleItems[i]);
            items.add(item);
        }
        DataHolder.getInstance().setScheduleItemList(items);
        //RecyclerView  recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        ListView listView = (ListView) findViewById(R.id.listView);



        // use a linear layout manager
       /* LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);*/

        // specify an adapter (see also next example)
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.schedule_item_layout, R.id.listText,scheduleItems);
        /*ScheduleItemAdapter adapter = new ScheduleItemAdapter(items,
                getApplicationContext());
        recyclerView.setAdapter(adapter);*/
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //listView.setItemsCanFocus(false);
        listView.setAdapter(adapter);

        Button confirmButton = (Button)findViewById(R.id.button2);
        confirmButton.setBackgroundColor(Color.LTGRAY);
        ScheduleItemOnClickListener listener = new ScheduleItemOnClickListener(items, confirmButton);

        listView.setOnItemClickListener(listener);
       /* listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                TextView textView = (TextView) parent.findViewById(R.id.listText);
                Toast.makeText(getApplicationContext(),
                        (textView).getText(), Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    public void confirmScheduleItems(View view){
        Intent intent = new Intent(this, EquipmentStatusActivity.class);



        startActivity(intent);
    }
}
