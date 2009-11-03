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
package org.jspresso.framework.application.backend.persistence.hibernate;

import org.hibernate.event.PostLoadEvent;
import org.hibernate.event.PostLoadEventListener;
import org.jspresso.framework.model.component.ILifecycleCapable;

/**
 * This hibernate event listener is used to trigger the onLoad lifecycle method.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class LifecyclePostLoadEventListener implements PostLoadEventListener {

  private static final long serialVersionUID = 7989551891750553087L;

  /**
   * Retrieves the enntity and triggers the onLoad lifecycle hook.
   * <p>
   * {@inheritDoc}
   */
  public void onPostLoad(PostLoadEvent event) {
    Object entity = event.getEntity();
    if (entity instanceof ILifecycleCapable) {
      ((ILifecycleCapable) entity).onLoad();
    }
  }

}
