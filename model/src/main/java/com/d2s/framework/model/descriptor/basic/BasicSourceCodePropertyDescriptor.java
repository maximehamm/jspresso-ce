/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import com.d2s.framework.model.descriptor.ISourceCodePropertyDescriptor;

/**
 * Default implementation of a text descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicSourceCodePropertyDescriptor extends
    BasicTextPropertyDescriptor implements ISourceCodePropertyDescriptor {

  private String language;

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicSourceCodePropertyDescriptor clone() {
    BasicSourceCodePropertyDescriptor clonedDescriptor = (BasicSourceCodePropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public String getLanguage() {
    return language;
  }

  /**
   * Sets the language.
   * 
   * @param language
   *            the language to set.
   */
  public void setLanguage(String language) {
    this.language = language;
  }
}
