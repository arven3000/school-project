package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exception.CustomNotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.AvatarRepository;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service

public class AvatarService {

    @Value("${students.avatars.dir.path}")
    private String avatarsDir;
    private final StudentService studentService;
    private final AvatarRepository avatarRepository;

    public AvatarService(StudentService studentService, AvatarRepository avatarRepository) {
        this.studentService = studentService;
        this.avatarRepository = avatarRepository;
    }

    @Transactional
    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        Student student = studentService.findStudent(studentId)
                .orElseThrow(() -> new CustomNotFoundException("Student not found"));
        Path filePath = Path.of(avatarsDir, student + "."
                + getExtensions(Objects.requireNonNull(avatarFile.getOriginalFilename())));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }
        Avatar avatar = findAvatar(studentId);
        if (avatar == null) {
            avatar = new Avatar();
        }
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(generateImageAvatar(filePath));
        avatarRepository.save(avatar);
    }

    @Transactional
    public Avatar findAvatar(Long studentId) {
        Student student = studentService.findStudent(studentId)
                .orElseThrow(() -> new CustomNotFoundException("Student not found"));
        return avatarRepository.findByStudentId(student.getId()).orElse(null);
    }

    @Transactional
    public void getAvatarFromFile(Long id, HttpServletResponse response) throws IOException {
        Avatar avatarFromFile = findAvatar(id);
        if (avatarFromFile == null) {
            throw new CustomNotFoundException("Avatar not found");
        }
        Path path = Path.of(avatarFromFile.getFilePath());
        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream();) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(avatarFromFile.getMediaType());
            response.setContentLength((int) avatarFromFile.getFileSize());
            is.transferTo(os);
        }
    }

    private byte[] generateImageAvatar(Path filePath) throws IOException {
        try (InputStream is = Files.newInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            BufferedImage image = ImageIO.read(bis);

            int height = image.getHeight() / (image.getWidth() / 100);
            BufferedImage data = new BufferedImage(100, height, image.getType());
            Graphics2D graphics = data.createGraphics();
            graphics.drawImage(image, 0, 0, 100, height, null);
            graphics.dispose();

            ImageIO.write(data, getExtensions(filePath.getFileName().toString()), baos);
            return baos.toByteArray();
        }
    }

    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
