package uoc.edu.jsanchezmend.tfg.ytct.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * An utility class to work with date formats
 * 
 * @author jsanchezmend
 *
 */
public class DateUtil {
	private static final String dateFormat = "dd/mm/yyyy";
	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		
	public static Date toDate(String strDate) throws ParseException {
		return simpleDateFormat.parse(strDate);	
	}
	
	public static String toString(Date date) {
		return simpleDateFormat.format(date);
	}

}
