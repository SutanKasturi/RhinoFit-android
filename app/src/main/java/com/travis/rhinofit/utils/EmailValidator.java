package com.travis.rhinofit.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {
	 
	private Pattern pattern;
	private Matcher matcher;
 
	private static final String EMAIL_PATTERN = 
		"^(([w-]+.)+[w-]+|([a-zA-Z]{1}|[w-]{2,}))@"
    	    +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9]).([0-1]?"
    	    +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])."
    	    +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9]).([0-1]?"
    	    +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
    	    +"([a-zA-Z]+[w-]+.)+[a-zA-Z]{2,4})$";
 
	public EmailValidator() {
		pattern = Pattern.compile(EMAIL_PATTERN);
	}
 
	/**
	 * Validate hex with regular expression
	 * 
	 * @param hex
	 *            hex for validation
	 * @return true valid hex, false invalid hex
	 */
	public boolean validate(final String hex) {
 
		matcher = pattern.matcher(hex);
		return matcher.matches();
 
	}
}