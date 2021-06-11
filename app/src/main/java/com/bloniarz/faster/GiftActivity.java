package com.bloniarz.faster;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.companion.AssociationRequest;
import android.companion.BluetoothDeviceFilter;
import android.companion.CompanionDeviceManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.icu.util.VersionInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bloniarz.faster.bluetooth.BluetoothManager;
import com.bloniarz.faster.database.Gift;

import java.util.ArrayList;
import java.util.Map;

public class GiftActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int SELECT_DEVICE_REQUEST_CODE = 1;
    private Gift gift;
    private GiftViewModel giftViewModel;
    private BluetoothAdapter bluetoothAdapter;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<BluetoothDevice> devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        giftViewModel = new ViewModelProvider(this).get(GiftViewModel.class);
        setContentView(R.layout.activity_gift);
        ImageView imageView = findViewById(R.id.gift_image_view);
        TextView textView = findViewById(R.id.gift_text_view);
        gift = (Gift) getIntent().getSerializableExtra(GiftsActivity.GIFT);
        imageView.setImageResource(gift.id);
        textView.setText(gift.name);
    }

    public void sendButtonClick(View view) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else
            scan();
    }

    private void scan() {
        BluetoothDeviceFilter deviceFilter = new BluetoothDeviceFilter.Builder().build();
        AssociationRequest pairingRequest = new AssociationRequest.Builder().addDeviceFilter(deviceFilter).build();
        CompanionDeviceManager deviceManager = (CompanionDeviceManager) getSystemService(Context.COMPANION_DEVICE_SERVICE);
        deviceManager.associate(pairingRequest, new CompanionDeviceManager.Callback() {
            // Called when a device is found. Launch the IntentSender so the user can
            // select the device they want to pair with.
            @Override
            public void onDeviceFound(IntentSender chooserLauncher) {
                try {
                    startIntentSenderForResult(
                            chooserLauncher, SELECT_DEVICE_REQUEST_CODE, null, 0, 0, 0
                    );
                } catch (IntentSender.SendIntentException e) {
                    Log.e("MainActivity", "Failed to send intent");
                }
            }

            @Override
            public void onFailure(CharSequence error) {
                // Handle the failure.
            }
        }, null);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_CANCELED)
                Toast.makeText(this, "Cannot send gifts", Toast.LENGTH_SHORT).show();
            else if (resultCode == RESULT_OK)
                scan();
        } else if (requestCode == SELECT_DEVICE_REQUEST_CODE && data != null) {
            BluetoothDevice deviceToPair = data.getParcelableExtra(CompanionDeviceManager.EXTRA_DEVICE);
            if (deviceToPair != null) {
                deviceToPair.createBond();
                (new BluetoothManager(bluetoothAdapter, getApplicationContext())).getConnectThread(deviceToPair, gift, giftViewModel::deleteGift).start();
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
