# Assignment 1 (COP 4520) - Randall Roberts

### Overview
This program is designed to find every prime number between (2, 10^8) using multithreading between 8 threads in Java.

### Prerequisites
1. Install the Java Development Kit (JDK 8 or Higher)
2. Install Git
2. Have a code editor with a terminal such as Visual Studio Code installed. (https://code.visualstudio.com/download)

### How to run the file
1. Clone the repository from Git to your machine. (https://docs.github.com/en/repositories/creating-and-managing-repositories/cloning-a-repository)
2. In your terminal, run the following commands:
```
javac ThreadManager.java
java ThreadManager
```
3. In the directory you downloaded the repository to, you will find a file named "primes.txt", which will provide relevant information about the program such as program execution time, the total number of prime numbers found, the sum of all prime numbers found, and the
top ten maximum primes, listed in order from lowest to highest.

### Analysis

I chose the Segmented Sieve algorithm for this assignment simply because it was the first efficient algorithm for finding prime numbers that I found that also allowed you to set a range of prime numbers. Before using Segmented Sieve, I was using a naive algorithm to check if any given number is prime without memorization.

For multithreading, I split up the work into 8 different segments and assigned each segment to a thread. This was done using Java's Executor Service class. I also created an implementation where I managed my own threads, but my runtimes were slightly worse so I felt no need to reinvent the wheel and chose the Executor Service.

On top of this, I tested both the naive algorithm and the Segmented Sieve algorithm with both 0 and 8 threads. Those average runtimes can be found below.

### Average runtimes based on different implementations (These averages will differ depending on hardware)
- Naive PrimeFinder, 0 Threads - **125 seconds**
- Naive PrimeFinder, 8-Threads - **16 Seconds**
- Segmented Sieve PrimeFinder, 0 Threads - **32 Seconds**
- Segmented Sieve PrimeFinder, 8-Threads and other small optimizations - **0.7 seconds**