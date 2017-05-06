package dk.itu.n.danmarkskort.backend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

public class ProgressMonitor extends InputStream {
	protected InputStream in;
	private int currentPct;
	private int totalByteCount;
	private int currentByteCount;
	private long timeStamp;
	private boolean streamStarted;
	private List<ProgressListener> listeners = new ArrayList<>();
	private ProgressTimer timer;
	
	public ProgressMonitor(InputStream in) {
		this.in = in;
		try {
			totalByteCount = in.available();
		} catch (IOException e) {
			e.printStackTrace();
		}
		timer = new ProgressTimer(5);
	}

	public void addListener(ProgressListener isl) {
		listeners.add(isl);
	}
	
	@Override
	public int read() throws IOException {
		return in.read();
	}
	
	@Override
	public synchronized void mark(int readlimit) {
		in.mark(readlimit);
	}

	@Override
	public boolean markSupported() {
		return in.markSupported();
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return in.read(b, off, len);
	}

	@Override
	public int read(byte[] b) throws IOException {
		return in.read(b);
	}

	@Override
	public long skip(long n) throws IOException {
		return in.skip(n);
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
				if(!streamStarted && in.available() != totalByteCount) {
					for(ProgressListener listener : listeners) listener.onStreamStarted();
					streamStarted = true;
				}
				else if(streamStarted) {
					int byteCount = totalByteCount- in.available();
					
					double pct = ((double)byteCount/(double)totalByteCount)*100;
					if((int)pct != currentPct) {
						for(ProgressListener listener : listeners) listener.onPercent((int)pct-currentPct);
						currentPct = (int)pct;
						
						int bytesPassed = byteCount-currentByteCount;
						if(bytesPassed > 0) {
							long currentTime = System.currentTimeMillis();
							
							int msRemaining = (int)(((totalByteCount-byteCount)/bytesPassed)*(currentTime-timeStamp));
							for(ProgressListener listener : listeners) listener.getTimeRemaining(msRemaining);
						}
					}
					else timeStamp = System.currentTimeMillis();
					currentByteCount = byteCount;
				}
			}
			catch(Exception ex) {
				
			}
		}
	}
}
