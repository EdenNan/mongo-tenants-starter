package com.convertlab.mongo.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "user1")
public class User {
    private String id;

    private String name;

    private int age;
}
