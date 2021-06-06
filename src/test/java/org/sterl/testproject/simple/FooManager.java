package org.sterl.testproject.simple;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class FooManager {

    @Inject BarActivity activity;
    
    public SimpleEntityBE doStruff() {
        return activity.delegateStuff();
    }
}
