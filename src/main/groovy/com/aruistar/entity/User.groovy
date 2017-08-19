package com.aruistar.entity

import io.vertx.codegen.annotations.DataObject
import io.vertx.core.json.JsonObject

@DataObject(generateConverter = true)
class User {
    private String name
    private int age

    User(String name, int age) {
        this.name = name
        this.age = age
    }

    User(JsonObject user) {
        this.name = user.getString("name")
        this.age = user.getInteger("age")
    }

    JsonObject toJson() {
        new JsonObject([
                name: name,
                age : age
        ])
    }

    @Override
    String toString() {
        return toJson()
    }
}
