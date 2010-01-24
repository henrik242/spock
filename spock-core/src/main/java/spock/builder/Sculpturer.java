/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package spock.builder;

import org.codehaus.groovy.runtime.InvokerHelper;

import groovy.lang.*;

/**
 * Forms a gestalt from its blueprint.
 */
public class Sculpturer extends GroovyObjectSupport {
  private IGestalt $gestalt;

  public void $form(IGestalt gestalt) {
    Closure blueprint = gestalt.getBlueprint();
    if (blueprint == null) return;

    $gestalt = gestalt;
    blueprint.setDelegate(this);
    blueprint.setResolveStrategy(Closure.DELEGATE_FIRST);
    blueprint.call(gestalt.getSubject());
  }

  public Object getProperty(String name) {
    return $gestalt.getValue(name);
  }

  public void setProperty(String name, Object value) {
    $gestalt.setValue(name, value);
  }

  public Object invokeMethod(String name, Object args) {
    IGestalt subGestalt = $gestalt.subGestalt(name, InvokerHelper.asArray(args));
    new Sculpturer().$form(subGestalt);
    return null;
  }
}