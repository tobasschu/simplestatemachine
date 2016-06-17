/*
 * Copyright 2016 Tobias Schumacher
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package de.tschumacher.simplestatemachine;

import de.tschumacher.simplestatemachine.configuration.SimpleStateMachineConfig;
import de.tschumacher.simplestatemachine.configuration.handler.StateChangeHandler;
import de.tschumacher.simplestatemachine.configuration.state.StateConfiguration;
import de.tschumacher.simplestatemachine.exception.TransitionNotAllowedException;

public class DefaultSimpleStateMachine<State> implements SimpleStateMachine<State> {

  private final SimpleStateMachineConfig<State> config;
  private State actualState;


  public DefaultSimpleStateMachine(SimpleStateMachineConfig<State> config, State actualState) {
    super();
    this.config = config;
    this.actualState = actualState;
  }


  @Override
  public void change(State newState) {
    final StateConfiguration<State> actualStateConfig = this.config.fetch(this.actualState);
    if (actualStateConfig == null || !actualStateConfig.transitionAllowed(newState))
      throw new TransitionNotAllowedException();

    final StateChangeHandler handler = actualStateConfig.handler(newState);
    if (handler != null) {
      handler.handle();
    }
    this.actualState = newState;
  }
}
