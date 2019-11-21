package cn.com.sample.intelligent.ui.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.com.sample.intelligent.R;
import cn.com.sample.intelligent.bean.StorageBean;
import cn.com.sample.intelligent.bean.StorageDataBean;

/**
 * Description:
 * Creator : wangminjian
 * Create time : 2019/11/18.
 */
public class DeviceAdapter extends BaseAdapter {

    private List<StorageBean> storageList;
    private List<StorageDataBean> mList;
    private OnControlListener onControlListener;
    private Context mContext;

    public DeviceAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<StorageBean> storage, List<StorageDataBean> data) {
        this.storageList = storage;
        this.mList = data;
    }

    public void setOnControlListener(OnControlListener onControlListener) {
        this.onControlListener = onControlListener;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        DeviceAdapter.ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.item_storage, null);
            viewHolder = new DeviceAdapter.ViewHolder();
            viewHolder.name = convertView.findViewById(R.id.param_name);
            viewHolder.net = convertView.findViewById(R.id.storage_net);
            viewHolder.wendu = convertView.findViewById(R.id.param_wendu);
            viewHolder.shidu = convertView.findViewById(R.id.param_rh);
            viewHolder.co2 = convertView.findViewById(R.id.param_co);
            viewHolder.zhiliang = convertView.findViewById(R.id.param_quality);
            viewHolder.jiaquan = convertView.findViewById(R.id.param_jiaquan);
            viewHolder.ben = convertView.findViewById(R.id.param_ben);
            viewHolder.pm = convertView.findViewById(R.id.param_pm);
            viewHolder.control = convertView.findViewById(R.id.control);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (DeviceAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onControlListener.onControlClick(position);
            }
        });

        if (storageList.get(position) != null) {
            viewHolder.name.setText(storageList.get(position).getStorageRoomName());
        }
        StorageDataBean storageDataBean = mList.get(position);
        if (storageDataBean != null && storageDataBean.getDataInfo() != null) {
            viewHolder.wendu.setText(storageDataBean.getDataInfo().getDangQianWenDu());
            viewHolder.shidu.setText(storageDataBean.getDataInfo().getDangQianShiDu());
            viewHolder.co2.setText(storageDataBean.getDataInfo().getErYangHuaTan());
            viewHolder.zhiliang.setText(storageDataBean.getDataInfo().getKongQiZhiLiang());
            viewHolder.jiaquan.setText(storageDataBean.getDataInfo().getJiaQuan());
            viewHolder.ben.setText(storageDataBean.getDataInfo().getBen());
            viewHolder.pm.setText(storageDataBean.getDataInfo().getPM25());
        } else {
            viewHolder.net.setText("暂无数据");
            viewHolder.net.setTextColor(viewHolder.net.getResources().getColor(R.color.colorAlarm));
            viewHolder.net.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.data_alarm), null, null, null);
        }
        return convertView;
    }

    class ViewHolder {
        TextView name;
        TextView net;
        TextView wendu;
        TextView shidu;
        TextView co2;
        TextView zhiliang;
        TextView jiaquan;
        TextView ben;
        TextView pm;
        TextView control;
    }

    public interface OnControlListener {
        void onControlClick(int position);
    }
}
