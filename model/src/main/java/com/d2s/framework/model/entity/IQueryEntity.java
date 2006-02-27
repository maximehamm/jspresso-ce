/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity;

import java.util.List;

import com.d2s.framework.util.bean.ElementClass;

/**
 * A simple adapter to wrap an entity used as selection criteria and a list of
 * entities. It only serve as a placeholder for the result of the query.
 * instances of this calss do not perform queries by themselves.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IQueryEntity extends IEntity {

  /**
   * Gets the list of entities result of the query.
   * 
   * @return the list of entities result of the query.
   */
  @ElementClass(IEntity.class)
  List<IEntity> getQueriedEntities();

  /**
   * Sets the list of entities result of the query.
   * 
   * @param queriedEntities
   *          the list of entities result of the query.
   */
  void setQueriedEntities(List<IEntity> queriedEntities);
}
