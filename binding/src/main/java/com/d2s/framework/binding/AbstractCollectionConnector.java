/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.d2s.framework.util.collection.CollectionHelper;
import com.d2s.framework.util.event.ISelectionChangeListener;
import com.d2s.framework.util.event.SelectionChangeEvent;
import com.d2s.framework.util.event.SelectionChangeSupport;

/**
 * This class is the base class of all default collection connectors. It
 * implements the dynamic management of the child connectors which represent the
 * collection.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractCollectionConnector extends
    AbstractCompositeValueConnector implements ICollectionConnector,
    IConnectorSelector {

  private CollectionConnectorSupport collectionConnectorSupport;
  private IMvcBinder                 mvcBinder;
  private ICompositeValueConnector   childConnectorPrototype;
  private SelectionChangeSupport     connectorSelectionSupport;
  private List<IValueConnector>      removedChildrenConnectors;

  /**
   * Creates a new <code>AbstractCollectionConnector</code>.
   * 
   * @param id
   *          the connector id.
   * @param binder
   *          the <code>IMvcBinder</code> used to bind dynamicatlly created
   *          child connectors.
   * @param childConnectorPrototype
   *          the connector prototype used to create new instances of child
   *          connectors.
   */
  public AbstractCollectionConnector(String id, IMvcBinder binder,
      ICompositeValueConnector childConnectorPrototype) {
    super(id);
    this.mvcBinder = binder;
    this.childConnectorPrototype = childConnectorPrototype;
    collectionConnectorSupport = new CollectionConnectorSupport();
    connectorSelectionSupport = new SelectionChangeSupport(this);
  }

  /**
   * Dynamically adapts collection of child connectors (child connectors are
   * added or removed depending on the state of the source connector of the
   * event) before calling super implementation.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void connectorValueChange(ConnectorValueChangeEvent evt) {
    updateChildConnectors((ICompositeValueConnector) evt.getSource());
    super.connectorValueChange(evt);
  }

  /**
   * Overrides the default to produce
   * <code>CollectionConnectorValueChangeEvent</code>s.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected ConnectorValueChangeEvent createChangeEvent(
      Object oldConnectorValue, Object newConnectorValue) {
    ConnectorValueChangeEvent changeEvent = new CollectionConnectorValueChangeEvent(
        this, oldConnectorValue, newConnectorValue, removedChildrenConnectors);
    removedChildrenConnectors = null;
    return changeEvent;
  }

  /**
   * After having called the super implementation, removes the child connector
   * from the cache since it is held by the connector itself.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void addChildConnector(IValueConnector connector) {
    super.addChildConnector(connector);
    uncacheConnector(connector);
  }

  /**
   * After having called the super implementation, adds the child connector to
   * the cache for later use.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void removeChildConnector(IValueConnector connector) {
    super.removeChildConnector(connector);
    cacheConnector(connector);
  }

  private void uncacheConnector(IValueConnector connector) {
    collectionConnectorSupport.uncacheConnector(connector);
  }

  private void cacheConnector(IValueConnector connector) {
    collectionConnectorSupport.cacheConnector(connector);
  }

  private IValueConnector getCachedConnector(String connectorId) {
    return collectionConnectorSupport.getCachedConnector(connectorId);
  }

  /**
   * Updates child connectors depending on the state of the model connector.
   * 
   * @param modelConnector
   *          the model connector to synchronize with or null.
   */
  protected void updateChildConnectors(ICompositeValueConnector modelConnector) {
    Collection<String> childConnectorsToRemove = new HashSet<String>();
    removedChildrenConnectors = new ArrayList<IValueConnector>();
    childConnectorsToRemove.addAll(getChildConnectorKeys());
    if (modelConnector != null) {
      int i = 0;
      for (String nextModelConnectorId : modelConnector.getChildConnectorKeys()) {
        childConnectorsToRemove.remove(nextModelConnectorId);
        IValueConnector childModelConnector = modelConnector
            .getChildConnector(nextModelConnectorId);
        IValueConnector childConnector = getChildConnector(nextModelConnectorId);
        if (childConnector == null) {
          childConnector = getCachedConnector(nextModelConnectorId);
          if (childConnector == null) {
            childConnector = createChildConnector(computeConnectorId(i));
            mvcBinder.bind(childConnector, childModelConnector);
          }
          addChildConnector(childConnector);
        }
        i++;
      }
    }
    for (String nextModelConnectorId : childConnectorsToRemove) {
      IValueConnector removedConnector = getChildConnector(nextModelConnectorId);
      removedChildrenConnectors.add(removedConnector);
      removeChildConnector(removedConnector);
    }
  }

  private String computeConnectorId(int i) {
    return collectionConnectorSupport.computeConnectorId(getId(), i);
  }

  /**
   * creates a new connector cloning the connector prototype.
   * <p>
   * {@inheritDoc}
   */
  public IValueConnector createChildConnector(String newConnectorId) {
    return childConnectorPrototype.clone(newConnectorId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AbstractCollectionConnector clone(String newConnectorId) {
    AbstractCollectionConnector clonedConnector = (AbstractCollectionConnector) super
        .clone(newConnectorId);
    clonedConnector.collectionConnectorSupport = new CollectionConnectorSupport();
    clonedConnector.connectorSelectionSupport = new SelectionChangeSupport(
        clonedConnector);
    clonedConnector.removedChildrenConnectors = null;
    return clonedConnector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AbstractCollectionConnector clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  public IValueConnector getChildConnector(int index) {
    return getChildConnector(computeConnectorId(index));
  }

  /**
   * Takes a snapshot of the collection (does not keep the reference itself).
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object computeOldConnectorValue(Object connectorValue) {
    return CollectionHelper.cloneCollection((Collection<?>) connectorValue);
  }

  /**
   * Gets the childConnectorPrototype.
   * 
   * @return the childConnectorPrototype.
   */
  public ICompositeValueConnector getChildConnectorPrototype() {
    return childConnectorPrototype;
  }

  /**
   * {@inheritDoc}
   */
  public void addSelectionChangeListener(ISelectionChangeListener listener) {
    connectorSelectionSupport.addSelectionChangeListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  public void removeSelectionChangeListener(ISelectionChangeListener listener) {
    connectorSelectionSupport.removeSelectionChangeListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  public void setSelectedIndices(int[] newSelectedIndices) {
    connectorSelectionSupport.setSelectedIndices(newSelectedIndices);
    if (newSelectedIndices == null || newSelectedIndices.length == 0) {
      implFireSelectedConnectorChange((IValueConnector) null);
    } else {
      implFireSelectedConnectorChange(getChildConnector(newSelectedIndices[0]));
    }
  }

  /**
   * {@inheritDoc}
   */
  public int[] getSelectedIndices() {
    return connectorSelectionSupport.getSelectedIndices();
  }

  /**
   * {@inheritDoc}
   */
  public void selectionChange(SelectionChangeEvent evt) {
    if (evt.getSource() instanceof ISelectionChangeListener) {
      connectorSelectionSupport
          .addInhibitedListener((ISelectionChangeListener) evt.getSource());
    }
    try {
      setSelectedIndices(evt.getNewSelection());
    } finally {
      if (evt.getSource() instanceof ISelectionChangeListener) {
        connectorSelectionSupport
            .removeInhibitedListener((ISelectionChangeListener) evt.getSource());
      }
    }
  }

  /**
   * Returns this.
   * <p>
   * {@inheritDoc}
   */
  public ICollectionConnector getCollectionConnector() {
    return this;
  }

  /**
   * Returns singleton list of this.
   * <p>
   * {@inheritDoc}
   */
  public List<ICollectionConnector> getCollectionConnectors() {
    return Collections.singletonList((ICollectionConnector) this);
  }

  /**
   * Gets the removedChildrenConnectors.
   * 
   * @return the removedChildrenConnectors.
   */
  public List<IValueConnector> getRemovedChildrenConnectors() {
    return removedChildrenConnectors;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void connectorModelChange(IValueConnector oldModelConnector,
      IValueConnector newModelConnector) {
    if (oldModelConnector != newModelConnector) {
      // don't bind to model connector selections.
      // if (getModelConnector() != null) {
      // removeSelectionChangeListener((ICollectionConnector)
      // getModelConnector());
      // ((ICollectionConnector) getModelConnector())
      // .removeSelectionChangeListener(this);
      // }
      collectionConnectorSupport.clearConnectorCache(mvcBinder);
      // don't bind to model connector selections.
      // if (getModelConnector() != null) {
      // addSelectionChangeListener((ICollectionConnector) getModelConnector());
      // ((ICollectionConnector) getModelConnector())
      // .addSelectionChangeListener(this);
      // }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return getId();
  }

  /**
   * {@inheritDoc}
   */
  public void addConnectorSelectionListener(IConnectorSelectionListener listener) {
    implAddConnectorSelectionListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  public void fireSelectedConnectorChange(ConnectorSelectionEvent evt) {
    implFireSelectedConnectorChange(evt);
  }

  /**
   * {@inheritDoc}
   */
  public void removeConnectorSelectionListener(
      IConnectorSelectionListener listener) {
    implRemoveConnectorSelectionListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  public void setTracksChildrenSelection(boolean tracksChildrenSelection) {
    implSetTracksChildrenSelection(tracksChildrenSelection);
  }

}
