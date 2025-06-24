// Logic cho màn hình chỉnh sửa sẽ tương tự màn hình Tạo chiến dịch
// Chúng ta sẽ cần fetch dữ liệu, hiển thị dialog, và sau đó gửi request PUT
// Đây là file đầy đủ, bạn chỉ cần tạo và dán vào
package com.example.hanoconnectadmin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hanoconnectadmin.models.Cause;
import com.example.hanoconnectadmin.models.SkillDto;
import com.example.hanoconnectadmin.models.VolunteerProfileResponse;
import com.example.hanoconnectadmin.models.VolunteerProfileUpdateRequest;
import com.example.hanoconnectadmin.networking.ApiService;
import com.example.hanoconnectadmin.util.SessionManager;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etFullName, etPhoneNumber, etDistrict;
    private ChipGroup chipGroupSkills, chipGroupCauses;
    private MaterialButton btnSelectSkills, btnSelectCauses, btnSaveChanges;
    private ProgressBar progressBar;
    private LinearLayout formContainer;

    private ApiService apiService;
    private SessionManager sessionManager;

    private List<SkillDto> allSkills = new ArrayList<>();
    private List<Cause> allCauses = new ArrayList<>();
    private List<SkillDto> selectedSkills = new ArrayList<>();
    private List<Cause> selectedCauses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        apiService = com.example.hanoconnectadmin.networking.RetrofitClient.getApiService(this);
        sessionManager = new SessionManager(this);

        setupViews();
        fetchAllData();
    }

    private void setupViews() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        etFullName = findViewById(R.id.etFullName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etDistrict = findViewById(R.id.etDistrict);
        chipGroupSkills = findViewById(R.id.chipGroupSkills);
        chipGroupCauses = findViewById(R.id.chipGroupCauses);
        btnSelectSkills = findViewById(R.id.btnSelectSkills);
        btnSelectCauses = findViewById(R.id.btnSelectCauses);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        progressBar = findViewById(R.id.progressBar);
        formContainer = findViewById(R.id.formContainer);

        btnSelectSkills.setOnClickListener(v -> showMultiSelectDialog("Kỹ năng", allSkills, selectedSkills, this::updateSkillChips));
        btnSelectCauses.setOnClickListener(v -> showMultiSelectDialog("Lĩnh vực", allCauses, selectedCauses, this::updateCauseChips));
        btnSaveChanges.setOnClickListener(v -> handleSaveChanges());
    }

    // Tải tất cả dữ liệu cần thiết: profile hiện tại, danh sách skills, danh sách causes
    private void fetchAllData() {
        progressBar.setVisibility(View.VISIBLE);
        formContainer.setVisibility(View.INVISIBLE);

        int userId = sessionManager.getUserId();
        if(userId == -1) return;

        // Tạo các call API
        Call<VolunteerProfileResponse> profileCall = apiService.getVolunteerProfile(userId);
        Call<List<SkillDto>> skillsCall = apiService.getSkills();
        Call<List<Cause>> causesCall = apiService.getCauses();

        // Thực thi song song (ví dụ đơn giản)
        profileCall.enqueue(new Callback<VolunteerProfileResponse>() {
            @Override
            public void onResponse(Call<VolunteerProfileResponse> call, Response<VolunteerProfileResponse> response) {
                if(response.isSuccessful()) {
                    populateForm(response.body());
                }
                checkAllDataLoaded();
            }
            @Override
            public void onFailure(Call<VolunteerProfileResponse> call, Throwable t) {
                checkAllDataLoaded();
            }
        });

        skillsCall.enqueue(new Callback<List<SkillDto>>() {
            @Override
            public void onResponse(Call<List<SkillDto>> call, Response<List<SkillDto>> response) {
                if(response.isSuccessful()) {
                    allSkills.addAll(response.body());
                }
                checkAllDataLoaded();
            }
            @Override
            public void onFailure(Call<List<SkillDto>> call, Throwable t) {
                checkAllDataLoaded();
            }
        });

        causesCall.enqueue(new Callback<List<Cause>>() {
            @Override
            public void onResponse(Call<List<Cause>> call, Response<List<Cause>> response) {
                if(response.isSuccessful()) {
                    allCauses.addAll(response.body());
                }
                checkAllDataLoaded();
            }
            @Override
            public void onFailure(Call<List<Cause>> call, Throwable t) {
                checkAllDataLoaded();
            }
        });
    }

    private int loadCount = 0;
    private synchronized void checkAllDataLoaded() {
        loadCount++;
        if (loadCount == 3) { // Khi cả 3 cuộc gọi API đã hoàn thành (thành công hoặc thất bại)
            progressBar.setVisibility(View.GONE);
            formContainer.setVisibility(View.VISIBLE);
        }
    }

    // Điền dữ liệu của người dùng vào form
    private void populateForm(VolunteerProfileResponse profile) {
        if(profile == null) return;
        etFullName.setText(profile.getFullName());
        etPhoneNumber.setText(profile.getPhoneNumber());
        etDistrict.setText(profile.getDistrict());

        // Xử lý các kỹ năng đã chọn
        for(String skillName : profile.getSkills()) {
            allSkills.stream()
                    .filter(s -> s.getSkillName().equals(skillName))
                    .findFirst()
                    .ifPresent(selectedSkills::add);
        }
        updateSkillChips();

        // Xử lý các lĩnh vực đã chọn
        for(String causeName : profile.getCauses()) {
            allCauses.stream()
                    .filter(c -> c.getCauseName().equals(causeName))
                    .findFirst()
                    .ifPresent(selectedCauses::add);
        }
        updateCauseChips();
    }

    // Dialog chọn nhiều mục
    private <T> void showMultiSelectDialog(String title, List<T> allItems, List<T> selectedItems, Runnable onConfirm) {
        String[] itemNames = allItems.stream().map(Object::toString).toArray(String[]::new);
        boolean[] checkedItems = new boolean[allItems.size()];

        for (int i = 0; i < allItems.size(); i++) {
            checkedItems[i] = selectedItems.contains(allItems.get(i));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMultiChoiceItems(itemNames, checkedItems, (dialog, which, isChecked) -> {
            T item = allItems.get(which);
            if (isChecked) {
                if (!selectedItems.contains(item)) selectedItems.add(item);
            } else {
                selectedItems.remove(item);
            }
        });

        builder.setPositiveButton("OK", (dialog, which) -> onConfirm.run());
        builder.setNegativeButton("Hủy", null);
        builder.create().show();
    }

    private void updateSkillChips() {
        chipGroupSkills.removeAllViews();
        for (SkillDto skill : selectedSkills) {
            Chip chip = new Chip(this);
            chip.setText(skill.getSkillName());
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(v -> {
                selectedSkills.remove(skill);
                updateSkillChips();
            });
            chipGroupSkills.addView(chip);
        }
    }

    private void updateCauseChips() {
        chipGroupCauses.removeAllViews();
        for (Cause cause : selectedCauses) {
            Chip chip = new Chip(this);
            chip.setText(cause.getCauseName());
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(v -> {
                selectedCauses.remove(cause);
                updateCauseChips();
            });
            chipGroupCauses.addView(chip);
        }
    }

    private void handleSaveChanges() {
        String fullName = etFullName.getText().toString().trim();
        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Họ tên là bắt buộc");
            return;
        }

        String phone = etPhoneNumber.getText().toString().trim();
        String district = etDistrict.getText().toString().trim();
        List<Integer> skillIds = selectedSkills.stream().map(SkillDto::getSkillId).collect(Collectors.toList());
        List<Integer> causeIds = selectedCauses.stream().map(Cause::getCauseId).collect(Collectors.toList());

        int userId = sessionManager.getUserId();

        VolunteerProfileUpdateRequest request = new VolunteerProfileUpdateRequest(fullName, phone, district, skillIds, causeIds);

        showLoading(true);
        apiService.updateVolunteerProfile(userId, request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                showLoading(false);
                if(response.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Cập nhật thất bại. Lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showLoading(false);
                Toast.makeText(EditProfileActivity.this, "Lỗi kết nối.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        btnSaveChanges.setEnabled(!isLoading);
    }
}
