package com.aruistar.entity

import io.vertx.codegen.annotations.DataObject
import io.vertx.core.json.JsonObject

@DataObject(generateConverter = true)
class User {
    private String name
    private int age

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    int getAge() {
        return age
    }

    void setAge(int age) {
        this.age = age
    }

    User(String name, int age) {
        this.name = name
        this.age = age
    }

    User(JsonObject user) {
        UserConverter.fromJson(user, this)
    }

    JsonObject toJson() {
        def json = new JsonObject()
        UserConverter.toJson(this, json)
        return json
    }

    @Override
    String toString() {
        return toJson()
    }
}
