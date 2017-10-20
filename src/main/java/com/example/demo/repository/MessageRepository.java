package com.example.demo.repository;

import com.example.demo.model.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message,Integer>{
    List<Message> findTop10ByOrderById();
}
