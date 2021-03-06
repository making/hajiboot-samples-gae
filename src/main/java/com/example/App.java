package com.example;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import org.gmr.web.multipart.GMultipartResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
@Controller
public class App {

    @Bean
    @Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
    DatastoreService datastoreService() {
        return DatastoreServiceFactory.getDatastoreService();
    }

    @Bean(name = DispatcherServlet.MULTIPART_RESOLVER_BEAN_NAME)
    MultipartResolver multipartResolver(@Value("${multipart.maxFileSize:1048576}") int maxUploadSize) {
        GMultipartResolver multipartResolver = new GMultipartResolver();
        multipartResolver.setMaxUploadSize(maxUploadSize);
        return multipartResolver;
    }

    @RequestMapping("/")
    String index() {
        return "redirect:/customers";
    }
}