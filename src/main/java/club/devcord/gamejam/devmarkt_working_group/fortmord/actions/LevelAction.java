package club.devcord.gamejam.devmarkt_working_group.fortmord.actions;

import io.micronaut.context.annotation.Bean;
import io.micronaut.core.annotation.Introspected;

@Bean
@Introspected
public @interface LevelAction {
    int value();

    boolean levelBound() default false;
}
