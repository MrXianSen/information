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
 * ���������� �ṩ�����������������ֱ�����ӣ���ȡ����
 *
 * @author �Ž���
 **************************************************/
public class HttpUtil {
    /*************************************************
     * ���ݶ�����
     *************************************************/
    public static final String BASE_URL = "http://10.0.2.2:8080/";
    public static User loginUser;

    /***********************************************
     * �ӷ�������ȡ��Ϣ��Information������
     *
     * @param page ҳ��
     * @return ����JsonArray��������
     **************************************************/
    @SuppressLint("NewApi")
    public static JSONArray getInformationFromServer(int page) {
        String result = null;
        JSONArray jsonArray = null;
        String url = BASE_URL
                + "information.do?action=sendInformationsToClient&page=" + page;
        // ����http�������
        HttpGet request = new HttpGet(url);
        try {
            // ִ������
            HttpResponse response = new DefaultHttpClient().execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
            } else {
                System.out.println("��������Ӧʧ�ܡ�������");
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
     * ��ȡ���ۣ�Comment������
     **************************************************/
    @SuppressLint("NewApi")
    public static JSONArray getCommentFromServer(String infoID) {
        String result = null;
        JSONArray jsonArray = null;
        String url = BASE_URL + "informationComment.do?action=commentList&infoID=" + infoID;
        // ����http�������
        HttpGet request = new HttpGet(url);
        try {
            HttpResponse response = new DefaultHttpClient().execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
            } else {
                System.out.println("��������Ӧʧ�ܡ�������");
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
     * ��ȡ���ݿ�������(Information)��������
     *
     * @return ���ط��������ݿ��е���Ϣ��¼��
     *************************************************/
    public static int getTotalRecord() {
        String result = null;
        int totalRecord = 0;
        String url = BASE_URL + "information.do?action=sendTotalRecordToClient";
        // ��ȡrequest����
        HttpGet request = new HttpGet(url);
        try {
            // ִ������
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
     * ��ȡ���Ӷ���
     *
     * @param url URL��ַ
     * @return ����HttpURLConnection����
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
     * ��װ����������
     *
     * @param params Map���͵����ݼ�
     * @param encode ���뷽ʽ
     * @return ����StringBuilder����ʵ��
     *************************************************/
    private static StringBuffer _getRequestData(Map<String, String> params,
                                                String encode) {
        StringBuffer stringBuffer = new StringBuffer(); // �洢��װ�õ���������Ϣ
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey()).append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1); // ɾ������һ��"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    /*************************************************
     * ����������˷��ص�����
     * ��Streamת��ΪString
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
     * ��������������󲢻�ȡ���صĶ���
     *
     * @param params ����Ĳ���
     * @param encode �����ı����ʽ
     * @param url    �����·��
     * @return ����JSONArray����
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
                // ���ش���������
                return dealResponseResult(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /*************************************************
     * �û�ע��
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
     * ��¼����
     *
     * @param username �û���
     * @param password ����
     * @return ���ص�¼״̬
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
     * �޸��û���Ϣ
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
     * �޸��û�����
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
     * ��������
     *
     * @param comment
     * @return �ɹ���1��������0��
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
