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
	
	private static final String neo4JDateFormat = "yyyy-mm-dd";
	private static final SimpleDateFormat neo4JSimpleDateFormat = new SimpleDateFormat(neo4JDateFormat);
		
	public static Date toDate(String strDate) throws ParseException {
		return simpleDateFormat.parse(strDate);	
	}
	
	public static String toString(Date date) {
		return simpleDateFormat.format(date);
	}
	
	public static String toNeo4jString(String strDate) throws ParseException {
		final Date date = toDate(strDate);
		return neo4JSimpleDateFormat.format(date);
	}
	
	public static String toNeo4jString(Date date) {
		return neo4JSimpleDateFormat.format(date);
	}

}
