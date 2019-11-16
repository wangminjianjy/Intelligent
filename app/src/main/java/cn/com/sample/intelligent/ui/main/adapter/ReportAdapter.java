package cn.com.sample.intelligent.ui.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.com.sample.intelligent.R;
import cn.com.sample.intelligent.bean.DayReportBean;

/**
 * Description:
 * Creator : wangminjian
 * Create time : 2019/11/16.
 */
public class ReportAdapter extends BaseAdapter {

    private List<DayReportBean> mList;
    private Context mContext;

    public ReportAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<DayReportBean> data) {
        this.mList = data;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.item_report, null);
            viewHolder = new ViewHolder();
            viewHolder.reportAvgTem = convertView.findViewById(R.id.report_avg_tem);
            viewHolder.reportAvgSD = convertView.findViewById(R.id.report_avg_SD);
            viewHolder.reportDate = convertView.findViewById(R.id.report_date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DayReportBean dayReportBean = mList.get(position);
        viewHolder.reportAvgTem.setText(dayReportBean.getPingJunWenDu());
        viewHolder.reportAvgSD.setText(dayReportBean.getPingJunShiDu());
        viewHolder.reportDate.setText(dayReportBean.getCaiJiShiJian().substring(0, 10));
        return convertView;
    }

    class ViewHolder {
        TextView reportAvgTem;
        TextView reportAvgSD;
        TextView reportDate;
    }
}
