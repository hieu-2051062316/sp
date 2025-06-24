package com.example.hanoconnectapp.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "HanoConnectAppPref";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_ORGANIZATION_ID = "organization_id"; // Key mới
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final int PRIVATE_MODE = 0;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Lưu thông tin session sau khi đăng nhập thành công.
     * @param userId ID của người dùng
     * @param organizationId ID của tổ chức (có thể là null)
     */
    public void createLoginSession(int userId, Integer organizationId) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, userId);

        // Chỉ lưu organizationId nếu nó không null
        if (organizationId != null) {
            editor.putInt(KEY_ORGANIZATION_ID, organizationId);
        }

        editor.commit();
    }

    /**
     * Lấy User ID đã được lưu.
     * @return Trả về User ID, hoặc -1 nếu không tìm thấy.
     */
    public int getUserId() {
        return pref.getInt(KEY_USER_ID, -1);
    }

    /**
     * Lấy Organization ID đã được lưu.
     * @return Trả về Organization ID, hoặc -1 nếu không tìm thấy.
     */
    public int getOrganizationId() {
        return pref.getInt(KEY_ORGANIZATION_ID, -1);
    }


    /**
     * Kiểm tra xem người dùng đã đăng nhập hay chưa.
     */
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    /**
     * Xóa thông tin phiên đăng nhập (dùng cho chức năng logout).
     */
    public void logoutUser() {
        editor.clear();
        editor.commit();
    }
}
