package cn.edu.fjnu.math.classsignin;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

public class BluetoothReceiver extends BroadcastReceiver {
    private static final String TAG = BluetoothReceiver.class.getSimpleName();
    public static ArrayList<String> sAdressList;

    static {
        if (sAdressList == null) {
            sAdressList =  new ArrayList<>();
        }
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        if (device != null) {
            Log.i(TAG, "find address:" + device.getAddress());
            sAdressList.add(device.getAddress());
        }
    }
}
