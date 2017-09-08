package com.dempseywood.operatordatacollector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ChooseMaterialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_material);

        ListView listView = (ListView)findViewById(R.id.materialList);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.material_list, R.layout.list_view_layout);
        //adapter.setDropDownViewResource(R.layout.list_item);

        listView.setAdapter(adapter);

        //highlight current selected material
        Log.d("ChooseMaterialActivity",  "debug");
        String[] materialArray = getResources().getStringArray(R.array.material_list);
        for(int i = 0; i < materialArray.length; i ++){
            if(materialArray[i].equals(Data.getMaterial())){
                Log.d("ChooseMaterialActivity", Data.getMaterial() + "  matched");
                listView.setItemChecked(i, true);
            }
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] materialArray = getResources().getStringArray(R.array.material_list);
                Data.setMaterial(materialArray[position]);
                ChooseMaterialActivity activity = (ChooseMaterialActivity) view.getContext();
                Intent intent = new Intent(activity, CountByTapActivity.class);
                activity.startActivity(intent);

            }
        });


    }

}
