
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
	public static void main(String [] args) {
		Lock lock = new ReentrantLock();
		Condition ATOB = lock.newCondition();
		Condition BTOA = lock.newCondition();
		
		AtoB aTOb = new AtoB(lock,ATOB,BTOA,"atob");
		BtoA bTOa = new BtoA(lock,ATOB,BTOA,"btoa");
		
		aTOb.start();
		bTOa.start();
	}
}
