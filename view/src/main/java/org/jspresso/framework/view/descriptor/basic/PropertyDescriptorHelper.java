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
package org.jspresso.framework.view.descriptor.basic;

import java.util.ArrayList;
import java.util.List;

import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;

/**
 * Helper class for property descriptors.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class PropertyDescriptorHelper {

  /**
   * Constructs a new <code>PropertyDescriptorHelper</code> instance.
   */
  private PropertyDescriptorHelper() {
    // Helper class constructor
  }

  /**
   * Explodes component reference property descriptors.
   * 
   * @param propertyViewDescriptor
   *          the property view descriptor to explode if necessary.
   * @param componentDescriptorProvider
   *          the component descriptor holding the property descriptor.
   * @return the list of potentially exploded property view descriptors.
   */
  public static List<IPropertyViewDescriptor> explodeComponentReferences(
      IPropertyViewDescriptor propertyViewDescriptor,
      IComponentDescriptorProvider<?> componentDescriptorProvider) {
    List<IPropertyViewDescriptor> returnedList = new ArrayList<IPropertyViewDescriptor>();
    IComponentDescriptor<?> rootComponentDescriptor = componentDescriptorProvider
        .getComponentDescriptor();
    IPropertyDescriptor propertyDescriptor = rootComponentDescriptor
        .getPropertyDescriptor(propertyViewDescriptor.getName());
    if ((propertyDescriptor instanceof IReferencePropertyDescriptor<?> && !IEntity.class
        .isAssignableFrom(((IReferencePropertyDescriptor<?>) propertyDescriptor)
            .getReferencedDescriptor().getComponentContract()))) {
      IComponentDescriptor<?> referencedComponentDescriptor = ((IReferencePropertyDescriptor<?>) propertyDescriptor)
          .getReferencedDescriptor();
      for (String nestedRenderedProperty : referencedComponentDescriptor
          .getRenderedProperties()) {
        BasicPropertyViewDescriptor nestedPropertyViewDescriptor = new BasicPropertyViewDescriptor();
        nestedPropertyViewDescriptor.setName(propertyDescriptor.getName() + "."
            + nestedRenderedProperty);
        nestedPropertyViewDescriptor.setModelDescriptor(rootComponentDescriptor
            .getPropertyDescriptor(nestedPropertyViewDescriptor.getName()));
        returnedList.addAll(explodeComponentReferences(
            nestedPropertyViewDescriptor, componentDescriptorProvider));
      }
    } else {
      if (propertyViewDescriptor.getModelDescriptor() == null
          && propertyViewDescriptor instanceof BasicPropertyViewDescriptor) {
        ((BasicPropertyViewDescriptor) propertyViewDescriptor)
            .setModelDescriptor(propertyDescriptor);
      }
      returnedList.add(propertyViewDescriptor);
    }
    return returnedList;
  }
}
