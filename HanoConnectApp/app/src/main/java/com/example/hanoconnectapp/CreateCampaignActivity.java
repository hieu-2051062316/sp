package com.example.hanoconnectapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hanoconnectapp.models.Cause;
import com.example.hanoconnectapp.models.OpportunityCreateRequest;
import com.example.hanoconnectapp.models.OpportunityResponseDto;
import com.example.hanoconnectapp.models.SkillDto;
import com.example.hanoconnectapp.networking.ApiService;
import com.example.hanoconnectapp.networking.RetrofitClient;
import com.example.hanoconnectapp.util.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCampaignActivity extends AppCompatActivity {

    private EditText etCampaignName, etCampaignDescription, etLocation, etQuantity;
    private AutoCompleteTextView acCause;
    private MaterialButton btnSelectSkills, btnPostCampaign;
    private ChipGroup chipGroupSkills;
    private ProgressBar progressBar;

    private SessionManager sessionManager;
    private ApiService apiService;

    // Lists để lưu dữ liệu từ API
    private List<Cause> causeList = new ArrayList<>();
    private List<SkillDto> skillList = new ArrayList<>();

    // Lists để lưu lựa chọn của người dùng
    private Cause selectedCause;
    private ArrayList<SkillDto> selectedSkills = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_campaign);

        // Khởi tạo các thành phần
        sessionManager = new SessionManager(this);
        apiService = RetrofitClient.getApiService();

        // Ánh xạ views và cài đặt sự kiện
        setupViews();

        // Tải dữ liệu cho các lựa chọn
        fetchCauses();
        fetchSkills();
    }

    private void setupViews() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        etCampaignName = findViewById(R.id.etCampaignName);
        etCampaignDescription = findViewById(R.id.etCampaignDescription);
        etLocation = findViewById(R.id.etLocation);
        etQuantity = findViewById(R.id.etQuantity);
        acCause = findViewById(R.id.acCause);
        btnSelectSkills = findViewById(R.id.btnSelectSkills);
        chipGroupSkills = findViewById(R.id.chipGroupSkills);
        btnPostCampaign = findViewById(R.id.btnPostCampaign);
        progressBar = findViewById(R.id.progressBarCreate);

        // Đặt sự kiện click
        btnPostCampaign.setOnClickListener(v -> handlePostCampaign());
        btnSelectSkills.setOnClickListener(v -> showSkillSelectionDialog());

        // Lắng nghe sự kiện chọn item cho Lĩnh vực
        acCause.setOnItemClickListener((parent, view, position, id) -> {
            selectedCause = (Cause) parent.getItemAtPosition(position);
        });
    }

    private void fetchCauses() {
        apiService.getCauses().enqueue(new Callback<List<Cause>>() {
            @Override
            public void onResponse(Call<List<Cause>> call, Response<List<Cause>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    causeList = response.body();
                    // Setup Adapter cho AutoCompleteTextView
                    ArrayAdapter<Cause> causeAdapter = new ArrayAdapter<>(CreateCampaignActivity.this, android.R.layout.simple_dropdown_item_1line, causeList);
                    acCause.setAdapter(causeAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Cause>> call, Throwable t) {
                Toast.makeText(CreateCampaignActivity.this, "Lỗi tải danh sách lĩnh vực", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchSkills() {
        apiService.getSkills().enqueue(new Callback<List<SkillDto>>() {
            @Override
            public void onResponse(Call<List<SkillDto>> call, Response<List<SkillDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    skillList = response.body();
                }
            }
            @Override
            public void onFailure(Call<List<SkillDto>> call, Throwable t) {
                Toast.makeText(CreateCampaignActivity.this, "Lỗi tải danh sách kỹ năng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSkillSelectionDialog() {
        if (skillList.isEmpty()) {
            Toast.makeText(this, "Đang tải danh sách kỹ năng, vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Chuẩn bị dữ liệu cho dialog
        String[] skillNames = skillList.stream().map(SkillDto::getSkillName).toArray(String[]::new);
        boolean[] checkedSkills = new boolean[skillList.size()];

        // Đánh dấu các kỹ năng đã được chọn trước đó
        for (int i = 0; i < skillList.size(); i++) {
            int currentSkillId = skillList.get(i).getSkillId();
            checkedSkills[i] = selectedSkills.stream().anyMatch(s -> s.getSkillId() == currentSkillId);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn kỹ năng yêu cầu");
        builder.setMultiChoiceItems(skillNames, checkedSkills, (dialog, which, isChecked) -> {
            SkillDto skill = skillList.get(which);
            if (isChecked) {
                // Thêm vào danh sách nếu chưa có
                if (selectedSkills.stream().noneMatch(s -> s.getSkillId() == skill.getSkillId())) {
                    selectedSkills.add(skill);
                }
            } else {
                // Xóa khỏi danh sách
                selectedSkills.removeIf(s -> s.getSkillId() == skill.getSkillId());
            }
        });

        builder.setPositiveButton("OK", (dialog, which) -> updateSkillChips());
        builder.setNegativeButton("Hủy", null);
        builder.create().show();
    }

    private void updateSkillChips() {
        chipGroupSkills.removeAllViews();
        for (SkillDto skill : selectedSkills) {
            Chip chip = new Chip(this);
            chip.setText(skill.getSkillName());
            chip.setCloseIconVisible(true);
            // Sự kiện khi nhấn nút xóa trên chip
            chip.setOnCloseIconClickListener(v -> {
                selectedSkills.remove(skill);
                updateSkillChips(); // Cập nhật lại giao diện
            });
            chipGroupSkills.addView(chip);
        }
    }


    private void handlePostCampaign() {
        // Validate dữ liệu
        String title = etCampaignName.getText().toString().trim();
        String description = etCampaignDescription.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String quantityStr = etQuantity.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || selectedCause == null) {
            Toast.makeText(this, "Vui lòng điền đầy đủ các trường bắt buộc (*)", Toast.LENGTH_SHORT).show();
            return;
        }

        int organizationId = sessionManager.getOrganizationId();
        if (organizationId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin tổ chức. Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
            return;
        }

        // Chuẩn bị dữ liệu để gửi
        Integer quantity = quantityStr.isEmpty() ? null : Integer.parseInt(quantityStr);
        int causeId = selectedCause.getCauseId();
        List<Integer> skillIds = selectedSkills.stream().map(SkillDto::getSkillId).collect(Collectors.toList());

        progressBar.setVisibility(View.VISIBLE);
        btnPostCampaign.setEnabled(false);

        // Tạo request và gọi API
        OpportunityCreateRequest request = new OpportunityCreateRequest(organizationId, title, description, location, quantity, causeId, skillIds);
        Call<OpportunityResponseDto> call = apiService.createOpportunity(request);

        call.enqueue(new Callback<OpportunityResponseDto>() {
            @Override
            public void onResponse(Call<OpportunityResponseDto> call, Response<OpportunityResponseDto> response) {
                progressBar.setVisibility(View.GONE);
                btnPostCampaign.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(CreateCampaignActivity.this, "Đăng bài thành công!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(CreateCampaignActivity.this, "Đăng bài thất bại. Mã lỗi: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OpportunityResponseDto> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnPostCampaign.setEnabled(true);
                Log.e("CreateCampaignAPI", "Lỗi kết nối", t);
                Toast.makeText(CreateCampaignActivity.this, "Lỗi kết nối, không thể đăng bài.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
