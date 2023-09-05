package com.example.demo.service;

import com.example.demo.model.TodoEntity;
import com.example.demo.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TodoService {

    @Autowired
    private TodoRepository repository;

    public String testService() {
        TodoEntity entity = TodoEntity.builder().title("My first todo item").build();

        repository.save(entity);

        TodoEntity savedEntity = repository.findById(entity.getId()).get();

        return savedEntity.getTitle();
    }

    // 1. 추가
    public List<TodoEntity> create(final TodoEntity entity) {
        if(entity == null) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }

        if(entity.getUserId() == null) {
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
        
        repository.save(entity);
        
        log.info("Entity id: {} is saved.", entity.getId());
        
        return repository.findByUserId(entity.getUserId());      

        
    }

    // 2. 검색
    public List<TodoEntity> retrieve(final String userId) {

        return repository.findByUserId(userId);
    }


    public List<TodoEntity> retrieveTitle(final String title){
        return repository.findByTitle(title);
    }

    // 3. 수정
    public TodoEntity update(final TodoEntity entity) {
        validate(entity);

        final Optional<TodoEntity> original = repository.findById(entity.getId());
        TodoEntity returnEntity = null;

        if(original.isPresent()) {
            final TodoEntity todo = original.get();
            todo.setTitle(entity.getTitle());
            todo.setBrand(entity.getBrand());
            todo.setType(entity.getType());

            repository.save(todo);
            returnEntity = todo;
        };

        return returnEntity;
    }

    // 4.삭제
    public List<TodoEntity> delete(final TodoEntity entity) {
        validate(entity);

        try {
            repository.delete(entity);
        } catch (Exception e) {
            log.error("error deleting entity", entity.getId(), e);
            throw new RuntimeException("error deleting entity" + entity.getId());
        }

        return retrieve(entity.getUserId());
    }
    
    private void validate(final TodoEntity entity) {
        if(entity == null) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException(("Entity cannot be null."));
        } if(entity.getUserId() == null) {
            log.warn("Unknown user.");
            throw new RuntimeException(("Unknown user."));
        }
    }
}
