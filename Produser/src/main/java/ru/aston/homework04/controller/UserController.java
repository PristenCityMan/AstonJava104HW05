package ru.aston.homework04.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aston.homework04.assembler.UserModelAssembler;
import ru.aston.homework04.dto.UserDto;
import ru.aston.homework04.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Управление пользователями", description = "REST API для работы с пользователями (CRUD, HATEOAS)")
public class UserController {

    private final UserService userService;
    private final UserModelAssembler userModelAssembler;

    public UserController(UserService userService, UserModelAssembler userModelAssembler) {
        this.userService = userService;
        this.userModelAssembler = userModelAssembler;
    }

    @GetMapping
    @Operation(summary = "Получить список всех пользователей", description = "Возвращает список всех пользователей с навигационными ссылками")
    public ResponseEntity<CollectionModel<EntityModel<UserDto>>> getAllUsers() {
        List<EntityModel<UserDto>> users = userService.getAllUsers().stream()
                .map(userModelAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<UserDto>> collectionModel = CollectionModel.of(users,
                linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID", description = "Возвращает данные пользователя и связанные действия")
    @ApiResponse(responseCode = "200", description = "Пользователь успешно найден")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public ResponseEntity<EntityModel<UserDto>> getUserById(
            @PathVariable @Parameter(description = "Идентификатор пользователя") Long id) {
        UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok(userModelAssembler.toModel(userDto));
    }

    @PostMapping
    @Operation(summary = "Создать нового пользователя", description = "Создает пользователя и генерирует Kafka событие CREATE")
    @ApiResponse(responseCode = "201", description = "Пользователь успешно создан")
    public ResponseEntity<EntityModel<UserDto>> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto createdUser = userService.createUser(userDto);
        return new ResponseEntity<>(userModelAssembler.toModel(createdUser), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить существующего пользователя", description = "Обновляет данные профиля по ID")
    public ResponseEntity<EntityModel<UserDto>> updateUser(
            @PathVariable @Parameter(description = "Идентификатор пользователя") Long id,
            @Valid @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(userModelAssembler.toModel(updatedUser));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя", description = "Удаляет запись из базы и генерирует Kafka событие DELETE")
    @ApiResponse(responseCode = "204", description = "Пользователь успешно удален")
    public ResponseEntity<Void> deleteUser(
            @PathVariable @Parameter(description = "Идентификатор пользователя") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
