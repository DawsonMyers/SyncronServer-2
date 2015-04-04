package fx.tables;

/**
 * Created by Dawson on 4/3/2015.
 */

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyTableRow {

	private Object one;
	private Object two;
	private Object three;
	private Object four;
	private Object five;
	private Object six;

	public MyTableRow(Object... args) {
		Field[] fields = getClass().getDeclaredFields();
		int i = 0;
		for (Object arg : args) {
			try {
				fields[i++].set(this, arg);
			} catch (IllegalArgumentException | IllegalAccessException ex) {
				Logger.getLogger(MyTableRow.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	/**
	 * @return the one
	 */
	public Object getOne() {
		return one;
	}

	/**
	 * @return the two
	 */
	public Object getTwo() {
		return two;
	}

	/**
	 * @return the three
	 */
	public Object getThree() {
		return three;
	}

	/**
	 * @return the four
	 */
	public Object getFour() {
		return four;
	}

	/**
	 * @return the five
	 */
	public Object getFive() {
		return five;
	}

	/**
	 * @return the six
	 */
	public Object getSix() {
		return six;
	}
}
