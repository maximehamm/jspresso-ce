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
package org.jspresso.framework.binding.remote;

import java.util.ArrayList;
import java.util.List;

import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IMvcBinder;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.basic.BasicCollectionConnector;
import org.jspresso.framework.state.remote.IRemoteStateOwner;
import org.jspresso.framework.state.remote.RemoteCollectionValueState;
import org.jspresso.framework.state.remote.RemoteValueState;
import org.jspresso.framework.util.remote.IRemotePeer;

/**
 * The server peer of a remote collection connector.
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
public class RemoteCollectionConnector extends BasicCollectionConnector
    implements IRemotePeer, IRemoteStateOwner {

  private String                     guid;
  private RemoteCollectionValueState state;
  private RemoteConnectorFactory     connectorFactory;

  /**
   * Constructs a new <code>RemoteCollectionConnector</code> instance.
   * 
   * @param id
   *          the connector id.
   * @param binder
   *          the MVC binder.
   * @param childConnectorPrototype
   *          the prototype of connector children.
   * @param connectorFactory
   *          the remote connector factory.
   */
  public RemoteCollectionConnector(String id, IMvcBinder binder,
      ICompositeValueConnector childConnectorPrototype,
      RemoteConnectorFactory     connectorFactory) {
    super(id, binder, childConnectorPrototype);
    this.guid = connectorFactory.generateGUID();
    this.connectorFactory = connectorFactory;
  }

  /**
   * Gets the guid.
   * 
   * @return the guid.
   */
  public String getGuid() {
    return guid;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteCollectionConnector clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteCollectionConnector clone(String newConnectorId) {
    RemoteCollectionConnector clonedConnector = (RemoteCollectionConnector) super
        .clone(newConnectorId);
    clonedConnector.guid = connectorFactory.generateGUID();
    clonedConnector.state = null;
    connectorFactory.attachListeners(clonedConnector);
    return clonedConnector;
  }

  /**
   * {@inheritDoc}
   */
  public RemoteCollectionValueState getState() {
    if (state == null) {
      state = createState();
    }
    return state;
  }

  /**
   * Creates a new state instance rerpesenting this connector.
   * 
   * @return the newly created state.
   */
  protected RemoteCollectionValueState createState() {
    RemoteCollectionValueState createdState = new RemoteCollectionValueState(
        getGuid());
    createdState.setValue(getDisplayValue());
    createdState.setReadable(isReadable());
    createdState.setWritable(isWritable());
    createdState.setDescription(getDisplayDescription());
    createdState.setIconImageUrl(getDisplayIconImageUrl());
    createdState.setSelectedIndices(getSelectedIndices());
    List<RemoteValueState> children = new ArrayList<RemoteValueState>();
    for (int i = 0; i < getChildConnectorCount(); i++) {
      IValueConnector childConnector = getChildConnector(i);
      if (childConnector instanceof IRemoteStateOwner) {
        children.add(((IRemoteStateOwner) childConnector).getState());
      }
    }
    createdState.setChildren(children);
    return createdState;
  }
}
