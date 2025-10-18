package accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;

import config.AppConfig;

@SpringBootApplication
@Import(AppConfig.class)
@EntityScan("rewards.internal")
public class ActuatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActuatorApplication.class, args);
    }
}

/*
 * HACK: jconsole on wayland, needs xwayland, i don't!
 *
 * TODO-27 (Optional): Access Actuator endpoints using JMX
 * - In a terminal window, run "jconsole" (from <JDK-directory>/bin)
 * - Select "accounts.ActuatorApplication" under "Local Process"
 * then click "Connect"
 * - Click "insecure connection" if prompted
 * - Select the MBeans tab, find the "org.springframework.boot"
 * folder, then open the "Endpoint" sub-folder
 * - Note that all the actuator endpoints ARE exposed for JMX
 * - Expand Health->Operations->health
 * - Click "health" button on the top right pane
 * - Observe the health data gets displayed
 *
 */
