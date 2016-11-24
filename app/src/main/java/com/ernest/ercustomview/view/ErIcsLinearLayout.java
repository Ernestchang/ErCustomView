package com.ernest.ercustomview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 *	Due to above sdk3.0,the LinearLayout get new porperties,such as divider,showDividers ect.
 *	For compatible to below sdk3.0, this class is created.
 *	1. as you known, you can add new properties to strong system's view.
 *
 */
public class ErIcsLinearLayout extends LinearLayout
{
	// omit styleable, byself
	private static final int[] LL = new int[]
	{ //
		android.R.attr.divider,//
			android.R.attr.showDividers,//
			android.R.attr.dividerPadding //
	};

	private static final int LL_DIVIDER = 0;
	private static final int LL_SHOW_DIVIDER = 1;
	private static final int LL_DIVIDER_PADDING = 2;

	/**
	 * android:dividers
	 */
	private Drawable mDivider;
	/**
	 * android:showDividers
	 */
	private int mShowDividers;
	/**
	 * android:dividerPadding
	 */
	private int mDividerPadding;

	private int mDividerWidth;
	private int mDividerHeight;

	public ErIcsLinearLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs, LL);
		setDividerDrawable(a.getDrawable(ErIcsLinearLayout.LL_DIVIDER));
		mDividerPadding = a.getDimensionPixelSize(LL_DIVIDER_PADDING, 0);
		mShowDividers = a.getInteger(LL_SHOW_DIVIDER, SHOW_DIVIDER_NONE);
		a.recycle();
	}
	
	// init divider width and height info
	public void setDividerDrawable(Drawable divider)
	{
		if (divider == mDivider)
		{
			return;
		}
		mDivider = divider;
		if (divider != null)
		{
			// get drable width and height
			mDividerWidth = divider.getIntrinsicWidth();
			mDividerHeight = divider.getIntrinsicHeight();
		} else
		{
			mDividerWidth = 0;
			mDividerHeight = 0;
		}
		setWillNotDraw(divider == null);
		requestLayout();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		//transform divider to margin
		setChildrenDivider();
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}


	protected void setChildrenDivider()
	{
		final int count = getChildCount();
		for (int i = 0; i < count; i++)
		{
			//go through every child
			View child = getChildAt(i);
			//get the position of the child in parent
			final int index = indexOfChild(child);
			//orientation
			final int orientation = getOrientation();
		
			final LayoutParams params = (LayoutParams) child.getLayoutParams();
			//fingure out whether should set divider
			if (hasDividerBeforeChildAt(index))
			{
				if (orientation == VERTICAL)
				{
					//save position to set divider
					params.topMargin = mDividerHeight;
				} else
				{
					//give the divider position to set
					params.leftMargin = mDividerWidth;
				}
			}
		}
	}
	
	// judge whether to set divider
	public boolean hasDividerBeforeChildAt(int childIndex)
	{
		if (childIndex == 0 || childIndex == getChildCount())
		{
			return false;
		}
		// showDividers == middle in layout
		if ((mShowDividers & SHOW_DIVIDER_MIDDLE) != 0)
		{
			boolean hasVisibleViewBefore = false;
			for (int i = childIndex - 1; i >= 0; i--)
			{
				// before this index has view to show
				if (getChildAt(i).getVisibility() != GONE)
				{
					hasVisibleViewBefore = true;
					break;
				}
			}
			return hasVisibleViewBefore;
		}
		return false;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{

		if (mDivider != null)
		{
			if (getOrientation() == VERTICAL)
			{
				//draw vertical divider
				drawDividersVertical(canvas);
			} else
			{
				//draw horizontal divider
				drawDividersHorizontal(canvas);
			}
		}
		super.onDraw(canvas);
	}

	/**
	 * draw horizontal divider
	 * @param canvas
	 */
	private void drawDividersHorizontal(Canvas canvas)
	{
		final int count = getChildCount();
		// go through all child views
		for (int i = 0; i < count; i++)
		{
			final View child = getChildAt(i);

			if (child != null && child.getVisibility() != GONE)
			{
				//if need to draw
				if (hasDividerBeforeChildAt(i))
				{
					final LayoutParams lp = (LayoutParams) child
							.getLayoutParams();
					// get the start position. through child.getLeft() get absolute x-coordination,
					// then minus the relative value lp.leftMargin which is equal to the divider's
					// width, last we get the absolute x-coordination of the divider left start.
					final int left = child.getLeft() - lp.leftMargin;
					//draw divider
					drawVerticalDivider(canvas, left);
				}
			}
		}
	}
	
	/**
	 * due to left start x-coordination,draw the divider
	 * @param canvas
	 * @param left
	 */
	public void drawVerticalDivider(Canvas canvas, int left)
	{
		//set the bounds of divider
		mDivider.setBounds(left, getPaddingTop() + mDividerPadding, left
				+ mDividerWidth, getHeight() - getPaddingBottom()
				- mDividerPadding);
		//draw
		mDivider.draw(canvas);
	}
	
	private void drawDividersVertical(Canvas canvas)
	{
		final int count = getChildCount();
		for (int i = 0; i < count; i++)
		{
			final View child = getChildAt(i);

			if (child != null && child.getVisibility() != GONE)
			{
				if (hasDividerBeforeChildAt(i))
				{
					final LayoutParams lp = (LayoutParams) child
							.getLayoutParams();
					final int top = child.getTop() - lp.topMargin/*
																 * -
																 * mDividerHeight
																 */;
					drawHorizontalDivider(canvas, top);
				}
			}
		}
	}
	private void drawHorizontalDivider(Canvas canvas, int top)
	{
		mDivider.setBounds(getPaddingLeft() + mDividerPadding, top, getWidth()
				- getPaddingRight() - mDividerPadding, top + mDividerHeight);
		mDivider.draw(canvas);
	}

	

	
}
