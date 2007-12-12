/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class is a simple implementation of IConnectorMap. For instance, it may
 * serve for federating all the connectors of a view designed to represent the
 * same model.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ConnectorMap implements IConnectorMap {

  private ICompositeValueConnector     parentConnector;
  private Map<String, IValueConnector> storageMap;

  /**
   * Constructs a new <code>ConnectorMap</code> instance.
   * 
   * @param parentConnector
   *            the composite connector holding the connector map.
   */
  public ConnectorMap(ICompositeValueConnector parentConnector) {
    this.parentConnector = parentConnector;
  }

  /**
   * {@inheritDoc}
   */
  public void addConnector(String storageKey, IValueConnector connector) {
    if (storageKey != null && connector != null) {
      getStorageMap().put(storageKey, connector);
      connector.setParentConnector(parentConnector);
    }
  }

  /**
   * {@inheritDoc}
   */
  public IValueConnector getConnector(String connectorId) {
    return getStorageMap().get(connectorId);
  }

  /**
   * {@inheritDoc}
   */
  public Collection<String> getStorageKeys() {
    return getStorageMap().keySet();
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasConnectors() {
    return storageMap != null && !storageMap.isEmpty();
  }

  /**
   * {@inheritDoc}
   */
  public void removeConnector(String storageKey) {
    IValueConnector connectorToRemove = null;
    if (storageKey != null) {
      connectorToRemove = getStorageMap().remove(storageKey);
    }
    if (connectorToRemove != null) {
      connectorToRemove.setParentConnector(null);
    }
  }

  /**
   * Gets the parentConnector.
   * 
   * @return the parentConnector.
   */
  protected ICompositeValueConnector getParentConnector() {
    return parentConnector;
  }

  /**
   * @return Returns the storageMap.
   */
  private Map<String, IValueConnector> getStorageMap() {
    if (storageMap == null) {
      storageMap = new LinkedHashMap<String, IValueConnector>(8);
    }
    return storageMap;
  }
}
