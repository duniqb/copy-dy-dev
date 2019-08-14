package cn.duniqb.copydy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"cn.duniqb", "org.n3r"})

public class CopyDyDevApplication {

    public static void main(String[] args) {
        SpringApplication.run(CopyDyDevApplication.class, args);
    }

}
