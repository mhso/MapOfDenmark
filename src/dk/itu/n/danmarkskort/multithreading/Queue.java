package dk.itu.n.danmarkskort.multithreading;
import java.util.ArrayList;
import java.util.HashMap;

public class Queue implements Runnable {

	private HashMap<TaskPriority, ArrayList<Task>> tasks = new HashMap<TaskPriority, ArrayList<Task>>();
	private boolean interrupt = false;
	private boolean wait = false;
	
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
	
	private void runTaskList(ArrayList<Task> taskList) {
		while(taskList.size() > 0) {
			Task task = taskList.get(0);
			taskList.remove(0);
			Thread thread = new Thread(task);
			thread.start();
			while(thread.isAlive()) {};
		}
	}
	
	public void interrupt() {
		interrupt = true;
	}
	
	private void yield() {
		while(wait)try{Thread.sleep(1);}catch(InterruptedException e){}
	}
	
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
	
	public static void run(Queue queue) {
		Thread thread = new Thread(queue);
		thread.start();
	}
	
	public int size() {
		int size = 0;
		for(TaskPriority priority : tasks.keySet()) size += tasks.get(priority).size();
		return size;
	}
	
}
