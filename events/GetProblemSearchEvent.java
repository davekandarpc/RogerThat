package com.rogerthat.rlvltd.com.events;

/**
 * Created by macbookair on 06/04/17.
 */

public class GetProblemSearchEvent {

    public final String pendingcallNumber;

    public GetProblemSearchEvent(String pendingcallNumber) {
        this.pendingcallNumber = pendingcallNumber;
    }

}
