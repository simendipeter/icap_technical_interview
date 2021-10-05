package com.icap.axon.replay;

public interface ReplayCompletionAware {

    default void replayCompleted() {
    }
}
