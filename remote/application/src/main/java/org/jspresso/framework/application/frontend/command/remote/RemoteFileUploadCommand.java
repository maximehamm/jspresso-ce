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
package org.jspresso.framework.application.frontend.command.remote;

import java.util.Map;

import org.jspresso.framework.gui.remote.RAction;

/**
 * This command is used to upload a file from the client peer.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
public class RemoteFileUploadCommand extends RemoteCommand {

  private Map<String, String[]> fileFilter;
  private RAction               callbackAction;
  private String                uploadUrl;

  /**
   * Gets the fileFilter.
   * 
   * @return the fileFilter.
   */
  public Map<String, String[]> getFileFilter() {
    return fileFilter;
  }

  /**
   * Sets the fileFilter.
   * 
   * @param fileFilter
   *          the fileFilter to set.
   */
  public void setFileFilter(Map<String, String[]> fileFilter) {
    this.fileFilter = fileFilter;
  }

  /**
   * Gets the callbackAction.
   * 
   * @return the callbackAction.
   */
  public RAction getCallbackAction() {
    return callbackAction;
  }

  /**
   * Sets the callbackAction.
   * 
   * @param callbackAction
   *          the callbackAction to set.
   */
  public void setCallbackAction(RAction callbackAction) {
    this.callbackAction = callbackAction;
  }

  
  /**
   * Gets the uploadUrl.
   * 
   * @return the uploadUrl.
   */
  public String getUploadUrl() {
    return uploadUrl;
  }

  
  /**
   * Sets the uploadUrl.
   * 
   * @param uploadUrl the uploadUrl to set.
   */
  public void setUploadUrl(String uploadUrl) {
    this.uploadUrl = uploadUrl;
  }

}
