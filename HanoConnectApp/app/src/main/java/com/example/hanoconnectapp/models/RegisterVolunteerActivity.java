package com.example.hanoconnectapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RegisterVolunteerActivity extends AppCompatActivity {

    private TextInputEditText etVolunteerFullName;
    private TextInputEditText etVolunteerEmail;
    private TextInputEditText etVolunteerPassword;
    private TextInputEditText etVolunteerConfirmPassword;
    private TextInputEditText etVolunteerDateOfBirth;
    private TextInputEditText etVolunteerPhoneNumber;
    private TextInputEditText etVolunteerDistrict;
    private LinearLayout llVolunteerSkills;
    private LinearLayout llVolunteerCauses;
    private Button btnRegisterVolunteer;

    private List<String> availableSkills = Arrays.asList(
            "Dạy học", "Thiết kế đồ họa", "IT / Lập trình",
            "Tổ chức sự kiện", "Chăm sóc sức khỏe", "Truyền thông / Marketing",
            "Biên phiên dịch", "Vận chuyển / Hậu cần", "Chụp ảnh / Quay phim"
    );
    private List<String> availableCauses = Arrays.asList(
            "Bảo vệ Môi trường", "Giáo dục", "Chăm sóc Trẻ em",
            "Hỗ trợ Người già", "Y tế và Sức khỏe", "Văn hóa và Nghệ thuật",
            "Phát triển Cộng đồng", "Cứu trợ thiên tai"
    );

    private List<String> selectedSkills = new ArrayList<>();
    private List<String> selectedCauses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_volunteer);

        // Ánh xạ các thành phần UI
        etVolunteerFullName = findViewById(R.id.etVolunteerFullName);
        etVolunteerEmail = findViewById(R.id.etVolunteerEmail);
        etVolunteerPassword = findViewById(R.id.etVolunteerPassword);
        etVolunteerConfirmPassword = findViewById(R.id.etVolunteerConfirmPassword);
        etVolunteerDateOfBirth = findViewById(R.id.etVolunteerDateOfBirth);
        etVolunteerPhoneNumber = findViewById(R.id.etVolunteerPhoneNumber);
        etVolunteerDistrict = findViewById(R.id.etVolunteerDistrict);
        llVolunteerSkills = findViewById(R.id.llVolunteerSkills);
        llVolunteerCauses = findViewById(R.id.llVolunteerCauses);
        btnRegisterVolunteer = findViewById(R.id.btnRegisterVolunteer);

        // Khởi tạo DatePicker cho Ngày sinh
        etVolunteerDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(etVolunteerDateOfBirth);
            }
        });

        // Tạo Checkbox động cho Kỹ năng và Lĩnh vực quan tâm
        populateCheckboxes(llVolunteerSkills, availableSkills, selectedSkills);
        populateCheckboxes(llVolunteerCauses, availableCauses, selectedCauses);

        // Xử lý sự kiện nút Đăng ký
        btnRegisterVolunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerVolunteer();
            }
        });
    }

    private void showDatePickerDialog(final TextInputEditText dateEditText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                    dateEditText.setText(selectedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void populateCheckboxes(LinearLayout parentLayout, List<String> dataList, List<String> selectedItemsList) {
        parentLayout.removeAllViews(); // Xóa các CheckBox mẫu trong XML nếu có
        for (String item : dataList) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(item);
            checkBox.setTextSize(16);
            checkBox.setPadding(8, 8, 8, 8); // Example padding

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedItemsList.add(item);
                } else {
                    selectedItemsList.remove(item);
                }
            });
            parentLayout.addView(checkBox);
        }
    }

    private void registerVolunteer() {
        String fullName = etVolunteerFullName.getText().toString().trim();
        String email = etVolunteerEmail.getText().toString().trim();
        String password = etVolunteerPassword.getText().toString().trim();
        String confirmPassword = etVolunteerConfirmPassword.getText().toString().trim();
        String dateOfBirth = etVolunteerDateOfBirth.getText().toString().trim();
        String phoneNumber = etVolunteerPhoneNumber.getText().toString().trim();
        String district = etVolunteerDistrict.getText().toString().trim();

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                dateOfBirth.isEmpty() || phoneNumber.isEmpty() || district.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin bắt buộc.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu và xác nhận mật khẩu không khớp.", Toast.LENGTH_LONG).show();
            return;
        }

        // TODO: Validate email format, phone number format (OTP)

        // TODO: Gửi dữ liệu đăng ký lên API Back-end
        // Bạn sẽ cần tạo một DTO (Data Transfer Object) để gửi dữ liệu này
        // Ví dụ: VolunteerRegistrationDto
        Log.d("RegisterVolunteer", "Họ tên: " + fullName);
        Log.d("RegisterVolunteer", "Email: " + email);
        Log.d("RegisterVolunteer", "Ngày sinh: " + dateOfBirth);
        Log.d("RegisterVolunteer", "SĐT: " + phoneNumber);
        Log.d("RegisterVolunteer", "Quận/Huyện: " + district);
        Log.d("RegisterVolunteer", "Kỹ năng đã chọn: " + selectedSkills.toString());
        Log.d("RegisterVolunteer", "Lĩnh vực quan tâm đã chọn: " + selectedCauses.toString());

        Toast.makeText(this, "Đăng ký TNV thành công (Demo)!", Toast.LENGTH_SHORT).show();
        // Sau khi đăng ký thành công, có thể chuyển hướng người dùng
        // finish(); // Đóng Activity hiện tại
    }
}