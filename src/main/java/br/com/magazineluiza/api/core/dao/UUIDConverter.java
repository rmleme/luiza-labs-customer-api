package br.com.magazineluiza.api.core.dao;

import javax.xml.bind.DatatypeConverter;

public class UUIDConverter {

	public static String convertBinaryToUUID(byte[] binaryValue) {
		String uuidValue = DatatypeConverter.printHexBinary(binaryValue).toLowerCase();
		return uuidValue.substring(0, 8) + "-" + uuidValue.substring(8, 12) + "-" + uuidValue.substring(12, 16) + "-"
				+ uuidValue.substring(16, 20) + "-" + uuidValue.substring(20);
	}

	public static byte[] convertUUIDToBinary(String uuidValue) {
		return DatatypeConverter.parseHexBinary(uuidValue.replace("-", ""));
	}
}