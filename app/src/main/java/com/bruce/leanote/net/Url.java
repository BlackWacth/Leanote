package com.bruce.leanote.net;

import android.text.TextUtils;
import android.view.TextureView;

import java.util.Iterator;
import java.util.Map;

/**
 * 所有URL地址常量
 * Created by Bruce on 2017/5/9.
 */
class Url {

    /**基地址 */
    public static final String BASE_URL = "https://leanote.com/api";


    // ------------ Auth 登录与注册 --------------
    /**登录 */
    public static final String LOGIN = BASE_URL + "/auth/login";

    /**注销 */
    public static final String LOGOUT = BASE_URL + "/auth/logout";

    /**注册 */
    public static final String REGISTER = BASE_URL + "/auth/register";


    // ------------ User 用户 --------------
    /**用户信息 */
    public static final String USER_INFO = BASE_URL + "/user/info";

    /**修改用户名 */
    public static final String UPDATE_USER_NAME= BASE_URL + "/user/updateUsername";

    /**修改密码 */
    public static final String UPDATE_PASSWORD = BASE_URL + "/user/updatePwd";

    /**修改头像 */
    public static final String UPDATE_LOGO = BASE_URL + "/user/updateLogo";

    /**最新同步状态 */
    public static final String SYNC_STATE = BASE_URL + "/user/getSyncState";


    // ------------ Notebook 笔记本 --------------
    /**得到需要同步的笔记本 */
    public static final String SYNC_NOTEBOOKS = BASE_URL + "/notebook/getSyncNotebooks";

    /**得到所有笔记本 */
    public static final String GET_NOTEBOOKS = BASE_URL + "/notebook/getNotebooks";

    /**添加笔记本 */
    public static final String ADD_NOTEBOOK = BASE_URL + "/notebook/addNotebook";

    /**修改笔记本 */
    public static final String UPDATE_NOTEBOOK = BASE_URL + "/notebook/updateNotebook";

    /**删除笔记本 */
    public static final String DELETE_NOTEBOOK = BASE_URL + "/notebook/deleteNotebook";


    // ------------ Note 笔记 --------------
    /**同步的笔记 */
    public static final String SYNC_NOTES = BASE_URL + "/note/getSyncNotes";

    /**获得某笔记本下的笔记(无内容) */
    public static final String GET_NOTES = BASE_URL + "/note/getNotes";

    /**获得笔记与内容 */
    public static final String GET_NOTE_AND_CONTENT = BASE_URL + "/note/getNoteAndContent";

    /**获得笔记内容 */
    public static final String GET_NOTE_CONTENT = BASE_URL + "/note/getNoteContent";

    /**添加笔记 */
    public static final String ADD_NOTE = BASE_URL + "/note/addNote";

    /**更新笔记 */
    public static final String UPDATE_NOTE = BASE_URL + "/note/updateNote";

    /**彻底删除笔记 */
    public static final String DELETE_NOTE = BASE_URL + "/note/deleteTrash";


    // ------------ Tag 标签 --------------
    /**同步标签 */
    public static final String SYNC_TAGS = BASE_URL + "/tag/getSyncTags";

    /**添加标签 */
    public static final String ADD_TAG = BASE_URL + "/tag/addTag";

    /**删除标签 */
    public static final String DELETE_TAG = BASE_URL + "/tag/deleteTag";


    // ------------ File 文件(获取图片, 附件) --------------
    /**获取图片 */
    public static final String GET_IMAGE = BASE_URL + "/file/getImage";

    /**获取附件 */
    public static final String GET_ATTACH = BASE_URL + "/file/getAttach";

    /**获取所有附件 */
    public static final String GET_ALL_ATTACHS = BASE_URL + "/file/getAllAttachs";

    /**
     * 地址拼接
     * @param url 地址
     * @param params 参数
     * @return 参数拼接后的地址
     */
    static String splitJointUrl(String url, Map<String, String> params) {
        if(url == null) {
            return "";
        }

        if(params == null) {
            return url;
        }

        StringBuilder builder = null;

        Iterator<String> iterator = params.keySet().iterator();
        String key;
        while (iterator.hasNext()) {
            if(builder == null) {
                builder = new StringBuilder();
                builder.append("?");
            } else {
                builder.append("&");
            }
            key = iterator.next();
            builder.append(key);
            builder.append("=");
            builder.append(params.get(key));
        }
        return url + (builder != null ? builder.toString() : "");
    }

    /**
     * 拼接token
     * @param url 地址
     * @param token Token
     * @return 加上token的地址
     */
    static String splitJoinToken(String url, String token) {
        if(TextUtils.isEmpty(url)) {
            return null;
        }

        if(TextUtils.isEmpty(token)) {
            return url;
        }
        if(!url.contains("?")) {
            url = url + "?token=" + token;
        } else {
            url = url + "&token=" + token;
        }
        return url;
    }
}
