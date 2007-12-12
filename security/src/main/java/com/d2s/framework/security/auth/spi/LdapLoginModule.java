/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.security.auth.spi;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.ldap.InitialLdapContext;

import org.jboss.security.auth.spi.LdapExtLoginModule;

import com.d2s.framework.security.UserPrincipal;

/**
 * Extends the JBoss LdapExtLoginModule to keep track of the authenticated
 * ditinguished name.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class LdapLoginModule extends LdapExtLoginModule {

  private static final String CUSTOM_PROPERTY_OPT = "custom.";
  private static final String SLICE_END           = "]";
  private static final String SLICE_START         = "[";

  /**
   * Overriden to complete the main principal with optional data from the
   * backing store.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  protected String bindDNAuthentication(InitialLdapContext ctx, String user,
      Object credential, String searchBaseDN, String filter)
      throws NamingException {
    String userDN = super.bindDNAuthentication(ctx, user, credential,
        searchBaseDN, filter);

    Attributes attrs = ctx.getAttributes(userDN, null);

    NameParser nameParser = ctx.getNameParser(userDN);

    UserPrincipal userPrincipal = (UserPrincipal) getIdentity();

    for (Map.Entry<String, String> option : ((Map<String, String>) options)
        .entrySet()) {
      if (option.getKey().startsWith(CUSTOM_PROPERTY_OPT)) {
        String attributeId = option.getValue();
        if (option.getValue().contains(SLICE_START)) {
          String attributeIdBase = attributeId.substring(0, attributeId
              .indexOf(SLICE_START));

          String[] indices = option.getValue().substring(
              option.getValue().indexOf(SLICE_START) + 1,
              option.getValue().indexOf(SLICE_END)).split(
              SLICE_START + "," + SLICE_END);
          int startIndex = Integer.parseInt(indices[0]);
          int endIndex = Integer.parseInt(indices[1]);

          if ("DN".equalsIgnoreCase(attributeIdBase)) {
            String dnSlice = extractSlice(userDN, nameParser, startIndex,
                endIndex);
            int dotIndex = attributeId.indexOf(".");
            if (dotIndex > -1) {
              Attributes nestedAttrs = ctx.getAttributes(dnSlice, null);
              Attribute attr = nestedAttrs.get(attributeId
                  .substring(dotIndex + 1));
              if (attr != null && attr.size() > 0) {
                if (attr.size() == 1) {
                  userPrincipal.putCustomProperty(option.getKey().substring(
                      CUSTOM_PROPERTY_OPT.length()), attr.get());
                } else {
                  List<Object> values = new ArrayList<Object>();
                  for (NamingEnumeration<?> avne = attr.getAll(); avne
                      .hasMore();) {
                    values.add(avne.next());
                  }
                  userPrincipal.putCustomProperty(option.getKey().substring(
                      CUSTOM_PROPERTY_OPT.length()), values);
                }
              }
            } else {
              userPrincipal.putCustomProperty(option.getKey().substring(
                  CUSTOM_PROPERTY_OPT.length()), dnSlice);
            }
          } else {
            Attribute attr = attrs.get(attributeId);
            if (attr != null && attr.size() > 0) {
              if (attr.size() == 1) {
                userPrincipal.putCustomProperty(option.getKey().substring(
                    CUSTOM_PROPERTY_OPT.length()), extractSlice((String) attr
                    .get(), nameParser, startIndex, endIndex));
              } else {
                List<Object> values = new ArrayList<Object>();
                for (NamingEnumeration<?> avne = attr.getAll(); avne.hasMore();) {
                  values.add(extractSlice((String) avne.next(), nameParser,
                      startIndex, endIndex));
                }
                userPrincipal.putCustomProperty(option.getKey().substring(
                    CUSTOM_PROPERTY_OPT.length()), values);
              }
            }
          }
        } else {
          if ("DN".equalsIgnoreCase(attributeId)) {
            userPrincipal.putCustomProperty(option.getKey().substring(
                CUSTOM_PROPERTY_OPT.length()), userDN);
          } else {
            Attribute attr = attrs.get(attributeId);
            if (attr != null && attr.size() > 0) {
              if (attr.size() == 1) {
                userPrincipal.putCustomProperty(option.getKey().substring(
                    CUSTOM_PROPERTY_OPT.length()), attr.get());
              } else {
                List<Object> values = new ArrayList<Object>();
                for (NamingEnumeration<?> avne = attr.getAll(); avne.hasMore();) {
                  values.add(avne.next());
                }
                userPrincipal.putCustomProperty(option.getKey().substring(
                    CUSTOM_PROPERTY_OPT.length()), values);
              }
            }
          }
        }
      }
    }
    return userDN;
  }

  /**
   * Overriden to construct a user principal instead of the default
   * SimplePrincipal.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Principal createIdentity(String username) throws Exception {
    if (principalClassName == null && getIdentity() == null) {
      // we are creating the user principal.
      return new UserPrincipal(username);
    }
    return super.createIdentity(username);
  }

  private String extractSlice(String nameAsString, NameParser nameParser,
      int startIndex, int endIndex) throws NamingException {
    int startI = startIndex;
    int endI = endIndex;
    Name name = nameParser.parse(nameAsString);
    if (startI < 0) {
      startI = name.size() + startI;
    }
    if (endI < 0) {
      endI = name.size() + endI;
    }
    return name.getPrefix(endI).getSuffix(startI).toString();
  }
}
