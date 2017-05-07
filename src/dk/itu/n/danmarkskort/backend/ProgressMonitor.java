package dk.itu.n.danmarkskort.backend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

/**
 * This class extends <code>FilteredInputStream</code> to provide ways to monitor the progress of an <code>InputStream</code>.
 * A class can implement <code>ProgressListener</code> to receive notifications from the Input Stream. <p>
 * This can then be used to create Loading Screens where users can visually track the progress of the stream.
 * This class is inspired by the class <code>CountingInputStream</code> from {@link https://commons.apache.org/proper/commons-io/}
 * but the code has not been copied in any way (I saw it as a challenge to create this on my own!).
 * 
 * @see ProgressListener
 * @author Team N ITU
 */
public class ProgressMonitor extends FilterInputStream {
	private int currentPct;
	private int totalByteCount;
	private int currentByteCount;
	private int byteCountStamp;
	private long timeStamp;
	private byte timeStampDelay;
	private boolean streamStarted;
	private List<ProgressListener> listeners = new ArrayList<>();
	private ProgressTimer timer;
	
	/**
	 * Create a new ProgressMonitor to monitor the supplied <code>InputStream</code>.
	 * 
	 * @param in The <code>InputStream</code> to monitor, this <code>InputStream</code> is supplied to the superclass <code>FilterInputStream</code>.
	 */
	public ProgressMonitor(InputStream in) {
		super(in);
		try {
			totalByteCount = super.available();
		} catch (IOException e) {
			e.printStackTrace();
		}
		timer = new ProgressTimer(10);
	}

	public void addListener(ProgressListener isl) {
		listeners.add(isl);
	}
	
	private void countBytes(int n) {
		currentByteCount += n;
	}

	@Override
	public int read() throws IOException {
		final int n = in.read();
		countBytes(1);
		return n;
	}

	@Override
	public int read(byte[] b) throws IOException {
		final int n = in.read(b);
		countBytes(n);
		return n;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		final int n = in.read(b, off, len);
		countBytes(n);
		return n;
	}

	@Override
	public long skip(long n) throws IOException {
		final long skip = in.skip(n);
		countBytes((int)skip);
		return skip;
	}

	@Override
	public int available() throws IOException {
		return super.available();
	}

	@Override
	public synchronized void mark(int readlimit) {
		in.mark(readlimit);
	}

	@Override
	public synchronized void reset() throws IOException {
		in.reset();
	}

	@Override
	public boolean markSupported() {
		return in.markSupported();
	}

	@Override
	public void close() throws IOException {
		for(ProgressListener listener : listeners) listener.onStreamEnded();
		timer.stop();
		in.close();
	}
	
	private class ProgressTimer implements ActionListener {
		private Timer timer;
		
		public ProgressTimer(int delay) {
			timer = new Timer(delay, this);
			timer.start();
		}
		
		public void stop() {
			timer.stop();
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				if(streamStarted) {
					double pct = ((double)currentByteCount/(double)totalByteCount)*100;
					if((int)pct != currentPct) {
						for(ProgressListener listener : listeners) listener.onPercent((int)pct-currentPct);
						currentPct = (int)pct;
						
						int bytesPassed = currentByteCount-byteCountStamp;
						if(bytesPassed > 0) {
							for(ProgressListener listener : listeners) 
								listener.getTimeRemaining((int)(((totalByteCount-currentByteCount)/bytesPassed)*(System.currentTimeMillis()-timeStamp)));
						}
					}
					timeStampDelay++;
					if(timeStampDelay % 10 == 0) {
						timeStamp = System.currentTimeMillis();
						byteCountStamp = currentByteCount;
						timeStampDelay = 0;
					}
				}
				else if(!streamStarted && currentByteCount > 0) {
					for(ProgressListener listener : listeners) listener.onStreamStarted();
					streamStarted = true;
				}
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
