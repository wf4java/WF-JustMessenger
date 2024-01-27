package wf.spring.justmessenger.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import wf.spring.justmessenger.model.ContentType;
import wf.spring.justmessenger.model.exception.StorageException;
import wf.spring_boot.minio.MinioException;
import wf.spring_boot.minio.MinioService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {


    private final MinioService minioService;


    @Override
    public List<UUID> save(MultipartFile[] files) {
        List<UUID> uuids = new ArrayList<>(files.length);
        for (int i = 0; i < files.length; i++) {
            try {
                uuids.add(save(files[i]));
            } catch (StorageException e) {
                if (i == files.length - 1 && uuids.isEmpty()) throw e;
                uuids.add(null);
            }
        }
        return uuids;
    }


    @Override
    public UUID save(MultipartFile file) {
        try {
            UUID uuid = UUID.randomUUID();
            minioService.upload(Path.of(uuid.toString()), file.getInputStream(), file.getContentType() != null ? file.getContentType() : "other");

            return uuid;
        } catch (MinioException | IOException e) {
            throw new StorageException("Error on save file");
        }
    }


    @Override
    public void delete(UUID uuid) {
        try {minioService.remove(Path.of(uuid.toString()));}
        catch (MinioException e) {throw new StorageException("Error on remove file");}
    }

    @Override
    public InputStream get(UUID uuid) {
        try {return minioService.get(Path.of(uuid.toString()));}
        catch (MinioException e) {throw new StorageException("Error on get file");}
    }

}
