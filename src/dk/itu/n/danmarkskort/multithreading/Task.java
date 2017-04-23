package dk.itu.n.danmarkskort.multithreading;

public abstract class Task implements Runnable{
	
	private boolean isRunning = true;
	private TaskPriority priority = TaskPriority.MEDIUM;
	
	public Task() {
		priority = TaskPriority.MEDIUM;
	}
	
	public Task(TaskPriority priority) {
		this.priority = priority;
	}
	
	public void run() {
		isRunning = true;
		onRunStart();
		work();
		onRunEnd();
		isRunning = false;
	}
	
	public boolean isRunning() {
		return isRunning;
	}
	
	public TaskPriority getPriority() {
		return priority;
	}
	
	public void setPriority(TaskPriority priority) {
		this.priority = priority;
	}
	
	public abstract void work();
	public abstract void onRunStart();
	public abstract void onRunEnd();
	
}
