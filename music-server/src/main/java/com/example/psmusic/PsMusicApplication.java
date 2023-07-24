package com.example.psmusic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
* 热更新、热加载
* 1. ctrl+shif+a  --> 搜索registry --> 勾选compiler.automake.allow.when.app.running 勾选
* 2. ctrl+F9 才进行热加载
* */
@SpringBootApplication
@MapperScan("com.example.psmusic.dao")
public class PsMusicApplication {

	public static void main(String[] args) {
		SpringApplication.run(PsMusicApplication.class, args);
	}

}
