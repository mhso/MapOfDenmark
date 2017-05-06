package dk.itu.n.danmarkskort.backend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

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
		private int delay;
		
		public ProgressTimer(int delay) {
			this.delay = delay;
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
							long currentTime = System.currentTimeMillis();
							
							int msRemaining = (int)(((totalByteCount-currentByteCount)/bytesPassed)*(currentTime-timeStamp));
							for(ProgressListener listener : listeners) listener.getTimeRemaining(msRemaining);
						}
					}
					timeStampDelay++;
					if(timeStampDelay % 10 == 0) {
						timeStamp = System.currentTimeMillis();
						timeStampDelay = 0;
					}
					byteCountStamp = currentByteCount;
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
