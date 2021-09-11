package de.ckthomas.smarthome.configuration;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * Will be check, while Spring boot will scan this class. Check the META-INF/spring.factories file
 *
 * @author Christian Thomas
 */
@Configuration
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE) // first camunda should be configured
@ConditionalOnBean(type = "org.camunda.bpm.engine.ProcessEngine") // beware, that the process engine exists
public class SpringBootConfiguration {

    @Configuration
    @ComponentScan(basePackageClasses = {})
    public static class ComponentScanConfiguration {}

}
