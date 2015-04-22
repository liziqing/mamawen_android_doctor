package mark.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup
{

	private static final String TAG = "FlowLayout";


	public FlowLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	protected ViewGroup.LayoutParams generateLayoutParams(
			ViewGroup.LayoutParams p)
	{
		return new MarginLayoutParams(p);
	}

	@Override
	public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs)
	{
		return new MarginLayoutParams(getContext(), attrs);
	}

	@Override
	protected ViewGroup.LayoutParams generateDefaultLayoutParams()
	{
		return new MarginLayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
	}

	/**
	 * 负责设置子控件的测量模式和大?? 根据??有子控件设置自己的宽和高
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 获得它的父容器为它设置的测量模式和大??
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
		int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
		int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

		Log.e(TAG, sizeWidth + "," + sizeHeight);

		// 如果是warp_content情况下，记录宽和??
		int width = 0;
		int height = 0;
		/**
		 * 记录每一行的宽度，width不断取最大宽??
		 */
		int lineWidth = 0;
		/**
		 * 每一行的高度，累加至height
		 */
		int lineHeight = 0;

		int cCount = getChildCount();

		// 遍历每个子元??
		for (int i = 0; i < cCount; i++)
		{
			View child = getChildAt(i);
			// 测量每一个child的宽和高
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
			// 得到child的lp
			MarginLayoutParams lp = (MarginLayoutParams) child
					.getLayoutParams();
			// 当前子空间实际占据的宽度
			int childWidth = child.getMeasuredWidth() + lp.leftMargin
					+ lp.rightMargin;
			// 当前子空间实际占据的高度
			int childHeight = child.getMeasuredHeight() + lp.topMargin
					+ lp.bottomMargin;
			/**
			 * 如果加入当前child，则超出??大宽度，则的到目前最大宽度给width，类加height 然后??启新??
			 */
			if (lineWidth + childWidth > sizeWidth)
			{
				width = Math.max(lineWidth, childWidth);// 取最大的
				lineWidth = childWidth; // 重新??启新行，??始记??
				// 叠加当前高度??
				height += lineHeight;
				// ??启记录下??行的高度
				lineHeight = childHeight;
			} else
			// 否则累加值lineWidth,lineHeight取最大高??
			{
				lineWidth += childWidth;
				lineHeight = Math.max(lineHeight, childHeight);
			}
			// 如果是最后一个，则将当前记录的最大宽度和当前lineWidth做比??
			if (i == cCount - 1)
			{
				width = Math.max(width, lineWidth);
				height += lineHeight;
			}

		}
		setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth
				: width, (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight
				: height);

	}
	/**
	 * 存储??有的View，按行记??
	 */
	private List<List<View>> mAllViews = new ArrayList<List<View>>();
	/**
	 * 记录每一行的??大高??
	 */
	private List<Integer> mLineHeight = new ArrayList<Integer>();
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		mAllViews.clear();
		mLineHeight.clear();

		int width = getWidth();

		int lineWidth = 0;
		int lineHeight = 0;
		// 存储每一行所有的childView
		List<View> lineViews = new ArrayList<View>();
		int cCount = getChildCount();
		// 遍历??有的孩子
		for (int i = 0; i < cCount; i++)
		{
			View child = getChildAt(i);
			MarginLayoutParams lp = (MarginLayoutParams) child
					.getLayoutParams();
			int childWidth = child.getMeasuredWidth();
			int childHeight = child.getMeasuredHeight();

			// 如果已经??要换??
			if (childWidth + lp.leftMargin + lp.rightMargin + lineWidth > width)
			{
				// 记录这一行所有的View以及??大高??
				mLineHeight.add(lineHeight);
				// 将当前行的childView保存，然后开启新的ArrayList保存下一行的childView
				mAllViews.add(lineViews);
				lineWidth = 0;// 重置行宽
				lineViews = new ArrayList<View>();
			}
			/**
			 * 如果不需要换行，则累??
			 */
			lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
			lineHeight = Math.max(lineHeight, childHeight + lp.topMargin
					+ lp.bottomMargin);
			lineViews.add(child);
		}
		// 记录??后一??
		mLineHeight.add(lineHeight);
		mAllViews.add(lineViews);

		int left = 0;
		int top = 0;
		// 得到总行??
		int lineNums = mAllViews.size();
		for (int i = 0; i < lineNums; i++)
		{
			// 每一行的??有的views
			lineViews = mAllViews.get(i);
			// 当前行的??大高??
			lineHeight = mLineHeight.get(i);

			Log.e(TAG, "??" + i + "?? ??" + lineViews.size() + " , " + lineViews);
			Log.e(TAG, "??" + i + "行， ??" + lineHeight);

			// 遍历当前行所有的View
			for (int j = 0; j < lineViews.size(); j++)
			{
				View child = lineViews.get(j);
				if (child.getVisibility() == View.GONE)
				{
					continue;
				}
				MarginLayoutParams lp = (MarginLayoutParams) child
						.getLayoutParams();

				//计算childView的left,top,right,bottom
				int lc = left + lp.leftMargin;
				int tc = top + lp.topMargin;
				int rc =lc + child.getMeasuredWidth();
				int bc = tc + child.getMeasuredHeight();

				Log.e(TAG, child + " , l = " + lc + " , t = " + t + " , r ="
						+ rc + " , b = " + bc);

				child.layout(lc, tc, rc, bc);
				
				left += child.getMeasuredWidth() + lp.rightMargin
						+ lp.leftMargin;
			}
			left = 0;
			top += lineHeight;
		}

	}
}
