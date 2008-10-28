/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
 * This is a simple helper class to deal with font representations.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision: 1249 $
 * @author Vincent Vandenschrick
 */
public final class FontHelper {

  private static final String PLAIN  = "PLAIN";
  private static final String BOLD   = "BOLD";
  private static final String ITALIC = "ITALIC";
  private static final String SEP    = ";";

  private FontHelper() {
    // private constructor for helper class.
  }

  /**
   * Transforms a font string representation to a font.
   * 
   * @param fontString
   *          the font string representation. The font is coded
   *          <code>[name];[style];[size]</code>. <li>[name] is the name of the
   *          font. <li>[style] is PLAIN, BOLD, ITALIC or a union of BOLD and
   *          ITALIC combined with the '|' character, i.e. BOLD|ITALIC. <li>
   *          [size] is the size of the font.
   * @return the font represented by the string.
   */
  public static Font fromString(String fontString) {
    if (fontString != null) {
      String[] fontAttributes = fontString.split(SEP);
      if (fontAttributes.length == 3) {
        Font font = new Font();
        font.setName(fontAttributes[0]);
        if (fontAttributes[1].indexOf(BOLD) >= 0) {
          font.setBold(true);
        }
        if (fontAttributes[1].indexOf(ITALIC) >= 0) {
          font.setItalic(true);
        }
        font.setSize(Integer.parseInt(fontAttributes[2]));
      }
    }
    throw new IllegalArgumentException(
        "Font string representation must be formed as [name];[style];[size]");
  }

  /**
   * Transforms a font to its string representation.
   * 
   * @param font
   *          the font to transform.
   * @return the font string representation.
   */
  public static String toString(Font font) {
    StringBuffer fontString = new StringBuffer();
    fontString.append(font.getName());
    fontString.append(SEP);
    if (font.isBold() && font.isItalic()) {
      fontString.append(BOLD).append("|").append(ITALIC);
    } else if (font.isBold()) {
      fontString.append(BOLD);
    } else if (font.isItalic()) {
      fontString.append(ITALIC);
    } else {
      fontString.append(PLAIN);
    }
    fontString.append(SEP);
    fontString.append(font.getSize());
    return fontString.toString();
  }

}
