/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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

import org.jspresso.framework.util.format.IFormatter;

/**
 * This is the interface defining the contract of a configurable connector
 * factory. These factories are designed to be used by view factories.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IConfigurableConnectorFactory {

  /**
   * Creates a <code>ICollectionConnector</code>.
   * 
   * @param id
   *          the connector identifier.
   * @param binder
   *          the MVC bider used by the created collection connector.
   * @param childConnectorPrototype
   *          the element prototype connector used by the created collection
   *          connector.
   * @return the created connector.
   */
  ICollectionConnector createCollectionConnector(String id, IMvcBinder binder,
      ICompositeValueConnector childConnectorPrototype);

  /**
   * Creates a <code>ICompositeValueConnector</code>.
   * 
   * @param id
   *          the connector identifier.
   * @param renderingConnectorId
   *          the child connector used to render the composite connector value.
   * @return the created connector.
   */
  ICompositeValueConnector createCompositeValueConnector(String id,
      String renderingConnectorId);

  /**
   * Creates a <code>IConfigurableCollectionConnectorListProvider</code>.
   * 
   * @param id
   *          the connector identifier.
   * @param renderingConnectorId
   *          the child connector used to render the composite connector value.
   * @return the created connector.
   */
  IConfigurableCollectionConnectorListProvider createConfigurableCollectionConnectorListProvider(
      String id, String renderingConnectorId);

  /**
   * Creates a <code>IConfigurableCollectionConnectorProvider</code>.
   * 
   * @param id
   *          the connector identifier.
   * @param renderingConnectorId
   *          the child connector used to render the composite connector value.
   * @return the created connector.
   */
  IConfigurableCollectionConnectorProvider createConfigurableCollectionConnectorProvider(
      String id, String renderingConnectorId);

  /**
   * Creates a <code>IFormattedValueConnector</code>.
   * 
   * @param id
   *          the connector identifier.
   * @param formatter
   *          the formatter used to parse and format connector value.
   * @return the created connector.
   */
  IFormattedValueConnector createFormattedValueConnector(String id,
      IFormatter formatter);

  /**
   * Creates a <code>IValueConnector</code>.
   * 
   * @param id
   *          the connector identifier.
   * @return the created connector.
   */
  IValueConnector createValueConnector(String id);
}
