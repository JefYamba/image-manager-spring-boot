package com.jefy.img.dto;

import com.jefy.img.entity.ImageFile;
import lombok.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import static com.jefy.img.dto.Constant.IMAGES_URL;

/**
 * @Author JefYamba
 * @Email joph.e.f.yamba@gmail.com
 * @Since 25/04/2024
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponse {
    private Integer id;
    private String name;
    private String imageUrl;

    public static ImageResponse fromEntity(ImageFile imageFile) {
        return ImageResponse.builder()
                .id(imageFile.getId())
                .name(imageFile.getName())
                .imageUrl(getUrl(imageFile.getCompleteName()))
                .build();
    }

    private static String getUrl(String completeName) {
        String url = IMAGES_URL + "/" + completeName;
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(url).toUriString();
    }
}
