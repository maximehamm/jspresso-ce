/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.client;

import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.d2s.framework.gui.ulc.components.shared.ExtendedInternalFrameConstants;
import com.ulcjava.base.client.UIInternalFrame;
import com.ulcjava.base.shared.internal.Anything;

/**
 * UI internal frame extended to take care of activation events.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class UIExtendedInternalFrame extends UIInternalFrame {

  /**
   * {@inheritDoc}
   */
  @Override
  public void restoreState(Anything args) {
    super.restoreState(args);

    JInternalFrame basicInternalFrame = (JInternalFrame) getBasicComponent();
    basicInternalFrame.addInternalFrameListener(new InternalFrameAdapter() {

      /**
       * {@inheritDoc}
       */
      @Override
      public void internalFrameActivated(@SuppressWarnings("unused")
      javax.swing.event.InternalFrameEvent e) {
        sendOptionalEventULC(
            ExtendedInternalFrameConstants.EXTENDED_INTERNAL_FRAME_EVENT,
            ExtendedInternalFrameConstants.EXTENDED_INTERNAL_FRAME_ACTIVATED);
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public void internalFrameDeactivated(@SuppressWarnings("unused")
      javax.swing.event.InternalFrameEvent e) {
        sendOptionalEventULC(
            ExtendedInternalFrameConstants.EXTENDED_INTERNAL_FRAME_EVENT,
            ExtendedInternalFrameConstants.EXTENDED_INTERNAL_FRAME_DEACTIVATED);
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public void internalFrameDeiconified(@SuppressWarnings("unused")
      InternalFrameEvent e) {
        sendOptionalEventULC(
            ExtendedInternalFrameConstants.EXTENDED_INTERNAL_FRAME_EVENT,
            ExtendedInternalFrameConstants.EXTENDED_INTERNAL_FRAME_DEICONIFIED);
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public void internalFrameIconified(@SuppressWarnings("unused")
      InternalFrameEvent e) {
        sendOptionalEventULC(
            ExtendedInternalFrameConstants.EXTENDED_INTERNAL_FRAME_EVENT,
            ExtendedInternalFrameConstants.EXTENDED_INTERNAL_FRAME_ICONIFIED);
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public void internalFrameOpened(@SuppressWarnings("unused")
      InternalFrameEvent e) {
        sendOptionalEventULC(
            ExtendedInternalFrameConstants.EXTENDED_INTERNAL_FRAME_EVENT,
            ExtendedInternalFrameConstants.EXTENDED_INTERNAL_FRAME_OPENED);
      }
    });
  }
}
