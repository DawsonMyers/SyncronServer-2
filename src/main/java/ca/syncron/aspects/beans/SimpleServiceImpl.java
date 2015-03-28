package ca.syncron.aspects.beans;

import org.slf4j.LoggerFactory;

/**
 * Created by Dawson on 3/26/2015.
 */
public class SimpleServiceImpl {
	static              String           nameId = SimpleServiceImpl.class.getSimpleName();
	public final static org.slf4j.Logger log    = LoggerFactory.getLogger(nameId);

	public SimpleServiceImpl() {}

//	public interface SimpleService {
//
//		public void printNameId();
//
//		public void checkName();
//
//		public String sayHello(String message);
//
//	}

	private String name;

	private int id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void printNameId() {
		System.out.println("SimpleService : Method printNameId() : My name is " + name
				                   + " and my id is " + id);
	}

	public void checkName() {
		if (name.length() < 20) {
			throw new IllegalArgumentException();
		}
	}

	public String sayHello(String message) {
		System.out.println("SimpleService : Method sayHello() : Hello! " + message);
		return message;
	}
}
