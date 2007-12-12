/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.model;

import java.beans.PropertyChangeEvent;
import java.util.Collection;

import com.d2s.framework.binding.ChildConnectorSupport;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.binding.IConnectorMap;
import com.d2s.framework.binding.IConnectorMapProvider;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.model.IModelChangeListener;
import com.d2s.framework.model.IModelProvider;
import com.d2s.framework.model.ModelChangeEvent;
import com.d2s.framework.model.ModelChangeSupport;
import com.d2s.framework.model.descriptor.IComponentDescriptorProvider;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.model.entity.IQueryEntity;

/**
 * This class is a model property connector which manages a model reference
 * property.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */

public class ModelRefPropertyConnector extends ModelPropertyConnector implements
    ICompositeValueConnector, IConnectorMapProvider, IModelProvider {

  /**
   * <code>THIS_PROPERTY</code> is a fake property name returning the model
   * itself.
   */
  public static final String     THIS_PROPERTY = "&this";
  private IConnectorMap          childConnectors;
  private ChildConnectorSupport  childConnectorSupport;
  private ModelChangeSupport     modelChangeSupport;

  private IModelConnectorFactory modelConnectorFactory;

  /**
   * Constructs a new model property connector on a model reference property.
   * 
   * @param modelDescriptor
   *            the model descriptor backing this connector.
   * @param modelConnectorFactory
   *            the factory used to create the property connectors.
   */
  ModelRefPropertyConnector(IComponentDescriptorProvider<?> modelDescriptor,
      IModelConnectorFactory modelConnectorFactory) {
    super(modelDescriptor, modelConnectorFactory.getAccessorFactory());
    this.modelConnectorFactory = modelConnectorFactory;
    modelChangeSupport = new ModelChangeSupport(this);
    childConnectors = new ModelConnectorMap(this, modelConnectorFactory);
    childConnectorSupport = new ChildConnectorSupport(this);
  }

  /**
   * {@inheritDoc}
   */
  public void addChildConnector(@SuppressWarnings("unused")
  IValueConnector childConnector) {
    throw new UnsupportedOperationException();
  }

  /**
   * The child connectors will use this method to keep track of the referenced
   * model. They will then be notified of the model reference changes.
   * <p>
   * {@inheritDoc}
   */
  public void addModelChangeListener(IModelChangeListener listener) {
    if (listener != null) {
      modelChangeSupport.addModelChangeListener(listener);
    }
  }

  /**
   * {@inheritDoc}
   */
  public boolean areChildrenReadable() {
    return isReadable();
  }

  /**
   * {@inheritDoc}
   */
  public boolean areChildrenWritable() {
    return isWritable();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelRefPropertyConnector clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelRefPropertyConnector clone(String newConnectorId) {
    ModelRefPropertyConnector clonedConnector = (ModelRefPropertyConnector) super
        .clone(newConnectorId);
    clonedConnector.modelChangeSupport = new ModelChangeSupport(clonedConnector);
    clonedConnector.childConnectors = new ModelConnectorMap(clonedConnector,
        modelConnectorFactory);
    clonedConnector.childConnectorSupport = new ChildConnectorSupport(
        clonedConnector);
    return clonedConnector;
  }

  /**
   * {@inheritDoc}
   */
  public IValueConnector getChildConnector(String connectorKey) {
    if (THIS_PROPERTY.equals(connectorKey)) {
      return this;
    }
    return childConnectorSupport.getChildConnector(connectorKey);
  }

  /**
   * {@inheritDoc}
   */
  public int getChildConnectorCount() {
    return getChildConnectorKeys().size();
  }

  /**
   * {@inheritDoc}
   */
  public Collection<String> getChildConnectorKeys() {
    return childConnectorSupport.getChildConnectorKeys();
  }

  /**
   * Returns the child connectors of this <code>ModelRefPropertyConnector</code>
   * instance. This is the lazilly created map of
   * <code>ModelPropertyConnector</code> s connected to the model properties
   * of the referenced model.
   * <p>
   * {@inheritDoc}
   */
  public IConnectorMap getConnectorMap() {
    return childConnectors;
  }

  /**
   * Returns the referenced model.
   * <p>
   * {@inheritDoc}
   */
  public Object getModel() {
    return getConnecteeValue();
  }

  /**
   * Overriden to deal with polymorphism.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public IComponentDescriptorProvider<?> getModelDescriptor() {
    IComponentDescriptorProvider<?> registeredModelDescriptor = (IComponentDescriptorProvider<?>) super
        .getModelDescriptor();
    if (getModel() instanceof IEntity && !(getModel() instanceof IQueryEntity)) {
      Class<? extends IEntity> entityContract = ((IEntity) getModel())
          .getContract();
      if (!entityContract.equals(registeredModelDescriptor.getModelType())) {
        // we must take care of subclasses (polymorphism)
        return modelConnectorFactory.getDescriptorRegistry()
            .getComponentDescriptor(entityContract);
      }
    }
    return registeredModelDescriptor;
  }

  /**
   * After having performed the standard (super implementation) handling of the
   * <code>ModelChangeEvent</code>, it will notify its child connectors of
   * the referenced model change.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void modelChange(ModelChangeEvent evt) {
    // preserve the old value before it gets changed.
    Object oldValue = getOldConnectorValue();
    // handle the change normally
    super.modelChange(evt);
    // then notify the listeners
    fireModelChange(oldValue, getConnecteeValue());
  }

  /**
   * The referenced model of this <code>ModelRefPropertyConnector</code>
   * changed. It will notify its <code>IModelChangeListener</code> s (i.e. the
   * child property connectors) of the change.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    super.propertyChange(evt);
    fireModelChange(evt.getOldValue(), evt.getNewValue());
  }

  /**
   * {@inheritDoc}
   * 
   * @see #addModelChangeListener(IModelChangeListener)
   */
  public void removeModelChangeListener(IModelChangeListener listener) {
    if (listener != null) {
      modelChangeSupport.removeModelChangeListener(listener);
    }
  }

  /**
   * Notifies its listeners that the connector's model changed.
   * 
   * @param oldModel
   *            The old model of the connector
   * @param newModel
   *            The new model of the connector
   */
  protected void fireModelChange(Object oldModel, Object newModel) {
    modelChangeSupport.fireModelChange(oldModel, newModel);
  }
}
