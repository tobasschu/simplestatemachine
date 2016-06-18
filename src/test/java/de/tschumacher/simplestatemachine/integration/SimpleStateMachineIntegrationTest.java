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
import de.tschumacher.simplestatemachine.exception.TransitionNotAllowedException;
import de.tschumacher.simplestatemachine.handler.StateChangeHandler;
import de.tschumacher.simplestatemachine.state.TestState;

public class SimpleStateMachineIntegrationTest {
  private SimpleStateMachine<TestState, String> service = null;
  private SimpleStateMachineConfig<TestState, String> config;

  @Before
  public void setUp() {
    final TestState actualState = TestState.A;
    this.config = new DefaultSimpleStateMachineConfig<TestState, String>();
    this.service = new DefaultSimpleStateMachine<TestState, String>(this.config, actualState);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void simpleTransitionTest() {
    final StateChangeHandler<String> handler = Mockito.mock(StateChangeHandler.class);
    this.config.configure(TestState.A).permit(TestState.B, handler);
    this.service.change(TestState.B);

    Mockito.verify(handler).handle(null);
  }


  @Test
  @SuppressWarnings("unchecked")
  public void simpleTransitionWithContextTest() {
    final String context = "context";
    final StateChangeHandler<String> handler = Mockito.mock(StateChangeHandler.class);
    this.config.configure(TestState.A).permit(TestState.B, handler);
    this.service.change(TestState.B, context);

    Mockito.verify(handler).handle(context);
  }


  @Test(expected = TransitionNotAllowedException.class)
  public void simpleTransitionNotConfiguredTest() {
    this.service.change(TestState.D);
  }


  @SuppressWarnings("unchecked")
  @Test(expected = TransitionNotAllowedException.class)
  public void simpleTransitionNotAllowedTest() {
    final StateChangeHandler<String> handler = Mockito.mock(StateChangeHandler.class);
    this.config.configure(TestState.A).permit(TestState.B, handler);
    this.service.change(TestState.D);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void multiTransitionTest() {
    final StateChangeHandler<String> aToBhandler = Mockito.mock(StateChangeHandler.class);
    this.config.configure(TestState.A).permit(TestState.B, aToBhandler);
    final StateChangeHandler<String> bToChandler = Mockito.mock(StateChangeHandler.class);
    this.config.configure(TestState.B).permit(TestState.C, bToChandler);

    this.service.change(TestState.B);
    this.service.change(TestState.C);

    Mockito.verify(aToBhandler).handle(null);
    Mockito.verify(bToChandler).handle(null);
  }
}
