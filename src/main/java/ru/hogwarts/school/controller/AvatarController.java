package ru.hogwarts.school.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exception.CustomNotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping("/avatar")
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @GetMapping("/all")
    public ResponseEntity<Collection<Avatar>> getAll(@RequestParam("page") Integer page,
                                                     @RequestParam("size") Integer size) {
        return ResponseEntity.ok(avatarService.getAllAvatars(page, size));
    }

    @GetMapping("/{id}/avatar-from-file")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        avatarService.getAvatarFromFile(id, response);
    }

    @GetMapping("/{id}/avatar-from-db")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {
        Avatar avatarFromDB = avatarService.findAvatar(id);
        if (avatarFromDB == null) {
            throw new CustomNotFoundException("Avatar not found");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatarFromDB.getMediaType()));
        headers.setContentLength(avatarFromDB.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatarFromDB.getData());
    }

    @PostMapping(value = "/{id}/add-avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long id,
                                               @RequestParam MultipartFile avatar) throws IOException {
        avatarService.uploadAvatar(id, avatar);
        return ResponseEntity.ok().build();
    }
}
