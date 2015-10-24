package com.itheima.demo;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {

	private View view;
	private ArrayList<String> list;
	private ListView lv;
	private Context context;

	public ListAdapter(ArrayList<String> list, ListView lv, Context context) {
		this.list = list;
		this.lv = lv;
		this.context = context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		view = View.inflate(context, R.layout.lv_item, null);

		TextView tv = (TextView) view.findViewById(R.id.item_tv);
		Button btn_up = (Button) view.findViewById(R.id.btn_up);
		Button btn_down = (Button) view.findViewById(R.id.btn_down);
		btn_up.setOnClickListener(new MyOnClickListener(position, true));
		btn_down.setOnClickListener(new MyOnClickListener(position, false));

		tv.setText(list.get(position));

		return view;
	}

	/*
	 * 交互数据在集合中的位置
	 */
	private void swap(int pre, int now) {
		String prestr = list.get(pre);
		String nowstr = list.get(now);
		list.set(now, prestr);
		list.set(pre, nowstr);

	}

	class MyOnClickListener implements OnClickListener {

		int position = 0;
		boolean isUP = false;//根据传入参数判断是此时操作是up还是down

		public MyOnClickListener(int position, boolean isUP) {
			this.position = position;
			this.isUP = isUP;
		}

		@Override
		public void onClick(View v) {
			if (position == 0 && isUP)
				return;
			if (position == list.size() - 1 && !isUP)
				return;
			int firstPosition = lv.getFirstVisiblePosition();// 获取当前可视第一个条目位置

			/**
			 * 以为滚动后获取条目的高会大于屏幕 需要通过减去当前可视第一个条目的高度得到相对屏幕的高
			 */

			final View preView;
			if (isUP) {
				preView = lv.getChildAt(position - 1 - firstPosition);// 前一个item
			} else {
				preView = lv.getChildAt(position + 1 - firstPosition);// 前一个item
			}
			final int pretop = preView.getTop();
			final View nowView = lv.getChildAt(position - firstPosition);// 当前item
			final int nowtop = nowView.getTop();

			ValueAnimator animator = ValueAnimator.ofFloat(nowtop, pretop);
			animator.addUpdateListener(new AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					float fraction = animation.getAnimatedFraction();

					int value = (int) ((nowtop - pretop) * fraction);// 移动的差值
					int r = preView.getRight();
					// 回调item对象的layout方法
					preView.layout(0, pretop + value, r, preView.getBottom()
							+ value);
					nowView.layout(0, nowtop - value, r, nowView.getBottom()
							- value);
				}
			});
			animator.setDuration(800);
			animator.start();

			// 点击动画监听
			animator.addListener(new AnimatorListener() {

				@Override
				public void onAnimationStart(Animator animation) {
				}

				@Override
				public void onAnimationRepeat(Animator animation) {
				}

				@Override
				public void onAnimationEnd(Animator animation) {
					// 动画结束后改变数据位置并刷新
					if (isUP) {
						swap(position - 1, position);
					} else {
						swap(position + 1, position);
					}
					notifyDataSetChanged();
				}

				@Override
				public void onAnimationCancel(Animator animation) {
				}
			});
		}

	}
}
