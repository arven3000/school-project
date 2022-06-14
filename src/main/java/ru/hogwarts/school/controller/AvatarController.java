package ru.hogwarts.school.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @GetMapping("/student/{id}/avatar-from-file")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Avatar avatarFromFile = avatarService.findAvatar(id);
        Path path = Path.of(avatarFromFile.getFilePath());
        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream();) {
            response.setStatus(200);
            response.setContentType(avatarFromFile.getMediaType());
            response.setContentLength((int) avatarFromFile.getFileSize());
            is.transferTo(os);
        }
    }

    @GetMapping("/student/{id}/avatar-from-db")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {
        Avatar avatarFromDB = avatarService.findAvatar(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatarFromDB.getMediaType()));
        headers.setContentLength(avatarFromDB.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatarFromDB.getData());
    }

    @PostMapping(value = "/student/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long id,
                                               @RequestParam MultipartFile avatar) throws IOException {
        avatarService.uploadAvatar(id, avatar);
        return ResponseEntity.ok().build();
    }
}
