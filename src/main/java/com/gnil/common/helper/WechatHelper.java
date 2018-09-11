package com.gnil.common.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.CollectionUtils;

import java.io.IOException;

/**
 * 微信相关帮助类
 */
public class WechatHelper {

    /**
     * 微信开放平台获取Code时本地应用重定向到微信登录访问的页面时页面中有个二维码，此方法即为获取那个二维码图片的URL地址
     * @param appId 必须，应用唯一标识
     * @param redirectUri 必须，请使用urlEncode对链接进行处理
     * @param state 非必须，用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击），建议第三方带上该参数，可设置为简单的随机数加session进行校验
     * @return 微信登录二维码URL
     */
    public static String getWechatLoginQRCodeURL(String appId, String redirectUri, String state) {
        String url = "https://open.weixin.qq.com/connect/qrconnect?appid=" +
                appId +"&redirect_uri=" + redirectUri + "&response_type=code&scope=snsapi_login" +
                "&state=" + state + "#wechat_redirect";
        Document doc = null;
        try {
            doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(30000).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements elements = doc.getElementsByTag("img");

        String src = null;
        if (!CollectionUtils.isEmpty(elements)) {
            Element element = elements.get(0);
            src = element.attr("src");
            if (!src.contains("http")) {
                src = "https://open.weixin.qq.com" + src;
            }
        }

        return src;
    }
}
