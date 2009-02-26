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
package org.jspresso.framework.application.frontend.file;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.binding.IValueConnector;


/**
 * Default handler implementation to deal with getting binary properties storing
 * them in files.
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
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ConnectorValueGetterCallback implements IFileSaveCallback {

  /**
   * {@inheritDoc}
   */
  public void cancel(@SuppressWarnings("unused")
  Map<String, Object> context) {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  public void fileChosen(OutputStream out, Map<String, Object> context) {
    OutputStream os = new BufferedOutputStream(out);
    try {
      Object connectorValue = ((IValueConnector) context
          .get(ActionContextConstants.VIEW_CONNECTOR)).getConnectorValue();
      byte[] content;
      if (connectorValue instanceof String) {
        content = ((String) connectorValue).getBytes();
      } else {
        content = (byte[]) connectorValue;
      }
      if (connectorValue != null) {
        os.write(content);
        os.flush();
      }
    } catch (IOException ex) {
      throw new ActionException(ex);
    } finally {
      try {
        os.close();
      } catch (IOException ex) {
        // NO-OP.
      }
    }
  }
}
