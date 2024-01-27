package wf.spring.justmessenger.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public interface StorageService {
    List<UUID> save(MultipartFile[] files);

    UUID save(MultipartFile file);

    void delete(UUID uuid);

    InputStream get(UUID uuid);
}
