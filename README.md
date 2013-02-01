Gingertooth
-----------

Gingertooth provides support for voice recognition via bluetooth headset for
Android versions prior to Honeycomb. It has currently only been tested on
Android 2.3.3.

## Usage
```java
    Bluetooth bt;

    ...
    if(Build.VERSION.SDK_INT < 11) {
        bt = new Gingertooth(getApplicationContext());
    } else {
        bt = new ModernBluetooth(getApplicationContext());
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
