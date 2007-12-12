/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity;

/**
 * This interface defines the contract of an application session entity
 * registry. An entity registry is used at the application session level to help
 * in keeping object identity across the whole JVM. Entity registries must be
 * memory friendly. This means that the registry must not prevent an object from
 * being garbage collected. Given this point implementations should leverage on
 * weak references.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IEntityRegistry {

  /**
   * Get an entity from the registry.
   * 
   * @param entityContract
   *            the entity contract.
   * @param id
   *            the entity identifier.
   * @return the registered entity or <tt>null</tt>
   */
  IEntity get(Class<? extends IEntity> entityContract, Object id);

  /**
   * Registers an entity in the registry.
   * 
   * @param entity
   *            the entity to register.
   */
  void register(IEntity entity);
}
