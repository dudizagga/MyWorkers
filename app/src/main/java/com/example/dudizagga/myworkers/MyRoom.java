package com.example.dudizagga.myworkers;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.example.dudizagga.myworkers.helpClassesAndTables.UserDtails;
import com.example.dudizagga.myworkers.helpClassesAndTables.Users;
import com.example.dudizagga.myworkers.helpClassesAndTables.utlShared;

import java.util.ArrayList;
import java.util.List;

public class MyRoom extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Hospital H = new Hospital();
    utlShared u;
    String hospital, clazz, room, reason, name,lastname, phone, ownerId,id;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_room);
        setPointer();
        getBack();
    }

    private void setPointer() {
        this.context = this;
        u = new utlShared(context);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        final Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        final Spinner spinner3 = (Spinner) findViewById(R.id.spinner3);
        final Spinner spinner4 = (Spinner) findViewById(R.id.spinner4);
        Button btnSave = (Button) findViewById(R.id.btnSave);
        array(H.hostpital, R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clazz = spinner2.getSelectedItem().toString();
                room = spinner3.getSelectedItem().toString();
                reason = spinner4.getSelectedItem().toString();
                saveBack(hospital, name, lastname, clazz, room, phone, reason, id);


            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        hospital = adapterView.getItemAtPosition(i).toString();
        array(H.reason, R.id.spinner4);
        array(H.a, R.id.spinner3);

        switch (hospital) {
            case "Carmel":
                array(H.carmel, R.id.spinner2);
                //Toast.makeText(this, ""+H.carmel[i], Toast.LENGTH_SHORT).show();
                break;
            case "Rambam":
                array(H.rambam, R.id.spinner2);

                break;
            case "Bnezion":
                array(H.bnezion, R.id.spinner2);

                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void array(String[] a, int spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, a);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinners = (Spinner) findViewById(spinner);
        spinners.setAdapter(adapter);


    }

    public void saveBack(final String table, String name , String lastName, String clazz, String roomNumber, String phone, String reason ,String id) {
        final UserDtails user = new UserDtails();
        user.user = name;
        user.lastName = lastName;
        user.id = id;
        user.clazz = clazz;
        user.roomNumber = roomNumber;
        user.phoneNumber = phone;
        user.reason = reason;
        tre(table, UserDtails.class);
        Backendless.Persistence.of(UserDtails.class).save(user, new AsyncCallback<UserDtails>() {
            @Override
            public void handleResponse(UserDtails response) {
                if (response.ownerId.equals(u.getId("own"))){
                    Toast.makeText(MyRoom.this, "this user already exist!", Toast.LENGTH_SHORT).show();
                }
                }
            @Override
            public void handleFault(BackendlessFault fault) {

            }

        });
    }


    public void getBack() {
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        QueryOptions queryOptions = new QueryOptions();
        dataQuery.setPageSize(100);
        dataQuery.setOffset(0);
        dataQuery.setQueryOptions(queryOptions);


        Backendless.Persistence.of(Users.class).find(new AsyncCallback<List<Users>>() {
            @Override
            public void handleResponse(List<Users> response) {
                for (int i = 0; i < response.size(); i++) {

                    if (response.get(i).objectId.equals(u.getId("own"))) {
                        name = response.get(i).name;
                        lastname = response.get(i).lastName;
                        phone = response.get(i).phone;
                        ownerId = response.get(i).ownerId;
                        id = response.get(i).id;
                    }
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }

    public void delete(String table) {
        final UserDtails user = new UserDtails();
        user.user = name;
        user.clazz = clazz;
        //user.roomNumber = roomNumber;
        user.phoneNumber = phone;
        user.reason = reason;
        tre(table, UserDtails.class);
        Backendless.Persistence.of(UserDtails.class).remove(user);

    }

    public void tre(String tableName, Class clazz) {


        Backendless.Data.mapTableToClass(tableName, clazz);
    }
}






