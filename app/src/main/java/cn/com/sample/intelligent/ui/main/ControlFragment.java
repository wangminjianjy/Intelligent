package cn.com.sample.intelligent.ui.main;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.com.sample.intelligent.R;
import cn.com.sample.intelligent.base.BaseConfig;
import cn.com.sample.intelligent.base.BaseFragment;
import cn.com.sample.intelligent.base.CustomApplication;
import cn.com.sample.intelligent.base.okhttp.WebSocketManage;
import cn.com.sample.intelligent.base.okhttp.callback.WebSocketCallBack;
import cn.com.sample.intelligent.bean.FactoryBean;
import cn.com.sample.intelligent.bean.StorageBean;
import cn.com.sample.intelligent.bean.StorageDataBean;
import cn.com.sample.intelligent.manager.SharedPreferencesManager;
import cn.com.sample.intelligent.net.websocket.WebSocketEnum;
import cn.com.sample.intelligent.net.websocket.WebSocketMsg;
import cn.com.sample.intelligent.ui.login.LoginActivity;
import cn.com.sample.intelligent.util.AppUtil;
import cn.com.sample.intelligent.util.ListAlert.AlertListUtil;
import cn.com.sample.intelligent.util.LogUtil;
import cn.com.sample.intelligent.util.ToastUtil;
import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;

public class ControlFragment extends BaseFragment {

    private TextView controlBack;
    private TextView controlTitle;
    private TextView controlStorage;
    private TextView controlTem;
    private TextView controlRH;
    private TextView controlJQ;
    private TextView controlBen;
    private TextView controlPM;
    private TextView controlCO;
    private ImageView controlAlert;

    private List<StorageBean> storageList;
    private List<String> storageNameList;
    private AlertListUtil alertList;
    private StorageBean storageBean;

    public static ControlFragment newInstance() {
        Bundle args = new Bundle();
        ControlFragment fragment = new ControlFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_control;
    }

    @Override
    protected void initView(View contentView) {
        controlBack = contentView.findViewById(R.id.custom_back);
        controlTitle = contentView.findViewById(R.id.custom_title);
        controlStorage = contentView.findViewById(R.id.control_storage);
        controlTem = contentView.findViewById(R.id.param_wendu);
        controlRH = contentView.findViewById(R.id.param_rh);
        controlJQ = contentView.findViewById(R.id.param_jiaquan);
        controlBen = contentView.findViewById(R.id.param_ben);
        controlPM = contentView.findViewById(R.id.param_pm);
        controlCO = contentView.findViewById(R.id.param_co);
        controlAlert = contentView.findViewById(R.id.control_alert);
    }

    @Override
    protected void bindEvent(View mContentView) {
        controlStorage.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        controlBack.setVisibility(View.GONE);
        controlTitle.setText(getString(R.string.control_title));

        storageList = new ArrayList<>();
        storageNameList = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.control_storage:
                WebSocketManage.getInstance().openConnect(webSocketListener, BaseConfig.HOST_WEBSOCKET);
                break;
            default:
                break;
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
            WebSocketManage.getInstance().openConnect(socketListener, BaseConfig.HOST_WEBSOCKET);
        }
    };

    private

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
            WebSocketMsg msg1 = new WebSocketMsg();
            msg1.setHead("" + WebSocketEnum.HuoQuShiShiShuJu);
            msg1.setType("0");
            msg1.setTo(SharedPreferencesManager.getCode());
            msg1.setMe(SharedPreferencesManager.getPhoneCode());
            msg1.setData(storageBean.getID());
            String sendMsg1 = new Gson().toJson(msg1) + "\0";
            webSocket.send(sendMsg1);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            Gson gson = new Gson();
            String jsonStr = text.replace("\0", "");
            WebSocketMsg socketMsg = gson.fromJson(jsonStr, WebSocketMsg.class);
            if (socketMsg.getHead().equals("24")) {
                clearStorageData();
                if (TextUtils.isEmpty(socketMsg.getData())) {
                    ToastUtil.showSingleToast(getString(R.string.control_null_hint), Toast.LENGTH_SHORT);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(socketMsg.getData());
                        StorageDataBean storageData = gson.fromJson(jsonObject.toString(), StorageDataBean.class);
                        setStorageData(storageData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    private void setStorageData(StorageDataBean storageData) {
        if (!TextUtils.isEmpty(storageData.getDataInfo().getDangQianWenDu())) {
            controlTem.setText(storageData.getDataInfo().getDangQianWenDu());
        }
        if (!TextUtils.isEmpty(storageData.getDataInfo().getDangQianShiDu())) {
            controlRH.setText(storageData.getDataInfo().getDangQianShiDu());
        }
        if (!TextUtils.isEmpty(storageData.getDataInfo().getJiaQuan())) {
            controlJQ.setText(storageData.getDataInfo().getJiaQuan());
        }
        if (!TextUtils.isEmpty(storageData.getDataInfo().getBen())) {
            controlBen.setText(storageData.getDataInfo().getBen());
        }
        if (!TextUtils.isEmpty(storageData.getDataInfo().getPM25())) {
            controlPM.setText(storageData.getDataInfo().getPM25());
        }
        if (!TextUtils.isEmpty(storageData.getDataInfo().getErYangHuaTan())) {
            controlCO.setText(storageData.getDataInfo().getErYangHuaTan());
        }
    }

    private void clearStorageData() {
        controlTem.setText("0.0");
        controlRH.setText("0.0");
        controlJQ.setText("0.0");
        controlBen.setText("0.0");
        controlPM.setText("0.0");
        controlCO.setText("0.0");
    }
}
