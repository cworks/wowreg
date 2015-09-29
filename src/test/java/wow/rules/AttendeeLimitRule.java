package wow.rules;

import net.cworks.wowreg.domain.Attendee;
import org.easyrules.annotation.Action;
import org.easyrules.annotation.Condition;
import org.easyrules.annotation.Rule;

import java.util.ArrayList;
import java.util.List;

@Rule(name="attendeeLimitRule")
public class AttendeeLimitRule {

    public static final int LIMIT = 6;

    private final List<Attendee> attendees;

    public AttendeeLimitRule(final List<Attendee> attendees) {
        if(attendees == null) {
            this.attendees = new ArrayList<>();
        } else {
            this.attendees = attendees;
        }
    }

    /**
     * If this condition returns true then @Action method will get invoked
     * @return
     */
    @Condition
    public boolean isOverLimit() {
        return this.attendees.size() > LIMIT;
    }

    @Action
    public void createError() {
        System.out.println("Creating Error Response");
    }

}
