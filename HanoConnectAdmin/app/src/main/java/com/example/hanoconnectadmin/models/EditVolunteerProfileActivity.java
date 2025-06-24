package com.example.hanoconnectadmin;

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

import de.hdodenhof.circleimageview.CircleImageView; // Import CircleImageView

public class EditVolunteerProfileActivity extends AppCompatActivity {

    private CircleImageView ivVolunteerProfileImage;
    private TextInputEditText etEditVolunteerFullName;
    private TextInputEditText etEditVolunteerDateOfBirth;
    private TextInputEditText etEditVolunteerPhoneNumber;
    private TextInputEditText etEditVolunteerDistrict;
    private TextInputEditText etEditVolunteerBio;
    private LinearLayout llEditVolunteerSkills;
    private LinearLayout llEditVolunteerCauses;
    private Button btnSaveVolunteerProfile;

    // Ví dụ về dữ liệu có sẵn (trong thực tế sẽ tải từ API)
    private String currentFullName = "Nguyễn Thị A";
    private String currentDateOfBirth = "10/05/1995";
    private String currentPhoneNumber = "0987654321";
    private String currentDistrict = "Đống Đa";
    private String currentBio = "Yêu thích các hoạt động cộng đồng, đặc biệt là liên quan đến giáo dục và môi trường.";
    private List<String> currentSkills = Arrays.asList("Dạy học", "IT / Lập trình");
    private List<String> currentCauses = Arrays.asList("Giáo dục", "Bảo vệ Môi trường");

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
        setContentView(R.layout.activity_edit_volunteer_profile);

        // Ánh xạ các thành phần UI
        ivVolunteerProfileImage = findViewById(R.id.ivVolunteerProfileImage);
        etEditVolunteerFullName = findViewById(R.id.etEditVolunteerFullName);
        etEditVolunteerDateOfBirth = findViewById(R.id.etEditVolunteerDateOfBirth);
        etEditVolunteerPhoneNumber = findViewById(R.id.etEditVolunteerPhoneNumber);
        etEditVolunteerDistrict = findViewById(R.id.etEditVolunteerDistrict);
        etEditVolunteerBio = findViewById(R.id.etEditVolunteerBio);
        llEditVolunteerSkills = findViewById(R.id.llEditVolunteerSkills);
        llEditVolunteerCauses = findViewById(R.id.llEditVolunteerCauses);
        btnSaveVolunteerProfile = findViewById(R.id.btnSaveVolunteerProfile);

        // Đổ dữ liệu hiện tại vào các trường (trong thực tế sẽ từ API)
        loadVolunteerData();

        // Khởi tạo DatePicker cho Ngày sinh
        etEditVolunteerDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(etEditVolunteerDateOfBirth);
            }
        });

        // Tạo Checkbox động và chọn các mục hiện có
        populateCheckboxes(llEditVolunteerSkills, availableSkills, selectedSkills, currentSkills);
        populateCheckboxes(llEditVolunteerCauses, availableCauses, selectedCauses, currentCauses);

        // Xử lý sự kiện nút Lưu Thay Đổi
        btnSaveVolunteerProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveVolunteerProfile();
            }
        });
    }

    private void loadVolunteerData() {
        etEditVolunteerFullName.setText(currentFullName);
        etEditVolunteerDateOfBirth.setText(currentDateOfBirth);
        etEditVolunteerPhoneNumber.setText(currentPhoneNumber);
        etEditVolunteerDistrict.setText(currentDistrict);
        etEditVolunteerBio.setText(currentBio);
        // ivVolunteerProfileImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_default_avatar)); // Tải ảnh thực tế nếu có
    }

    private void showDatePickerDialog(final TextInputEditText dateEditText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Nếu trường EditText đã có giá trị, phân tích nó để đặt ngày mặc định cho DatePicker
        String existingDate = dateEditText.getText().toString();
        if (!existingDate.isEmpty()) {
            try {
                String[] dateParts = existingDate.split("/");
                day = Integer.parseInt(dateParts[0]);
                month = Integer.parseInt(dateParts[1]) - 1; // Month is 0-indexed
                year = Integer.parseInt(dateParts[2]);
            } catch (Exception e) {
                Log.e("DatePicker", "Error parsing existing date: " + existingDate, e);
                // Fallback to current date if parsing fails
            }
        }

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

    private void populateCheckboxes(LinearLayout parentLayout, List<String> allItems, List<String> selectedItemsTrackingList, List<String> initiallySelectedItems) {
        parentLayout.removeAllViews();
        selectedItemsTrackingList.clear(); // Clear previous selections for re-population

        for (String item : allItems) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(item);
            checkBox.setTextSize(16);
            checkBox.setPadding(8, 8, 8, 8);

            if (initiallySelectedItems.contains(item)) {
                checkBox.setChecked(true);
                selectedItemsTrackingList.add(item); // Add to tracking list if initially selected
            }

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedItemsTrackingList.add(item);
                } else {
                    selectedItemsTrackingList.remove(item);
                }
            });
            parentLayout.addView(checkBox);
        }
    }

    private void saveVolunteerProfile() {
        String fullName = etEditVolunteerFullName.getText().toString().trim();
        String dateOfBirth = etEditVolunteerDateOfBirth.getText().toString().trim();
        String phoneNumber = etEditVolunteerPhoneNumber.getText().toString().trim();
        String district = etEditVolunteerDistrict.getText().toString().trim();
        String bio = etEditVolunteerBio.getText().toString().trim();

        if (fullName.isEmpty() || dateOfBirth.isEmpty() || phoneNumber.isEmpty() || district.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin bắt buộc.", Toast.LENGTH_LONG).show();
            return;
        }

        // TODO: Validate input data (e.g., phone number format)

        // TODO: Gửi dữ liệu cập nhật lên API Back-end
        // Bạn sẽ cần tạo một DTO (Data Transfer Object) để gửi dữ liệu này
        // Ví dụ: VolunteerUpdateProfileDto
        Log.d("EditVolunteerProfile", "Họ tên: " + fullName);
        Log.d("EditVolunteerProfile", "Ngày sinh: " + dateOfBirth);
        Log.d("EditVolunteerProfile", "SĐT: " + phoneNumber);
        Log.d("EditVolunteerProfile", "Quận/Huyện: " + district);
        Log.d("EditVolunteerProfile", "Bio: " + bio);
        Log.d("EditVolunteerProfile", "Kỹ năng đã chọn: " + selectedSkills.toString());
        Log.d("EditVolunteerProfile", "Lĩnh vực quan tâm đã chọn: " + selectedCauses.toString());

        Toast.makeText(this, "Cập nhật hồ sơ TNV thành công (Demo)!", Toast.LENGTH_SHORT).show();
        // Sau khi cập nhật thành công, có thể hiển thị thông báo và đóng Activity
        // finish();
    }
}