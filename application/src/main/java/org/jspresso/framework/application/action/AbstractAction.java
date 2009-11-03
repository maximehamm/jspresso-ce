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
package org.jspresso.framework.application.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.IController;
import org.jspresso.framework.binding.ConnectorHelper;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.util.lang.StringUtils;

/**
 * Base class for all application actions. Takes care of the context reference
 * as well as the input context keys reference.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractAction implements IAction {

  /**
   * <code>ACTION_MODEL_NAME</code>.
   */
  protected static final String ACTION_MODEL_NAME = "ActionModel";
  private Collection<String>    grantedRoles;

  private boolean               longOperation;

  private IAction               nextAction;
  private IAction               wrappedAction;

  /**
   * {@inheritDoc}
   */
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    if (actionHandler.execute(getWrappedAction(context), context)) {
      return actionHandler.execute(getNextAction(context), context);
    }
    return false;
  }

  /**
   * Sets the wrappedAction.
   * 
   * @param wrappedAction
   *          the wrappedAction to set.
   */
  public void setWrappedAction(IAction wrappedAction) {
    this.wrappedAction = wrappedAction;
  }

  /**
   * Gets the wrapped action reference. If the wrapped action has been
   * configured strongly through the setter method, it is directly returned. If
   * not, it is looked up into the action context.
   * 
   * @param context
   *          the action context.
   * @return the wrapped action to execute.
   * @see #setWrappedAction(IAction)
   */
  protected IAction getWrappedAction(Map<String, Object> context) {
    return wrappedAction;
  }

  /**
   * Gets the next action reference. If the next action has been configured
   * strongly through the setter method, it is directly returned. If not, it is
   * looked up into the action context.
   * 
   * @param context
   *          the action context.
   * @return the next action to execute.
   * @see #setNextAction(IAction)
   */
  protected IAction getNextAction(Map<String, Object> context) {
    return nextAction;
  }

  /**
   * Sets the nextAction.
   * 
   * @param nextAction
   *          the next action to execute.
   */
  public void setNextAction(IAction nextAction) {
    this.nextAction = nextAction;
  }

  /**
   * {@inheritDoc}
   */
  public Collection<String> getGrantedRoles() {
    return grantedRoles;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isLongOperation() {
    return longOperation;
  }

  /**
   * Sets the grantedRoles.
   * 
   * @param grantedRoles
   *          the grantedRoles to set.
   */
  public void setGrantedRoles(Collection<String> grantedRoles) {
    this.grantedRoles = StringUtils.ensureSpaceFree(grantedRoles);
  }

  /**
   * Sets the longOperation.
   * 
   * @param longOperation
   *          the longOperation to set.
   */
  public void setLongOperation(boolean longOperation) {
    this.longOperation = longOperation;
  }

  /**
   * Gets the controller (frontend or backend) out of the action context.
   * 
   * @param context
   *          the action context.
   * @return the controller (frontend or backend).
   */
  protected abstract IController getController(Map<String, Object> context);

  /**
   * This is a utility method which is able to retrieve the model descriptor
   * this action has been executed on from its context. It uses well-known
   * context keys of the action context which are:
   * <ul>
   * <li> <code>ActionContextConstants.MODEL_DESCRIPTOR</code> to get the the
   * view model descriptor the action executes on.
   * </ul>
   * 
   * @param context
   *          the action context.
   * @return the model descriptor this model action was triggered on.
   */
  protected IModelDescriptor getModelDescriptor(Map<String, Object> context) {
    return (IModelDescriptor) context
        .get(ActionContextConstants.MODEL_DESCRIPTOR);
  }

  /**
   * Gets a translation provider out of the action context.
   * 
   * @param context
   *          the action context.
   * @return the translation provider.
   */
  protected ITranslationProvider getTranslationProvider(
      Map<String, Object> context) {
    return getController(context).getTranslationProvider();
  }

  /**
   * Retrieves the locale the action has to use to execute from its context
   * using a well-known key.
   * 
   * @param context
   *          the action context.
   * @return the locale the action executes in.
   */
  protected Locale getLocale(Map<String, Object> context) {
    return getController(context).getLocale();
  }

  /**
   * Gets the action parameter out of the context.
   * 
   * @param context
   *          the action context.
   * @return the action parameter if it exists in the action context or null.
   */
  protected Object getActionParameter(Map<String, Object> context) {
    return context.get(ActionContextConstants.ACTION_PARAM);
  }

  /**
   * Sets the action parameter to the context.
   * 
   * @param actionParam
   *          the action parameter to set to the context.
   * @param context
   *          the action context.
   */
  protected void setActionParameter(Object actionParam,
      Map<String, Object> context) {
    context.put(ActionContextConstants.ACTION_PARAM, actionParam);
  }

  /**
   * Gets the (string) action command out of the context.
   * 
   * @param context
   *          the action context.
   * @return the (string) action command if it exists in the action context or
   *         null.
   */
  protected String getActionCommand(Map<String, Object> context) {
    return (String) context.get(ActionContextConstants.ACTION_COMMAND);
  }

  /**
   * This is a utility method which is able to retrieve the model connector this
   * action has been executed on from its context. It uses well-known context
   * keys of the action context which are:
   * <ul>
   * <li> <code>ActionContextConstants.VIEW_CONNECTOR</code> to get the model
   * value connector of the connector hierarchy.
   * </ul>
   * <p>
   * The returned connector mainly serves for retrieving the domain object the
   * action has to be triggered on.
   * 
   * @param context
   *          the action context.
   * @return the value connector this model action was triggered on.
   */
  protected IValueConnector getModelConnector(Map<String, Object> context) {
    return ((IValueConnector) context
        .get(ActionContextConstants.VIEW_CONNECTOR)).getModelConnector();
  }

  /**
   * Gets the selected indices out of the action context. the value is stored
   * with the key <code>ActionContextConstants.SELECTED_INDICES</code>.
   * 
   * @param context
   *          the action context.
   * @return the selected indices stored in the action context.
   */
  protected int[] getSelectedIndices(Map<String, Object> context) {
    return (int[]) context.get(ActionContextConstants.SELECTED_INDICES);
  }

  /**
   * Sets the selected indices to the action context. the value is stored with
   * the key <code>ActionContextConstants.SELECTED_INDICES</code>.
   * 
   * @param selectedIndices
   *          the selected indices to store in the action context.
   * @param context
   *          the action context.
   */
  protected void setSelectedIndices(int[] selectedIndices,
      Map<String, Object> context) {
    context.put(ActionContextConstants.SELECTED_INDICES, selectedIndices);
  }

  /**
   * This is a versatile helper method that retrieves the selected model either
   * from the 1st selected child connector if the action was trigerred on a
   * collection connector or the connector itself.
   * 
   * @param context
   *          the action context.
   * @return the selected model.
   */
  protected Object getSelectedModel(Map<String, Object> context) {
    IValueConnector modelConnector = getModelConnector(context);
    Object model = null;
    if (modelConnector instanceof ICollectionConnector) {
      int[] selectedIndices = getSelectedIndices(context);
      if (selectedIndices != null && selectedIndices.length > 0) {
        model = ((ICollectionConnector) modelConnector).getChildConnector(
            selectedIndices[0]).getConnectorValue();
      }
    } else {
      model = modelConnector.getConnectorValue();
    }
    return model;
  }

  /**
   * This is a versatile helper method that retrieves the selected models model
   * either from the selected child connectors if the action was trigerred on a
   * collection connector or the connector itself.
   * 
   * @param context
   *          the action context.
   * @return the list of selected models.
   */
  protected List<?> getSelectedModels(Map<String, Object> context) {
    IValueConnector modelConnector = getModelConnector(context);
    if (modelConnector == null) {
      return null;
    }
    List<Object> models;
    if (modelConnector instanceof ICollectionConnector) {
      models = new ArrayList<Object>();
      int[] selectedIndices = getSelectedIndices(context);
      if (selectedIndices != null && selectedIndices.length > 0) {
        for (int i = 0; i < selectedIndices.length; i++) {
          models.add(((ICollectionConnector) modelConnector).getChildConnector(
              selectedIndices[i]).getConnectorValue());
        }
      }
    } else {
      models = Collections.singletonList(modelConnector.getConnectorValue());
    }
    return models;
  }

  /**
   * Retrieves the selectedModels indices out of the model connector if it's a
   * collection connector and set them as selected indices in the action
   * context.
   * 
   * @param selectedModels
   *          the list of models to select in the view connector.
   * @param context
   *          the action context.
   */
  protected void setSelectedModels(Collection<?> selectedModels,
      Map<String, Object> context) {
    IValueConnector modelConnector = getModelConnector(context);
    if (modelConnector instanceof ICollectionConnector) {
      setSelectedIndices(ConnectorHelper.getIndicesOf(
          (ICollectionConnector) modelConnector, selectedModels), context);
    }
  }
}
