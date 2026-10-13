package com.umc.study.dto.requestdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.antlr.v4.runtime.misc.NotNull;

public record CreateBookRequest (
        @NotNull Long categoryId,
        @NotBlank @Size(max=100) String title,
        String description
){}
