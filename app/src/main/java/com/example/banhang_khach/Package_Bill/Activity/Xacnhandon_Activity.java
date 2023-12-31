package com.example.banhang_khach.Package_Bill.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.banhang_khach.DTO.BillDTO;
import com.example.banhang_khach.Package_Bill.Adapter.Bill_Adapter;
import com.example.banhang_khach.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Xacnhandon_Activity extends AppCompatActivity {
    String TAG = "xacnhandonactivity";
    ArrayList<BillDTO> list;
    Bill_Adapter adapter;
    ListView rc_listcart;
    ImageView id_back;
    int sl = 0;
    TextView noProductMessage;
    public void Anhxa(){
        rc_listcart = findViewById(R.id.list_donhang);
        id_back = findViewById(R.id.id_back);
        noProductMessage = findViewById(R.id.noProductMessage);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xacnhandon);
        Anhxa();
        list = new ArrayList<>();
        adapter = new Bill_Adapter(Xacnhandon_Activity.this, list);
        rc_listcart.setAdapter(adapter);
        getdata();
        id_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void getdata(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference myRefId = database.getReference("BillProduct");
        myRefId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    BillDTO billDTO = dataSnapshot.getValue(BillDTO.class);
                    if (billDTO.getStatus() == 1 && auth.getUid().equals(billDTO.getIduser())){
                        sl++;
                        list.add(billDTO);
                    }
                }
                if (list.isEmpty()) {
                    noProductMessage.setVisibility(TextView.VISIBLE);
                    rc_listcart.setVisibility(ListView.GONE);
                } else {
                    noProductMessage.setVisibility(TextView.GONE);
                    rc_listcart.setVisibility(ListView.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "err: " + error);
            }
        });
    }

}