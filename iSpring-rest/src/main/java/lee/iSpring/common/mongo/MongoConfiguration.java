package lee.iSpring.common.mongo;

import com.mongodb.Mongo;
import com.mongodb.MongoClientURI;
import lee.iSpring.common.bean.PropertiesObject;
import lee.iSpring.common.util.SpringContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;


//@Configuration
public class MongoConfiguration extends AbstractMongoConfiguration{

    private PropertiesObject propertiesObject = SpringContextHolder.getBean(PropertiesObject.class);

    @Bean
    public MongoDbFactory mongoDbFactory()throws Exception{

        return  new SimpleMongoDbFactory(new MongoClientURI(propertiesObject.getMongoUrl()));
    }

    @Bean
    public MongoTemplate mongoTemplate()throws Exception{

        return new MongoTemplate(mongoDbFactory());
    }

    @Bean
    public GridFsTemplate gridFsTemplate()throws Exception{

        return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
    }

    @Override
    protected String getDatabaseName() {
        return null;
    }

    @Override
    public Mongo mongo() throws Exception {
        return null;
    }
}
