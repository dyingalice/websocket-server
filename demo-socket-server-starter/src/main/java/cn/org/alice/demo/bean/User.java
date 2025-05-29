package cn.org.alice.demo.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {

    private String name;

    private Integer age;
}