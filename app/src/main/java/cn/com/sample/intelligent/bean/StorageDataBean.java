package cn.com.sample.intelligent.bean;

import java.util.List;

/**
 * Description:
 * Creator : wangminjian
 * Create time : 2019/11/16.
 */
public class StorageDataBean {

    private boolean ZhiLengZhuangTai;
    private boolean ZhiReZhuangTai;
    private boolean ChuShiZhuangTai;
    private boolean JiaShiZhuangTai;
    private boolean ZiDongMoShi;
    private boolean TongFengZhuangTai;
    private boolean JingHuaZhuangTai;
    private boolean JinJiBaoJing;
    private boolean BuFangZhuangTai;
    private DataInfo DataInfo;
    private String ShuJuShiJian;
    private List<BJInfo> BJInfo;

    public boolean isZhiLengZhuangTai() {
        return ZhiLengZhuangTai;
    }

    public void setZhiLengZhuangTai(boolean zhiLengZhuangTai) {
        ZhiLengZhuangTai = zhiLengZhuangTai;
    }

    public boolean isZhiReZhuangTai() {
        return ZhiReZhuangTai;
    }

    public void setZhiReZhuangTai(boolean zhiReZhuangTai) {
        ZhiReZhuangTai = zhiReZhuangTai;
    }

    public boolean isChuShiZhuangTai() {
        return ChuShiZhuangTai;
    }

    public void setChuShiZhuangTai(boolean chuShiZhuangTai) {
        ChuShiZhuangTai = chuShiZhuangTai;
    }

    public boolean isJiaShiZhuangTai() {
        return JiaShiZhuangTai;
    }

    public void setJiaShiZhuangTai(boolean jiaShiZhuangTai) {
        JiaShiZhuangTai = jiaShiZhuangTai;
    }

    public boolean isZiDongMoShi() {
        return ZiDongMoShi;
    }

    public void setZiDongMoShi(boolean ziDongMoShi) {
        ZiDongMoShi = ziDongMoShi;
    }

    public boolean isTongFengZhuangTai() {
        return TongFengZhuangTai;
    }

    public void setTongFengZhuangTai(boolean tongFengZhuangTai) {
        TongFengZhuangTai = tongFengZhuangTai;
    }

    public boolean isJingHuaZhuangTai() {
        return JingHuaZhuangTai;
    }

    public void setJingHuaZhuangTai(boolean jingHuaZhuangTai) {
        JingHuaZhuangTai = jingHuaZhuangTai;
    }

    public boolean isJinJiBaoJing() {
        return JinJiBaoJing;
    }

    public void setJinJiBaoJing(boolean jinJiBaoJing) {
        JinJiBaoJing = jinJiBaoJing;
    }

    public boolean isBuFangZhuangTai() {
        return BuFangZhuangTai;
    }

    public void setBuFangZhuangTai(boolean buFangZhuangTai) {
        BuFangZhuangTai = buFangZhuangTai;
    }

    public cn.com.sample.intelligent.bean.DataInfo getDataInfo() {
        return DataInfo;
    }

    public void setDataInfo(cn.com.sample.intelligent.bean.DataInfo dataInfo) {
        DataInfo = dataInfo;
    }

    public String getShuJuShiJian() {
        return ShuJuShiJian;
    }

    public void setShuJuShiJian(String shuJuShiJian) {
        ShuJuShiJian = shuJuShiJian;
    }

    public List<cn.com.sample.intelligent.bean.BJInfo> getBJInfo() {
        return BJInfo;
    }

    public void setBJInfo(List<cn.com.sample.intelligent.bean.BJInfo> BJInfo) {
        this.BJInfo = BJInfo;
    }
}
