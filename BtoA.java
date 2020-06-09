import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class BtoA extends Thread{
	private int numberCarsInB = 3;
	Lock lock;
	Condition BtoA;
	Condition AtoB;
	int i = 0;  
	private String name;
	Runnable c1;
	private boolean releaseAccess = false;
	Thread t1;

	public BtoA(Lock lock, Condition condition, Condition condition1, String name) {
		this.lock = lock;
		this.name = name;
		this.AtoB = condition;
		this.BtoA = condition1;
		this.simCars();
		//ft = new FutureTask<Void>(c1);
		t1 = new Thread(c1);
		t1.start();
	}

	public int getNumberCarsInB() {
		return numberCarsInB;
	}

	public void setNumberCarsInB(int numberCarsInB) {
		this.numberCarsInB = numberCarsInB;
	}

	public void btoa() {
			try {
				lock.lock();
				
				long start = System.currentTimeMillis();
				while (System.currentTimeMillis() - start < 10000) {
					if (this.numberCarsInB > 0) {
						System.out.println(name+i+" Je traverse le pont");
						Thread.sleep(2000);
						this.numberCarsInB--;
						i++;
					} else {
						System.out.println("There are no cars in the B side");
						while (this.numberCarsInB == 0 || releaseAccess == false) {
							Thread.sleep(2000);
							System.out.println("Still waiting for cars "+this.getNumberCarsInB()+ " in B side");
							releaseAccess = System.currentTimeMillis() - start > 10000 ? true : false;
						}
					}
				}

				AtoB.signalAll();

				System.out.println("Giving access to AtoB");
					
				BtoA.await();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
	}
	
	public void run() {
		while(true) {
			btoa();
		}
	}

	public void simCars() {
		c1 = new Runnable() {
			@Override
			public void run(){
				
				while (true) {
					long start = System.currentTimeMillis();
					while (System.currentTimeMillis() - start < 10000) {
						try {
							Thread.sleep(2000);
							//System.out.println("Sleeping BtoA");
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					if (numberCarsInB > 3) {
						numberCarsInB = +2;
						System.out.println(" 2 more cars in the B side");
					} else if (numberCarsInB == 0) {
						numberCarsInB = +3;
						System.out.println(" 3 more cars in the B side ");
					}
				}
			}

		};
	}
}
