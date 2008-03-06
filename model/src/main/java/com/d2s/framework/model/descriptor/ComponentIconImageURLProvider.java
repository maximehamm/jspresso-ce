/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.descriptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.d2s.framework.model.descriptor.util.ComponentInheritanceComparator;
import com.d2s.framework.util.IIconImageURLProvider;

/**
 * This class uses a collection of component descriptors to be able to determine
 * the rendering image of a component based on its contract. It basically
 * iterates over the descriptor collection and returns the image url of the
 * first compatible descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ComponentIconImageURLProvider implements IIconImageURLProvider {

  private Collection<IComponentDescriptor<?>> componentDescriptors;
  private IComponentDescriptorRegistry        componentDescriptorRegistry;

  /**
   * {@inheritDoc}
   */
  public String getIconImageURLForObject(Object userObject) {
    if (userObject == null) {
      return null;
    }
    if (componentDescriptors == null) {
      componentDescriptors = new ArrayList<IComponentDescriptor<?>>(
          componentDescriptorRegistry.getComponentDescriptors());
      Collections.sort((List<IComponentDescriptor<?>>) componentDescriptors,
          new ComponentInheritanceComparator());
    }
    Class<?> modelClass = userObject.getClass();
    for (IComponentDescriptor<?> componentDescriptor : componentDescriptors) {
      if (((Class<?>) componentDescriptor.getComponentContract())
          .isAssignableFrom(modelClass)) {
        return componentDescriptor.getIconImageURL();
      }
    }
    return null;
  }

  /**
   * Sets the componentDescriptors.
   * 
   * @param componentDescriptors
   *            the componentDescriptors to set.
   */
  public void setComponentDescriptors(
      Collection<IComponentDescriptor<?>> componentDescriptors) {
    this.componentDescriptors = componentDescriptors;
  }

  /**
   * Sets the componentDescriptorRegistry.
   * 
   * @param componentDescriptorRegistry
   *            the componentDescriptorRegistry to set.
   */
  public void setComponentDescriptorRegistry(
      IComponentDescriptorRegistry componentDescriptorRegistry) {
    this.componentDescriptorRegistry = componentDescriptorRegistry;
  }
}
