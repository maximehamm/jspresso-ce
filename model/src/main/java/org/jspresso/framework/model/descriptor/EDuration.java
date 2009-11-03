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
package org.jspresso.framework.model.descriptor;


/**
 * Duration constants.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public enum EDuration {
  
  /**
   * <code>ONE_DAY</code>.
   */
  ONE_DAY(24 * 60 * 60 * 1000),
  
  /**
   * <code>ONE_HOUR</code>.
   */
  ONE_HOUR(60 * 60 * 1000),
  
  /**
   * <code>ONE_MINUTE</code>.
   */
  ONE_MINUTE(60 * 1000),
  
  /**
   * <code>ONE_SECOND</code>.
   */
  ONE_SECOND(1000),
  
  /**
   * <code>ONE_WEEK</code>.
   */
  ONE_WEEK(7 * 24 * 60 * 60 * 1000);
  
  private int millis;
  
  private EDuration(int millis) {
    this.millis = millis;
  }
  
  /**
   * get milliseconds value.
   * 
   * @return milliseconds value;
   */
  public int getMillis() {
    return millis;
  }

}
