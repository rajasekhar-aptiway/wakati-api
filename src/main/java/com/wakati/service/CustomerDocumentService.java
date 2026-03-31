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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
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

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}