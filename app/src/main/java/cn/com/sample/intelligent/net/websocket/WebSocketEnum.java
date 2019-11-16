package cn.com.sample.intelligent.net.websocket;

/**
 * Description:
 * Creator : wangminjian
 * Create time : 2019/11/16.
 */
public interface WebSocketEnum {

    /// <summary>
    /// 上线
    /// </summary>
    int LogIn = 0;
    /// <summary>
    /// 下线
    /// </summary>
    int LogOut = 1;
    /// <summary>
    /// 紧急报警关闭
    /// </summary>
    int JinJiBaoJingGuanBi = 2;
    /// <summary>
    /// 紧急报警开启
    /// </summary>
    int JinJiBaoJingKaiQi = 3;
    /// <summary>
    /// 加热关闭
    /// </summary>
    int JiaReGuanBi = 4;
    /// <summary>
    /// 加热开启
    /// </summary>
    int JiaReKaiQi = 5;
    /// <summary>
    /// 制冷关闭
    /// </summary>
    int ZhiLengGuanBi = 6;
    /// <summary>
    /// 制冷开启
    /// </summary>
    int ZhiLengKaiQi = 7;
    /// <summary>
    /// 除湿关闭
    /// </summary>
    int ChuShiGuanBi = 8;
    /// <summary>
    /// 除湿开启
    /// </summary>
    int ChuShiKaiQi = 9;
    /// <summary>
    /// 加湿关闭
    /// </summary>
    int JiaShiGuanBi = 10;
    /// <summary>
    /// 加湿开启
    /// </summary>
    int JiaShiKaiQi = 11;
    /// <summary>
    /// 净化关闭
    /// </summary>
    int JingHuaGuanBi = 12;
    /// <summary>
    /// 净化开启
    /// </summary>
    int JingHuaKaiQi = 13;
    /// <summary>
    /// 通风关闭
    /// </summary>
    int TongFengGuanBi = 14;
    /// <summary>
    /// 通风开启
    /// </summary>
    int TongFengKaiQi = 15;
    /// <summary>
    /// 布防关闭
    /// </summary>
    int BuFangGuanBi = 16;
    /// <summary>
    /// 布防开启
    /// </summary>
    int BuFangKaiQi = 17;
    /// <summary>
    /// 手动模式
    /// </summary>
    int ShouDongMoShi = 18;
    /// <summary>
    /// 自动模式
    /// </summary>
    int ZiDongMoShi = 19;
    /// <summary>
    /// 设置温湿度
    /// </summary>
    int SheZhiWenShiDu = 20;
    /// <summary>
    /// 获取库房列表
    /// </summary>
    int HuoQuKuFangLieBiao = 21;
    /// <summary>
    /// 返回库房列表
    /// </summary>
    
    int FanHuiHuoQuKuFangLieBiao = 22;
    /// <summary>
    /// 获取实时数据
    /// </summary>
    int HuoQuShiShiShuJu = 23;
    /// <summary>
    /// 返回实时数据
    /// </summary>
    int FanHuiShiShiShuJu = 24;
    /// <summary>
    /// 获取报警信息
    /// </summary>
    int HuoQuBaoJingXinXi = 25;
    /// <summary>
    /// 返回报警信息
    /// </summary>
    int FanHuiBaoJingXinXi = 26;
    /// <summary>
    /// 获取日报表
    /// </summary>
    int HuoQuRiBaoBiao = 27;
    /// <summary>
    /// 返回日报表
    /// </summary>
    int FanHuiRiBaoBiao = 28;
    /// <summary>
    /// 获取月报表
    /// </summary>
    int HuoQuYueBaoBiao = 29;
    /// <summary>
    /// 返回月报表
    /// </summary>
    int FanHuiYueBaoBiao = 30;
    /// <summary>
    /// 错误信息
    /// </summary>
    int Error = 31;
    /// <summary>
    /// 刷新门禁监听服务
    /// </summary>
    int MenJinXiuGai = 32;
    /// <summary>
    /// 新的门禁刷卡服务
    /// </summary>
    int MenJinShuaKa = 33;

}
