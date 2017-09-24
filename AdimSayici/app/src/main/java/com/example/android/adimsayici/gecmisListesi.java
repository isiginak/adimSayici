package com.example.android.adimsayici;

import android.content.Context;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class gecmisListesi extends AppCompatActivity {
ListView gecmisList;
    ArrayAdapter adapter;
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gecmis_listesi);

        gecmisList=(ListView)findViewById(R.id.gecmisList);
        arrayList=new ArrayList<>();
        adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,android.R.id.text1,arrayList);//android içindeki hazır yapıyı kullandık
      DatabaseReference dbref= FirebaseDatabase.getInstance().getReference().child(config.android_id);
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    dataSnapshot.getKey();
                    uygModel model=ds.getValue(uygModel.class);
                    arrayList.add(model.getTarih()+" tarihinde "+model.getAdimSayisi()+" adım atıldı");
                }
                gecmisList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
