package cn.com.sample.intelligent.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.sample.intelligent.R;
import cn.com.sample.intelligent.base.BaseConfig;
import cn.com.sample.intelligent.base.BaseFragment;
import cn.com.sample.intelligent.base.okhttp.WebSocketManage;
import cn.com.sample.intelligent.base.okhttp.callback.WebSocketCallBack;
import cn.com.sample.intelligent.bean.StorageBean;
import cn.com.sample.intelligent.bean.StorageDataBean;
import cn.com.sample.intelligent.manager.SharedPreferencesManager;
import cn.com.sample.intelligent.net.websocket.WebSocketEnum;
import cn.com.sample.intelligent.net.websocket.WebSocketMsg;
import cn.com.sample.intelligent.ui.main.adapter.DeviceAdapter;
import cn.com.sample.intelligent.ui.main.control.ControlActivity;
import cn.com.sample.intelligent.util.AppUtil;
import cn.com.sample.intelligent.util.ListAlert.AlertListUtil;
import cn.com.sample.intelligent.util.ProgressUtil;
import cn.com.sample.intelligent.util.ToastUtil;
import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;

public class DeviceFragment extends BaseFragment {

    private TextView controlBack;
    private TextView controlTitle;
    private TextView controlAction;
    private ListView deviceView;

    private List<StorageBean> storageList;
    private List<StorageDataBean> deviceList;
    private DeviceAdapter deviceAdapter;
    private int dataNum = 0;
    private static ProgressUtil progressUtil;
    private Gson gson;

    public static DeviceFragment newInstance() {
        Bundle args = new Bundle();
        DeviceFragment fragment = new DeviceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_device;
    }

    @Override
    protected void initView(View contentView) {
        controlBack = contentView.findViewById(R.id.custom_back);
        controlTitle = contentView.findViewById(R.id.custom_title);
        controlAction = contentView.findViewById(R.id.custom_action);
        deviceView = contentView.findViewById(R.id.device_list);
    }

    @Override
    protected void bindEvent(View mContentView) {
        controlAction.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        controlBack.setVisibility(View.GONE);
        controlTitle.setText(getString(R.string.control_title));
        controlAction.setText(getString(R.string.control_update));
        controlAction.setVisibility(View.VISIBLE);

        storageList = new ArrayList<>();
        deviceList = new ArrayList<>();

        gson = new Gson();
        progressUtil = new ProgressUtil();

        deviceAdapter = new DeviceAdapter(getActivity());
        deviceView.setAdapter(deviceAdapter);
        deviceAdapter.setOnControlListener(new DeviceAdapter.OnControlListener() {
            @Override
            public void onControlClick(int position) {
                Intent intent = new Intent(getActivity(), ControlActivity.class);
                intent.putExtra(BaseConfig.PARAM_STORAGE, gson.toJson(storageList.get(position)));
                intent.putExtra(BaseConfig.PARAM_STORAGE_DATA, gson.toJson(deviceList.get(position)));
                startActivity(intent);
            }
        });

        getStorageData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.custom_action:
                getStorageData();
                break;
            default:
                break;
        }
    }

    private void getStorageData() {
        storageList.clear();
        WebSocketManage.getInstance().openConnect(new WebSocketCallBack() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                progressUtil.showProgress(getActivity(), getActivity().getString(R.string.hint_socket_get), false);
                WebSocketMsg msg = new WebSocketMsg();
                msg.setHead("" + WebSocketEnum.LogIn);
                msg.setType("0");
                msg.setTo(SharedPreferencesManager.getCode());
                msg.setMe(SharedPreferencesManager.getPhoneCode());
                msg.setData("1|" + SharedPreferencesManager.getPhoneCode());
                String sendMsg = gson.toJson(msg) + "\0";
                webSocket.send(sendMsg);
                WebSocketMsg msg1 = new WebSocketMsg();
                msg1.setHead("" + WebSocketEnum.HuoQuKuFangLieBiao);
                msg1.setType("0");
                msg1.setTo(SharedPreferencesManager.getCode());
                msg1.setMe(SharedPreferencesManager.getPhoneCode());
                msg1.setData(SharedPreferencesManager.getPhoneCode());
                String sendMsg1 = gson.toJson(msg1) + "\0";
                webSocket.send(sendMsg1);
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                progressUtil.hideProgress(getActivity());
                String jsonStr = text.replace("\0", "");
                WebSocketMsg socketMsg = gson.fromJson(jsonStr, WebSocketMsg.class);
                if (socketMsg.getHead().equals("22")) {
                    String output = AppUtil.toURLDecoder(socketMsg.getData());
                    try {
                        JSONArray jsonArray = new JSONArray(output);
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                StorageBean storage = gson.fromJson(jsonArray.getJSONObject(i).toString(), StorageBean.class);
                                storageList.add(storage);
                            }
                            dataNum = 0;
                            deviceList.clear();
                            setStorageData();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastUtil.showSingleToast(socketMsg.getData(), Toast.LENGTH_SHORT);
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
                progressUtil.hideProgress(getActivity());
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                progressUtil.hideProgress(getActivity());
            }
        }, BaseConfig.HOST_WEBSOCKET);
    }

    private void setStorageData() {
        if (dataNum < storageList.size()) {
            WebSocketManage.getInstance().openConnect(new WebSocketCallBack() {
                @Override
                public void onOpen(WebSocket webSocket, Response response) {
                    progressUtil.showProgress(getActivity(), getActivity().getString(R.string.hint_socket_get), false);
                    WebSocketMsg msg = new WebSocketMsg();
                    msg.setHead("" + WebSocketEnum.LogIn);
                    msg.setType("0");
                    msg.setTo(SharedPreferencesManager.getCode());
                    msg.setMe(SharedPreferencesManager.getPhoneCode());
                    msg.setData("1|" + SharedPreferencesManager.getPhoneCode());
                    String sendMsg = gson.toJson(msg) + "\0";
                    webSocket.send(sendMsg);
                    WebSocketMsg msg1 = new WebSocketMsg();
                    msg1.setHead("" + WebSocketEnum.HuoQuShiShiShuJu);
                    msg1.setType("0");
                    msg1.setTo(SharedPreferencesManager.getCode());
                    msg1.setMe(SharedPreferencesManager.getPhoneCode());
                    msg1.setData(storageList.get(dataNum).getID());
                    String sendMsg1 = gson.toJson(msg1) + "\0";
                    webSocket.send(sendMsg1);
                }

                @Override
                public void onMessage(WebSocket webSocket, String text) {
                    progressUtil.hideProgress(getActivity());
                    String jsonStr = text.replace("\0", "");
                    WebSocketMsg socketMsg = gson.fromJson(jsonStr, WebSocketMsg.class);
                    StorageDataBean storageData = new StorageDataBean();
                    if (socketMsg.getHead().equals("24")) {
                        if (TextUtils.isEmpty(socketMsg.getData())) {
                            ToastUtil.showSingleToast(getString(R.string.control_null_hint), Toast.LENGTH_SHORT);
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(socketMsg.getData());
                                storageData = gson.fromJson(jsonObject.toString(), StorageDataBean.class);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    deviceList.add(storageData);
                    deviceAdapter.setData(storageList, deviceList);
                    deviceAdapter.notifyDataSetChanged();
                    dataNum = dataNum + 1;
                    setStorageData();
                }

                @Override
                public void onMessage(WebSocket webSocket, ByteString bytes) {

                }

                @Override
                public void onClosing(WebSocket webSocket, int code, String reason) {

                }

                @Override
                public void onClosed(WebSocket webSocket, int code, String reason) {
                    progressUtil.hideProgress(getActivity());
                }

                @Override
                public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                    progressUtil.hideProgress(getActivity());
                }
            }, BaseConfig.HOST_WEBSOCKET);
        }
    }
}
