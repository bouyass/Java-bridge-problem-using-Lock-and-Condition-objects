import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class AtoB extends Thread {

	private int numberCarsInA = 4;
	Lock lock;
	Condition BtoA;
	Condition AtoB;
	int i = 0;
	private String name;
	Runnable c1;
	boolean releaseAccess = false;
	Thread t1;

	public AtoB(Lock lock, Condition condition, Condition condition1, String name) {
		this.lock = lock;
		this.name = name;
		this.AtoB = condition;
		this.BtoA = condition1;
		this.simCars();
		t1 = new Thread(c1);
		t1.start();
	}

	public int getNumberCarsInA() {
		return numberCarsInA;
	}

	public void setNumberCarsInA(int numberCarsInA) {
		this.numberCarsInA = numberCarsInA;
	}

	public void atob() {
			try {
				lock.lock();

				long start = System.currentTimeMillis();
				while (System.currentTimeMillis() - start < 10000) {
					if (this.numberCarsInA > 0) {
						System.out.println(name+i+ " Je traverse le pont");
						Thread.sleep(2000);
						this.numberCarsInA--;
						i++;
					} else {
						System.out.println("There are no cars in the A side");
						while (this.numberCarsInA == 0 || releaseAccess == false) {
							Thread.sleep(2000);
							System.out.println("Still waiting for cars "+this.getNumberCarsInA()+" in A side");
							releaseAccess = System.currentTimeMillis() - start > 10000 ? true : false;
						}
					}
				}

				BtoA.signalAll();
				
				System.out.println(" Giving access to BtoA ");
				
				AtoB.await();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
	}
	
	public void run() {
		while(true) {
			atob();
		}
	}
	


	public void simCars() {
		c1 = new Runnable(){
			@Override
			public void run(){
				
				while (true) {
					long start = System.currentTimeMillis();
					while (System.currentTimeMillis() - start < 10000) {
						try {
							Thread.sleep(2000);
							//System.out.println("Sleeping AtoB");
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (numberCarsInA > 3) {
						numberCarsInA = +2;
						System.out.println(" 2 more cars in the A side");
					} else if (numberCarsInA == 0) {
						numberCarsInA = +3;
						System.out.println(" 3 more cars in the A side");
					}
				}
			}

		};
	}

}
