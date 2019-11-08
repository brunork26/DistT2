import java.util.concurrent.*;

public class pombo {
	static int num_threads = 20;
	static volatile int postit = 0;

	static class usuario implements Runnable {
		Semaphore mutex;
		Semaphore cheia;
		Semaphore enchendo;
		private int tid;

		public usuario(int id, Semaphore mtx, Semaphore ch, Semaphore en){
			this.tid = id;
			mutex = mtx;
			cheia = ch;
			enchendo = en;
		}

		public void run(){
			while (true) {
				try {
					enchendo.acquire();
					mutex.acquire();
					System.out.println("usuario " + tid + " cola post it " + postit);
					postit++;
					if (postit == num_threads)
						cheia.release();
					mutex.release();
				}catch (InterruptedException e) {
				}
			}
		}
	}

	static class pomba implements Runnable {
		Semaphore mutex;
		Semaphore cheia;
		Semaphore enchendo;
		private int tid;
		private int i;

		public pomba(int id, Semaphore mtx, Semaphore ch, Semaphore en){
			this.tid = id;
			mutex = mtx;
			cheia = ch;
			enchendo = en;
		}

		public void run(){
			while (true) {
				try {
					cheia.acquire();
					mutex.acquire();
					System.out.println("pombo levando a mochila e voltando...");
					postit = 0;
					for (i = 0; i < num_threads; i++)
						enchendo.release();
					mutex.release();
				}catch (InterruptedException e) {
				}
			}
		}
	}

	public static void main(String[] args) {
		Semaphore mutex = new Semaphore(1);
		Semaphore cheia = new Semaphore(0);
		Semaphore enchendo = new Semaphore(num_threads);
		int i;

		Thread p = new Thread(new pomba(0, mutex, cheia, enchendo));
		p.start();
		for (i = 1; i <= num_threads; i++){
			Thread t = new Thread(new usuario(i, mutex, cheia, enchendo));
			t.start();
		}
	}
}
