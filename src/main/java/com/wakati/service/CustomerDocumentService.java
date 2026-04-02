package com.wakati.service;

import com.wakati.I18NConstants;
import com.wakati.I18NConstants.*;
import com.wakati.entity.User;
import com.wakati.entity.UserDocuments;
import com.wakati.enums.DocumentType;
import com.wakati.enums.RegistrationStage;
import com.wakati.enums.VerificationStatus;
import com.wakati.exception.WakatiException;
import com.wakati.model.response.ResponseBuilder;
import com.wakati.repository.UserCredentialsRepository;
import com.wakati.repository.UserDocumentsRepository;
import com.wakati.repository.UserRepository;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.wakati.I18NConstants.*;

@Service
public class CustomerDocumentService {

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private UserDocumentsRepository documentRepo;
    @Autowired
    private ResponseBuilder responseBuilder;

    @Transactional
    public Map<String, Object> uploadDocuments(String userId,
                                               String documentNumber,
                                               String pin,
                                               Map<String, MultipartFile> files) {

        if (userId == null || userId.isBlank()) {
            throw new WakatiException(USER_ID_REQUIRED);
        }

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new WakatiException(USER_NOT_FOUND));

        String basePath = System.getenv("KYC_STORAGE_PATH");
        if (basePath == null) {
            basePath = System.getProperty("user.dir") + "/kycdata";
        }

        Path userDir = Paths.get(basePath, userId);
        try {
            Files.createDirectories(userDir);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directory");
        }

        // ✅ UPDATE USER STAGE
        user.setRegistrationStage(RegistrationStage.PIN_SETUP_PENDING);

        // ✅ EXPIRE OLD DOCUMENTS
        documentRepo.expireDocuments(userId);

        // ✅ UPLOAD FILES
        for (Map.Entry<String, MultipartFile> entry : files.entrySet()) {

            String documentType = entry.getKey();
            MultipartFile file = entry.getValue();

            if (file.isEmpty()) continue;

            String docId = UUID.randomUUID().toString();
            String ext = getExtension(file.getOriginalFilename());

            Path filePath = userDir.resolve(docId + "." + ext);

            try {
                Files.copy(file.getInputStream(), filePath);
            } catch (IOException e) {
                throw new WakatiException(DOCUMENT_UPLOAD_FAILED," "+documentType);
            }

            String url = "kycdata/" + userId + "/" + docId + "." + ext;

            UserDocuments doc = new UserDocuments();
            doc.setDocumentId(docId);
            doc.setUser(user);
            doc.setDocumentType(DocumentType.valueOf(documentType));
            doc.setDocumentNumber(documentNumber);
            doc.setDocumentUrl(url);
            doc.setVerificationStatus(VerificationStatus.PENDING);
            doc.setCreatedBy(userId);
            doc.setCreatedAt(LocalDateTime.now());

            documentRepo.save(doc);
        }

        return responseBuilder.success(I18NConstants.DOCUMENTS_UPLOADED_SUCCESSFULLY,"data", Map.of(
                "userId", userId,
                "documentNumber", documentNumber
        ));
    }

    public ResponseEntity<?> viewDocument(String documentId) {

        if (documentId == null || documentId.isBlank()) {
            throw new RuntimeException("document_id is required");
        }

        // ✅ FETCH DOCUMENT
        UserDocuments doc = documentRepo
                .findByDocumentIdAndNotExpired(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        String docUserId = doc.getUser().getUserId();
        String relativePath = doc.getDocumentUrl(); // kycdata/{userId}/{file}

        // =====================================================
        // ✅ RESOLVE FILE PATH
        // =====================================================
        String base = System.getenv("KYC_STORAGE_PATH");
        if (base == null) {
            base = System.getProperty("user.dir") + "/kycdata";
        }

        Path basePath = Paths.get(base).toAbsolutePath().normalize();

        // remove "kycdata/" prefix
        String cleanPath = relativePath.replaceFirst("^kycdata/", "");

        Path filePath = basePath.resolve(cleanPath).normalize();

        // 🔥 Path traversal protection
        if (!filePath.startsWith(basePath)) {
            throw new RuntimeException("Access denied");
        }

        File file = filePath.toFile();

        if (!file.exists()) {
            throw new RuntimeException("File not found");
        }

        // =====================================================
        // ✅ MIME TYPE
        // =====================================================
        String mime = getMimeType(file.getName());

        UrlResource resource;
        try {
            resource = new UrlResource(file.toURI());
        } catch (MalformedURLException e) {
            throw new RuntimeException("File error");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mime))
                .contentLength(file.length())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + file.getName() + "\"")
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                .header("X-Content-Type-Options", "nosniff")
                .body(resource);
    }

    private String getMimeType(String fileName) {

        String ext = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

        return switch (ext) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "pdf" -> "application/pdf";
            case "doc" -> "application/msword";
            case "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            default -> "application/octet-stream";
        };
    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}