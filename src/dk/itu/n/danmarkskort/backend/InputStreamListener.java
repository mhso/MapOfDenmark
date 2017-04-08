package dk.itu.n.danmarkskort.backend;

public interface InputStreamListener {
	void onStreamStarted();
	void onPercent(int percentAmount);
	void onStreamEnded();
	void onSetupDone();
}
