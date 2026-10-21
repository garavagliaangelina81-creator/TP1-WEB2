package apiblanck.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistroRequest(
    @NotBlank @Email @Size(max = 255)
    String email,

    // BCrypt solo usa los primeros 72 bytes, por eso el máximo
    @NotBlank @Size(min = 8, max = 72)
    String password
) {}