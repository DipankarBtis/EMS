package com.etrm.fms.util;

import java.io.FileInputStream;
import java.io.IOException;

/*<!--Added By : Harsh Maheta 20230804  --> */

public class ExcelVersionDtl {
	
	public static final byte[] OLE2_MASTER_NUMBER = new byte[] {(byte) 0xD0, (byte) 0xCF, (byte) 0x11, (byte) 0xE0,
            (byte) 0xA1, (byte) 0xB1, (byte) 0x1A, (byte) 0xE1};

	public static final byte[] OOXML_MASTER_NUMBER = new byte[] {(byte) 0x50, (byte) 0x4B, (byte) 0x03, (byte) 0x04};

	public static boolean isMasterNumberMatch(String filePath, byte[] masterNumber) throws IOException 
	{
        try (FileInputStream fileStream = new FileInputStream(filePath))
        {
            byte[] buffer = new byte[masterNumber.length];
            int bytesRead = fileStream.read(buffer);
            return bytesRead == masterNumber.length && areArraysEqual(buffer, masterNumber);
        }
    }
	
	public static boolean areArraysEqual(byte[] array1, byte[] array2) 
	{
        if (array1.length != array2.length) 
        {
            return false;
        }
        for (int i = 0; i < array1.length; i++) 
        {
            if (array1[i] != array2[i]) 
            {
                return false;
            }
        }
        return true;
    }
}
