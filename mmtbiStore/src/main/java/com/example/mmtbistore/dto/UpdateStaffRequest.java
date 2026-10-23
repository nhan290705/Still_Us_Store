package com.example.mmtbistore.dto;

import com.example.mmtbistore.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStaffRequest {

    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 100, message = "Họ tên không được quá 100 ký tự")
    private String fullName;

    @Email(message = "Email không đúng định dạng")
    @Size(max = 100, message = "Email không được quá 100 ký tự")
    private String email;

    @Size(max = 15, message = "Số điện thoại không được quá 15 ký tự")
    private String phoneNumber;

    @NotNull(message = "Vai trò không được để trống")
    private UserRole role;

    // Đổi mật khẩu là tuỳ chọn (nếu để trống/null thì giữ nguyên mật khẩu cũ)
    @Size(min = 6, message = "Mật khẩu mới nếu đổi phải từ 6 ký tự trở lên")
    private String password;
}
