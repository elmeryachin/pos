package bo.clync.pos;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Created by eyave on 05-10-17.
 */
@SpringBootApplication
public class App extends SpringBootServletInitializer {


    public static void main( String[] args ) {
        SpringApplication.run(App.class);
    }
/*
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(App.class);
    }

    private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder) {
        return builder.sources(App.class).bannerMode(Banner.Mode.OFF);
    }*/
}
