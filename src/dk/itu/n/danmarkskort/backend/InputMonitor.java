package dk.itu.n.danmarkskort.backend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import org.apache.commons.io.input.CountingInputStream;

import dk.itu.n.danmarkskort.Util;

public class InputMonitor extends CountingInputStream {
	protected InputStream in;
	private long fileSize;
	private int currentPct;
	private boolean streamStarted;
	private List<InputStreamListener> listeners = new ArrayList<>();
	private MonitorTimer monitor;
	
	public InputMonitor(InputStream in, String fileName) {
		super(in);
		this.in = in;
		fileSize = Util.getFileSize(new File(fileName));
	}
	
	@Override
	protected synchronized void afterRead(int n) {
		super.afterRead(n);
		double pct = ((double)getByteCount()/(double)fileSize)*100;
		if((int)pct != currentPct) {
			if(!streamStarted) {
				for(InputStreamListener listener : listeners) listener.onStreamStarted();
				streamStarted = true;
			}
			for(InputStreamListener listener : listeners) listener.onPercent((int)pct-currentPct);
			currentPct = (int)pct;
		}
	}

	public void addListener(InputStreamListener isl) {
		listeners.add(isl);
	}
	
	public void close() throws IOException {
		for(InputStreamListener listener : listeners) listener.onStreamEnded();
		super.close();
	}
	
	private class MonitorTimer implements ActionListener {
		private Timer timer;
		
		public MonitorTimer(int delay) {
			timer = new Timer(delay, this);
			timer.start();
		}
		
		public void stop() {
			timer.stop();
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("timer");
			
		}		
	}
}
