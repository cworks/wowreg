package wow.rules;

import org.easyrules.api.Rule;
import org.easyrules.api.RuleListener;

public class BusinessRulesListener implements RuleListener {

    @Override
    public void beforeExecute(Rule rule) {


    }

    @Override
    public void onSuccess(Rule rule) {
        if(rule instanceof AttendeeLimitRule) {
            System.out.println("AttendeeLimitRule success");
        }
    }

    @Override
    public void onFailure(Rule rule, Exception exception) {
        if(rule instanceof AttendeeLimitRule) {
            System.out.println("AttendeeLimitRule failure");
        }
    }
}
