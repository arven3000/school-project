package ru.hogwarts.school.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AvatarService {
    @Transactional
    void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException;

    @Transactional
    Avatar findAvatar(Long studentId);

    @Transactional
    void getAvatarFromFile(Long id, HttpServletResponse response) throws IOException;
}
