/*
 * The dirty, unsupported version. Targets API Level 10, Android 2.3.3
 */
package cc.gtank.bt;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class Gingertooth implements Bluetooth {
	
	private Context context;
	private Object btHeadset;
	private boolean connected;
	
	public Gingertooth(Context parent) {
		context = parent;
		connected = false;
	}

	@Override
	public boolean initializeHeadset() {
		if(Build.VERSION.SDK_INT == 10) {
			try {
				Class<?> baseHeadset = Class.forName("android.bluetooth.BluetoothHeadset");
				Class<?> serviceListener = baseHeadset.getDeclaredClasses()[0]; //android.bluetooth.BluetoothHeadset$ServiceListener
				Object listenerProxy = java.lang.reflect.Proxy.newProxyInstance(serviceListener.getClassLoader(), 
                        new Class[] { serviceListener },
                        new InvocationHandler() {
                            @Override
                            public Object invoke(Object proxy, Method method,
                                    Object[] args) throws Throwable {
                                String methodName = method.getName();
                                if(methodName.equals("onServiceConnected")) {
                                    connected = true;
                                } else if(methodName.equals("onServiceDisconnected")) {
                                    connected = false;
                                }
                                return null;
                            }
                    
					});
				Constructor<?> constructor = baseHeadset.getConstructor(Context.class, serviceListener);
				btHeadset = constructor.newInstance(context, listenerProxy);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean startVoiceRecognition() {
		if(connected) {
			try {
				Method startVoice = btHeadset.getClass().getMethod("startVoiceRecognition");
				boolean started = (Boolean) startVoice.invoke(btHeadset, (Object[])null);
				return started;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean stopVoiceRecognition() {
		if(connected) {
			try {
				Method stopVoice = btHeadset.getClass().getMethod("stopVoiceRecognition");
				boolean stopped = (Boolean) stopVoice.invoke(btHeadset, (Object[])null);
				return stopped;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}

}
