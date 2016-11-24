package com.ernest.ercustomview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 *  set four child to layout at four corner.
 *  1.marginlayoutparagrams:
 *  only use width, height, margins layouts. also rewrite three functions.
 *
 *  2.measureChildren(int, int):
 *  measure children's info
 *
 *  3.childView.layout(cl, ct, cr, cb):
 *  set every child's layout position.
 */
public class ErFourCornerContainer extends ViewGroup
{

	private static final String TAG = "ErFourCornerContainer";

	public ErFourCornerContainer(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public ErFourCornerContainer(Context context)
	{
		super(context);
	}

	public ErFourCornerContainer(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	/**
	 *  measure children's width and height,then use them to set self's width and height.
	 * @param widthMeasureSpec
	 * @param heightMeasureSpec
     */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		// get parent's suggest width and height info
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

		Log.e(TAG, (heightMode == MeasureSpec.UNSPECIFIED) + "," + sizeHeight
				+ "," + getLayoutParams().height);

		// measure all child's info
		measureChildren(widthMeasureSpec, heightMeasureSpec);

		//only when widthMode==layout.wrap_content, use child's width info
		int width = 0;
		int height = 0;

		int cCount = getChildCount();

		int cWidth = 0;
		int cHeight = 0;
		MarginLayoutParams cParams = null;

		// use to record left two view's heights sum
		int lHeight = 0;
		// use to record right two view's heghts sum
		int rHeight = 0;

		// use to record top two view's width's sum
		int tWidth = 0;
		// use to record bottom two views's width's sum
		int bWidth = 0;

		/**
		 * account to children's info,set self's info, only when self's mode is wrap_content
		 */
		for (int i = 0; i < cCount; i++)
		{
			View childView = getChildAt(i);
			cWidth = childView.getMeasuredWidth();
			cHeight = childView.getMeasuredHeight();
			cParams = (MarginLayoutParams) childView.getLayoutParams();

			// two top views
			if (i == 0 || i == 1)
			{
				tWidth += cWidth + cParams.leftMargin + cParams.rightMargin;
			}

			if (i == 2 || i == 3)
			{
				bWidth += cWidth + cParams.leftMargin + cParams.rightMargin;
			}

			if (i == 0 || i == 2)
			{
				lHeight += cHeight + cParams.topMargin + cParams.bottomMargin;
			}

			if (i == 1 || i == 3)
			{
				rHeight += cHeight + cParams.topMargin + cParams.bottomMargin;
			}

		}
		
		width = Math.max(tWidth, bWidth);
		height = Math.max(lHeight, rHeight);

		//only when wrap_content,use we caculate values;or use parent set valuse
		setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? sizeWidth
				: width, (heightMode == MeasureSpec.EXACTLY) ? sizeHeight
				: height);
	}

	// abstract method in viewgroup
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		int cCount = getChildCount();
		int cWidth = 0;
		int cHeight = 0;
		MarginLayoutParams cParams = null;

		//go through children to set layout info
		for (int i = 0; i < cCount; i++)
		{
			View childView = getChildAt(i);
			cWidth = childView.getMeasuredWidth();
			cHeight = childView.getMeasuredHeight();
			cParams = (MarginLayoutParams) childView.getLayoutParams();

			int cl = 0, ct = 0, cr = 0, cb = 0;

			switch (i)
			{
			case 0:
				cl = cParams.leftMargin;
				ct = cParams.topMargin;
				break;
			case 1:
				cl = getWidth() - cWidth - cParams.leftMargin
						- cParams.rightMargin;
				ct = cParams.topMargin;

				break;
			case 2:
				cl = cParams.leftMargin;
				ct = getHeight() - cHeight - cParams.bottomMargin;
				break;
			case 3:
				cl = getWidth() - cWidth - cParams.leftMargin
						- cParams.rightMargin;
				ct = getHeight() - cHeight - cParams.bottomMargin;
				break;

			}
			cr = cl + cWidth;
			cb = cHeight + ct;
			childView.layout(cl, ct, cr, cb);
		}

	}


	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs)
	{
		return new MarginLayoutParams(getContext(), attrs);
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams()
	{
		Log.e(TAG, "generateDefaultLayoutParams");
		return new MarginLayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
	}

	@Override
	protected LayoutParams generateLayoutParams(
			LayoutParams p)
	{
		Log.e(TAG, "generateLayoutParams p");
		return new MarginLayoutParams(p);
	}

	/*
	 * if (heightMode == MeasureSpec.UNSPECIFIED)
		{
			int tmpHeight = 0 ;
			LayoutParams lp = getLayoutParams();
			if (lp.height == LayoutParams.MATCH_PARENT)
			{
				Rect outRect = new Rect();
				getWindowVisibleDisplayFrame(outRect);
				tmpHeight = outRect.height();
			}else
			{
				tmpHeight = getLayoutParams().height ; 
			}
			height = Math.max(height, tmpHeight);

		}
	 */
}
