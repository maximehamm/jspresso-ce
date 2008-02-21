/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.component;

import com.d2s.framework.model.descriptor.IComponentDescriptorRegistry;

/**
 * This interface defines the contract of a component factory.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IComponentFactory extends IComponentDescriptorRegistry {

  /**
   * Creates a new component instance based on the component descriptor.
   * 
   * @param <T>
   *            the concrete class of the created component.
   * @param componentContract
   *            the class of the component to create.
   * @return the component instance.
   */
  <T extends IComponent> T createComponentInstance(Class<T> componentContract);

  /**
   * Creates a new component instance based on the component descriptor. All
   * method calls are handled by the component delegate.
   * 
   * @param <T>
   *            the concrete class of the created component.
   * @param componentContract
   *            the class of the component to create.
   * @param delegate
   *            the component delegate instance.
   * @return the component instance.
   */
  <T extends IComponent> T createComponentInstance(Class<T> componentContract,
      Object delegate);

  /**
   * Creates a new query component instance based on the component descriptor.
   * 
   * @param componentContract
   *            the class of the component to create.
   * @return the query component instance.
   */
  IQueryComponent createQueryComponentInstance(
      Class<? extends IComponent> componentContract);
}
