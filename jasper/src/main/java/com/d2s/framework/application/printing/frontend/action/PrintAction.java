/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.printing.frontend.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.action.AbstractChainedAction;
import com.d2s.framework.application.printing.model.IReport;
import com.d2s.framework.application.printing.model.IReportFactory;
import com.d2s.framework.application.printing.model.descriptor.IReportDescriptor;
import com.d2s.framework.application.printing.model.descriptor.basic.BasicReportDescriptor;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.binding.model.IModelConnectorFactory;
import com.d2s.framework.model.descriptor.basic.BasicCollectionDescriptor;
import com.d2s.framework.util.i18n.ITranslationProvider;

/**
 * Frontend action to select a report.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            the actual gui component type used.
 * @param <F>
 *            the actual icon type used.
 * @param <G>
 *            the actual action type used.
 */
public class PrintAction<E, F, G> extends AbstractChainedAction<E, F, G> {

  private IModelConnectorFactory  beanConnectorFactory;
  private List<IReportDescriptor> reportDescriptors;
  private IReportFactory          reportFactory;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    BasicCollectionDescriptor<IReport> modelDescriptor = new BasicCollectionDescriptor<IReport>();
    modelDescriptor
        .setCollectionInterface(List.class);
    modelDescriptor.setElementDescriptor(BasicReportDescriptor.INSTANCE);
    IValueConnector reportsConnector = beanConnectorFactory
        .createModelConnector(modelDescriptor);
    reportsConnector.setConnectorValue(createReportInstances(
        getTranslationProvider(context), getLocale(context)));
    context.put(ActionContextConstants.ACTION_PARAM, reportsConnector);
    return super.execute(actionHandler, context);
  }

  /**
   * Sets the beanConnectorFactory.
   * 
   * @param beanConnectorFactory
   *            the beanConnectorFactory to set.
   */
  public void setBeanConnectorFactory(
      IModelConnectorFactory beanConnectorFactory) {
    this.beanConnectorFactory = beanConnectorFactory;
  }

  /**
   * Sets the reportDescriptors.
   * 
   * @param reportDescriptors
   *            the reportDescriptors to set.
   */
  public void setReportDescriptors(List<IReportDescriptor> reportDescriptors) {
    this.reportDescriptors = reportDescriptors;
  }

  /**
   * Sets the reportFactory.
   * 
   * @param reportFactory
   *            the reportFactory to set.
   */
  public void setReportFactory(IReportFactory reportFactory) {
    this.reportFactory = reportFactory;
  }

  private List<IReport> createReportInstances(
      ITranslationProvider translationProvider, Locale locale) {
    List<IReport> reports = new ArrayList<IReport>();
    if (reportDescriptors != null) {
      for (IReportDescriptor descriptor : reportDescriptors) {
        reports.add(reportFactory.createReportInstance(descriptor,
            translationProvider, locale));
      }
    }
    return reports;
  }
}
