package com.convertlab.mongo.service;

import com.convertlab.library.multitenancy.context.TenantContextHolder;
import com.convertlab.mongo.domain.User;
import com.convertlab.mongo.template.TenantAwareMongoTemplate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SpringBootTest
class TenantAwareMongoTemplateTest {
    @Resource
    private TenantAwareMongoTemplate tenantAwareMongoTemplate;

    @BeforeAll
    public static void setTenantId(){
        TenantContextHolder.setTenant(26L);
    }

    @Test
    public void query(){
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is("61a84383757f3c1bad118b4d"));
        List<User> users = tenantAwareMongoTemplate.find(query, User.class);
        System.out.println(users);
    }

    @Test
    public void insert(){
        User user = new User();
        user.setName("李0");
        user.setAge(12);
        User insert = tenantAwareMongoTemplate.insert(user);
        System.out.println(insert);
    }

    @Test
    public void batchInsert(){
        ArrayList<User> users = new ArrayList<>();
        User user = new User();
        user.setName("王五");
        user.setAge(123);
        users.add(user);
        Collection<User> users1 = tenantAwareMongoTemplate.insertAll(users);
        System.out.println(users1);
    }

    @Test
    public void update(){
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is("61a84383757f3c1bad118b4e"));
        Update update = Update.update("name", "王五").set("age", 32);
        tenantAwareMongoTemplate.updateMulti(query,update,User.class);
    }

    @Test
    public void remove(){
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is("61a84448e736a879490af589"));
        tenantAwareMongoTemplate.remove(query,User.class);
    }
}