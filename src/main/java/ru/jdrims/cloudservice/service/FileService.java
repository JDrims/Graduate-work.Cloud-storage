package ru.jdrims.cloudservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jdrims.cloudservice.entities.FileEntity;
import ru.jdrims.cloudservice.repository.FileRepository;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class FileService {
    private final Logger logger = LoggerFactory.getLogger(FileService.class);

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Transactional
    public synchronized void addFile(String filename, byte[] file) {
        fileRepository.save(new FileEntity(filename, file));
        logger.info("File {} was added", filename);
    }

    @Transactional
    public synchronized void deleteFile(String filename) {
        if (!fileRepository.existsById(filename)) {
            logger.error("File " + filename + " not found");
            throw new RuntimeException("File " + filename + " not found");
        }
        fileRepository.deleteById(filename);
        logger.info("File {} was deleted", filename);
    }

    public byte[] getFile(String filename) {
        final FileEntity file = getFileByName(filename);
        logger.info("File {} was received", filename);
        return file.getFileContent();
    }

    @Transactional
    public synchronized void editFileName(String oldFilename, String newFilename) {
        final FileEntity fileEntity = getFileByName(oldFilename);
        final FileEntity newFileEntity = new FileEntity(newFilename, fileEntity.getFileContent());
        fileRepository.delete(fileEntity);
        fileRepository.save(newFileEntity);
        logger.info("File {} was renamed to {}", oldFilename, newFilename);
    }

    public List<FileEntity> getFileList(int limit) {
        return fileRepository.getFiles(limit);
    }

    private FileEntity getFileByName(String filename) {
        return fileRepository.findById(filename).orElseThrow(() -> {
            logger.error("File " + filename + " not found");
            return new RuntimeException("File " + filename + " not found");
        });
    }
}