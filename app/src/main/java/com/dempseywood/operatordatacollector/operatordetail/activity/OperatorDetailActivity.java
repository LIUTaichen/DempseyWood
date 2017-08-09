package com.dempseywood.operatordatacollector.operatordetail.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.database.DataCollectorContract.MachineTO;

import com.dempseywood.operatordatacollector.database.DbHelper;
import com.dempseywood.operatordatacollector.operatordetail.CustomSpinnerAdapter;
import com.dempseywood.operatordatacollector.operatordetail.Machine;
import com.dempseywood.operatordatacollector.operatordetail.listener.OperatorDetailEventListener;
import com.dempseywood.operatordatacollector.scheduleitem.DataHolder;

import java.util.ArrayList;
import java.util.List;

public class OperatorDetailActivity extends AppCompatActivity {
    private DbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_detail);
        mDbHelper = new DbHelper(this);


        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        prepareData();
        //String[] machines = getMachines();

        List<Machine> machines = getMachines();
        //ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, R.layout.spinner_layout,  machines);

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, R.layout.spinner_layout, machines);
// Create an ArrayAdapter using the string array and a default spinner layout
      /*  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.schedule_items_array, R.layout.spinner_layout);
*/

// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.dropdown_layout);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        OperatorDetailEventListener listener = new OperatorDetailEventListener();
        spinner.setOnItemSelectedListener(listener);

        Button confirmButton = (Button)findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(listener);

        EditText operatorNameEditText = (EditText)findViewById(R.id.operator_name);
        operatorNameEditText.setText(DataHolder.getInstance().getEquipmentStatus().getOperator());
        Machine selectedMachine = DataHolder.getInstance().getMachine();
        if(selectedMachine != null) {
            int position = 0;
            for (int i = 0; i < machines.size(); i++) {
                if (machines.get(i).getPlateNo().equals(selectedMachine.getPlateNo())) {
                    position = i;
                }
            }
            spinner.setSelection(position);
        }



    }

    private List<Machine> getMachines() {
        List<Machine> machines = new ArrayList<Machine>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                MachineTO._ID,
                MachineTO.COLUMN_NAME_PLATE_NO,
                MachineTO.COLUMN_NAME_DESCRIPTION
        };

        Cursor cursor = db.query(
                MachineTO.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
       // String[] machines = new String[ cursor.getCount()];
        while(cursor.moveToNext()){
            Machine machine = new Machine();
            machine.setPlateNo(cursor.getString(cursor.getColumnIndexOrThrow(MachineTO.COLUMN_NAME_PLATE_NO)));
            machine.setDesc(cursor.getString(cursor.getColumnIndexOrThrow(MachineTO.COLUMN_NAME_DESCRIPTION)));
            machines.add(machine);
        }
        cursor.close();
        return machines;
    }

    private void prepareData(){

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(MachineTO.TABLE_NAME, "", new String[]{});
        insertMachine(db,"KJF981", "Excavator" );


        insertMachine(db, "A78SDF", "Loader");

        insertMachine(db, "HK9F0A", "Dump Truck");

    }

    private void insertMachine(SQLiteDatabase db, String plate, String desc) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MachineTO.COLUMN_NAME_PLATE_NO, plate);
        values.put(MachineTO.COLUMN_NAME_DESCRIPTION, desc);
// Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(MachineTO.TABLE_NAME, null, values);
    }

    private void deleteMachine(String name){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(MachineTO.TABLE_NAME, "", new String[]{});
    }

    public void deleteRowFromTable(String tableName, String columnName, String keyValue) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String whereClause = columnName + "=?";
        String[] whereArgs = new String[]{String.valueOf(keyValue)};
        //yourDatabase.delete(tableName, whereClause, whereArgs);
    }

    @Override
    protected void onDestroy(){
        mDbHelper.close();
        super.onDestroy();
    }
}
