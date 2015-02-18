package com.travis.rhinofit.customs;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.travis.rhinofit.utils.EmailValidator;

public class CustomEditText extends EditText {
	public static final int DEFAULT = 0;
	public static final int EMAIL = 1;
	
	private int _mInputType = DEFAULT;
	
	public CustomEditText(Context context) {
		super(context);
		init();
	}

	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
        // set your input filter here
		this.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				String value = ((EditText)v).getText().toString();
				if ( hasFocus == true ) {
					v.setActivated(false);
				}
				else if ( value.isEmpty() || (_mInputType == EMAIL && !CustomEditText.isEmailValid(value)) ) {
					v.setActivated(true);
				}
			}
		});
	}
	
	public void setType(int inputType) {
		_mInputType = inputType;
	}

	public boolean isValidInput() {
		String value = getText().toString();
		if ( value.isEmpty() || (_mInputType == EMAIL && !CustomEditText.isEmailValid(value)) ) {
			setActivated(true);
			return false;
		}
		else {
			setActivated(false);
			return true;
		}
	}
	/**
	 * method is used for checking valid email id format.
	 * 
	 * @param email
	 * @return boolean true for valid false for invalid
	 */
	public static boolean isEmailValid(String email) {
	    return (new EmailValidator()).validate(email);
	}
}
