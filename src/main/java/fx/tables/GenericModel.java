package fx.tables;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Generic data model is a wrapper for ArrayList to easily populate tableViews
 * and other javafx controls. This class would be perfect if java reflection
 * supported magic getters and setters like php or if there was a nice way to
 * dynamically add them during runtime.
 */
public class GenericModel {

	/**
	 * The indexed list of objects
	 */
	private ArrayList data;

	/**
	 * Public constructor with variable number of parameters.
	 *
	 * @param args
	 */
	public GenericModel(Object... args) {
		data = new ArrayList<>(Arrays.asList(args));
	}

	public int size() {
		return data.size();
	}

	public Object get(int i) {
		return data.get(i);
	}

	public Object get0() {
		return data.get(0);
	}

	public Object get1() {
		return data.get(1);
	}

	public Object get2() {
		return data.get(2);
	}

	public Object get3() {
		return data.get(3);
	}

	public Object get4() {
		return data.get(4);
	}

	public Object get5() {
		return data.get(5);
	}
}