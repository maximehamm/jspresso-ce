/*
 * Copyright (c) 2005-2014 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.view.mobile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.application.frontend.action.std.EditComponentAction;
import org.jspresso.framework.application.view.BasicQueryViewDescriptorFactory;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.query.ComparableQueryStructureDescriptor;
import org.jspresso.framework.model.descriptor.query.EnumQueryStructureDescriptor;
import org.jspresso.framework.util.descriptor.DefaultDescriptor;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.ICompositeViewDescriptor;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicComponentViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicReferencePropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileComponentViewDescriptor;

/**
 * A default implementation for query view descriptor factory.
 *
 * @version $LastChangedRevision : 1091 $
 * @author Vincent Vandenschrick
 * @param <E>           the actual gui component type used.
 * @param <F>           the actual icon type used.
 * @param <G>           the actual action type used.
 */
public class MobileQueryViewDescriptorFactory<E, F, G> extends BasicQueryViewDescriptorFactory<E, F, G> {

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("ConstantConditions")
  @Override
  public IViewDescriptor createQueryViewDescriptor(
      IComponentDescriptorProvider<IComponent> componentDescriptorProvider,
      IComponentDescriptor<? extends IQueryComponent> queryComponentDescriptor, Map<String, Object> actionContext) {
    MobileComponentViewDescriptor queryComponentViewDescriptor = new MobileComponentViewDescriptor();
    List<IPropertyViewDescriptor> propertyViewDescriptors = new ArrayList<>();
    for (String queriableProperty : componentDescriptorProvider
        .getQueryableProperties()) {
      IPropertyDescriptor actualPropertyDescriptor = queryComponentDescriptor
          .getPropertyDescriptor(queriableProperty);
      if (actualPropertyDescriptor instanceof ComparableQueryStructureDescriptor) {
          BasicReferencePropertyViewDescriptor propertyView = new BasicReferencePropertyViewDescriptor();
          propertyView.setName(queriableProperty);
          propertyView
              .setLovAction(createComparableEditAction((ComparableQueryStructureDescriptor) actualPropertyDescriptor));
          propertyViewDescriptors.add(propertyView);
      } else {
        BasicPropertyViewDescriptor propertyView = new BasicPropertyViewDescriptor();
        propertyView.setName(queriableProperty);
        propertyViewDescriptors.add(propertyView);
      }
    }
    queryComponentViewDescriptor
        .setPropertyViewDescriptors(propertyViewDescriptors);

    queryComponentViewDescriptor.setModelDescriptor(queryComponentDescriptor);

    return queryComponentViewDescriptor;
  }

  /**
   * Create comparable edit action.
   *
   * @param comparablePropertyDescriptor the comparable property descriptor
   * @return the edit action
   */
  @Override
  protected IDisplayableAction createComparableEditAction(ComparableQueryStructureDescriptor
                                                              comparablePropertyDescriptor) {
    EditComponentAction<E, F, G> comparableEditAction = new EditComponentAction<>();
    if (comparablePropertyDescriptor.getIcon() != null) {
      comparableEditAction.setIcon(comparablePropertyDescriptor.getIcon());
    } else {
      comparableEditAction.setIconImageURL(getDefaultFindIconImageUrl());
    }

    MobileComponentViewDescriptor viewDescriptor = new MobileComponentViewDescriptor();

    BasicPropertyViewDescriptor propertyView;

    List<IPropertyViewDescriptor> propertyViewDescriptors = new ArrayList<>();

    propertyView = new BasicPropertyViewDescriptor();
    propertyView.setName(ComparableQueryStructureDescriptor.COMPARATOR);
    propertyViewDescriptors.add(propertyView);

    propertyView = new BasicPropertyViewDescriptor();
    propertyView.setName(ComparableQueryStructureDescriptor.INF_VALUE);
    propertyViewDescriptors.add(propertyView);

    propertyView = new BasicPropertyViewDescriptor();
    propertyView.setName(ComparableQueryStructureDescriptor.SUP_VALUE);
    propertyViewDescriptors.add(propertyView);

    viewDescriptor.setPropertyViewDescriptors(propertyViewDescriptors);

    viewDescriptor.setName(comparablePropertyDescriptor.getName());
    viewDescriptor.setModelDescriptor(comparablePropertyDescriptor.getReferencedDescriptor());

    comparableEditAction.setViewDescriptor(viewDescriptor);
    comparableEditAction.setCancelAction(null);
    comparableEditAction.setOkAction(getOkCloseDialogAction());

    comparableEditAction.setNextAction(getModalDialogAction());

    return comparableEditAction;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void adaptExistingViewDescriptor(IViewDescriptor viewDescriptor) {
    if (viewDescriptor instanceof BasicComponentViewDescriptor) {
      List<IPropertyViewDescriptor> propertyViews = ((BasicComponentViewDescriptor) viewDescriptor)
          .getPropertyViewDescriptors(false);
      for (int i = 0; i < propertyViews.size(); i++) {
        IPropertyViewDescriptor propertyView = propertyViews.get(i);
        if ((propertyView.getModelDescriptor() instanceof ComparableQueryStructureDescriptor)
            || propertyView.getModelDescriptor() instanceof EnumQueryStructureDescriptor) {
          if (!(propertyView instanceof BasicReferencePropertyViewDescriptor)) {
            BasicReferencePropertyViewDescriptor refPropertyView = new BasicReferencePropertyViewDescriptor();
            refPropertyView.setAction(propertyView.getAction());
            refPropertyView.setBackground(propertyView.getBackground());
            refPropertyView.setBorderType(propertyView.getBorderType());
            refPropertyView.setDescription(propertyView.getDescription());
            refPropertyView.setFont(propertyView.getFont());
            refPropertyView.setForeground(propertyView.getForeground());
            refPropertyView.setGrantedRoles(propertyView.getGrantedRoles());
            refPropertyView.setHorizontalAlignment(propertyView
                .getHorizontalAlignment());
            if (propertyView instanceof DefaultDescriptor) {
              refPropertyView.setI18nNameKey(((DefaultDescriptor) propertyView)
                  .getI18nNameKey());
            }
            refPropertyView.setIcon(propertyView.getIcon());
            refPropertyView.setLabelBackground(propertyView.getLabelBackground());
            refPropertyView.setLabelFont(propertyView.getLabelFont());
            refPropertyView.setLabelForeground(propertyView.getLabelForeground());
            refPropertyView.setModelDescriptor(propertyView.getModelDescriptor());
            refPropertyView.setName(propertyView.getName());
            refPropertyView.setPermId(propertyView.getPermId());
            refPropertyView.setPreferredSize(propertyView.getPreferredSize());
            refPropertyView.setReadabilityGates(propertyView.getReadabilityGates());
            refPropertyView.setReadOnly(propertyView.isReadOnly());
            refPropertyView.setWritabilityGates(propertyView.getWritabilityGates());
            refPropertyView.setWidth(propertyView.getWidth());
            refPropertyView.setAutoCompleteEnabled(false);
            propertyView = refPropertyView;
            propertyViews.set(i, propertyView);
          }
          if (((BasicReferencePropertyViewDescriptor) propertyView)
              .getLovAction() == null) {
            if (propertyView.getModelDescriptor() instanceof ComparableQueryStructureDescriptor) {
              ((BasicReferencePropertyViewDescriptor) propertyView).setLovAction(
                  createComparableEditAction((ComparableQueryStructureDescriptor) propertyView.getModelDescriptor()));
            }
          }
        }
      }
      ((BasicComponentViewDescriptor) viewDescriptor).setPropertyViewDescriptors(propertyViews);
    }
    if (viewDescriptor instanceof ICompositeViewDescriptor) {
      List<IViewDescriptor> children = ((ICompositeViewDescriptor) viewDescriptor)
          .getChildViewDescriptors();
      if (children != null) {
        for (IViewDescriptor childViewDesc : children) {
          adaptExistingViewDescriptor(childViewDesc);
        }
      }
    }
  }
}
