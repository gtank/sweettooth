/**
 * Provides Bluetooth headset support
 */

package cc.gtank.bt;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ModernBluetooth implements Bluetooth {
	
	private Context context;
	private BluetoothHeadset mBluetoothHeadset;
	private BluetoothProfile.ServiceListener mServiceListener;
	private BluetoothDevice mDevice;
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	
	public ModernBluetooth(Context parent) {
		this.context = parent;
		mServiceListener = new BluetoothProfile.ServiceListener() {
			
			@Override
			public void onServiceConnected(int profile, BluetoothProfile proxy) {
		        if (profile == BluetoothProfile.HEADSET) {
		            mBluetoothHeadset = (BluetoothHeadset)proxy;
		            // only one headset
		            mDevice = mBluetoothHeadset.getConnectedDevices().get(0);
		        }	
			}
			
			@Override
			public void onServiceDisconnected(int profile) {
		        if (profile == BluetoothProfile.HEADSET) {
		            mBluetoothHeadset = null;
		            mDevice = null;
		        }	
			}
			
		};
	}
	
	public boolean initializeHeadset() {
		return mBluetoothAdapter.getProfileProxy(context, mServiceListener, BluetoothProfile.HEADSET);
	}

	@Override
	public boolean startVoiceRecognition() {
		if(mBluetoothHeadset != null) {
			return mBluetoothHeadset.startVoiceRecognition(mDevice);
		}
		return false;
	}

	@Override
	public boolean stopVoiceRecognition() {
		if(mBluetoothHeadset != null) {
			return mBluetoothHeadset.stopVoiceRecognition(mDevice);
		}
		return false;
	}
	
}
