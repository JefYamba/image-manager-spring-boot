package com.jefy.img.service;

import com.jefy.img.dto.ImageRequest;
import com.jefy.img.dto.ImageResponse;
import org.hibernate.ObjectNotFoundException;

import java.io.IOException;
import java.util.List;

/**
 * @Author JefYamba
 * @Email joph.e.f.yamba@gmail.com
 * @Since 25/04/2024
 */
public interface ImageFileService {
    ImageResponse get(String fileName) throws ObjectNotFoundException;
    byte[] getImage(String fileName) throws IllegalArgumentException, IOException;
    List<ImageResponse> getAll();
    ImageResponse save(ImageRequest imageRequest) throws IllegalArgumentException, IOException;
    void delete(Integer id) throws IllegalArgumentException, IOException;
}
