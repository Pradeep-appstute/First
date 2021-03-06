package com.masergy.iscControl.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.util.Patterns;

public class Validation {

	public static boolean isEmpty(String text) {
	    
	    return text.length()>0?false:true;
	}
	
	public static boolean validEmail(String email) {
	    Pattern pattern = Patterns.EMAIL_ADDRESS;
	    return pattern.matcher(email).matches();
	}
	
	public static boolean validText(String name)
	{
		Pattern ps = Pattern.compile("^[a-zA-Z0-9 ]+$");
        Matcher ms = ps.matcher(name);
        boolean bs = ms.matches();
        return bs;
	}
	
	public static boolean validOnlyText(String name)
	{
		Pattern ps = Pattern.compile("^[a-zA-Z]+$");
        Matcher ms = ps.matcher(name);
        boolean bs = ms.matches();
        return bs;
	}
	
	public static boolean validNumeric(String name)
	{
		Pattern ps = Pattern.compile("^[0-9]+$");
        Matcher ms = ps.matcher(name);
        boolean bs = ms.matches();
        return bs;
	}
}
