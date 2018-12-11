package com.channabelle.test;

import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        String encoding = "UTF-8";
        String path = (null != args && 0 < args.length) ? args[0] : "/Users/charlie/workspace/alibaba/aliyun-log/AAA.log";

        File file = new File(path);
        if(file.isFile() && file.exists()) {
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            ArrayList<String> content = new ArrayList<String>();
            while((lineTxt = bufferedReader.readLine()) != null) {
                content.add(lineTxt);
            }
            read.close();

            for(int m = 0; m < content.size(); m++) {
                lineTxt = content.get(content.size() - 1 - m);
                // System.out.println(lineTxt);
                JSONObject json = JSONObject.fromObject(lineTxt);
                lineTxt = String.format("[%s] [%s] [%s, %s] [%s] [%s]", json.getString("time"), json.getString("__topic__"), json.getString("level"), json.getString("thread"), getJavaFileNameAndLine(json.getString("location")), json.getString("message"));
                System.out.println(lineTxt);
            }
        } else {
            System.out.println("找不到指定的文件");
        }
        return;
    }

    private static String getJavaFileNameAndLine(String s) {
        return s.substring(s.indexOf("(") + 1, s.indexOf(")"));
    }
}
