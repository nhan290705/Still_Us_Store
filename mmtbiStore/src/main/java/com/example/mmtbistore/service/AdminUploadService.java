package com.example.mmtbistore.service;

import com.example.mmtbistore.dto.UploadResponse;
import com.example.mmtbistore.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class AdminUploadService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final List<String> ALLOWED_EXTENSIONS = List.of(".jpg", ".jpeg", ".png", ".webp");
    private static final List<String> ALLOWED_CONTENT_TYPES = List.of("image/jpeg", "image/png", "image/webp");

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    public UploadResponse uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File tải lên không được để trống");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("Kích thước file vượt quá giới hạn cho phép (tối đa 5MB)");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new BadRequestException("Tên file không hợp lệ");
        }

        String extension = "";
        int lastDot = originalFilename.lastIndexOf('.');
        if (lastDot >= 0) {
            extension = originalFilename.substring(lastDot).toLowerCase();
        }

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new BadRequestException("Chỉ chấp nhận file ảnh định dạng JPG, JPEG, PNG hoặc WEBP");
        }

        String contentType = file.getContentType();
        if (contentType != null && !contentType.isBlank() && !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new BadRequestException("Định dạng nội dung file không hợp lệ: " + contentType);
        }

        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            String cleanName = StringUtils.cleanPath(originalFilename).replaceAll("[^a-zA-Z0-9._-]", "_");
            String storedFileName = UUID.randomUUID().toString() + "_" + cleanName;
            Path targetLocation = uploadPath.resolve(storedFileName).normalize();

            if (!targetLocation.startsWith(uploadPath)) {
                throw new BadRequestException("Phát hiện đường dẫn file không hợp lệ");
            }

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String url = "/uploads/" + storedFileName;
            log.info("Uploaded file thành công: {} -> {}", originalFilename, url);

            return UploadResponse.builder()
                    .url(url)
                    .originalFileName(originalFilename)
                    .storedFileName(storedFileName)
                    .fileSize(file.getSize())
                    .contentType(contentType != null ? contentType : "image/jpeg")
                    .build();

        } catch (IOException ex) {
            log.error("Lỗi khi lưu file upload", ex);
            throw new BadRequestException("Không thể lưu file tải lên: " + ex.getMessage());
        }
    }
}
