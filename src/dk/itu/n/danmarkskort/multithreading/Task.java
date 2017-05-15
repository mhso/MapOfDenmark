package dk.itu.n.danmarkskort.multithreading;

public abstract class Task implements Runnable{
	
	private boolean isRunning = true;
	private TaskPriority priority = TaskPriority.MEDIUM;
	
	/** The task will by default set its priority to medium.*/
	public Task() {
		priority = TaskPriority.MEDIUM;
	}
	
	/** You can also initialize a task with a priority.*/
	public Task(TaskPriority priority) {
		this.priority = priority;
	}
	
	/** This will run the task. <br>
	This method is usually only handled by Queues.*/
	public void run() {
		isRunning = true;
		onRunStart();
		work();
		onRunEnd();
		isRunning = false;
	}
	
	/**This will check if the task is running.*/
	public boolean isRunning() {
		return isRunning;
	}
	
	/**This will return the priority of the Task.*/
	public TaskPriority getPriority() {
		return priority;
	}
	
	/**This will set the priority of the Task.*/
	public void setPriority(TaskPriority priority) {
		this.priority = priority;
	}
	
	/**This is the actual work that the task will do.<br>
	This method is supposed to be overwritten by a subclass.*/
	public abstract void work();
	
	/**This is the work that the task should do before it does the actual work.*/
	public abstract void onRunStart();
	
	/**This is the work that the task should do when the actual work is done.*/
	public abstract void onRunEnd();
	
}
