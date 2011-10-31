/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.util.accessor;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.expression.DefaultResolver;
import org.jspresso.framework.util.bean.PropertyHelper;

/**
 * Abstract class for property accessors.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractPropertyAccessor implements IAccessor {

  private String                         property;

  private static final PropertyUtilsBean PROPERTY_UTILS_BEAN = new PropertyUtilsBean();
  static {
    PROPERTY_UTILS_BEAN.setResolver(new DefaultResolver() {

      /**
       * Supports properties starting with a single lowercase letter.
       * <p>
       * {@inheritDoc}
       */
      @Override
      public String getProperty(String expression) {
        String prop = super.getProperty(expression);
        prop = toJavaBeanPropertyName(prop);
        return prop;
      }
    });
  }

  /**
   * Whenever a property name starts with a single lowercase letter, the actual
   * java bean property starts with an upper case letter.
   * 
   * @param prop
   *          the property name.
   * @return the fixed java bean property name.
   */
  public static String toJavaBeanPropertyName(String prop) {
    if (prop != null && prop.length() >= 2) {
      if (Character.isLowerCase(prop.charAt(0))
          && Character.isUpperCase(prop.charAt(1))) {
        StringBuffer fixedProp = new StringBuffer(prop.substring(0, 1)
            .toUpperCase());
        fixedProp.append(prop.substring(1));
        return fixedProp.toString();
      }
    }
    return prop;
  }

  /**
   * Whenever a property name starts with a single lowercase letter, the actual
   * java bean property starts with an upper case letter.
   * 
   * @param prop
   *          the property name.
   * @return the fixed java bean property name.
   */
  public static String fromJavaBeanPropertyName(String prop) {
    if (prop != null && prop.length() >= 2) {
      if (Character.isUpperCase(prop.charAt(0))
          && Character.isUpperCase(prop.charAt(1))) {
        StringBuffer fixedProp = new StringBuffer(prop.substring(0, 1)
            .toLowerCase());
        fixedProp.append(prop.substring(1));
        return fixedProp.toString();
      }
    }
    return prop;
  }

  /**
   * Constructs a new <code>AbstractPropertyAccessor</code> instance.
   * 
   * @param property
   *          the property to access.
   */
  public AbstractPropertyAccessor(String property) {
    this.property = property;
  }

  /**
   * Gets a property value, taking care of Map vs bean implementation and nested
   * properties.
   * 
   * @param target
   *          the target object.
   * @throws IllegalAccessException
   *           whenever an exception occurs.
   * @throws InvocationTargetException
   *           whenever an exception occurs.
   * @throws NoSuchMethodException
   *           whenever an exception occurs.
   * @return the property value.
   */
  @Override
  public Object getValue(Object target) throws IllegalAccessException,
      InvocationTargetException, NoSuchMethodException {
    Object finalTarget = getLastNestedTarget(target, getProperty());
    if (finalTarget != null) {
      if (finalTarget instanceof Map<?, ?>) {
        if (PropertyHelper.getPropertyNames(finalTarget.getClass()).contains(
            getLastNestedProperty())) {
          // We are explicitely on a bean property. Do not use
          // PROPERTY_UTILS_BEAN.getProperty since it will detect that the
          // target
          // is a Map and access its properties as such.
          return PROPERTY_UTILS_BEAN.getSimpleProperty(finalTarget,
              getLastNestedProperty());
        }
        return PROPERTY_UTILS_BEAN.getProperty(finalTarget,
            getLastNestedProperty());
      }
      return PROPERTY_UTILS_BEAN.getProperty(finalTarget,
          getLastNestedProperty());
    }
    return null;
  }

  /**
   * Sets a property value, taking care of Map vs bean implementation and nested
   * properties.
   * 
   * @param target
   *          the target object.
   * @param value
   *          the value to set.
   * @throws IllegalAccessException
   *           whenever an exception occurs.
   * @throws InvocationTargetException
   *           whenever an exception occurs.
   * @throws NoSuchMethodException
   *           whenever an exception occurs.
   */
  @Override
  public void setValue(Object target, Object value)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    try {
      Object finalTarget = getLastNestedTarget(target, getProperty());
      if (finalTarget != null) {
        if (finalTarget instanceof Map<?, ?>) {
          if (PropertyHelper.getPropertyNames(finalTarget.getClass()).contains(
              getLastNestedProperty())) {
            // We are explicitely on a bean property. Do not use
            // PROPERTY_UTILS_BEAN.getProperty since it will detect that the
            // target
            // is a Map and access its properties as such.
            PROPERTY_UTILS_BEAN.setSimpleProperty(finalTarget,
                getLastNestedProperty(), value);
          } else {
            PROPERTY_UTILS_BEAN.setProperty(finalTarget,
                getLastNestedProperty(), value);
          }
        } else {
          PROPERTY_UTILS_BEAN.setProperty(finalTarget, getLastNestedProperty(),
              value);
        }
      }
    } catch (InvocationTargetException ex) {
      // unnest invocation target exceptions so that the original
      // one can be correctly handled by the exception handlers.
      if (ex.getCause() instanceof RuntimeException) {
        throw (RuntimeException) ex.getCause();
      }
      throw ex;
    }
  }

  /**
   * Gets the final nested property.
   * 
   * @return the final nested property.
   */
  protected String getLastNestedProperty() {
    return getProperty().substring(
        getProperty().lastIndexOf(IAccessor.NESTED_DELIM) + 1);
  }

  /**
   * Gets the last target of a nested property.
   * 
   * @param target
   *          the starting target.
   * @param prop
   *          the property.
   * @return the last target of a nested property.
   * @throws IllegalAccessException
   *           whenever an exception occurs.
   * @throws InvocationTargetException
   *           whenever an exception occurs.
   * @throws NoSuchMethodException
   *           whenever an exception occurs.
   */
  protected Object getLastNestedTarget(Object target, String prop)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    if (target != null) {
      int indexOfNestedDelim = prop.indexOf(IAccessor.NESTED_DELIM);
      if (indexOfNestedDelim < 0) {
        return target;
      }
      if (target instanceof Map<?, ?>) {
        if (PropertyHelper.getPropertyNames(target.getClass()).contains(prop)) {
          // We are explicitely on a bean property. Do not use
          // PROPERTY_UTILS_BEAN.getProperty since it will detect that the
          // target is
          // a
          // Map and access its properties as such.
          return getLastNestedTarget(
              PROPERTY_UTILS_BEAN.getSimpleProperty(target,
                  prop.substring(0, indexOfNestedDelim)),
              prop.substring(indexOfNestedDelim + 1));
        }
        return getLastNestedTarget(
            PROPERTY_UTILS_BEAN.getProperty(target,
                prop.substring(0, indexOfNestedDelim)),
            prop.substring(indexOfNestedDelim + 1));
      }
      return getLastNestedTarget(
          PROPERTY_UTILS_BEAN.getProperty(target,
              prop.substring(0, indexOfNestedDelim)),
          prop.substring(indexOfNestedDelim + 1));
    }
    return null;
  }

  /**
   * Gets the property property.
   * 
   * @return the property.
   */
  protected String getProperty() {
    return property;
  }
}
