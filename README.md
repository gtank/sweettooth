Sweettooth
-----------

Sweettooth provides support for voice recognition via Bluetooth headset for
Android. It supports both Gingerbread and the post-Honeycomb Bluetooth APIs,
abstracting away their differences and the nasty reflection necessary to enable
this support on Gingerbread.

Usage is simple- just instantiate the class that corresponds to the version of
Android you are on.

```java
    Bluetooth bt;

    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
        bt = new Gingerbread();
    } else {
        bt = new Honeycomb();
    }
```
    
The rest of the setup procedure doesn't change. obtainProxy() will throw an
exception if you haven't set the application context.

```java
    bt.setContext(getApplicationContext());
    
    try {
        bt.obtainProxy();
    } catch (Exception e) {
        e.printStackTrace();
    }
```

To start routing audio from your Bluetooth headset, you have to explictly open a
voice recognition channel. This is as simple as calling startVoiceRecognition
before activating your SpeechRecognizer.

```java
        if(bt.isAvailable()) {
            bt.startVoiceRecognition();
        }
```

Sweettooth will fire an Intent, Bluetooth.BLUETOOTH_STATE, when the
Bluetooth service responds to your request. Boolean extra "bluetooth_connected"
will indicate success or failure to open the channel. Your application should
wait for this Intent before proceeeding with speech recognition.

After you have your results, close the channel with stopVoiceRecognition.

```java
        if(bt.isAvailable()) {
            bt.stopVoiceRecognition();
        }
```

At the end of your Activity, disconnect the proxy by calling releaseProxy().
