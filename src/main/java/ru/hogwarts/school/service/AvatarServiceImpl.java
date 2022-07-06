package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exception.CustomBadRequestException;
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
import java.util.Collection;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class AvatarServiceImpl implements AvatarService {

    Logger logger = LoggerFactory.getLogger(AvatarServiceImpl.class);
    @Value("${students.avatars.dir.path}")
    private String avatarsDir;
    private final StudentService studentService;
    private final AvatarRepository avatarRepository;

    public AvatarServiceImpl(StudentService studentService, AvatarRepository avatarRepository) {
        this.studentService = studentService;
        this.avatarRepository = avatarRepository;
    }

    @Override
    @Transactional
    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        logger.debug("Request to upload an avatar for student with id {}", studentId);
        Student student = studentService.findStudent(studentId)
                .orElseThrow(() -> {
                    CustomNotFoundException e = new CustomNotFoundException("Student not found");
                    logger.error("Upload avatar by student id {} finished with error {}", studentId, e);
                    throw e;
                });
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
        logger.debug("Avatar upload for student with id {} completed successfully", studentId);
    }

    @Override
    @Transactional
    public Avatar findAvatar(Long studentId) {
        logger.debug("Request to find an avatar by student id {}", studentId);
        Student student = studentService.findStudent(studentId)
                .orElseThrow(() -> {
                    CustomNotFoundException e = new CustomNotFoundException("Student not found");
                    logger.error("Find avatar by student id {} finished with error {}", studentId, e);
                    throw e;
                });
        logger.debug("Avatar by student id {} find successfully", studentId);
        return avatarRepository.findByStudentId(student.getId()).orElse(null);
    }

    @Override
    @Transactional
    public void getAvatarFromFile(Long id, HttpServletResponse response) throws IOException {
        logger.debug("Request to get an avatar with id {}", id);
        Avatar avatarFromFile = findAvatar(id);
        if (avatarFromFile == null) {
            CustomNotFoundException e = new CustomNotFoundException("Avatar not found");
            logger.error("get an avatar with id {} finished with error {}", id, e.getMessage());
            throw e;
        }
        Path path = Path.of(avatarFromFile.getFilePath());
        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream();) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(avatarFromFile.getMediaType());
            response.setContentLength((int) avatarFromFile.getFileSize());
            is.transferTo(os);
        }
        logger.debug("Get an avatar with id {} completed successfully", id);
    }

    @Override
    public Collection<Avatar> getAllAvatars(Integer page, Integer size) {
        logger.debug("Request to get all  avatars");

        PageRequest pageRequest;
        if (page >= 0 && size > 0) {
            pageRequest = page > 0 ? PageRequest.of(page - 1, size) : PageRequest.of(page, size);
        } else {
            CustomBadRequestException e = new CustomBadRequestException("The page number cannot be less than 0, " +
                    "the page size cannot be less than 1");
            logger.error("get all  avatars finished with error {}", e.getMessage());
            throw e;
        }
        logger.debug("Request to get all  avatars completed successfully");
        return avatarRepository.findAll(pageRequest).getContent();
    }

    private byte[] generateImageAvatar(Path filePath) throws IOException {
        logger.debug("Request to generate Image Avatar");
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
            logger.debug("Generate Image Avatar completed successfully");
            return baos.toByteArray();
        }
    }

    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
