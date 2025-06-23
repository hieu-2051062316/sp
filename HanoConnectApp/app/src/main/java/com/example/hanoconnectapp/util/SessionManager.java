package com.example.hanoconnectapp.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "HanoConnectAppPref";
    private static final String KEY_USER_ID = "user_id";
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
     * Lưu user ID vào SharedPreferences sau khi đăng nhập thành công.
     * @param userId ID của người dùng
     */
    public void createLoginSession(int userId) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, userId);
        editor.commit();
    }

    /**
     * Lấy User ID đã được lưu.
     * @return Trả về User ID, hoặc -1 nếu không tìm thấy.
     */
    public int getUserId() {
        return pref.getInt(KEY_USER_ID, -1); // -1 là giá trị mặc định nếu không tìm thấy key
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
        // Sau khi logout, có thể điều hướng người dùng về màn hình Login
        // Intent i = new Intent(_context, LoginActivity.class);
        // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // _context.startActivity(i);
    }
}
