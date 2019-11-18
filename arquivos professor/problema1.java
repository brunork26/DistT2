import java.util.concurrent.*;

public class problema1 {
	static class task0 implements Runnable {
		private int i;
		Semaphore s1;

		public task0(Semaphore sem1) {
			s1 = sem1;
		}

		public void run(){
			for (i = 1; i <= 200; i++)
				System.out.print(i + " ");
			s1.release();
		}
	}

	static class task1 implements Runnable {
		private int i;
		Semaphore s1, s2;

		public task1(Semaphore sem1, Semaphore sem2) {
			s1 = sem1;
			s2 = sem2;
		}

		public void run(){
			try {
				s1.acquire();
				for (i = 201; i <= 400; i++)
					System.out.print(i + " ");
				s2.release();
			}catch (InterruptedException e) {
			}
		}
	}

	static class task2 implements Runnable {
		private int i;
		Semaphore s2, s3;

		public task2(Semaphore sem2, Semaphore sem3) {
			s2 = sem2;
			s3 = sem3;
		}

		public void run(){
			try {
				s2.acquire();
				for (i = 401; i <= 600; i++)
					System.out.print(i + " ");
				s3.release();
			}catch (InterruptedException e) {
			}
		}
	}

	static class task3 implements Runnable {
		private int i;
		Semaphore s3, s4;

		public task3(Semaphore sem3, Semaphore sem4) {
			s3 = sem3;
			s4 = sem4;
		}

		public void run(){
			try {
				s3.acquire();
				for (i = 601; i <= 800; i++)
					System.out.print(i + " ");
				s4.release();
			}catch (InterruptedException e) {
			}
		}
	}

	static class task4 implements Runnable {
		private int i;
		Semaphore s4;

		public task4(Semaphore sem4) {
			s4 = sem4;
		}

		public void run(){
			try {
				s4.acquire();
				for (i = 801; i <= 1000; i++)
					System.out.print(i + " ");
			}catch (InterruptedException e) {
			}
		}
	}



	public static void main(String[] args) {
		Semaphore s1 = new Semaphore(0);
		Semaphore s2 = new Semaphore(0);
		Semaphore s3 = new Semaphore(0);
		Semaphore s4 = new Semaphore(0);

		Thread t0 = new Thread(new task0(s1));
		Thread t1 = new Thread(new task1(s1, s2));
		Thread t2 = new Thread(new task2(s2, s3));
		Thread t3 = new Thread(new task3(s3, s4));
		Thread t4 = new Thread(new task4(s4));

		t4.start();
		t3.start();
		t2.start();
		t1.start();
		t0.start();
	}
}
