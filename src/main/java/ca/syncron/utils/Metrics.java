package ca.syncron.utils;

import org.slf4j.LoggerFactory;

/**
 * Created by Dawson on 3/14/2015.
 */
public class Metrics {
	static              String           nameId = Metrics.class.getSimpleName();
	public final static org.slf4j.Logger log    = LoggerFactory.getLogger(nameId);

	public Metrics() {}

	public static class Counter {
		public int count = 0;

		public Counter() {
		}

		public void inc() {
			count++;
		}

		public void dec() {
			count--;
		}

		public int getCount() {
			return count;
		}
	}

	public static class Timer {
		public long start = 0;
		public long time  = 0;

		public Timer() {
		}

		public void start() {
			start = System.currentTimeMillis();
		}

		public long end() {
			time = System.currentTimeMillis() - start;
			return time;
		}

		public void print() {
			System.out.println("time = " + time + "ms");
		}

		public String getTime() {
			return "time = " + time + "ms";
		}
	}

}
