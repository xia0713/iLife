package com.szgs.bbs.answer;

import com.szgs.bbs.R;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.common.util.SharedPranceUtils;
import com.szgs.bbs.mine.NotificationActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AnswerFragment extends Fragment implements OnClickListener {

	private Context context;

	private TextView tv_title;
	private TextView top_left_tv;
	private LinearLayout answer_zxwt, answer_wgxqdwt, answer_gfxs, answer_wdrw,
			answer_phb, answer_jfdh;

	private ImageView home_notification_point;

	public AnswerFragment(Context context) {
		super();
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_answer, null);
		// header
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		top_left_tv = (TextView) view.findViewById(R.id.top_left_tv);
		top_left_tv
				.setBackgroundResource(R.drawable.navbar_notification_selector);
		top_left_tv.setOnClickListener(this);
		tv_title.setText("等你答");
		home_notification_point=(ImageView)view.findViewById(R.id.home_notification_point);

		answer_zxwt = (LinearLayout) view.findViewById(R.id.answer_zxwt);
		answer_wgxqdwt = (LinearLayout) view.findViewById(R.id.answer_wgxqdwt);
		answer_gfxs = (LinearLayout) view.findViewById(R.id.answer_gfxs);
		answer_wdrw = (LinearLayout) view.findViewById(R.id.answer_wdrw);
		answer_phb = (LinearLayout) view.findViewById(R.id.answer_phb);
		answer_jfdh = (LinearLayout) view.findViewById(R.id.answer_jfdh);
		answer_zxwt.setOnClickListener(this);
		answer_wgxqdwt.setOnClickListener(this);
		answer_gfxs.setOnClickListener(this);
		answer_wdrw.setOnClickListener(this);
		answer_phb.setOnClickListener(this);
		answer_jfdh.setOnClickListener(this);
		IntentFilter filter = new IntentFilter();
		filter.addAction("action.refresh");
		context.registerReceiver(mRefreshBroadcastReceiver, filter);
		return view;
	}
    @Override
    public void onResume() {
    	if (SharedPranceUtils.GetBolDate("hasnewmsg", context, false)) {
			home_notification_point.setVisibility(View.VISIBLE);
		} else {
			home_notification_point.setVisibility(View.GONE);
		}
    	super.onResume();
    }
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		/** 最新问题 */
		case R.id.answer_zxwt:
			LggsUtils.StartIntent(context, TheLatestQuestionsActivity.class);
			break;
		/** 我感谢兴趣的问题 */
		case R.id.answer_wgxqdwt:
			//
			LggsUtils.StartIntent(context, MyInterestingQuestionActivity.class);
			break;
		/** 高分悬赏 */
		case R.id.answer_gfxs:
			LggsUtils.StartIntent(context, HighMarkForActivity.class);
			break;
		/** 我的任务 */
		case R.id.answer_wdrw:
			LggsUtils.StartIntent(context, MineTaskActivity.class);
			break;
		/** 排行榜 */
		case R.id.answer_phb:
			LggsUtils.StartIntent(context, RankListActivity.class);
			break;
		/** 积分兑换 */
		case R.id.answer_jfdh:
			LggsUtils.StartIntent(context, CreditExchangeActivity.class);
			break;
		/** 点击右部小铃铛 */
		case R.id.top_left_tv:
			LggsUtils.StartIntent(context, NotificationActivity.class);
			break;
		default:
			break;
		}

	}

	private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("action.refresh")) {
				if (SharedPranceUtils.GetBolDate("hasnewmsg", context, false)) {
					home_notification_point.setVisibility(View.VISIBLE);
				}
			}
		}
	};
	
//	@Override
//	public void onResume() {
//		if (SharedPranceUtils.GetBolDate("hasnewmsg", context, false)) {
//			home_notification_point.setVisibility(View.VISIBLE);
//		} else {
//			home_notification_point.setVisibility(View.GONE);
//		}
//	};
}
