package cn.com.sample.intelligent.base.okhttp.callback;

import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;

/**
 * Description ：
 * creator ：但子龙
 * Create time : 2018/5/31
 */

public interface WebSocketCallBack {

    void onOpen(WebSocket webSocket, Response response);

    void onMessage(WebSocket webSocket, String text);

    void onMessage(WebSocket webSocket, ByteString bytes);

    void onClosing(WebSocket webSocket, int code, String reason);

    void onClosed(WebSocket webSocket, int code, String reason);

    void onFailure(WebSocket webSocket, Throwable t, Response response);
}
