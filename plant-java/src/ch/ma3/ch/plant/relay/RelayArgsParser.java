package ch.ma3.ch.plant.relay;

import com.phidgets.PhidgetException;

public class RelayArgsParser {

	public static void parseArgs(Relay relay, String[] args)
			throws PhidgetException {
		if (args.length == 0) {
			System.out
					.println("All switches are off. You can set the parameter p. eg: -p 1010");
		}
		searchForParameter(relay, args);
	}

	private static void searchForParameter(Relay relay, String[] args)
			throws PhidgetException {
		for (int i = 0; i < args.length; i++) {
			if (pParamWithCorrectValue(args, i)) {
				setRelayValues(relay, args[i + 1]);
			}
		}
	}

	private static boolean pParamWithCorrectValue(String[] args, int i) {
		return args[i].equals("-p") && (args.length >= i + 1);
	}

	private static void setRelayValues(Relay relay, String bits)
			throws PhidgetException {
		for (int j = 0; j < 4; j++) {
			if (bits.charAt(j) == '1') {
				System.out.println("Turning switch " + j + " on.");
				relay.setSwitch(j, true);
			}
		}
	}
}
