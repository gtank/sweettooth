package cc.gtank.bt;

import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BluetoothState extends BroadcastReceiver implements BluetoothProfile.ServiceListener {

	public static enum VOICE { DISCONNECTED, CONNECTING, CONNECTED };
    public static final String BLUETOOTH_STATE = "cc.gtank.bt.BLUETOOTH_STATE";
	
	private VOICE state = VOICE.DISCONNECTED;
	private BluetoothHeadset bluetoothHeadset = null;
	
	@Override
	public void onServiceConnected(int profile, BluetoothProfile proxy) {
		if(profile == BluetoothProfile.HEADSET) {
			bluetoothHeadset = (BluetoothHeadset)proxy;
		}
	}

	@Override
	public void onServiceDisconnected(int profile) {
		if(profile == BluetoothProfile.HEADSET) {
			bluetoothHeadset = null;
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED)) {
			Object state = intent.getExtras().get(BluetoothHeadset.EXTRA_STATE);
			if(state.equals(BluetoothHeadset.STATE_AUDIO_CONNECTING)) {
				state = VOICE.CONNECTING;
			} else if(state.equals(BluetoothHeadset.STATE_AUDIO_CONNECTED)) {
				state = VOICE.CONNECTED;
				Intent btState = new Intent(BLUETOOTH_STATE);
				btState.putExtra("bluetooth_connected", true);
				context.sendBroadcast(btState);
			} else {
				state = VOICE.DISCONNECTED;
                Intent btState = new Intent(BLUETOOTH_STATE);
                btState.putExtra("bluetooth_connected", false);
                context.sendBroadcast(btState);
			}
		}
	}

    public void startVoiceRecognition() {
        BluetoothDevice btDevice = bluetoothHeadset.getConnectedDevices().get(0);
        bluetoothHeadset.stopVoiceRecognition(btDevice);
        bluetoothHeadset.startVoiceRecognition(btDevice);
    }
    
    public void stopVoiceRecognition() {
        BluetoothDevice btDevice = bluetoothHeadset.getConnectedDevices().get(0);
        bluetoothHeadset.stopVoiceRecognition(btDevice); 
    }

	public boolean isAvailable() {
		return bluetoothHeadset != null && bluetoothHeadset.getConnectedDevices().size() > 0;
	}
	
	public BluetoothHeadset getProxy() {
		return bluetoothHeadset;
	}
	
	public VOICE getVoiceState() {
		return state;
	}

}
