import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

// An object that stores information about a thread, including the start and end of the range of numbers to check for primality, 
// the list of prime numbers found, the sum of the prime numbers found, and the number of remaining tasks.
public class PrimeFinder implements Runnable {

  private final int start;
  private final int end;
  private final List<Integer> primeNumbers;
  private final AtomicLong primeSum;
  private final AtomicInteger remainingTasks;

  public PrimeFinder(
    int start,
    int end,
    List<Integer> primeNumbers,
    AtomicLong primeSum,
    AtomicInteger remainingTasks
  ) {
    this.start = start;
    this.end = end;
    this.primeNumbers = primeNumbers;
    this.primeSum = primeSum;
    this.remainingTasks = remainingTasks;
  }

  // Run utilzes Segmented Sieve of Eratosthenes, an algorithm that finds all prime numbers from a lower bound to an upper bound.
  // (https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes#Segmented_sieve)
  @Override
  public void run() {
    try {
      synchronized (primeNumbers) {
        ArrayList<Integer> chprime = new ArrayList<Integer>();
        boolean[] ck = new boolean[end + 1];

        Arrays.fill(ck, true);
        ck[1] = false;
        ck[0] = false;

        for (int i = 2; (i * i) <= end; i++) {
          if (ck[i] == true) {
            for (int j = i * i; j <= Math.sqrt(end); j = j + i) {
              ck[j] = false;
            }
          }
        }
        for (int i = 2; i * i <= end; i++) {
          if (ck[i] == true) {
            chprime.add(i);
          }
        }

        boolean[] prime = new boolean[end - start + 1];
        Arrays.fill(prime, true);

        for (int i : chprime) {
          int lower = (start / i);

          if (lower <= 1) {
            lower = i + i;
          } else if (start % i != 0) {
            lower = (lower * i) + i;
          } else {
            lower = (lower * i);
          }
          for (int j = lower; j <= end; j = j + i) {
            prime[j - start] = false;
          }
        }
        for (int i = start; i <= end; i++) {
          if (prime[i - start] == true) {
            primeNumbers.add(i);
            primeSum.getAndAdd((long) i);
          }
        }
      }

    // After we've found all prime numbers in the range, we decrement the remaining tasks count.
    // If the remaining tasks count is 0, we notify the main thread that all tasks are completed.
    } finally {
      // Decrement the remaining tasks count
      if (remainingTasks.decrementAndGet() == 0) {

        // Notify when all tasks are completed
        synchronized (primeNumbers) {
          primeNumbers.notify();
        }
      }
    }
  }
}
