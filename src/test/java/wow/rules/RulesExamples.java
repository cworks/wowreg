package wow.rules;

import net.cworks.wowreg.domain.Attendee;
import net.cworks.wowreg.domain.AttendeeBuilder;
import org.easyrules.api.RulesEngine;
import org.easyrules.core.RulesEngineBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RulesExamples {

    @Test
    public void testAttendeeLimitRule() {

        List<Attendee> attendees = new ArrayList<>();
        for(int i = 0; i < 7; i++) {
            attendees.add(new AttendeeBuilder().create());
        }

        RulesEngine rulesEngine = RulesEngineBuilder.aNewRulesEngine()
            .withRuleListener(new BusinessRulesListener())
                .build();

        rulesEngine.registerRule(new AttendeeLimitRule(attendees));

        rulesEngine.fireRules();
    }
}
