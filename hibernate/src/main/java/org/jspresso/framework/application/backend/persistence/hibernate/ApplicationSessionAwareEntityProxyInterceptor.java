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
package org.jspresso.framework.application.backend.persistence.hibernate;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.Transaction;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.hibernate.type.Type;
import org.jspresso.framework.application.backend.session.IApplicationSession;
import org.jspresso.framework.application.backend.session.hibernate.HibernateAwareApplicationSession;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityLifecycleHandler;
import org.jspresso.framework.model.persistence.hibernate.EntityProxyInterceptor;
import org.jspresso.framework.security.UserPrincipal;

/**
 * Hibernate session interceptor aware of an application session to deal with
 * uniqueness of entity instances across the JVM.
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
public class ApplicationSessionAwareEntityProxyInterceptor extends
    EntityProxyInterceptor {

  private static final long   serialVersionUID = -6834992000307471098L;

  private IApplicationSession applicationSession;

  /**
   * Begins the application session current unit of work.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void afterTransactionBegin(Transaction tx) {
    applicationSession.beginUnitOfWork();
    super.afterTransactionBegin(tx);
  }

  /**
   * Either commits or rollbacks the application session current unit of work.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void afterTransactionCompletion(Transaction tx) {
    if (tx.wasCommitted()) {
      applicationSession.commitUnitOfWork();
    } else {
      applicationSession.rollbackUnitOfWork();
    }
    super.afterTransactionCompletion(tx);
  }

  /**
   * Uses the application session to retrieve the dirty properties of the
   * entity.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public int[] findDirty(Object entity, Serializable id, Object[] currentState,
      Object[] previousState, String[] propertyNames, Type[] types) {
    if (entity instanceof IEntity) {
      Map<String, Object> dirtyProperties = applicationSession
          .getDirtyProperties((IEntity) entity);
      if (dirtyProperties != null) {
        dirtyProperties.remove(IEntity.VERSION);
      }
      if (dirtyProperties == null) {
        return null;
      } else if (dirtyProperties.isEmpty()) {
        return new int[0];
      }
      if (dirtyProperties.containsKey(IEntity.ID)) {
        // whenever an entity has just been saved, its state is in the dirty
        // store.
        // hibernate might ask to check dirtyness especially for collection
        // members.
        // Those just saved entities must not be considered dirty.
        return new int[0];
      }
      // the entity is dirty and is going to be flushed.
      // To workaround a bug, the update lifecycle hook is handeled here.
      if (((IEntity) entity).isPersistent()
          && /* ((IEntity) entity).onUpdate(getEntityFactory()) */onFlushDirty(
              entity, id, currentState, previousState, propertyNames, types)) {
        dirtyProperties = applicationSession
            .getDirtyProperties((IEntity) entity);
      }
      int[] indices = new int[dirtyProperties.size()];
      int n = 0;
      for (int i = 0; i < propertyNames.length; i++) {
        if (dirtyProperties.containsKey(propertyNames[i])) {
          indices[n] = i;
          n++;
        }
      }
      return indices;
    }
    return super.findDirty(entity, id, currentState, previousState,
        propertyNames, types);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public Object getEntity(String entityName, Serializable id) {
    if (!applicationSession.isUnitOfWorkActive()) {
      try {
        IEntity registeredEntity = applicationSession.getRegisteredEntity(
            (Class<? extends IEntity>) Class.forName(entityName), id);
        if (registeredEntity instanceof HibernateProxy) {
          HibernateProxy proxy = (HibernateProxy) registeredEntity;
          LazyInitializer li = proxy.getHibernateLazyInitializer();
          registeredEntity = (IEntity) li.getImplementation();
        }

        HibernateAwareApplicationSession
            .cleanPersistentCollectionDirtyState(registeredEntity);
        return registeredEntity;
      } catch (ClassNotFoundException ex) {
        ex.printStackTrace();
      }
    }
    return super.getEntity(entityName, id);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean onLoad(Object entity, Serializable id, Object[] state,
      String[] propertyNames, Type[] types) {
    if (!applicationSession.isUnitOfWorkActive()) {
      if (entity instanceof IEntity
          && applicationSession.getRegisteredEntity(((IEntity) entity)
              .getComponentContract(), id) == null) {
        applicationSession.registerEntity((IEntity) entity, false);
      }
    }
    return super.onLoad(entity, id, state, propertyNames, types);
  }

  /**
   * registers Enitities to be merged back from the uow to the session on
   * commmit.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public void postFlush(Iterator entities) {
    while (entities.hasNext()) {
      Object entity = entities.next();
      if (entity instanceof IEntity) {
        applicationSession.recordAsSynchronized((IEntity) entity);
      }
    }
    super.postFlush(entities);
  }

  /**
   * Sets the applicationSession.
   * 
   * @param applicationSession
   *          the applicationSession to set.
   */
  public void setApplicationSession(IApplicationSession applicationSession) {
    this.applicationSession = applicationSession;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IEntityLifecycleHandler getEntityLifecycleHandler() {
    return applicationSession;
  }

  /**
   * Gets the principal of the application session.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected UserPrincipal getPrincipal() {
    return applicationSession.getPrincipal();
  }
}
