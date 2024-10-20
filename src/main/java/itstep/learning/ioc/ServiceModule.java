package itstep.learning.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import itstep.learning.services.bodyparser.BodyParseService;
import itstep.learning.services.bodyparser.JsonBodyParseService;
import itstep.learning.services.bodyparser.MultipartFormBodyParseService;

public class ServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(BodyParseService.class).to(JsonBodyParseService.class);
        bind(BodyParseService.class).annotatedWith(Names.named("Multipart")).to(MultipartFormBodyParseService.class);
    }
}

