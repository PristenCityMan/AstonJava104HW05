package ru.aston.homework04.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "Сущность пользователя для передачи данных")
public class UserDto {

    @Schema(description = "Уникальный идентификатор", example = "1")
    private Long id;

    @NotBlank(message = "Имя не должно быть пустым")
    @Schema(description = "Имя пользователя", example = "Иван Иванов")
    private String name;

    @NotBlank(message = "Email не должен быть пустым")
    @Email(message = "Некорректный формат email")
    @Schema(description = "Электронная почта", example = "ivan@example.com")
    private String email;

    @NotNull(message = "Возраст должен быть указан")
    @Min(value = 0, message = "Возраст не может быть отрицательным")
    @Schema(description = "Возраст пользователя", example = "25")
    private Integer age;

    @Schema(description = "Дата и время создания профиля", example = "2026-09-11T14:13:00")
    private LocalDateTime createdAt;

    public UserDto() {}

    public UserDto(Long id, String name, String email, Integer age, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
