package de.gravitex.banking_core.util;

public class StringHelper {

	public static String seperateList(Object[] aObjects, String aCustomSeperator) {
        String separated = "";
        int counter = 0;
        for (Object s : aObjects) {
            separated += s;
            if (counter < aObjects.length - 1) {
                separated += aCustomSeperator != null ? aCustomSeperator : ",";
            }
            counter++;
        }
        return separated;
    }

	public static String createHash(String value) {
		byte[] tmp = value.getBytes();
		StringBuilder hexString = new StringBuilder(2 * tmp.length);
		for (int i = 0; i < tmp.length; i++) {
			String hex = Integer.toHexString(0xff & tmp[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}

	public static boolean isBlank(String value) {
		return value == null || value.isEmpty();
	}
}