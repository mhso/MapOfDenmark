package dk.itu.n.danmarkskort.backend;

public interface ProgressListener {
	void onStreamStarted();
	void onPercent(int percentAmount);
	void getTimeRemaining(int timeRemaining);
	void onStreamEnded();
	void onSetupDone();
}
