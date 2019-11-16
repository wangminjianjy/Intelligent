package cn.com.sample.intelligent.util.ListAlert;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.com.sample.intelligent.R;

/**
 * Description:
 * Creator : wangminjian
 * Create time : 2019/11/16.
 */
public class AlertListAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> alertPoundList;

    public AlertListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<String> alertPoundList) {
        this.alertPoundList = alertPoundList;
    }

    public void clearData() {
        if (alertPoundList != null) {
            alertPoundList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return alertPoundList == null ? 0 : alertPoundList.size();
    }

    @Override
    public Object getItem(int position) {
        return alertPoundList == null ? null : alertPoundList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
            convertView = _LayoutInflater.inflate(R.layout.item_alert_list, null);
            viewHolder = new ViewHolder();
            viewHolder.alertListItem = convertView.findViewById(R.id.tv_item_alert_list);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.alertListItem.setText(alertPoundList.get(position));

        return convertView;
    }

    static class ViewHolder {
        TextView alertListItem;
    }
}
