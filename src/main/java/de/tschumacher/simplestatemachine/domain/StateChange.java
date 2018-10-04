package de.tschumacher.simplestatemachine.domain;

public class StateChange<State> {

  private final State oldState;
  private final State newState;

  public StateChange(State oldState, State newState) {
    super();
    this.oldState = oldState;
    this.newState = newState;
  }

  public State getOldState() {
    return this.oldState;
  }

  public State getNewState() {
    return this.newState;
  }



}
