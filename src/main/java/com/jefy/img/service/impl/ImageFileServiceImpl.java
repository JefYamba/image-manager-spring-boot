package com.jefy.img.service.impl;

import com.jefy.img.dto.ImageRequest;
import com.jefy.img.entity.ImageFile;
import com.jefy.img.repository.ImageFileRepository;
import com.jefy.img.service.ImageFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.jefy.img.dto.Constant.IMAGES_BASE_URL;

/**
 * @Author JefYamba
 * @Email joph.e.f.yamba@gmail.com
 * @Since 25/04/2024
 */
@Service

@RequiredArgsConstructor
public class ImageFileServiceImpl implements ImageFileService {
    private final ImageFileRepository imageFileRepository;
    private final ResourceLoader resourceLoader;

    @Override
    public Optional<ImageFile> get(String fileName) {

        return imageFileRepository.findByName(fileName);
    }

    @Override
    public List<ImageFile> getAll() {
        return imageFileRepository.findAll();
    }


    @Override
    public ImageFile create(ImageRequest imageRequest) throws IOException {

        if (imageRequest != null && !imageRequest.getImage().isEmpty()){
            ImageFile imageFile = ImageFile.builder()
                    .name(imageRequest.getName())
                    .url(createImageUrl(imageRequest))
                    .build();

            return imageFileRepository.save(imageFile);
        }

        return null;
    }

    private String createImageUrl(ImageRequest imageRequest) throws IOException {
        String imageName = imageRequest.getName() + "." + getImageExtension(imageRequest);

        File directory = ResourceUtils.getFile("classpath:static/images");
        String absolutePath = directory.getAbsolutePath();

        Path imagePath = Paths.get(absolutePath + File.separator + imageName);
        Files.copy(imageRequest.getImage().getInputStream(), imagePath);

        String url = IMAGES_BASE_URL + "/" + imageRequest.getName() + "." + getImageExtension(imageRequest);
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(url).toUriString();
    }

    private String getImageExtension(ImageRequest imageRequest) {
        return Arrays.stream(Objects.requireNonNull(
                imageRequest.getImage().getOriginalFilename()
        ).split("\\.")).toList().getLast();
    }

    @Override
    public ImageFile update(ImageRequest imageRequest) {
        return null;
    }

    @Override
    public ImageFile delete(Integer id) {
        return null;
    }
}
