package com.convertlab.mongo.service;

import com.convertlab.library.multitenancy.context.TenantContextHolder;
import com.convertlab.mongo.domain.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class MongoTemplateTest {
    @Resource
    private MongoTemplate mongoTemplate;

    @BeforeAll
    public static void setTenantId(){
        TenantContextHolder.setTenant(27L);
    }

    @Test
    public void query(){
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is("61a84383757f3c1bad118b4d"));
        List<User> users = mongoTemplate.find(query, User.class);
        System.out.println(users);
    }

    @Test
    public void insert(){
        User user = new User();
        user.setName("张0");
        user.setAge(12);
        User insert = mongoTemplate.insert(user);
        System.out.println(insert);
    }

    @Test
    public void batchInsert(){
        ArrayList<User> users = new ArrayList<>();
        User user = new User();
        user.setName("李四");
        user.setAge(123);
        users.add(user);
        mongoTemplate.insertAll(users);
    }

    @Test
    public void update(){
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is("61a8436d344b0125d9d86d41"));
        Update update = Update.update("name", "张三").set("age", 22);
        mongoTemplate.updateMulti(query,update,User.class);
    }

    @Test
    public void remove(){
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is("61a83f7ed67a8c484dc9aab1"));
        mongoTemplate.remove(query,User.class);
    }
}