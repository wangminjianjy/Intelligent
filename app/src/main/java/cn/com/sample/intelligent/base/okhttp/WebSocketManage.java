package cn.com.sample.intelligent.base.okhttp;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.TimeUnit;

import cn.com.sample.intelligent.base.okhttp.callback.WebSocketCallBack;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Description ： WebSocket工具类
 * creator ：但子龙
 * Create time : 2018/2/1
 */

public class WebSocketManage {

    private static WebSocketManage mInstance;

    private OkHttpClient webSocketClient;
    private WebSocket webSocket;

    private Handler mDelivery;
    /**
     * socket 是否连接
     */
    private boolean isSocketConnect = false;

    WebSocketManage() {
        mDelivery = new Handler(Looper.getMainLooper());
    }

    public static WebSocketManage getInstance() {
        if (mInstance == null) {
            synchronized (WebSocketManage.class) {
                if (mInstance == null) {
                    mInstance = new WebSocketManage();
                }
            }
        }
        return mInstance;
    }

    /**
     * 打开连接
     */
    public void openConnect(final WebSocketCallBack callBack, String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        webSocketClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
                .build();

        WebSocketListener webSocketListener = new WebSocketListener() {
            @Override
            public void onOpen(final WebSocket webSocket, final Response response) {
                super.onOpen(webSocket, response);
                mDelivery.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callBack != null) {
                            callBack.onOpen(webSocket, response);
                        }
                    }
                });
            }

            @Override
            public void onMessage(final WebSocket webSocket, final String text) {
                super.onMessage(webSocket, text);
                mDelivery.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callBack != null) {
                            callBack.onMessage(webSocket, text);
                        }
                    }
                });
            }

            @Override
            public void onMessage(final WebSocket webSocket, final ByteString bytes) {
                super.onMessage(webSocket, bytes);
                mDelivery.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callBack != null) {
                            callBack.onMessage(webSocket, bytes);
                        }
                    }
                });
            }

            @Override
            public void onClosing(final WebSocket webSocket, final int code, final String reason) {
                super.onClosing(webSocket, code, reason);
                mDelivery.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callBack != null) {
                            callBack.onClosing(webSocket, code, reason);
                        }
                    }
                });
            }

            @Override
            public void onClosed(final WebSocket webSocket, final int code, final String reason) {
                super.onClosed(webSocket, code, reason);
                mDelivery.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callBack != null) {
                            callBack.onClosed(webSocket, code, reason);
                        }
                    }
                });
            }

            @Override
            public void onFailure(final WebSocket webSocket, final Throwable t, final Response response) {
                super.onFailure(webSocket, t, response);
                mDelivery.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callBack != null) {
                            callBack.onFailure(webSocket, t, response);
                        }
                    }
                });
            }
        };
        webSocket = webSocketClient.newWebSocket(request, webSocketListener);

        webSocketClient.dispatcher().executorService().shutdown();
    }

    /**
     * 发送消息
     */
    public void sendMsg(String msg) {
        webSocket.send(msg);
    }

    /**
     * 关闭连接
     *
     * @param code   关闭发送码
     * @param reason 关闭原因
     */
    public void closeConnect(int code, String reason) {
        webSocket.close(code, reason);
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public boolean isSocketConnect() {
        return isSocketConnect;
    }

    public void setSocketConnect(boolean socketConnect) {
        isSocketConnect = socketConnect;
    }

    public OkHttpClient getWebSocketClient() {
        return webSocketClient;
    }

    public void setWebSocketClient(OkHttpClient webSocketClient) {
        this.webSocketClient = webSocketClient;
    }
}
