package dk.itu.n.danmarkskort.multithreading;
import java.util.ArrayList;
import java.util.HashMap;

public class Queue implements Runnable {

	private HashMap<TaskPriority, ArrayList<Task>> tasks = new HashMap<TaskPriority, ArrayList<Task>>();
	private boolean interrupt = false;
	private boolean wait = false;
	
	/** This will add a task to the queue.<br>
	It will run automatically if the queue is already running. */
	public void addTask(Task task) {
		if(!tasks.containsKey(task.getPriority())) {
			ArrayList<Task> taskList = new ArrayList<Task>();
			taskList.add(task);
			tasks.put(task.getPriority(), taskList);
		} else {
			tasks.get(task.getPriority()).add(task);
		}
		wait = false;
	}
	
	/** This will run a list of tasks.<br>
	Tasks with higher priority will run first.*/
	private void runTaskList(ArrayList<Task> taskList) {
		while(taskList.size() > 0) {
			Task task = taskList.get(0);
			taskList.remove(0);
			Thread thread = new Thread(task);
			thread.start();
			while(thread.isAlive()) {};
		}
	}
	
	/** This will stop the queue. */
	public void interrupt() {
		interrupt = true;
	}
	
	/** This will make the queue sleep until a new task is added. */
	private void yield() {
		while(wait)try{Thread.sleep(1);}catch(InterruptedException e){}
	}
	
	/** This will run the queue internally.<br>
	If it is not interrupted, it will run the tasks added. <br>
	When the tasks are done, it will wait for new tasks.<br>
	Don't use this method for starting a queue, but use the static Queue.run(). **/
	public void run() {
		while(!interrupt) {
			yield();
			wait = true;
			for(TaskPriority priority : TaskPriority.values()) {
				ArrayList<Task> taskList = tasks.get(priority);
				if(taskList != null && taskList.size() > 0) {
					runTaskList(taskList);
					tasks.remove(priority);
					wait = false;
				}
			}
		}
	}
	
	/** This will start a queue on a seperate thread. */
	public static void run(Queue queue) {
		Thread thread = new Thread(queue);
		thread.start();
	}
	
	/** This will return the number of tasks in a queue. */
	public int size() {
		int size = 0;
		for(TaskPriority priority : tasks.keySet()) size += tasks.get(priority).size();
		return size;
	}
	
}
