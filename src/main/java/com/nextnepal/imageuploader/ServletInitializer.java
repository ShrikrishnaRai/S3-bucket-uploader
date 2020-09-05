package com.nextnepal.imageuploader;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

//This class is used to display swagger ui in the browser it avoids (cannot find swagger endpoint)
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ImageuploaderApplication.class);
    }

}
