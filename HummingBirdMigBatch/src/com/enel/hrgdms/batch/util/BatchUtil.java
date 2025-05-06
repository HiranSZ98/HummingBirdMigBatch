package com.enel.hrgdms.batch.util;

import org.apache.log4j.Logger;

public class BatchUtil {

	private static Logger log = Logger.getLogger(BatchUtil.class);


	public static boolean checkString(String s) {
		if (s != null && !s.isEmpty())
			return true;
		return false;
	}



}
