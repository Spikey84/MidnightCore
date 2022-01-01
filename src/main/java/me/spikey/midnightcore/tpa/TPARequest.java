package me.spikey.midnightcore.tpa;

import java.util.UUID;

public class TPARequest {
    private UUID requester;
    private UUID target;

    public TPARequest(UUID requester, UUID target) {
        this.requester = requester;
        this.target = target;
    }

    public UUID getRequester() {
        return requester;
    }

    public UUID getTarget() {
        return target;
    }
}
