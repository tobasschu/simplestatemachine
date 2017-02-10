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
package de.tschumacher.simplestatemachine.configuration.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.tschumacher.simplestatemachine.handler.StateChangeHandler;
import de.tschumacher.simplestatemachine.state.StringService;
import de.tschumacher.simplestatemachine.state.TestState;

public class StateConfigurationTest {
  private StateConfiguration<TestState, String, StringService> service = null;

  @Before
  public void setUp() {
    this.service = new DefaultStateConfiguration<TestState, String, StringService>();
  }

  @Test
  public void transitionAllowedTest() {
    final TestState state = TestState.A;
    this.service.permit(state);

    assertTrue(this.service.transitionAllowed(state));
  }

  @Test
  public void transitionNotAllowedTest() {
    final TestState state = TestState.A;

    assertFalse(this.service.transitionAllowed(state));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void transitionAllowedWithHandlerTest() {
    final TestState state = TestState.A;
    final StateChangeHandler<String, StringService> handler =
        Mockito.mock(StateChangeHandler.class);
    this.service.permit(state, handler);
    assertTrue(this.service.transitionAllowed(state));
  }

  @Test
  public void handlerNotAvailableTest() {
    final TestState state = TestState.A;
    this.service.permit(state);
    assertNull(this.service.handler(state));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void handlerAvailableTest() {
    final TestState state = TestState.A;
    final StateChangeHandler<String, StringService> handler =
        Mockito.mock(StateChangeHandler.class);
    this.service.permit(state, handler);
    final StateChangeHandler<String, StringService> fetchedHandler = this.service.handler(state);
    assertNotNull(fetchedHandler);
    assertEquals(handler, fetchedHandler);
  }
}
