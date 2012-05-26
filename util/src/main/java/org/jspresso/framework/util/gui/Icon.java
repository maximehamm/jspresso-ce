/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.util.gui;

/**
 * Icon representation.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class Icon {

  private String    iconImageURL;
  private Dimension dimension;

  /**
   * Constructs a new <code>Icon</code> instance.
   */
  public Icon() {
  }

  /**
   * Constructs a new <code>Icon</code> instance.
   * 
   * @param iconImageURL
   *          the icon image url.
   * @param dimension
   *          the icon dimension.
   */
  public Icon(String iconImageURL, Dimension dimension) {
    this.iconImageURL = iconImageURL;
    this.dimension = dimension;
  }

  /**
   * Gets the URL of the image used by the icon. For Swing view factory a
   * special kind of URL is supported in the form of
   * <code>classpath:directory/image.ext</code> to be able to load images as
   * classpath resource streams.
   * 
   * @return the iconImageURL.
   */
  public String getIconImageURL() {
    return iconImageURL;
  }

  /**
   * Sets the URL of the image used by the icon. For Swing view factory a
   * special kind of URL is supported in the form of
   * <code>classpath:directory/image.ext</code> to be able to load images as
   * classpath resource streams.
   * 
   * @param iconImageURL
   *          the iconImageURL to set.
   */
  public void setIconImageURL(String iconImageURL) {
    this.iconImageURL = iconImageURL;
  }

  /**
   * Gets the dimension.
   * 
   * @return the dimension.
   */
  public Dimension getDimension() {
    return dimension;
  }

  /**
   * Sets the dimension.
   * 
   * @param dimension
   *          the dimension to set.
   */
  public void setDimension(Dimension dimension) {
    this.dimension = dimension;
  }
}