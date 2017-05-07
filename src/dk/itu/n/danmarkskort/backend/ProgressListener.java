package dk.itu.n.danmarkskort.backend;

/**
 * An Interface that a class can implement in order to receive events from a <code>ProgressMonitor</code> <code>InputStream</code>.
 * 
 * @author Team N ITU
 * @see ProgressMonitor
 */
public interface ProgressListener {
	void onStreamStarted();
	void onPercent(int percentAmount);
	void getTimeRemaining(int timeRemaining);
	void onStreamEnded();
	void onSetupDone();
}
