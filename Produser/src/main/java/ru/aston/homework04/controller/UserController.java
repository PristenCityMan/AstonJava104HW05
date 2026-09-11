package ru.aston.homework04.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aston.homework04.dto.UserDto;
import ru.aston.homework04.service.UserService;
import java.util.List;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Управление пользователями", description = "REST API для работы с пользователями (CRUD, HATEOAS)")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Получить список всех пользователей", description = "Возвращает список всех пользователей с навигационными ссылками")
    public ResponseEntity<CollectionModel<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();

        for (UserDto user : users) {
            addUserLinks(user);
        }
        Link selfLink = linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel();
        CollectionModel<UserDto> result = CollectionModel.of(users, selfLink);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID", description = "Возвращает данные пользователя и связанные действия")
    @ApiResponse(responseCode = "200", description = "Пользователь успешно найден")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public ResponseEntity<UserDto> getUserById(
            @PathVariable @Parameter(description = "Идентификатор пользователя") Long id) {
        UserDto userDto = userService.getUserById(id);
        addUserLinks(userDto);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping
    @Operation(summary = "Создать нового пользователя", description = "Создает пользователя и генерирует Kafka событие CREATE")
    @ApiResponse(responseCode = "201", description = "Пользователь успешно создан")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto createdUser = userService.createUser(userDto);
        addUserLinks(createdUser);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить существующего пользователя", description = "Обновляет данные профиля по ID")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable @Parameter(description = "Идентификатор пользователя") Long id,
            @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto);
        addUserLinks(updatedUser);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя", description = "Удаляет запись из базы и генерирует Kafka событие DELETE")
    @ApiResponse(responseCode = "204", description = "Пользователь успешно удален")
    public ResponseEntity<Void> deleteUser(
            @PathVariable @Parameter(description = "Идентификатор пользователя") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    private void addUserLinks(UserDto userDto) {
        userDto.add(linkTo(methodOn(UserController.class).getUserById(userDto.getId())).withSelfRel());
        userDto.add(linkTo(methodOn(UserController.class).updateUser(userDto.getId(), null)).withRel("update"));
        userDto.add(linkTo(methodOn(UserController.class).deleteUser(userDto.getId())).withRel("delete"));
        userDto.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users"));
    }
}