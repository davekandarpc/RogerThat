package com.rogerthat.rlvltd.com.events;

/**
 * Created by macbookair on 06/04/17.
 */

public class GetPendingCallEvent {

    public final String pendingcallNumber;

    public GetPendingCallEvent(String pendingcallNumber) {
        this.pendingcallNumber = pendingcallNumber;
    }

}
