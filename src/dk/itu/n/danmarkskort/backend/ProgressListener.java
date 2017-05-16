package dk.itu.n.danmarkskort.backend;

/**
 * An Interface that a class can implement in order to receive events from a <code>ProgressMonitor</code> <code>InputStream</code>.
 * 
 * @author Team N ITU
 * @see ProgressMonitor
 */
public interface ProgressListener {
	/** Called when the Stream starts receiving bytes. */
	void onStreamStarted();
	/**
	 * Called at every percent increase.
	 * @param percentAmount The current percentage of total bytes having passed through this Input Stream.
	 */
	void onPercent(int percentAmount);
	/**
	 * Also called at every percent increase.
	 * @param timeRemaining The time remaining in milliseconds.
	 */
	void getTimeRemaining(int timeRemaining);
	/** Called when the Stream is closed. */
	void onStreamEnded();
	 /** Called externally in our case, when all listeners are added and we are ready to parse/load a file. */
	void onSetupDone();
}
