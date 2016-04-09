package com.albery.util;

import android.annotation.SuppressLint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.albery.Activity.InfoDetail;
import com.albery.entity.Comment;
import com.albery.entity.FeedBackMsg;
import com.albery.entity.User;

/***************************************************
 * 功能描述： 提供联网操作，与服务器直接连接，获取数据
 *
 * @author 张建国
 **************************************************/
public class HttpUtil {
    /*************************************************
     * 数据定义区
     *************************************************/
    public static final String BASE_URL = "http://10.0.2.2:8080/";
    public static User loginUser;

    /***********************************************
     * 从服务器获取消息（Information）数据
     *
     * @param page 页码
     * @return 返回JsonArray对象数据
     **************************************************/
    @SuppressLint("NewApi")
    public static JSONArray getInformationFromServer(int page) {
        String result = null;
        JSONArray jsonArray = null;
        String url = BASE_URL
                + "information.do?action=sendInformationsToClient&page=" + page;
        // 创建http请求对象
        HttpGet request = new HttpGet(url);
        try {
            // 执行请求
            HttpResponse response = new DefaultHttpClient().execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
            } else {
                System.out.println("服务器响应失败。。。。");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (result != null && !result.trim().isEmpty()) {
            try {
                jsonArray = new JSONArray(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonArray;
        }
        return null;
    }

    /*************************************************
     * 获取评论（Comment）数据
     **************************************************/
    @SuppressLint("NewApi")
    public static JSONArray getCommentFromServer(String infoID) {
        String result = null;
        JSONArray jsonArray = null;
        String url = BASE_URL + "informationComment.do?action=commentList&infoID=" + infoID;
        // 创建http请求对象
        HttpGet request = new HttpGet(url);
        try {
            HttpResponse response = new DefaultHttpClient().execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
            } else {
                System.out.println("服务器响应失败。。。。");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (result != null && !result.trim().isEmpty()) {
            try {
                jsonArray = new JSONArray(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonArray;
        }
        return null;
    }

    /*************************************************
     * 获取数据库中数据(Information)的总条数
     *
     * @return 返回服务器数据库中的信息记录数
     *************************************************/
    public static int getTotalRecord() {
        String result = null;
        int totalRecord = 0;
        String url = BASE_URL + "information.do?action=sendTotalRecordToClient";
        // 获取request对象
        HttpGet request = new HttpGet(url);
        try {
            // 执行请求
            HttpResponse response = new DefaultHttpClient().execute(request);

            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
                totalRecord = Integer.parseInt(result);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return totalRecord;
    }

    /*************************************************
     * 获取连接对象
     *
     * @param url URL地址
     * @return 返回HttpURLConnection对象
     *************************************************/
    private static HttpURLConnection getHttpUrlConnection(String url) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) (new URL(url).openConnection());
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    /*************************************************
     * 封装发请求数据
     *
     * @param params Map类型的数据集
     * @param encode 编码方式
     * @return 返回StringBuilder对象实例
     *************************************************/
    private static StringBuffer _getRequestData(Map<String, String> params,
                                                String encode) {
        StringBuffer stringBuffer = new StringBuffer(); // 存储封装好的请求体信息
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey()).append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1); // 删除最后的一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    /*************************************************
     * 处理服务器端返回的数据
     * 将Stream转化为String
     *************************************************/
    private static String dealResponseResult(InputStream inputStream) {
        String result = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int length = 0;
        try {
            while ((length = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        result = new String(byteArrayOutputStream.toByteArray());
        return result;
    }

    /*************************************************
     * 象服务器发送请求并获取返回的对象
     *
     * @param params 请求的参数
     * @param encode 参数的编码格式
     * @param url    请求的路径
     * @return 返回JSONArray对象
     *************************************************/
    public static String _requestAndGetResponse(Map<String, String> params,
                                                String encode, String url) {
        byte[] data = _getRequestData(params, encode).toString().getBytes();
        HttpURLConnection httpUrlConnection = getHttpUrlConnection(url);
        try {
            OutputStream ouputStream = httpUrlConnection.getOutputStream();
            ouputStream.write(data);
            int responseCode = httpUrlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = httpUrlConnection.getInputStream();
                // 返回处理后的数据
                return dealResponseResult(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /*************************************************
     * 用户注册
     *
     * @param username
     * @param password
     * @param gender
     * @param school
     * @param icon
     * @return
     *************************************************/
    public static boolean register(String username, String password,
                                   String gender, String school, String icon) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("pwd", password);
        params.put("icon", icon);
        params.put("school", school);
        params.put("gender", gender);
        StringBuilder url = new StringBuilder(BASE_URL);
        url.append("student.do?action=addStudent");
        String strResponse = _requestAndGetResponse(params, "utf-8",
                url.toString());
        try {
            JSONObject jsonObj = new JSONObject(strResponse);
            if (!jsonObj.get("code").equals(FeedBackMsg.USER_EXIST.toString())) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*************************************************
     * 登录操作
     *
     * @param username 用户名
     * @param password 密码
     * @return 返回登录状态
     *************************************************/
    public static boolean login(String username, String password) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("pwd", password);
        StringBuilder url = new StringBuilder(BASE_URL);
        url.append("student.do?action=login");
        String strResponse = _requestAndGetResponse(params, "utf-8",
                url.toString());
        try {
            JSONObject jsonObj = new JSONObject(strResponse);
            if (jsonObj.get("code").equals(FeedBackMsg.USER_EXIST.toString())) {
                loginUser = new User(Integer.parseInt(jsonObj.get("stuIcon")
                        .toString()), jsonObj.get("stuName").toString(),
                        jsonObj.get("stuSchool").toString(), jsonObj.get(
                        "stuGender").toString(), jsonObj.get("stuSign")
                        .toString());
                loginUser.setID(jsonObj.getString("stuID"));
                loginUser.setPassword(jsonObj.getString("stuPwd"));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /*************************************************
     * 修改用户信息
     *
     * @param user
     * @return
     *************************************************/
    public static boolean modifyUserInfo(User user) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("stuID", user.getID());
        params.put("stuName", user.getName());
        params.put("stuGender", user.getGender());
        params.put("stuHead", String.valueOf(user.getHead()));
        params.put("stuSign", user.getSign());
        params.put("stuSchool", user.getSchool());
        StringBuilder url = new StringBuilder(BASE_URL);
        url.append("student.do?action=modifyStudentInfo");
        String strResponse = _requestAndGetResponse(params, "utf-8",
                url.toString());
        try {
            JSONObject jsonObj = new JSONObject(strResponse);
            if (jsonObj.get("Code").equals("1")) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*************************************************
     * 修改用户密码
     *
     * @param user
     * @return
     *************************************************/
    public static boolean modifyPwd(String newPwd, String oldPwd, String userID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("stuID", userID);
        params.put("oldPwd", oldPwd);
        params.put("newPwd", newPwd);
        StringBuilder url = new StringBuilder(BASE_URL);
        url.append("student.do?action=modifyPassword");
        String strResponse = _requestAndGetResponse(params, "utf-8",
                url.toString());
        try {
            JSONObject jsonObj = new JSONObject(strResponse);
            if (jsonObj.get("Code").equals("1")) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*************************************************
     * 保存评论
     *
     * @param comment
     * @return 成功“1”，否则“0”
     **************************************************/
    public static int saveComment(Comment comment) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userID", comment.getUserID());
        params.put("userName", comment.getName());
        params.put("infoID", comment.getInfoId());
        params.put("content", comment.getContent());
        StringBuilder url = new StringBuilder(BASE_URL);
        url.append("informationComment.do?action=addComment");
        String strResponse = _requestAndGetResponse(params, "utf-8",
                url.toString());
        try {
            JSONObject jsonObj = new JSONObject(strResponse);
            if (jsonObj.get("code").equals("1")) {
                return 1;
            }
        } catch (JSONException e) {
            return 0;
        }
        return 0;
    }


}
