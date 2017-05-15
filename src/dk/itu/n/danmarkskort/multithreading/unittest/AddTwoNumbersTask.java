package dk.itu.n.danmarkskort.multithreading.unittest;

import dk.itu.n.danmarkskort.multithreading.Task;

public class AddTwoNumbersTask extends Task {

	private int a, b, c;
	
	public AddTwoNumbersTask(int a, int b) {
		this.a = a;
		this.b = b;
	}
	
	public void work() {
		c = a + b;
	}

	public int getResult() {
		return c;
	}
	
	public void onRunStart() {}
	public void onRunEnd() {}

}
