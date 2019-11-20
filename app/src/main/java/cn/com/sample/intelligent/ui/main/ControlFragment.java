package cn.com.sample.intelligent.ui.main;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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
import cn.com.sample.intelligent.util.ProgressUtil;
import cn.com.sample.intelligent.util.ToastUtil;
import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;

public class ControlFragment extends BaseFragment {

    // 实时参数
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
    // 云台控制
    private View llControl;
    private View controlZL;
    private View controlZR;
    private View controlJS;
    private View controlCS;
    private View controlTF;
    private View controlXD;
    private View controlSZ;
    private View controlQH;
    private View controlBF;
    private View controlBJ;
    private ImageView imgZL;
    private ImageView imgZR;
    private ImageView imgJS;
    private ImageView imgCS;
    private ImageView imgTF;
    private ImageView imgXD;
    private ImageView imgSZ;
    private ImageView imgQH;
    private ImageView imgBF;
    private ImageView imgBJ;
    private TextView tvQH;
    // 参数设置
    private View llParam;
    private EditText temUp;
    private EditText temLow;
    private EditText humUp;
    private EditText humLow;
    private TextView paramSet;

    private List<StorageBean> storageList;
    private List<String> storageNameList;
    private AlertListUtil alertList;
    private StorageBean storageBean;
    private Gson gson;
    private static ProgressUtil progressUtil;

    private int controlType;
    private String temUpStr;
    private String temLowStr;
    private String humUpStr;
    private String humLowStr;

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
        // 云台控制
        llControl = contentView.findViewById(R.id.ll_control);
        controlZL = contentView.findViewById(R.id.control_ZL);
        controlZR = contentView.findViewById(R.id.control_ZR);
        controlJS = contentView.findViewById(R.id.control_JS);
        controlCS = contentView.findViewById(R.id.control_CS);
        controlTF = contentView.findViewById(R.id.control_TF);
        controlXD = contentView.findViewById(R.id.control_XD);
        controlSZ = contentView.findViewById(R.id.control_SZ);
        controlQH = contentView.findViewById(R.id.control_QH);
        controlBF = contentView.findViewById(R.id.control_BF);
        controlBJ = contentView.findViewById(R.id.control_BJ);
        imgZL = contentView.findViewById(R.id.img_ZL);
        imgZR = contentView.findViewById(R.id.img_ZR);
        imgJS = contentView.findViewById(R.id.img_JS);
        imgCS = contentView.findViewById(R.id.img_CS);
        imgTF = contentView.findViewById(R.id.img_TF);
        imgXD = contentView.findViewById(R.id.img_XD);
        imgSZ = contentView.findViewById(R.id.img_SZ);
        imgQH = contentView.findViewById(R.id.img_QH);
        imgBF = contentView.findViewById(R.id.img_BF);
        imgBJ = contentView.findViewById(R.id.img_BJ);
        tvQH = contentView.findViewById(R.id.tv_QH);
        // 参数设置
        llParam = contentView.findViewById(R.id.ll_param);
        temUp = contentView.findViewById(R.id.param_tem_up);
        temLow = contentView.findViewById(R.id.param_tem_low);
        humUp = contentView.findViewById(R.id.param_hum_up);
        humLow = contentView.findViewById(R.id.param_hum_low);
        paramSet = contentView.findViewById(R.id.param_set);
    }

    @Override
    protected void bindEvent(View mContentView) {
        controlStorage.setOnClickListener(this);
        controlZL.setOnClickListener(this);
        controlZR.setOnClickListener(this);
        controlJS.setOnClickListener(this);
        controlCS.setOnClickListener(this);
        controlTF.setOnClickListener(this);
        controlXD.setOnClickListener(this);
        controlSZ.setOnClickListener(this);
        controlQH.setOnClickListener(this);
        controlBF.setOnClickListener(this);
        controlBJ.setOnClickListener(this);
        paramSet.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        controlBack.setVisibility(View.GONE);
        controlTitle.setText(getString(R.string.control_title));

        storageList = new ArrayList<>();
        storageNameList = new ArrayList<>();

        progressUtil = new ProgressUtil();
        gson = new Gson();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.control_storage:
                controlType = WebSocketEnum.HuoQuKuFangLieBiao;
                WebSocketManage.getInstance().openConnect(webSocketListener, BaseConfig.HOST_WEBSOCKET);
                break;
            case R.id.control_ZL:
//                controlType = WebSocketEnum.HuoQuKuFangLieBiao;
//                WebSocketManage.getInstance().openConnect(webSocketListener, BaseConfig.HOST_WEBSOCKET);
                break;
            case R.id.control_ZR:
//                controlType = WebSocketEnum.HuoQuKuFangLieBiao;
//                WebSocketManage.getInstance().openConnect(webSocketListener, BaseConfig.HOST_WEBSOCKET);
                break;
            case R.id.control_JS:
//                controlType = WebSocketEnum.HuoQuKuFangLieBiao;
//                WebSocketManage.getInstance().openConnect(webSocketListener, BaseConfig.HOST_WEBSOCKET);
                break;
            case R.id.control_CS:
//                controlType = WebSocketEnum.HuoQuKuFangLieBiao;
//                WebSocketManage.getInstance().openConnect(webSocketListener, BaseConfig.HOST_WEBSOCKET);
                break;
            case R.id.control_TF:
//                controlType = WebSocketEnum.HuoQuKuFangLieBiao;
//                WebSocketManage.getInstance().openConnect(webSocketListener, BaseConfig.HOST_WEBSOCKET);
                break;
            case R.id.control_XD:
//                controlType = WebSocketEnum.HuoQuKuFangLieBiao;
//                WebSocketManage.getInstance().openConnect(webSocketListener, BaseConfig.HOST_WEBSOCKET);
                break;
            case R.id.control_SZ:
//                controlType = WebSocketEnum.HuoQuKuFangLieBiao;
//                WebSocketManage.getInstance().openConnect(webSocketListener, BaseConfig.HOST_WEBSOCKET);
                break;
            case R.id.control_QH:
//                controlType = WebSocketEnum.HuoQuKuFangLieBiao;
//                WebSocketManage.getInstance().openConnect(webSocketListener, BaseConfig.HOST_WEBSOCKET);
                break;
            case R.id.control_BF:
//                controlType = WebSocketEnum.HuoQuKuFangLieBiao;
//                WebSocketManage.getInstance().openConnect(webSocketListener, BaseConfig.HOST_WEBSOCKET);
                break;
            case R.id.control_BJ:
//                controlType = WebSocketEnum.HuoQuBaoJingXinXi;
//                WebSocketManage.getInstance().openConnect(webSocketListener, BaseConfig.HOST_WEBSOCKET);
                break;
            case R.id.param_set:
                setParam();
                break;
            default:
                break;
        }
    }

    AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            alertList.dismiss();
            storageBean = storageList.get(position);
            controlType = WebSocketEnum.HuoQuShiShiShuJu;
            WebSocketManage.getInstance().openConnect(webSocketListener, BaseConfig.HOST_WEBSOCKET);
        }
    };

    WebSocketCallBack webSocketListener = new WebSocketCallBack() {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            progressUtil.showProgress(getActivity(), getActivity().getString(R.string.hint_socket_net), false);
            socketLogin(webSocket);
            socket(webSocket);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            progressUtil.hideProgress(getActivity());
            socketData(text);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {

        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {

        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            progressUtil.hideProgress(getActivity());
            LogUtil.d("设备布控", "WebSocketCallBack...onFailure..." + t.toString());
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            progressUtil.hideProgress(getActivity());
            //TODO 判断当前是否有登录用户，有的话则重连
            LogUtil.d("设备布控", "WebSocketCallBack...onClosed...code:" + code + ",reason" + reason);
        }
    };

    private void socketLogin(WebSocket webSocket) {
        WebSocketMsg msg = new WebSocketMsg();
        msg.setHead("" + WebSocketEnum.LogIn);
        msg.setType("0");
        msg.setTo(SharedPreferencesManager.getCode());
        msg.setMe(SharedPreferencesManager.getPhoneCode());
        msg.setData("1|" + SharedPreferencesManager.getPhoneCode());
        String sendMsg = new Gson().toJson(msg) + "\0";
        webSocket.send(sendMsg);
    }

    private void socket(WebSocket webSocket) {
        WebSocketMsg msg1 = new WebSocketMsg();
        msg1.setHead("" + controlType);
        msg1.setType("0");
        msg1.setTo(SharedPreferencesManager.getCode());
        msg1.setMe(SharedPreferencesManager.getPhoneCode());
        switch (controlType) {
            case WebSocketEnum.HuoQuKuFangLieBiao:
                msg1.setData(SharedPreferencesManager.getPhoneCode());
                break;
            case WebSocketEnum.HuoQuShiShiShuJu:
                msg1.setData(storageBean.getID());
                break;
            default:
                break;
        }
        String sendMsg1 = new Gson().toJson(msg1) + "\0";
        webSocket.send(sendMsg1);
    }

    private void socketData(String dataStr) {
        String jsonStr = dataStr.replace("\0", "");
        WebSocketMsg socketMsg = gson.fromJson(jsonStr, WebSocketMsg.class);
        if (socketMsg.getHead().equals(WebSocketEnum.FanHuiHuoQuKuFangLieBiao + "")) {
            storageData(socketMsg.getData());
        } else if (socketMsg.getHead().equals(WebSocketEnum.FanHuiShiShiShuJu + "")) {
            actualData(socketMsg.getData());
        } else {
            ToastUtil.showSingleToast(socketMsg.getData(), Toast.LENGTH_SHORT);
        }
    }

    private void setParam() {
        temUpStr = temUp.getText().toString();
        temLowStr = temLow.getText().toString();
        humUpStr = humUp.getText().toString();
        humLowStr = humLow.getText().toString();
        if (TextUtils.isEmpty(temUpStr)) {
            ToastUtil.showSingleToast("请填写上限温度", Toast.LENGTH_SHORT);
        } else if (TextUtils.isEmpty(temLowStr)) {
            ToastUtil.showSingleToast("请填写下限温度", Toast.LENGTH_SHORT);
        } else if (TextUtils.isEmpty(humUpStr)) {
            ToastUtil.showSingleToast("请填写上限湿度", Toast.LENGTH_SHORT);
        } else if (TextUtils.isEmpty(humLowStr)) {
            ToastUtil.showSingleToast("请填写下限湿度", Toast.LENGTH_SHORT);
        } else {
//            controlType = WebSocketEnum.HuoQuKuFangLieBiao;
//            WebSocketManage.getInstance().openConnect(webSocketListener, BaseConfig.HOST_WEBSOCKET);
        }
    }

    private void storageData(String data) {
        String output = AppUtil.toURLDecoder(data);
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

    private void actualData(String data) {
        clearStorageData();
        if (TextUtils.isEmpty(data)) {
            ToastUtil.showSingleToast(getString(R.string.control_null_hint), Toast.LENGTH_SHORT);
        } else {
            try {
                JSONObject jsonObject = new JSONObject(data);
                StorageDataBean storageData = gson.fromJson(jsonObject.toString(), StorageDataBean.class);
                setStorageData(storageData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

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
