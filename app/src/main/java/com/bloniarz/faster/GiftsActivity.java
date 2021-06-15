package com.bloniarz.faster;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.bloniarz.faster.bluetooth.BluetoothManager;
import com.bloniarz.faster.database.Gift;
import com.bloniarz.faster.database.view.GiftListAdapter;

import java.util.ArrayList;

public class GiftsActivity extends AppCompatActivity {

    private final int REQUEST_ENABLE_BT = 0, REQUEST_DISCOVERABLE = 1;
    public final static String GIFT = "com.bloniarz.faster.GiftsActivity.gift";
    private GiftsViewModel giftViewModel;
    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifts);

        GridView gridView = findViewById(R.id.grid_view);
        final GiftListAdapter adapter = new GiftListAdapter(this, new ArrayList<>());
        gridView.setAdapter(adapter);

        giftViewModel = new ViewModelProvider(this).get(GiftsViewModel.class);
        giftViewModel.getGifts().observe(this, gifts -> {
            adapter.clear();
            adapter.addAll(gifts);
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showGift(adapter.getItem(i));
            }
        });
    }

    private void showGift(Gift gift){
        Intent intent = new Intent(this, GiftActivity.class).putExtra(GIFT, gift);
        startActivity(intent);
    }

    public void receiveButtonClick(View view){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        else
            requestDiscoverable();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT){
            if (resultCode == RESULT_CANCELED)
                Toast.makeText(this, "Cannot receive gits", Toast.LENGTH_SHORT).show();
            else if (resultCode == RESULT_OK)
                requestDiscoverable();
        }
        else if (requestCode == REQUEST_DISCOVERABLE){
            if (resultCode == RESULT_CANCELED)
                Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
            else
                receiveGift();
        }
    }

    private void requestDiscoverable(){
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(intent, REQUEST_DISCOVERABLE);
    }

    private void receiveGift(){
        (new BluetoothManager(bluetoothAdapter, getApplicationContext())).getAcceptThread(giftViewModel::insertGift).start();
    }

}