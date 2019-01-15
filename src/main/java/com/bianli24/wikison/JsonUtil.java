package com.bianli24.wikison;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JsonUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);


    public static void writeVersion() {
        try {
            File file = new File("version.dat");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWritter = new FileWriter(file.getName());
            fileWritter.write(getVersion());

            fileWritter.flush();
            fileWritter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加过之后的Version
     * @return
     */
    public static String getVersion( ) {
        String content = getContent();
        String jsonStr = content.replace("\\", "");
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        jsonObject = jsonObject.getJSONObject("version");
        return String.valueOf(Integer.parseInt(jsonObject.get("number").toString()) + 1);
    }

    /**
     * 获取文件内容
     * @return
     */
    public static String getContent() {
        try {
            BufferedReader in = new BufferedReader(new FileReader("content.json"));
            String str = in.readLine();
            return str;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return "";
    }
}
