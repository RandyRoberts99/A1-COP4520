import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ThreadManager {

  private static final int THREAD_COUNT = 8;
  private static final int RANGE_START = 2;
  private static final int RANGE_END = 100000000;

  public static void main(String[] args) {
    long startTime = System.nanoTime();

    // Create a thread pool with 8 threads
    ExecutorService executorService = Executors.newFixedThreadPool(
      THREAD_COUNT
    );

    // Divide the range into segments
    int segmentSize = (RANGE_END - RANGE_START + 1) / THREAD_COUNT;

    // Create and submit tasks to the thread pool
    AtomicLong primeSum = new AtomicLong(0);
    AtomicInteger remainingTasks = new AtomicInteger(THREAD_COUNT);

    List<Integer> primeNumbers = new ArrayList<>();

    for (int i = 0; i < THREAD_COUNT; i++) {
      int start = RANGE_START + i * segmentSize;
      int end = i == THREAD_COUNT - 1 ? RANGE_END : start + segmentSize - 1;
      executorService.submit(
        new PrimeFinder(
          start,
          end,
          primeNumbers,
          primeSum,
          remainingTasks
        )
      );
    }

    // Shutdown the executorService after all tasks are submitted
    executorService.shutdown();

    try {
      // Wait for all threads to finish
      executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

      long endTime = System.nanoTime();
      long totalTime = endTime - startTime;

      try (
        BufferedWriter writer = new BufferedWriter(new FileWriter("primes.txt"))
      ) {
        writer.write("Execution time (nanoseconds): " + totalTime + "\n");
        writer.write(
          "Total number of primes found: " + primeNumbers.size() + "\n"
        );
        writer.write("Sum of all primes found: " + primeSum.get() + "\n");
        writer.write(
          "Last 10 prime numbers: " +
          primeNumbers.subList(primeNumbers.size() - 10, primeNumbers.size())
        );
      } catch (IOException e) {
        e.printStackTrace();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
