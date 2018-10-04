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
package de.tschumacher.simplestatemachine.configuration;

import de.tschumacher.simplestatemachine.SimpleStateMachine;
import de.tschumacher.simplestatemachine.configuration.state.StateConfiguration;



public interface SimpleStateMachineConfig<State, Context> {

  StateConfiguration<State, Context> configure(State state);

  StateConfiguration<State, Context> fetch(State state);

  SimpleStateMachine<State, Context> createMachine(State state);
}
