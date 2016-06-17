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
package de.tschumacher.simplestatemachine.integration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.tschumacher.simplestatemachine.DefaultSimpleStateMachine;
import de.tschumacher.simplestatemachine.SimpleStateMachine;
import de.tschumacher.simplestatemachine.configuration.DefaultSimpleStateMachineConfig;
import de.tschumacher.simplestatemachine.configuration.SimpleStateMachineConfig;
import de.tschumacher.simplestatemachine.configuration.handler.StateChangeHandler;
import de.tschumacher.simplestatemachine.exception.TransitionNotAllowedException;
import de.tschumacher.simplestatemachine.state.TestState;

public class SimpleStateMachineIntegrationTest {
  private SimpleStateMachine<TestState> service = null;
  private SimpleStateMachineConfig<TestState> config;

  @Before
  public void setUp() {
    final TestState actualState = TestState.A;
    this.config = new DefaultSimpleStateMachineConfig<TestState>();
    this.service = new DefaultSimpleStateMachine<TestState>(this.config, actualState);
  }

  @Test
  public void simpleTransitionTest() {
    final StateChangeHandler handler = Mockito.mock(StateChangeHandler.class);
    this.config.configure(TestState.A).permit(TestState.B, handler);
    this.service.change(TestState.B);

    Mockito.verify(handler).handle();
  }

  @Test(expected = TransitionNotAllowedException.class)
  public void simpleTransitionNotConfiguredTest() {
    this.service.change(TestState.D);
  }


  @Test(expected = TransitionNotAllowedException.class)
  public void simpleTransitionNotAllowedTest() {
    final StateChangeHandler handler = Mockito.mock(StateChangeHandler.class);
    this.config.configure(TestState.A).permit(TestState.B, handler);
    this.service.change(TestState.D);
  }

  @Test
  public void multiTransitionTest() {
    final StateChangeHandler aToBhandler = Mockito.mock(StateChangeHandler.class);
    this.config.configure(TestState.A).permit(TestState.B, aToBhandler);
    final StateChangeHandler bToChandler = Mockito.mock(StateChangeHandler.class);
    this.config.configure(TestState.B).permit(TestState.C, bToChandler);

    this.service.change(TestState.B);
    this.service.change(TestState.C);

    Mockito.verify(aToBhandler).handle();
    Mockito.verify(bToChandler).handle();
  }
}
