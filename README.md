Gingertooth
-----------

Gingertooth provides support for voice recognition via bluetooth headset for
Android. It supports both Gingerbread and the current (Jelly Bean) 
Bluetooth API. The Gingerbread functionality calls the then-private
BluetoothHeadset class, and consequently is much different than the current
mechanism.

## Gingerbread usage
```java
    Bluetooth bt;

    ...
    if(Build.VERSION.SDK_INT < 11) {
        bt = new Gingertooth(getApplicationContext());
    } 
    ...
    // call this before trying to open a channel
    // returns true on success
    boolean bt_initialized = bt.initializeHeadset();

    ...
    // before using your SpeechRecognizer
    bt.startVoiceRecognition();

    ...
    // when you're done
    bt.stopVoiceRecognition();
```

## Post-Honeycomb usage
The modern Bluetooth API is slightly more involved but conceptually much nicer.

First, get the BluetoothHeadset proxy and set up a BluetoothState class as the
ServiceListener, like so:

```java
        //initialization
        BluetoothState bt = new BluetoothState();
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        btAdapter.getProfileProxy(getApplicationContext(), bt,
            BluetoothProfile.HEADSET);
```

To open a voice recognition channel, call startVoiceRecognition:

```java
        if(bt.isAvailable()) {
            bt.startVoiceRecognition();
        }
```

BluetoothState will fire an Intent, BluetoothState.BLUETOOTH_STATE, when the
Bluetooth service responds. Boolean extra "bluetooth_connected" will indicate
success or failure. Your application should wait for this Intent before
proceeeding with speech recognition.

When you have your results, close the channel with stopVoiceRecognition.

```java
        if(bt.isAvailable()) {
            bt.stopVoiceRecognition();
        }
```

At the end of your Activity, disconnect the proxy.

```java
        btAdapter.closeProfileProxy(BluetoothProfile.HEADSET, bt.getProxy());
```
