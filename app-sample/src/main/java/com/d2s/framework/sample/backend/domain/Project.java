/*
 * Generated by Design2see. All rights reserved.
 */
package com.d2s.framework.sample.backend.domain;

/**
 * Project entity.
 * <p>
 * Generated by Design2see. All rights reserved.
 * <p>
 * 
 * @hibernate.mapping default-access =
 *                    "com.d2s.framework.model.persistence.hibernate.property.EntityPropertyAccessor"
 *                    package = "com.d2s.framework.sample.backend.domain"
 * @hibernate.class table = "PROJECT" dynamic-insert = "true" dynamic-update =
 *                  "true" persister =
 *                  "com.d2s.framework.model.persistence.hibernate.entity.persister.EntityProxyJoinedSubclassEntityPersister"
 * @author Generated by Design2see
 */
public interface Project extends
    com.d2s.framework.sample.backend.domain.Nameable,
    com.d2s.framework.model.entity.IEntity,
    com.d2s.framework.sample.backend.domain.Traceable {

  /**
   * Gets the department.
   * 
   * @hibernate.many-to-one cascade = "save-update,lock"
   * @hibernate.column name = "DEPARTMENT_ID"
   * @return the department.
   */
  com.d2s.framework.sample.backend.domain.Department getDepartment();

  /**
   * Sets the department.
   * 
   * @param department
   *          the department to set.
   */
  void setDepartment(
      com.d2s.framework.sample.backend.domain.Department department);

  /**
   * Gets the projectMembers.
   * 
   * @hibernate.set cascade =
   *                "persist,merge,save-update,lock,refresh,evict,replicate"
   *                table = "PROJECT_PROJECT_MEMBERS"
   * @hibernate.key column = "PROJECT_ID"
   * @hibernate.many-to-many class =
   *                         "com.d2s.framework.sample.backend.domain.Employee"
   *                         column = "EMPLOYEE_ID"
   * @return the projectMembers.
   */
  java.util.Set<com.d2s.framework.sample.backend.domain.Employee> getProjectMembers();

  /**
   * Sets the projectMembers.
   * 
   * @param projectMembers
   *          the projectMembers to set.
   */
  void setProjectMembers(
      java.util.Set<com.d2s.framework.sample.backend.domain.Employee> projectMembers);

  /**
   * Adds an element to the projectMembers.
   * 
   * @param projectMembersElement
   *          the projectMembers element to add.
   */
  void addToProjectMembers(
      com.d2s.framework.sample.backend.domain.Employee projectMembersElement);

  /**
   * Removes an element from the projectMembers.
   * 
   * @param projectMembersElement
   *          the projectMembers element to remove.
   */
  void removeFromProjectMembers(
      com.d2s.framework.sample.backend.domain.Employee projectMembersElement);

}
