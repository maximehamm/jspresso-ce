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
package org.jspresso.framework.binding;

/**
 * This is the interface which has to be implemented by classes which bind model
 * connectors to view connectors in a MVC relationship.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IMvcBinder {

  /**
   * Binds two connectors altogether.
   * 
   * @param viewConnector
   *            The connector for the view
   * @param modelConnector
   *            The connector for the model
   */
  void bind(IValueConnector viewConnector, IValueConnector modelConnector);
}
