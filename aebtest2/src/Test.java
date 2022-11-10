import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Coding test for job candidates.<br>
 * The test creates given number of tasks/threads (e.g. 100) and runs them in
 * parallel. Each thread generates some random number and formats it to a
 * string. The generated number must be unique within the test. The results are
 * continuously collected in a list. At the end, the list is sorted and printed
 * to the console.
 */
public class Test implements Runnable {

	public static void main(String[] args) {
		// first test (function):
		doIt();
		// second test (stability):
		// while (true) { doIt(); }
	}

	static ArrayList<String> res = new ArrayList<String>();

	static void doIt() {
		Thread[] ts = new Thread[100];
		for (int i = 0; i < 100; i++) {
			Thread t = new Thread(new Test(), "" + i);
			ts[i] = t;
		}
		for (int i = 0; i < 100; i++) {
			ts[i].start();
		}
		int i = 0;
		while (i < 100) {
			if (ts[i++].isAlive()) {
				i = 0;
			}
		}
		System.out.println(res);
		Collections.sort(res);
		for (String rndItem : res) {
			System.out.println(rndItem);
		}
	}

	public Test() {
	}

	public void run() {
		try {
			xxx: while (true) {
				String n = "" + new Random().nextInt(500);
				synchronized (res) {
					int rndSize = res.size();
					for (int i = 0; i < rndSize; i++) {
						if (res.get(i) == n)
							continue xxx;
					}
					res.add(n);
				}
				break;
			}
		} catch (Throwable e) {
			System.err.println(e.toString());
		}
	}

}