package apiblanck.dto;

public record TokenResponse(
    String token,
    String tipo,
    long expiraEnSegundos
) {}
