import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Coding test for job candidates.<br>
 * The test creates given number of tasks/threads (e.g. 100) and runs them in
 * parallel. Each thread generates some random number and formats it to a
 * string. The generated number must be unique within the test. The results are
 * continuously collected in a list. At the end, the list is sorted and printed
 * to the console.
 */
public class Test implements Runnable {

	private static int MAX_THREADS = 1000;
	private static int MAX_RND = 5000;

	public static void main(String[] args) {
		// first test (function):
		doIt();
		// second test (stability):
		while (true) {
			doIt();
		}
	}

	static Set<String> res = new HashSet<String>();
	static ArrayList<String> res2 = new ArrayList<String>();

	static void doIt() {
		Thread[] ts = new Thread[MAX_THREADS];
		for (int i = 0; i < MAX_THREADS; i++) {
			Thread t = new Thread(new Test(), "" + i);
			ts[i] = t;
		}
		for (int i = 0; i < MAX_THREADS; i++) {
			ts[i].start();
		}
		int i = 0;
		while (i < MAX_THREADS) {
			if (ts[i++].isAlive()) {
				i = 0;
			}
		}
		System.out.println(res);

		res2.clear();
		res2.addAll(res);
		res.clear();

		Collections.sort(res2);
		validateRes();
		//for (String rndItem : res2) {
		//	System.out.println(rndItem);
		//}
	}

	public Test() {
	}

	public void run() {
		try {
			boolean success = false;
			do {
				String n = String.valueOf(new Random().nextInt(MAX_RND));
				success = threadSafeAdd(n);
			} while (!success);
		} catch (Throwable e) {
			System.err.println(e.toString());
		}
	}

	private boolean threadSafeAdd(String n) {
		synchronized (res) {
			if (res.isEmpty()) {
				res.add(n);
				return true;
			}
			if (res.contains(n)) {
				return false;
			}
			res.add(n);
			return true;
		}
	}

	private static boolean validateRes() {
		int rndSize = res2.size();
		if (rndSize != MAX_THREADS) {
			System.err.println("failed - not enough values:" + rndSize + " <> " + MAX_THREADS);
		}
		if (rndSize < 2) {
			// empty list or 1 item - always sorted
			return true;
		}
		for (int i = 0; i < rndSize - 1; i++) {
			if (res2.get(i) == null) {
				System.err.println("failed - a null present");
				// empty item
				return false;
			}
			if (res2.get(i).compareTo(res2.get(i + 1)) > 0) {
				System.err.println("failed - " + res2.get(i) + " > " + res2.get(i + 1));
				return false;
			}

		}
		System.out.println("validated ok");
		return true;
	}

	void testSortedRes() {
		doIt();
		// assertTrue(validateRes());
	}

}