package com.travis.rhinofit.customs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

@SuppressLint("NewApi")
public class CustomImageButton extends ImageButton {

	public CustomImageButton(Context context) {
		super(context);
		init();
	}

	public CustomImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CustomImageButton(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@SuppressLint("ClickableViewAccessibility") 
	public void init() {
		this.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if ( event.getAction() == MotionEvent.ACTION_UP ) {
					setBackgroundColor(Color.parseColor("#00000000"));
					setAlpha(1.0f);
				}
				else {
					setBackgroundColor(Color.parseColor("#4396c6"));
				}
				return false;
			}
		});
	}
}
