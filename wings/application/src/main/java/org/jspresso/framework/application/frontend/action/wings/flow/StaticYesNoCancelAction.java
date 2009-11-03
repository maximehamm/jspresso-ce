/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.wings.flow;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;

/**
 * Action with a static i18nalized message.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class StaticYesNoCancelAction extends YesNoAction {

  private String messageCode;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    setActionParameter(getTranslationProvider(context).getTranslation(
        messageCode, getLocale(context)), context);
    return super.execute(actionHandler, context);
  }

  /**
   * Sets the messageCode.
   * 
   * @param messageCode
   *          the messageCode to set.
   */
  public void setMessageCode(String messageCode) {
    this.messageCode = messageCode;
  }

}
