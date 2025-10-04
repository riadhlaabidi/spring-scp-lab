package rewards;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import config.RewardsConfig;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ConfigurationPropertiesScan(basePackageClasses = BasePackageMarker.class)
@Import(RewardsConfig.class)
public class RewardsApplication {
    static final String SQL = "SELECT count(*) FROM T_ACCOUNT";

    final Logger logger = LoggerFactory.getLogger(RewardsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(RewardsApplication.class, args);
    }

    @Bean
    CommandLineRunner accountsCount(JdbcTemplate jdbcTemplate) {
        return args -> logger.info("Hello, there are {} accounts", jdbcTemplate.queryForObject(SQL, Long.class));
    }

    @Bean
    CommandLineRunner rewardRecipients(RewardsRecipientProperties recipientProperties) {
        return args -> logger.info("Rewards recipient name is: {}", recipientProperties.getName());
    }
}
