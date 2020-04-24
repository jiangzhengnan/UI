package com.ng.ui.study.anim;

import android.content.Context;
import android.content.res.AssetManager;

import com.ng.nguilib.utils.LogUtils;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * 描述:xml|json动画文件解析器
 * xml使用sax解析
 * <p>
 * https://blog.csdn.net/bzlj2912009596/article/details/80262191
 *
 * @author Jzn
 * @date 2020-04-24
 */
public class PieResolver {

    private static PieResolver Instance;

    private PieResolver() {
    }

    public static PieResolver getInstance() {
        if (Instance == null) {
            synchronized (PieResolver.class) {
                if (Instance == null) {
                    Instance = new PieResolver();
                }
            }
        }
        return Instance;
    }

    public List<PieAnimShape> readShapesFromAssert(Context context, String fileName) {
        String content = readAssertResource(context, fileName);
        LogUtils.INSTANCE.d("assert文件 " + fileName + " 内容:" + content);
        if (fileName.contains("xml")) {
            return readShapesFromXml(content);
        } else if (fileName.contains("json")) {
            return readShapesFromJson(context, content);
        } else {
            return null;
        }
    }

    public List<PieAnimShape> readShapesFromJson(Context context, String content) {
        return null;
    }

    public List<PieAnimShape> readShapesFromXml(String content) {
        List<PieAnimShape> list = new ArrayList<>();
        try {
            //通过获取到的InputStream来得到InputSource实例
            InputSource inputSource = new InputSource(new StringReader(content));

            SAXParserFactory spf = SAXParserFactory.newInstance();
            //通过SAXParserFactory得到SAXParser的实例
            SAXParser sp = spf.newSAXParser();
            //通过SAXParser得到XMLReader的实例
            XMLReader xr = sp.getXMLReader();
            //初始化自定义的类MySaxHandler的变量msh，将beautyList传递给它，以便装载数据
            MySaxHandler msh = new MySaxHandler(list);
            //将对象msh传递给xr
            xr.setContentHandler(msh);
            //调用xr的parse方法解析输入流
            xr.parse(inputSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    private String readAssertResource(Context context, String strAssertFileName) {
        AssetManager assetManager = context.getAssets();
        String strResponse = "";
        try {
            InputStream ims = assetManager.open(strAssertFileName);
            strResponse = getStringFromInputStream(ims);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strResponse;
    }

    private String getStringFromInputStream(InputStream a_is) {
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(a_is))) {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException ignored) {
        }
        return sb.toString();
    }
}
