package com.huizhuang.zxsq.ui.adapter.foreman;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.foreman.CommentResult;
import com.huizhuang.zxsq.http.bean.foreman.ForemanComment;
import com.huizhuang.zxsq.http.task.foreman.ForemanCommentsDownTask;
import com.huizhuang.zxsq.http.task.foreman.ForemanCommentsUpTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.ImageUtil;
import com.huizhuang.zxsq.utils.LocalRestrictClicksUtil;
import com.huizhuang.zxsq.utils.ToastUtil;
import com.huizhuang.zxsq.widget.CircleImageView;

public class PublicCommentAdapter extends CommonBaseAdapter<ForemanComment>
        implements
            OnClickListener {

    private BaseActivity baseActivity;
    private final String mkeyCurrentTime;


    public PublicCommentAdapter(BaseActivity baseActivity) {
        super(baseActivity);
        this.baseActivity = baseActivity;
        mkeyCurrentTime = DateUtil.getStringDate("yyyyMMdd");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(baseActivity, R.layout.item_public_comment, null);

            ItemViewHolder holder = new ItemViewHolder();

            holder.head = (CircleImageView) convertView.findViewById(R.id.item_public_comment_head);
            holder.score = (RatingBar) convertView.findViewById(R.id.item_public_comment_score);
            holder.name = (TextView) convertView.findViewById(R.id.item_public_comment_name);
            holder.time = (TextView) convertView.findViewById(R.id.item_public_comment_time);
            holder.content = (TextView) convertView.findViewById(R.id.item_public_comment_content);
            holder.praise = (TextView) convertView.findViewById(R.id.item_public_comment_praise);
            holder.step = (TextView) convertView.findViewById(R.id.item_public_comment_step);

            holder.praise.setOnClickListener(PublicCommentAdapter.this);
            holder.step.setOnClickListener(PublicCommentAdapter.this);

            convertView.setTag(holder);
        }

        ItemViewHolder holder = (ItemViewHolder) convertView.getTag();
        ForemanComment foremanComment = getItem(position);

        if (foremanComment.getImage() != null && foremanComment.getImage().getThumb_path() != null) {
            ImageUtil.displayImage(getNotNullString(foremanComment.getImage().getThumb_path()),
                    holder.head, ImageLoaderOptions.getDefaultImageOption());
        }

        float score = 0f;
        if (foremanComment.getRank() != null) {
            try {
                score = Float.valueOf(foremanComment.getRank());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        holder.score.setProgress((int) (score * 2));
        holder.score.setMax(10);
        holder.name.setText(getNotNullString(foremanComment.getName()));
        if (!TextUtils.isEmpty(foremanComment.getTime())) {
            holder.time.setText(DateUtil.friendlyTime1(DateUtil.timestampToSdate(
                    foremanComment.getTime(), "yyyy-MM-dd HH:mm:ss")));
        }

        holder.content.setText(getNotNullString(foremanComment.getContent()));
        holder.praise.setText("有用（" + foremanComment.getUseful_count() + "）");
        holder.step.setText("踩（" + foremanComment.getUseless_count() + "）");
        holder.praise.setTag(position);
        holder.step.setTag(position);
        return convertView;
    }

    private String getNotNullString(String str) {
        if (str == null) {
            str = "";
        }
        return str.trim();
    }

    class ItemViewHolder {
        public CircleImageView head;
        public RatingBar score;
        public TextView name;
        public TextView time;
        public TextView content;
        public TextView praise;
        public TextView step;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_public_comment_praise:
                commentsUp(v);
                break;
            case R.id.item_public_comment_step:
                commentsDown(v);
                break;
            default:
                break;
        }
    }

    private void commentsUp(View v) {
        final int position = (Integer) v.getTag();
        final TextView text = (TextView) v;
        final ForemanComment comment = getItem(position);
        if (!LocalRestrictClicksUtil.getInstance().isUserCanClick(mkeyCurrentTime,
                comment.getId() + "")) {
            ToastUtil.showMessage(mContext, "不能重复点击");
            return;
        }
        baseActivity.showWaitDialog("请稍后...");
        ForemanCommentsUpTask task = new ForemanCommentsUpTask(baseActivity, comment.getId() + "");
        task.setCallBack(new AbstractHttpResponseHandler<CommentResult>() {

            @Override
            public void onSuccess(CommentResult t) {
                if (t != null) {
                    int pos = (Integer) text.getTag();
                    if (position == pos) {
                        text.setText("有用（" + t.getUp_num() + "）");
                    }
                }
                baseActivity.hideWaitDialog();
                LocalRestrictClicksUtil.getInstance().setUserClickStateDiseable(mkeyCurrentTime,
                        comment.getId() + "");
            }

            @Override
            public void onFailure(int code, String error) {
                baseActivity.showToastMsg(error);
                baseActivity.hideWaitDialog();
            }
        });
        task.send();

    }

    private void commentsDown(View v) {
        final int position = (Integer) v.getTag();
        final TextView text = (TextView) v;
        final ForemanComment comment = getItem(position);
        if (!LocalRestrictClicksUtil.getInstance().isUserCanClick(mkeyCurrentTime,
                comment.getId() + "")) {
            ToastUtil.showMessage(mContext, "不能重复点击");
            return;
        }
        baseActivity.showWaitDialog("请稍后...");
        ForemanCommentsDownTask task =
                new ForemanCommentsDownTask(baseActivity, comment.getId() + "");
        task.setCallBack(new AbstractHttpResponseHandler<CommentResult>() {

            @Override
            public void onSuccess(CommentResult t) {
                if (t != null) {
                    int pos = (Integer) text.getTag();
                    if (position == pos) {
                        text.setText("踩（" + t.getDown_num() + "）");
                    }
                }
                baseActivity.hideWaitDialog();
                LocalRestrictClicksUtil.getInstance().setUserClickStateDiseable(mkeyCurrentTime,
                        comment.getId() + "");
            }

            @Override
            public void onFailure(int code, String error) {
                baseActivity.showToastMsg(error);
                baseActivity.hideWaitDialog();
            }
        });
        task.send();
    }

}
