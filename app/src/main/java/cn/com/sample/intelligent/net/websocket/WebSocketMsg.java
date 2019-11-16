package cn.com.sample.intelligent.net.websocket;

/**
 * Description:
 * Creator : wangminjian
 * Create time : 2019/11/16.
 */
public class WebSocketMsg {

    private String Head;
    private String Type;
    private String Me;
    private String To;
    private String Data;

    public String getHead() {
        return Head;
    }

    public void setHead(String head) {
        Head = head;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getMe() {
        return Me;
    }

    public void setMe(String me) {
        Me = me;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }
}
