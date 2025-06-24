package com.example.hanoconnectadmin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import de.hdodenhof.circleimageview.CircleImageView; // Import CircleImageView

public class EditOrganizationProfileActivity extends AppCompatActivity {

    private CircleImageView ivOrganizationLogo;
    private TextInputEditText etEditOrgName;
    private TextInputEditText etEditOrgContactPerson;
    private TextInputEditText etEditOrgPhoneNumber;
    private TextInputEditText etEditOrgAddress;
    private TextInputEditText etEditOrgWebsite;
    private TextInputEditText etEditOrgDescription;
    private Button btnSaveOrganizationProfile;

    // Ví dụ về dữ liệu có sẵn (trong thực tế sẽ tải từ API)
    private String currentOrgName = "Tổ chức Bảo vệ Môi trường Xanh Hà Nội";
    private String currentContactPerson = "Người liên hệ Tổ chức Xanh";
    private String currentPhoneNumber = "02412345678";
    private String currentAddress = "Số 10, Ngõ 1, Đường Xanh, Đống Đa, Hà Nội";
    private String currentWebsite = "https://tochucxanhhanoi.org";
    private String currentDescription = "Chúng tôi cam kết bảo vệ và cải thiện môi trường sống tại Hà Nội.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_organization_profile);

        // Ánh xạ các thành phần UI
        ivOrganizationLogo = findViewById(R.id.ivOrganizationLogo);
        etEditOrgName = findViewById(R.id.etEditOrgName);
        etEditOrgContactPerson = findViewById(R.id.etEditOrgContactPerson);
        etEditOrgPhoneNumber = findViewById(R.id.etEditOrgPhoneNumber);
        etEditOrgAddress = findViewById(R.id.etEditOrgAddress);
        etEditOrgWebsite = findViewById(R.id.etEditOrgWebsite);
        etEditOrgDescription = findViewById(R.id.etEditOrgDescription);
        btnSaveOrganizationProfile = findViewById(R.id.btnSaveOrganizationProfile);

        // Đổ dữ liệu hiện tại vào các trường (trong thực tế sẽ từ API)
        loadOrganizationData();

        // Xử lý sự kiện nút Lưu Thay Đổi
        btnSaveOrganizationProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOrganizationProfile();
            }
        });
    }

    private void loadOrganizationData() {
        etEditOrgName.setText(currentOrgName);
        etEditOrgContactPerson.setText(currentContactPerson);
        etEditOrgPhoneNumber.setText(currentPhoneNumber);
        etEditOrgAddress.setText(currentAddress);
        etEditOrgWebsite.setText(currentWebsite);
        etEditOrgDescription.setText(currentDescription);
        // ivOrganizationLogo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_default_organization_logo)); // Tải logo thực tế nếu có
    }

    private void saveOrganizationProfile() {
        String orgName = etEditOrgName.getText().toString().trim();
        String contactPerson = etEditOrgContactPerson.getText().toString().trim();
        String phoneNumber = etEditOrgPhoneNumber.getText().toString().trim();
        String address = etEditOrgAddress.getText().toString().trim();
        String website = etEditOrgWebsite.getText().toString().trim();
        String description = etEditOrgDescription.getText().toString().trim();

        if (orgName.isEmpty() || contactPerson.isEmpty() || phoneNumber.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin bắt buộc.", Toast.LENGTH_LONG).show();
            return;
        }

        // TODO: Validate input data (e.g., phone number, website format)

        // TODO: Gửi dữ liệu cập nhật lên API Back-end
        // Bạn sẽ cần tạo một DTO (Data Transfer Object) để gửi dữ liệu này
        // Ví dụ: OrganizationUpdateProfileDto
        Log.d("EditOrgProfile", "Tên tổ chức: " + orgName);
        Log.d("EditOrgProfile", "Người liên hệ: " + contactPerson);
        Log.d("EditOrgProfile", "SĐT: " + phoneNumber);
        Log.d("EditOrgProfile", "Địa chỉ: " + address);
        Log.d("EditOrgProfile", "Website: " + website);
        Log.d("EditOrgProfile", "Mô tả: " + description);

        Toast.makeText(this, "Cập nhật hồ sơ Tổ chức thành công (Demo)!", Toast.LENGTH_SHORT).show();
        // Sau khi cập nhật thành công, có thể hiển thị thông báo và đóng Activity
        // finish();
    }
}