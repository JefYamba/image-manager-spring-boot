package com.jefy.img.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
public class ImageRequest {
    private String name;
    private MultipartFile image;
}
