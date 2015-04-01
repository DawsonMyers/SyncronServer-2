package fx.guice;/**
 * Created by Dawson on 3/30/2015.
 */


import ca.syncron.network.message.Message;

//SampleController
public class GuiceFxController {
	// Example Bean
	public class Person {
		private String firstName = "Richard";

		public final String getFirstName() { return firstName; }
	}

	/**
	 * This domain data model is supplied in the constructor
	 */
	private final Message person;

	/**
	 * Notice this constructor is using JSR 330 Inject annotation,
	 * which makes it "Guice Friendly".
	 *
	 * @param person
	 */
	//@Inject
	public GuiceFxController(Message person) {
		this.person = person;
	}

	/**
	 * Used within the FXML document. This is just some state which the
	 * controller wants to expose for the view to bind to or use.
	 *
	 * @return
	 */
//	public final String getPersonName() {
//		return person.getFirstName();
//	}
//
//	/**
//	 * Some method which I will call from the FXML file.
//	 */
//	public void print() {
//		System.out.println("Well done, " + getPersonName() + "!");
//	}
}