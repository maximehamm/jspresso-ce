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
package org.jspresso.framework.application.frontend.action.wings.flow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.wings.SOptionPane;


/**
 * Action to ask a binary question to the user with a cancel option.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class YesNoCancelAction extends AbstractMessageAction {

  private IAction cancelAction;
  private IAction noAction;
  private IAction yesAction;

  /**
   * Displays the message using a <code>SOptionPane.YES_NO_CANCEL_OPTION</code>.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(final IActionHandler actionHandler,
      final Map<String, Object> context) {
    SOptionPane.showConfirmDialog(getSourceComponent(context),
        getMessage(context), getI18nName(getTranslationProvider(context),
            getLocale(context)), SOptionPane.YES_NO_CANCEL_OPTION,
        new ActionListener() {

          public void actionPerformed(ActionEvent e) {
            IAction nextAction = null;
            if (SOptionPane.YES_ACTION.equals(e.getActionCommand())) {
              nextAction = yesAction;
            } else if (SOptionPane.NO_ACTION.equals(e.getActionCommand())) {
              nextAction = noAction;
            } else if (SOptionPane.CANCEL_ACTION.equals(e.getActionCommand())) {
              nextAction = cancelAction;
            }
            if (nextAction != null) {
              actionHandler.execute(nextAction, context);
            }
          }

        }, null);
    return true;
  }

  /**
   * Sets the cancelAction.
   * 
   * @param cancelAction
   *            the cancelAction to set.
   */
  public void setCancelAction(IAction cancelAction) {
    this.cancelAction = cancelAction;
  }

  /**
   * Sets the noAction.
   * 
   * @param noAction
   *            the noAction to set.
   */
  public void setNoAction(IAction noAction) {
    this.noAction = noAction;
  }

  /**
   * Sets the yesAction.
   * 
   * @param yesAction
   *            the yesAction to set.
   */
  public void setYesAction(IAction yesAction) {
    this.yesAction = yesAction;
  }
}
