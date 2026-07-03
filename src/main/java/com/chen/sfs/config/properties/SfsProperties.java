package com.chen.sfs.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.nio.file.Path;

@Data
@ConfigurationProperties(prefix = "sfs")
public class SfsProperties {

        private final Path pathPrefix;

        @ConstructorBinding
        public SfsProperties(Path pathPrefix) {
                this.pathPrefix = pathPrefix;
        }

}
