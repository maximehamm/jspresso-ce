/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.gui.remote;

/**
 * A remote number field component.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class RNumericComponent extends RComponent {

  private static final long serialVersionUID = 2168149225835180375L;

  private Number            maxValue;
  private Number            minValue;

  /**
   * Constructs a new <code>RNumericComponent</code> instance. Only used for GWT
   * serialization support.
   */
  protected RNumericComponent() {
    // For GWT support
  }

  /**
   * Constructs a new <code>RIntegerField</code> instance.
   * 
   * @param guid
   *          the guid.
   */
  public RNumericComponent(String guid) {
    super(guid);
  }

  /**
   * Gets the maxValue.
   * 
   * @return the maxValue.
   */
  public Number getMaxValue() {
    return maxValue;
  }

  /**
   * Gets the minValue.
   * 
   * @return the minValue.
   */
  public Number getMinValue() {
    return minValue;
  }

  /**
   * Sets the maxValue.
   * 
   * @param maxValue
   *          the maxValue to set.
   */
  public void setMaxValue(Number maxValue) {
    this.maxValue = maxValue;
  }

  /**
   * Sets the minValue.
   * 
   * @param minValue
   *          the minValue to set.
   */
  public void setMinValue(Number minValue) {
    this.minValue = minValue;
  }
}
