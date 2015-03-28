/**
 Copyright 2013 Luciano Zu project Ardulink http://www.ardulink.org/

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 @author Luciano Zu
 */

package ca.syncron.gui.netbeans;

import org.zu.ardulink.Link;
import org.zu.ardulink.event.DigitalReadChangeEvent;
import org.zu.ardulink.event.DigitalReadChangeListener;
import org.zu.ardulink.gui.DigitalPinStatus;
import org.zu.ardulink.gui.Linkable;
import org.zu.ardulink.gui.facility.UtilityModel;
import org.zu.ardulink.protocol.ReplyMessageCallback;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * [ardulinktitle] [ardulinkversion]
 * This class implements the DigitalReadChangeListener interface and is able to listen
 * events coming from arduino board about digital pin state change.
 *
 * @author Luciano Zu project Ardulink http://www.ardulink.org/
 * @DigitalReadChangeListener [adsense]
 */
public class DigitalPinStatusTester extends JPanel implements DigitalReadChangeListener, Linkable {

	/**
	 *
	 */
	private static final long serialVersionUID = -7773514191770737231L;

	static void setRegistrar(ArrayList<DigitalPinStatusTester> pins) {
		mPins = pins;
	}

	private JLabel        lblStatelabel;
	private JToggleButton tglbtnSensor;
	private JComboBox     pinComboBox;
	private JLabel        lblPin;

	private Link link = Link.getDefaultInstance();

	private static final String HIGH = "High";
	private static final String LOW  = "Low";

	private static final String HIGH_ICON_NAME = "/org/zu/ardulink/gui/icons/blue-on-32.png";
	private static final String LOW_ICON_NAME  = "/org/zu/ardulink/gui/icons/blue-off-32.png";

	private static final ImageIcon HIGH_ICON = new ImageIcon(DigitalPinStatus.class.getResource(HIGH_ICON_NAME));
	private static final ImageIcon LOW_ICON  = new ImageIcon(DigitalPinStatus.class.getResource(LOW_ICON_NAME));
	private       JPanel                            comboPanel;
	public static ArrayList<DigitalPinStatusTester> mPins;
	private int     mPinId   = -2;
	public  boolean pinState = false;

	/**
	 * Create the panel.
	 */
	public DigitalPinStatusTester() {
		registerPin();
		setLayout(new GridLayout(3, 1, 0, 0));

		lblStatelabel = new JLabel(LOW);
		lblStatelabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatelabel.setIcon(LOW_ICON);
		lblStatelabel.setEnabled(false);
		add(lblStatelabel);

		comboPanel = new JPanel();
		add(comboPanel);

		lblPin = new JLabel("Pin:");
		comboPanel.add(lblPin);

		pinComboBox = new JComboBox();
		comboPanel.add(pinComboBox);
		pinComboBox.setModel(new DefaultComboBoxModel(UtilityModel.generateModelForCombo(2, 40)));

		tglbtnSensor = new JToggleButton("Sensor off");
		tglbtnSensor.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					link.addDigitalReadChangeListener((DigitalReadChangeListener) tglbtnSensor.getParent());

					tglbtnSensor.setText("Sensor on");
					pinComboBox.setEnabled(false);

					lblStatelabel.setEnabled(true);

				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					link.removeDigitalReadChangeListener((DigitalReadChangeListener) tglbtnSensor.getParent());

					tglbtnSensor.setText("Sensor off");
					pinComboBox.setEnabled(true);

					lblStatelabel.setEnabled(false);
				}
			}
		});
		add(tglbtnSensor);
	}

	public void setState(boolean b) {
		pinState = b;
		System.out.println("Pin " + getPinId() + " Toggled to:" + ((b) ? "True" : "False"));
		lblStatelabel.setEnabled(b);
		lblStatelabel.setIcon(b ? HIGH_ICON : LOW_ICON);
	}

	public void setPinId(int pin) {
		mPinId = pin;
	}

	public int getPinId() {
		return mPinId;
	}

	public boolean getState() {
		return lblStatelabel.isEnabled();
	}

	private void registerPin() {
		mPins.add(this);
		setPinId(mPins.size() + 2);
		System.out.println("Pin " + mPins.size() + " registered");
	}

	public boolean digitalPin(boolean state) {
		setState(state);
		return getState();
	}

	public boolean digitalPin() {
		return getState();
	}

	public boolean toggle() {
		setState(getState() ? false : true);
		return getState();
	}


	@Override
	public void stateChanged(DigitalReadChangeEvent e) {
		int value = e.getValue();
		if (value == DigitalReadChangeEvent.POWER_HIGH) {
			lblStatelabel.setText(HIGH);
			lblStatelabel.setIcon(HIGH_ICON);
		} else if (value == DigitalReadChangeEvent.POWER_LOW) {
			lblStatelabel.setText(LOW);
			lblStatelabel.setIcon(LOW_ICON);
		}
	}

	@Override
	public int getPinListening() {
		return Integer.parseInt((String) pinComboBox.getSelectedItem());
	}

	public void setPin(int pin) {

		pinComboBox.setSelectedItem("" + pin);
	}

	public void setLink(Link link) {
		if (this.link != null) {
			this.link.removeDigitalReadChangeListener((DigitalReadChangeListener) tglbtnSensor.getParent());
		}
		tglbtnSensor.setText("Sensor off");
		pinComboBox.setEnabled(true);

		lblStatelabel.setEnabled(false);
		this.link = link;
	}

	public ReplyMessageCallback getReplyMessageCallback() {
		throw new RuntimeException("Not developed yet");
	}

	public void setReplyMessageCallback(ReplyMessageCallback replyMessageCallback) {
		throw new RuntimeException("Not developed yet");
	}

}
