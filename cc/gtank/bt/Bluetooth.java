package cc.gtank.bt;

public interface Bluetooth {
	public boolean initializeHeadset();
	public boolean startVoiceRecognition();
	public boolean stopVoiceRecognition();
}
