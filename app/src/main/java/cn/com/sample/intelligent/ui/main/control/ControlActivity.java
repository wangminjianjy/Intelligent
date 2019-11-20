package cn.com.sample.intelligent.ui.main.control;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.sample.intelligent.R;
import cn.com.sample.intelligent.base.BaseActivity;
import cn.com.sample.intelligent.base.BaseConfig;
import cn.com.sample.intelligent.base.okhttp.WebSocketManage;
import cn.com.sample.intelligent.base.okhttp.callback.WebSocketCallBack;
import cn.com.sample.intelligent.bean.StorageBean;
import cn.com.sample.intelligent.bean.StorageDataBean;
import cn.com.sample.intelligent.manager.SharedPreferencesManager;
import cn.com.sample.intelligent.net.websocket.WebSocketEnum;
import cn.com.sample.intelligent.net.websocket.WebSocketMsg;
import cn.com.sample.intelligent.util.ListAlert.AlertListUtil;
import cn.com.sample.intelligent.util.LogUtil;
import cn.com.sample.intelligent.util.ProgressUtil;
import cn.com.sample.intelligent.util.ToastUtil;
import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;

public class ControlActivity extends BaseActivity {

    // 实时参数
    private TextView controlBack;
    private TextView controlTitle;
    private TextView controlTem;
    private TextView controlRH;
    private TextView controlJQ;
    private TextView controlBen;
    private TextView controlPM;
    private TextView controlCO;
    private TextView controlAir;
    // 云台控制
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
    private ImageView imgQH;
    private ImageView imgBF;
    private ImageView imgBJ;
    // 参数设置
    private View llParam;
    private EditText temUp;
    private EditText temLow;
    private EditText humUp;
    private EditText humLow;
    private TextView paramSet;

    private StorageBean storageBean;
    private StorageDataBean storageDataBean;
    private Gson gson;
    private static ProgressUtil progressUtil;

    private int controlType;
    private String temUpStr;
    private String temLowStr;
    private String humUpStr;
    private String humLowStr;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_control;
    }

    @Override
    protected void initView(View contentView) {
        controlBack = contentView.findViewById(R.id.custom_back);
        controlTitle = contentView.findViewById(R.id.custom_title);
        controlTem = contentView.findViewById(R.id.param_wendu);
        controlRH = contentView.findViewById(R.id.param_rh);
        controlJQ = contentView.findViewById(R.id.param_jiaquan);
        controlBen = contentView.findViewById(R.id.param_ben);
        controlPM = contentView.findViewById(R.id.param_pm);
        controlCO = contentView.findViewById(R.id.param_co);
        controlAir = contentView.findViewById(R.id.control_air);
        // 云台控制
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
        imgQH = contentView.findViewById(R.id.img_QH);
        imgBF = contentView.findViewById(R.id.img_BF);
        imgBJ = contentView.findViewById(R.id.img_BJ);
        // 参数设置
        llParam = contentView.findViewById(R.id.ll_param);
        temUp = contentView.findViewById(R.id.param_tem_up);
        temLow = contentView.findViewById(R.id.param_tem_low);
        humUp = contentView.findViewById(R.id.param_hum_up);
        humLow = contentView.findViewById(R.id.param_hum_low);
        paramSet = contentView.findViewById(R.id.param_set);
    }

    @Override
    protected void bindEvent(View contentView) {
        controlBack.setOnClickListener(this);
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
        progressUtil = new ProgressUtil();
        gson = new Gson();

        storageBean = gson.fromJson(getIntent().getStringExtra(BaseConfig.PARAM_STORAGE), StorageBean.class);
        storageDataBean = gson.fromJson(getIntent().getStringExtra(BaseConfig.PARAM_STORAGE_DATA), StorageDataBean.class);

        setStorageData();
        setControlData();
        setControlState();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.custom_back:
                finish();
                break;
            case R.id.control_ZL:
                if (storageDataBean.isZhiLengZhuangTai()) {
                    storageDataBean.setZhiLengZhuangTai(false);
                    controlType = WebSocketEnum.ZhiLengGuanBi;
                    imgZL.setImageResource(R.drawable.zl_close);
                } else {
                    storageDataBean.setZhiLengZhuangTai(true);
                    controlType = WebSocketEnum.ZhiLengKaiQi;
                    imgZL.setImageResource(R.drawable.zl_open);
                }
                WebSocketManage.getInstance().openConnect(webSocketListener, BaseConfig.HOST_WEBSOCKET);
                break;
            case R.id.control_ZR:
                if (storageDataBean.isZhiReZhuangTai()) {
                    storageDataBean.setZhiReZhuangTai(false);
                    controlType = WebSocketEnum.JiaReGuanBi;
                    imgZR.setImageResource(R.drawable.zr_close);
                } else
                    storageDataBean.setZhiReZhuangTai(true);
                WebSocketManage.getInstance().openConnect(webSocketListener, BaseConfig.HOST_WEBSOCKET);
                break;
            case R.id.control_JS:
                if (storageDataBean.isJiaShiZhuangTai()) {
                    storageDataBean.setJiaShiZhuangTai(false);
                    controlType = WebSocketEnum.JiaShiGuanBi;
                    imgJS.setImageResource(R.drawable.js_close);
                } else {
                    storageDataBean.setJiaShiZhuangTai(true);
                    controlType = WebSocketEnum.JiaShiKaiQi;
                    imgJS.setImageResource(R.drawable.js_open);
                }
                WebSocketManage.getInstance().openConnect(webSocketListener, BaseConfig.HOST_WEBSOCKET);
                break;
            case R.id.control_CS:
                if (storageDataBean.isChuShiZhuangTai()) {
                    storageDataBean.setChuShiZhuangTai(false);
                    controlType = WebSocketEnum.ChuShiGuanBi;
                    imgCS.setImageResource(R.drawable.cs_close);
                } else {
                    storageDataBean.setChuShiZhuangTai(true);
                    controlType = WebSocketEnum.ChuShiKaiQi;
                    imgCS.setImageResource(R.drawable.cs_open);
                }
                WebSocketManage.getInstance().openConnect(webSocketListener, BaseConfig.HOST_WEBSOCKET);
                break;
            case R.id.control_TF:
                if (storageDataBean.isTongFengZhuangTai()) {
                    storageDataBean.setTongFengZhuangTai(false);
                    controlType = WebSocketEnum.TongFengGuanBi;
                    imgTF.setImageResource(R.drawable.tf_close);
                } else {
                    storageDataBean.setTongFengZhuangTai(true);
                    controlType = WebSocketEnum.TongFengKaiQi;
                    imgTF.setImageResource(R.drawable.tf_open);
                }
                WebSocketManage.getInstance().openConnect(webSocketListener, BaseConfig.HOST_WEBSOCKET);
                break;
            case R.id.control_XD:
                if (storageDataBean.isJingHuaZhuangTai()) {
                    storageDataBean.setJingHuaZhuangTai(false);
                    controlType = WebSocketEnum.JingHuaGuanBi;
                    imgXD.setImageResource(R.drawable.xd_close);
                } else {
                    storageDataBean.setJingHuaZhuangTai(true);
                    controlType = WebSocketEnum.JingHuaKaiQi;
                    imgXD.setImageResource(R.drawable.xd_open);
                }
                WebSocketManage.getInstance().openConnect(webSocketListener, BaseConfig.HOST_WEBSOCKET);
                break;
            case R.id.control_SZ:
                if (llParam.getVisibility() == View.VISIBLE) {
                    llParam.setVisibility(View.GONE);
                } else {
                    llParam.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.control_QH:
                if (storageDataBean.isZiDongMoShi()) {
                    storageDataBean.setZiDongMoShi(false);
                    controlType = WebSocketEnum.ShouDongMoShi;
                    imgQH.setImageResource(R.drawable.qh_close);
                } else {
                    storageDataBean.setZiDongMoShi(true);
                    controlType = WebSocketEnum.ZiDongMoShi;
                    imgQH.setImageResource(R.drawable.qh_open);
                }
                WebSocketManage.getInstance().openConnect(webSocketListener, BaseConfig.HOST_WEBSOCKET);
                break;
            case R.id.control_BF:
                if (storageDataBean.isBuFangZhuangTai()) {
                    storageDataBean.setBuFangZhuangTai(false);
                    controlType = WebSocketEnum.BuFangGuanBi;
                    imgBF.setImageResource(R.drawable.bf_close);
                } else {
                    storageDataBean.setBuFangZhuangTai(true);
                    controlType = WebSocketEnum.BuFangKaiQi;
                    imgBF.setImageResource(R.drawable.bf_open);
                }
                WebSocketManage.getInstance().openConnect(webSocketListener, BaseConfig.HOST_WEBSOCKET);
                break;
            case R.id.control_BJ:
                if (storageDataBean.isJinJiBaoJing()) {
                    storageDataBean.setJinJiBaoJing(false);
                    controlType = WebSocketEnum.JinJiBaoJingGuanBi;
                    imgBJ.setImageResource(R.drawable.bj_close);
                } else {
                    storageDataBean.setJinJiBaoJing(true);
                    controlType = WebSocketEnum.JinJiBaoJingKaiQi;
                    imgBJ.setImageResource(R.drawable.bj_open);
                }
                WebSocketManage.getInstance().openConnect(webSocketListener, BaseConfig.HOST_WEBSOCKET);
                break;
            case R.id.param_set:
                setParam();
                break;
            default:
                break;
        }
    }

    WebSocketCallBack webSocketListener = new WebSocketCallBack() {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            socketLogin(webSocket);
            socket(webSocket);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            progressUtil.hideProgress(ControlActivity.this);
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
            progressUtil.hideProgress(ControlActivity.this);
            LogUtil.d("设备布控", "WebSocketCallBack...onFailure..." + t.toString());
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            progressUtil.hideProgress(ControlActivity.this);
            //TODO 判断当前是否有登录用户，有的话则重连
            LogUtil.d("设备布控", "WebSocketCallBack...onClosed...code:" + code + ",reason" + reason);
        }
    };

    private void setControlData() {
        controlTitle.setText(storageBean.getStorageRoomName());
        temUp.setText(storageBean.getWD_SX());
        temLow.setText(storageBean.getWD_XX());
        humUp.setText(storageBean.getSD_SX());
        humLow.setText(storageBean.getSD_SX());
    }

    private void setControlState() {
        if (storageDataBean.isZhiLengZhuangTai()) {
            imgZL.setImageResource(R.drawable.zl_open);
        } else {
            imgZL.setImageResource(R.drawable.zl_close);
        }
        if (storageDataBean.isZhiReZhuangTai()) {
            imgZR.setImageResource(R.drawable.zr_open);
        } else {
            imgZR.setImageResource(R.drawable.zr_close);
        }
        if (storageDataBean.isJiaShiZhuangTai()) {
            imgJS.setImageResource(R.drawable.js_open);
        } else {
            imgJS.setImageResource(R.drawable.js_close);
        }
        if (storageDataBean.isChuShiZhuangTai()) {
            imgCS.setImageResource(R.drawable.cs_open);
        } else {
            imgCS.setImageResource(R.drawable.cs_close);
        }
        if (storageDataBean.isTongFengZhuangTai()) {
            imgTF.setImageResource(R.drawable.tf_open);
        } else {
            imgTF.setImageResource(R.drawable.tf_close);
        }
        if (storageDataBean.isJingHuaZhuangTai()) {
            imgXD.setImageResource(R.drawable.xd_open);
        } else {
            imgXD.setImageResource(R.drawable.xd_close);
        }
        if (storageDataBean.isZiDongMoShi()) {
            imgQH.setImageResource(R.drawable.qh_open);
        } else {
            imgQH.setImageResource(R.drawable.qh_close);
        }
        if (storageDataBean.isBuFangZhuangTai()) {
            imgBF.setImageResource(R.drawable.bf_open);
        } else {
            imgBF.setImageResource(R.drawable.bf_close);
        }
        if (storageDataBean.isJinJiBaoJing()) {
            imgBJ.setImageResource(R.drawable.bj_open);
        } else {
            imgBJ.setImageResource(R.drawable.bj_close);
        }
    }

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
        String data;
        if (controlType == WebSocketEnum.HuoQuKuFangLieBiao) {
            progressUtil.showProgress(ControlActivity.this, getString(R.string.hint_socket_net), false);
            data = SharedPreferencesManager.getPhoneCode();
        } else if (controlType == WebSocketEnum.SheZhiWenShiDu) {
            data = storageBean.getID().concat("|").concat(temUpStr)
                    .concat("|").concat(humUpStr)
                    .concat("|").concat(temLowStr)
                    .concat("|").concat(humLowStr);
        } else {
            data = storageBean.getID();
        }

        WebSocketMsg msg1 = new WebSocketMsg();
        msg1.setHead("" + controlType);
        msg1.setType("0");
        msg1.setTo(SharedPreferencesManager.getCode());
        msg1.setMe(SharedPreferencesManager.getPhoneCode());
        msg1.setData(data);
        String sendMsg1 = new Gson().toJson(msg1) + "\0";
        webSocket.send(sendMsg1);
        if (controlType != WebSocketEnum.HuoQuKuFangLieBiao) {
            ToastUtil.showSingleToast("指令发送成功", Toast.LENGTH_SHORT);
        }
    }

    private void socketData(String dataStr) {
        String jsonStr = dataStr.replace("\0", "");
        WebSocketMsg socketMsg = gson.fromJson(jsonStr, WebSocketMsg.class);
        if (socketMsg.getHead().equals(WebSocketEnum.FanHuiShiShiShuJu + "")) {
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
            controlType = WebSocketEnum.SheZhiWenShiDu;
            WebSocketManage.getInstance().openConnect(webSocketListener, BaseConfig.HOST_WEBSOCKET);
        }
    }

    private void actualData(String data) {
        clearStorageData();
        if (TextUtils.isEmpty(data)) {
            ToastUtil.showSingleToast(getString(R.string.control_null_hint), Toast.LENGTH_SHORT);
        } else {
            try {
                JSONObject jsonObject = new JSONObject(data);
                storageDataBean = gson.fromJson(jsonObject.toString(), StorageDataBean.class);
                setStorageData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setStorageData() {
        if (!TextUtils.isEmpty(storageDataBean.getDataInfo().getDangQianWenDu())) {
            controlTem.setText(storageDataBean.getDataInfo().getDangQianWenDu());
        }
        if (!TextUtils.isEmpty(storageDataBean.getDataInfo().getDangQianShiDu())) {
            controlRH.setText(storageDataBean.getDataInfo().getDangQianShiDu());
        }
        if (!TextUtils.isEmpty(storageDataBean.getDataInfo().getJiaQuan())) {
            controlJQ.setText(storageDataBean.getDataInfo().getJiaQuan());
        }
        if (!TextUtils.isEmpty(storageDataBean.getDataInfo().getBen())) {
            controlBen.setText(storageDataBean.getDataInfo().getBen());
        }
        if (!TextUtils.isEmpty(storageDataBean.getDataInfo().getPM25())) {
            controlPM.setText(storageDataBean.getDataInfo().getPM25());
        }
        if (!TextUtils.isEmpty(storageDataBean.getDataInfo().getErYangHuaTan())) {
            controlCO.setText(storageDataBean.getDataInfo().getErYangHuaTan());
        }
        if (!TextUtils.isEmpty(storageDataBean.getDataInfo().getKongQiZhiLiang())) {
            controlAir.setText(storageDataBean.getDataInfo().getKongQiZhiLiang());
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
