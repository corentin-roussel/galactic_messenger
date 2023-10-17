package org.example.Server.Controller;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackages = "org.example.Server.Model")
public class JpaConfig {

}
