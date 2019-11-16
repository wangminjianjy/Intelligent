package cn.com.sample.intelligent.ui.main;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.com.sample.intelligent.R;
import cn.com.sample.intelligent.base.BaseConfig;
import cn.com.sample.intelligent.base.BaseFragment;
import cn.com.sample.intelligent.base.okhttp.WebSocketManage;
import cn.com.sample.intelligent.base.okhttp.callback.WebSocketCallBack;
import cn.com.sample.intelligent.bean.DayReportBean;
import cn.com.sample.intelligent.bean.StorageBean;
import cn.com.sample.intelligent.manager.SharedPreferencesManager;
import cn.com.sample.intelligent.net.websocket.WebSocketEnum;
import cn.com.sample.intelligent.net.websocket.WebSocketMsg;
import cn.com.sample.intelligent.ui.main.adapter.ReportAdapter;
import cn.com.sample.intelligent.util.AppUtil;
import cn.com.sample.intelligent.util.ListAlert.AlertListUtil;
import cn.com.sample.intelligent.util.LogUtil;
import cn.com.sample.intelligent.util.ToastUtil;
import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;

public class ReportFragment extends BaseFragment {

    private TextView reportBack;
    private TextView reportTitle;
    private TextView reportStartTime;
    private TextView reportEndTime;
    private TextView reportStorage;
    private TextView reportSearch;
    private ListView reportListView;
    private View noDataView;
    private TextView noData;

    private TimePickerView datePickerStart, datePickerEnd;
    private Calendar startDate, endDate;
    private Date dateStart, dateEnd;
    private ReportAdapter reportAdapter;
    private AlertListUtil alertList;
    private List<DayReportBean> dayReportList;
    private List<StorageBean> storageList;
    private List<String> storageNameList;
    private StorageBean storageBean;

    public static ReportFragment newInstance() {
        Bundle args = new Bundle();
        ReportFragment fragment = new ReportFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_report;
    }

    @Override
    protected void initView(View contentView) {
        reportBack = contentView.findViewById(R.id.custom_back);
        reportTitle = contentView.findViewById(R.id.custom_title);
        reportStartTime = contentView.findViewById(R.id.report_reportStartTime);
        reportEndTime = contentView.findViewById(R.id.report_reportEndTime);
        reportStorage = contentView.findViewById(R.id.report_storage);
        reportSearch = contentView.findViewById(R.id.report_search);
        reportListView = contentView.findViewById(R.id.report_list);
        noDataView = contentView.findViewById(R.id.ll_data_null);
        noData = contentView.findViewById(R.id.data_null);
    }

    @Override
    protected void bindEvent(View mContentView) {
        reportStartTime.setOnClickListener(this);
        reportEndTime.setOnClickListener(this);
        reportStorage.setOnClickListener(this);
        reportSearch.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        reportBack.setVisibility(View.GONE);
        reportTitle.setText(getString(R.string.report_title));

        dayReportList = new ArrayList<>();
        storageList = new ArrayList<>();
        storageNameList = new ArrayList<>();

        reportAdapter = new ReportAdapter(getActivity());
        reportListView.setAdapter(reportAdapter);

        setDatePicker();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.report_reportStartTime:
                datePickerStart.show();
                break;
            case R.id.report_reportEndTime:
                datePickerEnd.show();
                break;
            case R.id.report_storage:
                WebSocketManage.getInstance().openConnect(webSocketListener, BaseConfig.HOST_WEBSOCKET);
                break;
            case R.id.report_search:
                ReportSearch();
                break;
            default:
                break;
        }
    }

    // 时间选择器
    private void setDatePicker() {
        initDate();

        datePickerStart = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                boolean isLegal = true;
                if (dateEnd != null) {
                    if (compareDate(date, dateEnd) > 0) {
                        ToastUtil.showSingleToast("开始时间应小于结束时间", Toast.LENGTH_SHORT);
                        isLegal = false;
                    }
                }
                if (isLegal) {
                    dateStart = date;
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    String dateYear = String.format(Locale.CHINA, "%d", calendar.get(Calendar.YEAR));
                    String dateMonth = String.format(Locale.CHINA, "%02d", calendar.get(Calendar.MONTH) + 1);
                    String dateDay = String.format(Locale.CHINA, "%02d", calendar.get(Calendar.DAY_OF_MONTH));
                    reportStartTime.setText(dateYear + "-" + dateMonth + "-" + dateDay);
                }
            }
        }).setType(new boolean[]{true, true, true, false, false, false})
                .setCancelText("取消")
                .setSubmitText("确定")
                .setTitleText("选择开始日期")
                .setOutSideCancelable(false)
                .setDate(startDate)
                .isCenterLabel(false)
                .isDialog(false)
                .build();

        datePickerEnd = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                boolean isLegal = true;
                if (dateStart != null) {
                    if (compareDate(dateStart, date) > 0) {
                        ToastUtil.showSingleToast("开始时间应小于结束时间", Toast.LENGTH_SHORT);
                        isLegal = false;
                    }
                }
                if (isLegal) {
                    dateEnd = date;
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    String dateYear = String.format(Locale.CHINA, "%d", calendar.get(Calendar.YEAR));
                    String dateMonth = String.format(Locale.CHINA, "%02d", calendar.get(Calendar.MONTH) + 1);
                    String dateDay = String.format(Locale.CHINA, "%02d", calendar.get(Calendar.DAY_OF_MONTH));
                    reportEndTime.setText(dateYear + "-" + dateMonth + "-" + dateDay);
                }
            }
        }).setType(new boolean[]{true, true, true, false, false, false})
                .setCancelText("取消")
                .setSubmitText("确定")
                .setTitleText("选择结束日期")
                .setOutSideCancelable(false)
                .setDate(endDate)
                .isCenterLabel(false)
                .isDialog(false)
                .build();
    }

    // 时间初始化
    private void initDate() {
        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();

//        startDate.set(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH) - 7);
        startDate.setTime(new Date());
        endDate.setTime(new Date());

        String dateYearStart = String.format(Locale.CHINA, "%d", startDate.get(Calendar.YEAR));
        String dateMonthStart = String.format(Locale.CHINA, "%02d", startDate.get(Calendar.MONTH) + 1);
        String dateDayStart = String.format(Locale.CHINA, "%02d", startDate.get(Calendar.DAY_OF_MONTH));
        reportStartTime.setText(dateYearStart.concat("-").concat(dateMonthStart).concat("-").concat(dateDayStart));

        String dateYearEnd = String.format(Locale.CHINA, "%d", endDate.get(Calendar.YEAR));
        String dateMonthEnd = String.format(Locale.CHINA, "%02d", endDate.get(Calendar.MONTH) + 1);
        String dateDayEnd = String.format(Locale.CHINA, "%02d", endDate.get(Calendar.DAY_OF_MONTH));
        reportEndTime.setText(dateYearEnd.concat("-").concat(dateMonthEnd).concat("-").concat(dateDayEnd));
    }

    // 开始结束事件对比
    public int compareDate(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        if (calendar1.get(Calendar.YEAR) > calendar2.get(Calendar.YEAR)) {
            return 1;
        } else if (calendar1.get(Calendar.YEAR) < calendar2.get(Calendar.YEAR)) {
            return -1;
        } else {
            if (calendar1.get(Calendar.MONTH) > calendar2.get(Calendar.MONTH)) {
                return 1;
            } else if (calendar1.get(Calendar.MONTH) < calendar2.get(Calendar.MONTH)) {
                return -1;
            } else {
                if (calendar1.get(Calendar.DAY_OF_MONTH) > calendar2.get(Calendar.DAY_OF_MONTH)) {
                    return 1;
                } else if (calendar1.get(Calendar.DAY_OF_MONTH) < calendar2.get(Calendar.DAY_OF_MONTH)) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    }

    WebSocketCallBack webSocketListener = new WebSocketCallBack() {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            WebSocketMsg msg = new WebSocketMsg();
            msg.setHead("" + WebSocketEnum.LogIn);
            msg.setType("0");
            msg.setTo(SharedPreferencesManager.getCode());
            msg.setMe(SharedPreferencesManager.getPhoneCode());
            msg.setData("1|" + SharedPreferencesManager.getPhoneCode());
            String sendMsg = new Gson().toJson(msg) + "\0";
            webSocket.send(sendMsg);
            WebSocketMsg msg1 = new WebSocketMsg();
            msg1.setHead("" + WebSocketEnum.HuoQuKuFangLieBiao);
            msg1.setType("0");
            msg1.setTo(SharedPreferencesManager.getCode());
            msg1.setMe(SharedPreferencesManager.getPhoneCode());
            msg1.setData(SharedPreferencesManager.getPhoneCode());
            String sendMsg1 = new Gson().toJson(msg1) + "\0";
            webSocket.send(sendMsg1);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            Gson gson = new Gson();
            String jsonStr = text.replace("\0", "");
            WebSocketMsg socketMsg = gson.fromJson(jsonStr, WebSocketMsg.class);
            if (socketMsg.getHead().equals("22")) {
                String output = AppUtil.toURLDecoder(socketMsg.getData());
                try {
                    JSONArray jsonArray = new JSONArray(output);
                    storageList.clear();
                    storageNameList.clear();
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            StorageBean storage = gson.fromJson(jsonArray.getJSONObject(i).toString(), StorageBean.class);
                            storageList.add(storage);
                            storageNameList.add(storage.getStorageRoomName());
                        }
                    }
                    alertList = new AlertListUtil(getActivity(), getString(R.string.login_storage_title), storageNameList, onItemClick);
                    alertList.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {

        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {

        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            LogUtil.d("设备布控", "WebSocketCallBack...onFailure..." + t.toString());
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            //TODO 判断当前是否有登录用户，有的话则重连
            LogUtil.d("设备布控", "WebSocketCallBack...onClosed...code:" + code + ",reason" + reason);
        }
    };

    AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            alertList.dismiss();
            storageBean = storageList.get(position);
            reportStorage.setText(storageBean.getStorageRoomName());
        }
    };

    private void ReportSearch() {
        if (TextUtils.isEmpty(reportStorage.getText().toString())) {
            ToastUtil.showSingleToast("请选择库房", Toast.LENGTH_SHORT);
        } else {
            WebSocketManage.getInstance().openConnect(socketListener, BaseConfig.HOST_WEBSOCKET);
        }
    }

    WebSocketCallBack socketListener = new WebSocketCallBack() {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            WebSocketMsg msg = new WebSocketMsg();
            msg.setHead("" + WebSocketEnum.LogIn);
            msg.setType("0");
            msg.setTo(SharedPreferencesManager.getCode());
            msg.setMe(SharedPreferencesManager.getPhoneCode());
            msg.setData("1|" + SharedPreferencesManager.getPhoneCode());
            String sendMsg = new Gson().toJson(msg) + "\0";
            webSocket.send(sendMsg);

            String data = storageBean.getID().concat("|")
                    .concat(reportStartTime.getText().toString()).concat("|")
                    .concat(reportEndTime.getText().toString());
            WebSocketMsg msg1 = new WebSocketMsg();
            msg1.setHead("" + WebSocketEnum.HuoQuRiBaoBiao);
            msg1.setType("0");
            msg1.setTo(SharedPreferencesManager.getCode());
            msg1.setMe(SharedPreferencesManager.getPhoneCode());
            msg1.setData(data);
            String sendMsg1 = new Gson().toJson(msg1) + "\0";
            webSocket.send(sendMsg1);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            Gson gson = new Gson();
            String jsonStr = text.replace("\0", "");
            WebSocketMsg socketMsg = gson.fromJson(jsonStr, WebSocketMsg.class);
            if (socketMsg.getHead().equals("28")) {
                String output = AppUtil.toURLDecoder(socketMsg.getData());
                dayReportList.clear();
                try {
                    JSONArray jsonArray = new JSONArray(output);
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            DayReportBean dayReport = gson.fromJson(jsonArray.getJSONObject(i).toString(), DayReportBean.class);
                            dayReportList.add(dayReport);
                        }
                        noDataView.setVisibility(View.GONE);
                        reportAdapter.setData(dayReportList);
                        reportAdapter.notifyDataSetChanged();
                    } else {
                        noDataView.setVisibility(View.VISIBLE);
                        reportAdapter.setData(dayReportList);
                        reportAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    noDataView.setVisibility(View.VISIBLE);
                    reportAdapter.setData(dayReportList);
                    reportAdapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {

        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {

        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {

        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            dayReportList.clear();
            noDataView.setVisibility(View.VISIBLE);
            noData.setText("获取报表失败");
            reportAdapter.setData(dayReportList);
            reportAdapter.notifyDataSetChanged();
        }
    };
}
