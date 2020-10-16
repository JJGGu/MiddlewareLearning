package com.study.middleware.server.controller;

import com.study.middleware.server.entity.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: middleware
 * @description: 控制器
 * @author: JJGGu
 * @create: 2020-10-16 09:08
 **/
@Slf4j
@RestController
@RequestMapping("/book")
public class BookController {
    @GetMapping("/info")
    public Book info(Integer bookNo, String name){
        log.info("bookNo: {}, name: {}", bookNo, name);
        return new Book(bookNo, name);
    }
}
