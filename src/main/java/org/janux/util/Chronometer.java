package org.janux.util;

import java.text.DecimalFormat;

/**
 * Provides the ability to record and output the time elapsed in different areas of the code, 
 * and start/stop the timer at arbitrary points in the code; general useage is:
 * <pre>
 * Chronometer timer = new Chronometer();
 * timer.start();
 * // or, alternatively:
 * // Chronometer timer = new Chronometer(true);
 * [...]
 * log.info(N + " widgets retrieved in " + timer.getElapsedTime() + " secs");
 * </pre>
 *
 * @author <a href="mailto:kevin.irlen@janux.org">Kevin Irlen</a>
 */
public class Chronometer
{
	/** default format used to format the time elapsed; the string '###.##' */
	public static final String DEFAULT_FORMAT_STRING = "###.###s";
	
	long lastStart = 0;
	long elapsedTime = 0;
	boolean isRunning;
	String formatString = DEFAULT_FORMAT_STRING;
	DecimalFormat decimalFormat;


	/** 
	 * plain vanilla constructor that returns a chronometer with its clock started, 
	 * and returns the time elapsed using the default format
	 */
	public Chronometer()
	{
		this(true, null);
	}

	/** 
	 * Constructor that provides the ability to instantiate and return a Chronometer with its clock
	 * stopped, and returns the time elapsed using the default format
	 */
	public Chronometer(boolean start)
	{
		this(start, null);
	}

	/** 
	 * Constructor that provides the ability to instantiate and start a Chronometer in one fell swoop, 
	 * and specify a formatting string to format the time elapsed
	 *
	 * @param start if true, start the timer when the Chronometer is instantiated
	 *
	 */
	public Chronometer(boolean start, String aFormatString)
	{
		if (aFormatString != null) this.formatString = aFormatString;
		this.zero();
		if (start) this.start();
	}

	
	/** returns the string used to format the elapsed time */
	public String getFormatString() {
		return this.formatString;
	}


	/** The decimal format object used to format the elapsed time */
	DecimalFormat getDecimalFormat() 
	{
		if (decimalFormat == null)
			decimalFormat = new DecimalFormat (this.formatString);

		return decimalFormat;
	}


	/**
	 * Start/resume the clock
	 *
	 */
	public void start()
	{
		if (!isRunning)
		{
			lastStart = System.currentTimeMillis();
			isRunning = true;
		}
	}
	
	/**
	 * Stop the clock. If we're running, capture the latest elapsed time
	 *
	 */
	public void stop()
	{
		if (isRunning)
		{
			long newElapsedTime = System.currentTimeMillis() - lastStart;
			elapsedTime += newElapsedTime;

			isRunning = false;
		}
	}

	/**
	 * Zero the clock. Forget the elapsed time.
	 *
	 */
	public void zero()
	{
		lastStart = 0;
		elapsedTime = 0;
		isRunning = false;
	}

	/** Report the elapsed time as a numeric amount */
	public long getElapsedTime()
	{
		long currElapsedTime = elapsedTime;
		
		if (isRunning)
		{
			long newElapsedTime = System.currentTimeMillis() - lastStart;
			currElapsedTime += newElapsedTime;
		}

		return currElapsedTime;
	}

	/**
	 * Report the elapsed time as a String using the format defined at object instantiation; 
	 * if running, make sure to add the going increment.
	 */
	public String printElapsedTime()
	{
		return this.printElapsedTime(null);
	}

	/** 
	 * Provides the ability to override the format of the elapsed time, on this
	 * invocation; calling getElapsedTime subsequently will use the format string
	 * that was specified at class instantiation
	 *
	 * @see DecimalFormat
	 */
	public String printElapsedTime(String aFormatString)
	{
		double seconds = this.getElapsedTime() / 1000.0;

		if (aFormatString != null)
			this.getDecimalFormat().applyPattern(aFormatString);

		String secondsStr = this.getDecimalFormat().format(seconds);

		// return to the pattern defined at object creation
		this.getDecimalFormat().applyPattern(this.formatString);
		
		return secondsStr;
	}
}
