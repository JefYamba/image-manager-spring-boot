package com.jefy.img.service;

import com.jefy.img.dto.ImageRequest;
import com.jefy.img.entity.ImageFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @Author JefYamba
 * @Email joph.e.f.yamba@gmail.com
 * @Since 25/04/2024
 */
public interface ImageFileService {
    Optional<ImageFile> get(String fileName);
    List<ImageFile> getAll();
    ImageFile create(ImageRequest imageRequest) throws IOException;
    ImageFile update(ImageRequest imageRequest);
    ImageFile delete(Integer id);
}
