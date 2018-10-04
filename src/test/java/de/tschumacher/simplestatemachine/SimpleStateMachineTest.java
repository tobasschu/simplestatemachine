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

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import de.tschumacher.simplestatemachine.configuration.SimpleStateMachineConfig;
import de.tschumacher.simplestatemachine.configuration.state.StateConfiguration;
import de.tschumacher.simplestatemachine.domain.StateChange;
import de.tschumacher.simplestatemachine.exception.TransitionNotAllowedException;
import de.tschumacher.simplestatemachine.handler.StateChangeHandler;
import de.tschumacher.simplestatemachine.state.TestState;

public class SimpleStateMachineTest {
  private SimpleStateMachine<TestState, String> service = null;
  private SimpleStateMachineConfig<TestState, String> config;
  private final TestState actualState = TestState.A;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    this.config = Mockito.mock(SimpleStateMachineConfig.class);
    this.service = new DefaultSimpleStateMachine<>(this.config, this.actualState);
  }

  @After
  public void after() {
    Mockito.verifyNoMoreInteractions(this.config);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void simpleTransitionTest() {
    final TestState newState = TestState.B;
    final StateChangeHandler<TestState, String> handler = Mockito.mock(StateChangeHandler.class);
    final StateConfiguration<TestState, String> stateConfig =
        Mockito.mock(StateConfiguration.class);
    Mockito.when(this.config.fetch(this.actualState)).thenReturn(stateConfig);
    Mockito.when(stateConfig.handler(newState)).thenReturn(handler);
    Mockito.when(stateConfig.transitionAllowed(newState)).thenReturn(true);

    final String change = this.service.change(newState);
    assertNull(change);

    Mockito.verify(this.config).fetch(this.actualState);
    Mockito.verify(stateConfig).handler(newState);
    Mockito.verify(stateConfig).transitionAllowed(newState);
    Mockito.verify(handler).handle(Matchers.any(StateChange.class), Matchers.isNull(String.class));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void simpleTransitionWithContextTest() {
    final String context = "context";
    final TestState newState = TestState.B;
    final StateChangeHandler<TestState, String> handler = Mockito.mock(StateChangeHandler.class);
    Mockito.when(handler.handle(Matchers.any(StateChange.class), Matchers.eq(context)))
        .thenReturn(context);
    final StateConfiguration<TestState, String> stateConfig =
        Mockito.mock(StateConfiguration.class);
    Mockito.when(this.config.fetch(this.actualState)).thenReturn(stateConfig);
    Mockito.when(stateConfig.handler(newState)).thenReturn(handler);
    Mockito.when(stateConfig.transitionAllowed(newState)).thenReturn(true);

    final String change = this.service.change(newState, context);

    assertNotNull(change);

    Mockito.verify(this.config).fetch(this.actualState);
    Mockito.verify(stateConfig).handler(newState);
    Mockito.verify(stateConfig).transitionAllowed(newState);
    Mockito.verify(handler).handle(Matchers.any(StateChange.class), Matchers.eq(context));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void simpleTransitionWithoutHandlerTest() {
    final String context = "context";
    final TestState newState = TestState.B;
    final StateConfiguration<TestState, String> stateConfig =
        Mockito.mock(StateConfiguration.class);
    Mockito.when(this.config.fetch(this.actualState)).thenReturn(stateConfig);
    Mockito.when(stateConfig.handler(newState)).thenReturn(null);
    Mockito.when(stateConfig.transitionAllowed(newState)).thenReturn(true);

    final String change = this.service.change(newState, context);

    assertNotNull(change);

    Mockito.verify(this.config).fetch(this.actualState);
    Mockito.verify(stateConfig).handler(newState);
    Mockito.verify(stateConfig).transitionAllowed(newState);
  }


  @Test
  @SuppressWarnings("unchecked")
  public void transitionWithoutHandlerTest() {
    final TestState newState = TestState.B;
    final StateConfiguration<TestState, String> stateConfig =
        Mockito.mock(StateConfiguration.class);
    Mockito.when(this.config.fetch(this.actualState)).thenReturn(stateConfig);
    Mockito.when(stateConfig.handler(newState)).thenReturn(null);
    Mockito.when(stateConfig.transitionAllowed(newState)).thenReturn(true);

    final String change = this.service.change(newState);
    assertNull(change);

    Mockito.verify(this.config).fetch(this.actualState);
    Mockito.verify(stateConfig).handler(newState);
    Mockito.verify(stateConfig).transitionAllowed(newState);
  }

  @Test(expected = TransitionNotAllowedException.class)
  @SuppressWarnings("unchecked")
  public void changeTransitionNotAllowedTest() {
    final TestState newState = TestState.B;
    final StateConfiguration<TestState, String> stateConfig =
        Mockito.mock(StateConfiguration.class);
    Mockito.when(this.config.fetch(this.actualState)).thenReturn(stateConfig);
    Mockito.when(stateConfig.transitionAllowed(newState)).thenReturn(false);
    try {
      this.service.change(newState);
    } catch (final Exception e) {
      throw e;
    } finally {
      Mockito.verify(this.config).fetch(this.actualState);
      Mockito.verify(stateConfig).transitionAllowed(newState);
    }
  }


  @Test
  @SuppressWarnings("unchecked")
  public void transitionAllowedTest() {
    final TestState newState = TestState.B;
    final StateConfiguration<TestState, String> stateConfig =
        Mockito.mock(StateConfiguration.class);
    Mockito.when(this.config.fetch(this.actualState)).thenReturn(stateConfig);
    Mockito.when(stateConfig.transitionAllowed(newState)).thenReturn(true);

    final boolean allowed = this.service.transitionAllowed(newState);
    assertTrue(allowed);

    Mockito.verify(this.config).fetch(this.actualState);
    Mockito.verify(stateConfig).transitionAllowed(newState);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void transitionNotAllowedTest() {
    final TestState newState = TestState.B;
    final StateConfiguration<TestState, String> stateConfig =
        Mockito.mock(StateConfiguration.class);
    Mockito.when(this.config.fetch(this.actualState)).thenReturn(stateConfig);
    Mockito.when(stateConfig.transitionAllowed(newState)).thenReturn(false);

    final boolean allowed = this.service.transitionAllowed(newState);
    assertFalse(allowed);

    Mockito.verify(this.config).fetch(this.actualState);
    Mockito.verify(stateConfig).transitionAllowed(newState);
  }


  @Test
  public void transitionNotConfiguredTest() {
    final TestState newState = TestState.B;

    final boolean allowed = this.service.transitionAllowed(newState);
    assertFalse(allowed);

    Mockito.verify(this.config).fetch(this.actualState);
  }
}
