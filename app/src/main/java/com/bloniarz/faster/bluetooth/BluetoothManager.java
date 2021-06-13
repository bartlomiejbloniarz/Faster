package com.bloniarz.faster.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.bloniarz.faster.database.Gift;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class BluetoothManager {
    private final UUID MY_UUID = UUID.fromString("3c083e8c-c9f3-11eb-b8bc-0242ac130003");
    private final String NAME = "FASTER_BLUETOOTH_SERVICE";
    private final String TAG = "FASTER_DEBUG_TAG";
    private final int GIFT_ACCEPTED = 1, GIFT_NOT_ACCEPTED = 0;
    private final BluetoothAdapter bluetoothAdapter;
    private final Context context;

    public BluetoothManager(BluetoothAdapter bluetoothAdapter, Context context){
        this.bluetoothAdapter = bluetoothAdapter;
        this.context = context;
    }

    public AcceptThread getAcceptThread(Function<Gift, Boolean> function){
        return new AcceptThread(function);
    }

    public ConnectThread getConnectThread(BluetoothDevice device, Gift gift, Consumer<Gift> consumer){
        return new ConnectThread(device, gift, consumer);
    }

    public class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;
        private final Function<Gift, Boolean> function;

        public AcceptThread(Function<Gift, Boolean> function) {
            BluetoothServerSocket tmp = null;
            this.function = function;
            try {
                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
                toast("Connection error");
            }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    toast("Connection error");
                    break;
                }

                if (socket != null) {
                    manageMyConnectedSocket(socket);
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        toast("Connection error");
                    }
                    break;
                }
            }
        }

        private void manageMyConnectedSocket(BluetoothSocket socket){
            new ReceiveThread(socket, function).start();
        }
    }

    private class ReceiveThread extends Thread {
        private final BluetoothSocket socket;
        private final InputStream inStream;
        private final OutputStream outStream;
        private final Function<Gift, Boolean> function;
        private final ObjectInputStream objectInputStream;

        public ReceiveThread(BluetoothSocket socket,  Function<Gift, Boolean> function) {
            this.socket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            ObjectInputStream temp = null;
            this.function = function;

            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                toast("Connection error");
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                toast("Connection error");
            }

            inStream = tmpIn;
            outStream = tmpOut;

            try {
                temp = new ObjectInputStream(inStream);
            } catch (IOException e){
                toast("Connection error");
            }

            objectInputStream = temp;
        }

        public void run() {
            Gift gift;

            try {
                gift = (Gift) objectInputStream.readObject();
                if (function.apply(gift))
                    outStream.write(GIFT_ACCEPTED);
                else
                    outStream.write(GIFT_NOT_ACCEPTED);
            } catch (IOException | ClassNotFoundException e) {
                toast("Connection error");
            }
        }
    }


    public class ConnectThread extends Thread {
        private final BluetoothSocket socket;
        private final Gift gift;
        private final Consumer<Gift> consumer;

        public ConnectThread(BluetoothDevice device, Gift gift, Consumer<Gift> consumer) {
            this.gift = gift;
            this.consumer = consumer;
            BluetoothSocket tmp = null;

            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                toast("Connection error");
            }
            socket = tmp;
        }

        public void run() {
            bluetoothAdapter.cancelDiscovery();

            try {
                socket.connect();
            } catch (IOException connectException) {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
                toast("Connection error");
                return;
            }
            manageMyConnectedSocket(socket);
        }

        private void manageMyConnectedSocket(BluetoothSocket socket){
            new SendThread(socket, gift, consumer).start();
        }
    }

    private class SendThread extends Thread {
        private final InputStream inStream;
        private final OutputStream outStream;
        private final ObjectOutputStream objectOutputStream;
        private final Gift gift;
        private final Consumer<Gift> consumer;

        public SendThread(BluetoothSocket socket, Gift gift, Consumer<Gift> consumer) {
            OutputStream tmpOut = null;
            InputStream tmpIn = null;
            this.gift = gift;
            this.consumer = consumer;
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                toast("Connection error");
            }
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                toast("Connection error");
            }

            inStream = tmpIn;
            outStream = tmpOut;

            ObjectOutputStream temp = null;
            try {
                temp = new ObjectOutputStream(outStream);
            } catch (IOException e){
                toast("Connection error");
            }

            objectOutputStream = temp;
        }

        public void run() {
            try {
                objectOutputStream.writeObject(gift);
                int accept = inStream.read();
                if (accept == GIFT_ACCEPTED) {
                    consumer.accept(gift);
                    toast("Your gift was accepted");
                }
                else{
                    toast("Your gift was rejected");
                }
            } catch (IOException e) {
                toast("Connection error");
            }
        }
    }

    private void toast(String text) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(context, text, Toast.LENGTH_LONG).show();
            }
        });
    }
}
