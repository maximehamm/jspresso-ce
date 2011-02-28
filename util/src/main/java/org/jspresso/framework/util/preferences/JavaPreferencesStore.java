/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.util.preferences;

import java.util.prefs.Preferences;

/**
 * An implementation of preference store relying on the Java preferences API.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class JavaPreferencesStore implements IPreferencesStore {

  private Preferences preferences;

  /**
   * Constructs a new <code>JavaPreferencesStore</code> instance.
   * 
   * @param root
   *          the root class from which the Java preferences entry is retrieved.
   * @param nodePath
   *          the preferences node path.
   */
  public JavaPreferencesStore(Class<?> root, String[] nodePath) {
    preferences = Preferences.userNodeForPackage(root);
    for (int i = 0; i < nodePath.length; i++) {
      preferences = preferences.node(nodePath[i]);
    }
  }

  /**
   * {@inheritDoc}
   */
  public String getPreference(String key) {
    return preferences.get(key, null);
  }

  /**
   * {@inheritDoc}
   */
  public void putPreference(String key, String value) {
    preferences.put(key, value);
  }

  /**
   * {@inheritDoc}
   */
  public void removePreference(String key) {
    preferences.remove(key);
  }

  /**
   * {@inheritDoc}
   */
  public void write() {
    // No need to perform any special save operation since Java preferences are
    // automatically persisted.
  }

  /**
   * {@inheritDoc}
   */
  public void read() {
    // No need to initialize anything from any store since Java preferences are
    // immediately available from the JVM.
  }

}
