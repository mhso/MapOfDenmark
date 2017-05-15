package dk.itu.n.danmarkskort.multithreading.unittest;

import static org.junit.Assert.*;
import org.junit.Test;
import dk.itu.n.danmarkskort.multithreading.Queue;

public class QueueTaskTest {

	@Test
	public void testQueueRun() throws InterruptedException {
		int a = 2;
		int b = 2;
		
		Queue queue = new Queue();
		AddTwoNumbersTask task = new AddTwoNumbersTask(a, b);
		queue.addTask(task);
		Queue.run(queue);
		Thread.sleep(100);
		assertEquals(a + b, task.getResult());
	}
	
}
