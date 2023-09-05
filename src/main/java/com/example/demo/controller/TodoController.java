package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.SimpleResponseDTO;
import com.example.demo.dto.TodoDTO;
import com.example.demo.model.TodoEntity;
import com.example.demo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
public class TodoController {

    @Autowired
    private TodoService service;

    @GetMapping("/test")
    public ResponseEntity<?> testTodo() {
        String str = service.testService();

        List<String> list = new ArrayList<>();
        list.add(str);

        ResponseDTO<String> response = ResponseDTO.<String>builder()
                .data(list).build();

        return ResponseEntity.ok().body(response);
    }

    // 1.추가
    @PostMapping
    public ResponseEntity<?> createTodo(
            @AuthenticationPrincipal String userId,
            @RequestBody TodoDTO dto) {
        try {
            String temporaryUserId = "hyeonjiChoi";

            TodoEntity entity = TodoDTO.toEntity(dto);

            entity.setId(null);

            entity.setUserId(userId);

            List<TodoEntity> entities = service.create(entity);

            List<TodoDTO> dtos = entities.stream()
                    .map(TodoDTO::new)
                    .collect(Collectors.toList());

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder()
                    .data(dtos).build();

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder()
                    .error(error).build();

            return ResponseEntity.badRequest().body(response);

        }
    }

    @GetMapping("/{title}")
    public ResponseEntity<?> retrieveTitle(@PathVariable(required = false) String title) {

        List<TodoEntity> entities = service.retrieveTitle(title);
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().
                data(dtos)
                .build();
        return ResponseEntity.ok().body(response);
    }

    // 2. 검색
    @GetMapping
    public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userId) {
        String temporaryUserId = "hyeonjiChoi";

        List<TodoEntity> entities = service.retrieve(userId);

        List<TodoDTO> dtos = entities.stream()
                .map(TodoDTO::new)
                .collect(Collectors.toList());

        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder()
                .data(dtos).build();

        return ResponseEntity.ok().body(response);
    }

    // 3. 삭제
    @DeleteMapping
    public ResponseEntity<?> deleteTodo(
            @AuthenticationPrincipal String userId,
            @RequestBody TodoDTO dto) {
        try {
            String temporaryUserId = "hyeonjiChoi";

            TodoEntity entity = TodoDTO.toEntity(dto);

            entity.setUserId(userId);

            List<TodoEntity> entities = service.delete(entity);

            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }

    }

    // 4. 수정
    @PutMapping
    public ResponseEntity<?> updateTodo(
            @AuthenticationPrincipal String userId,
            @RequestBody TodoDTO dto) {
        try {
            String temporaryUserId = "hyeonjiChoi";

            TodoEntity entity = TodoDTO.toEntity(dto);

            entity.setUserId(userId);

            TodoEntity updateEntity = service.update(entity);
            TodoDTO dtos = TodoDTO.builder() // 반환할 1개의 dto
                    .title(updateEntity.getTitle())
                    .userId(updateEntity.getUserId())
                    .id(updateEntity.getId())
                    .brand(updateEntity.getBrand())
                    .type(updateEntity.getType())
                    .build();
            SimpleResponseDTO<TodoDTO> response = SimpleResponseDTO.<TodoDTO>builder() // 반환할 TodoDTO 생성
                    .data(dtos) // 하나의 dto만을 전달하는 SimpleResponseDTO(추가함)
                    .build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            SimpleResponseDTO<TodoDTO> response = SimpleResponseDTO.<TodoDTO>builder()
                    .error(error)
                    .build();
            return ResponseEntity.badRequest().body(response);
        }

    }

}
