package com.example.demo.dto;

import com.example.demo.model.TodoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TodoDTO {
    private String id;
    private String title;

    private String userId;

    private String brand;

    private String type;



    public TodoDTO(final TodoEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.userId = entity.getUserId();
        this.brand = entity.getBrand();
        this.type = entity.getType();
    }

    public static TodoEntity toEntity(TodoDTO dto) {
        return TodoEntity.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .userId(dto.getUserId())
                .brand(dto.getBrand())
                .type(dto.getType())
                .build();
    }
}
