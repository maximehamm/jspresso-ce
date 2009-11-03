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
package org.jspresso.framework.binding.wings;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICollectionConnectorListProvider;
import org.jspresso.framework.binding.ICollectionConnectorProvider;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.util.event.IItemSelectable;
import org.jspresso.framework.util.event.ISelectionChangeListener;
import org.jspresso.framework.util.event.ItemSelectionEvent;
import org.jspresso.framework.util.event.SelectionChangeEvent;
import org.wings.STree;

/**
 * Default implementation of <code>ITreeSelectionModelBinder</code>.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultTreeSelectionModelBinder implements
    ITreeSelectionModelBinder {

  private SelectionModelListener genericSelectionModelListener;

  /**
   * Constructs a new <code>DefaultTreeSelectionModelBinder</code> instance.
   */
  public DefaultTreeSelectionModelBinder() {
    genericSelectionModelListener = new SelectionModelListener();
  }

  /**
   * {@inheritDoc}
   */
  public void bindSelectionModel(IValueConnector rootConnector, STree tree) {
    tree.getSelectionModel().addTreeSelectionListener(
        genericSelectionModelListener);
    TreeConnectorsListener connectorsListener = new TreeConnectorsListener(
        rootConnector, tree.getSelectionModel());
    tree.getModel().addTreeModelListener(connectorsListener);
  }

  private class CollectionConnectorsSelectionListener implements
      ISelectionChangeListener {

    private IValueConnector    rootConnector;
    private TreeSelectionModel selectionModel;

    /**
     * Constructs a new <code>CollectionConnectorsSelectionListener</code>
     * instance.
     * 
     * @param rootConnector
     * @param selectionModel
     */
    public CollectionConnectorsSelectionListener(IValueConnector rootConnector,
        TreeSelectionModel selectionModel) {
      this.rootConnector = rootConnector;
      this.selectionModel = selectionModel;
    }

    /**
     * {@inheritDoc}
     */
    public void selectionChange(SelectionChangeEvent evt) {
      ICollectionConnector connector = (ICollectionConnector) evt.getSource();
      int[] oldSelection = evt.getOldSelection();
      int[] newSelection = evt.getNewSelection();

      if (oldSelection != null) {
        for (int i : oldSelection) {
          if (newSelection == null || Arrays.binarySearch(newSelection, i) < 0) {
            selectionModel
                .removeSelectionPath(getTreePathForConnector(connector
                    .getChildConnector(i)));
          }
        }
      }
      if (newSelection != null) {
        for (int i : newSelection) {
          if (oldSelection == null || Arrays.binarySearch(oldSelection, i) < 0) {
            selectionModel.addSelectionPath(getTreePathForConnector(connector
                .getChildConnector(i)));
          }
        }
      }
    }

    private TreePath getTreePathForConnector(IValueConnector connector) {
      return ConnectorTreeHelper.getTreePathForConnector(rootConnector,
          connector);
    }
  }

  private static final class SelectionModelListener implements
      TreeSelectionListener {

    /**
     * Tracks changes in model selections to forward to a selectable.
     * <p>
     * {@inheritDoc}
     */
    public void valueChanged(TreeSelectionEvent e) {
      TreePath[] treePaths = e.getPaths();
      Map<ICollectionConnector, int[]> connectorSelection = new LinkedHashMap<ICollectionConnector, int[]>();
      TreeSelectionModel sm = (TreeSelectionModel) e.getSource();

      for (TreePath treePath : treePaths) {
        TreePath parentTreePath = treePath.getParentPath();
        if (parentTreePath != null) {
          IValueConnector connector = (IValueConnector) parentTreePath
              .getLastPathComponent();
          if (connector instanceof ICollectionConnectorProvider) {
            if (!connectorSelection.containsKey(connector)) {
              ICollectionConnector parentCollectionConnector = ((ICollectionConnectorProvider) connector)
                  .getCollectionConnector();
              connectorSelection.put(parentCollectionConnector, null);

              int[] rvTmp = new int[parentCollectionConnector
                  .getChildConnectorCount()];
              int n = 0;
              for (int i = 0; i < parentCollectionConnector
                  .getChildConnectorCount(); i++) {
                IValueConnector childConnector = parentCollectionConnector
                    .getChildConnector(i);
                if (sm.isPathSelected(parentTreePath
                    .pathByAddingChild(childConnector))) {
                  rvTmp[n++] = i;
                }
              }
              int[] selectedIndices = new int[n];
              System.arraycopy(rvTmp, 0, selectedIndices, 0, n);
              connectorSelection.put(((ICollectionConnectorProvider) connector)
                  .getCollectionConnector(), selectedIndices);
            }
          }
        }
      }
      // 1st pass to clear
      for (Map.Entry<ICollectionConnector, int[]> connectorIndicesPair : connectorSelection
          .entrySet()) {
        if (connectorIndicesPair.getValue() == null
            || connectorIndicesPair.getValue().length == 0) {
          ICollectionConnector connector = connectorIndicesPair.getKey();
          int[] indices = connectorIndicesPair.getValue();
          connector.setSelectedIndices(indices);
        }
      }
      // 2nd pass to set
      boolean atLeastOneSelected = false;
      for (Map.Entry<ICollectionConnector, int[]> connectorIndicesPair : connectorSelection
          .entrySet()) {
        if (connectorIndicesPair.getValue() != null
            && connectorIndicesPair.getValue().length > 0) {
          ICollectionConnector connector = connectorIndicesPair.getKey();
          int[] indices = connectorIndicesPair.getValue();
          connector.setSelectedIndices(indices);
          atLeastOneSelected = true;
        }
      }
      if (!atLeastOneSelected) {
        IValueConnector rootConnector = (IValueConnector) treePaths[0]
            .getPath()[0];
        if ((rootConnector instanceof IItemSelectable)) {
          ((IItemSelectable) rootConnector)
              .fireSelectedItemChange(new ItemSelectionEvent(rootConnector,
                  rootConnector));
        }
      }
    }
  }

  private class TreeConnectorsListener implements TreeModelListener {

    private CollectionConnectorsSelectionListener connectorsSelectionListener;

    /**
     * Constructs a new <code>TreeConnectorsListener</code> instance.
     * 
     * @param rootConnector
     *          the root connector of the connector hierarchy.
     * @param selectionModel
     *          the selection model of the related tree.
     */
    public TreeConnectorsListener(IValueConnector rootConnector,
        TreeSelectionModel selectionModel) {
      connectorsSelectionListener = new CollectionConnectorsSelectionListener(
          rootConnector, selectionModel);
      checkListenerRegistrationForConnector((ICollectionConnectorListProvider) rootConnector);
    }

    /**
     * {@inheritDoc}
     */
    public void treeNodesChanged(@SuppressWarnings("unused") TreeModelEvent e) {
      // NO-OP as of now.
    }

    /**
     * {@inheritDoc}
     */
    public void treeNodesInserted(TreeModelEvent e) {
      checkListenerRegistrationForConnector((ICollectionConnectorListProvider) e
          .getTreePath().getLastPathComponent());
    }

    /**
     * {@inheritDoc}
     */
    public void treeNodesRemoved(@SuppressWarnings("unused") TreeModelEvent e) {
      // NO-OP as of now.
    }

    /**
     * {@inheritDoc}
     */
    public void treeStructureChanged(TreeModelEvent e) {
      checkListenerRegistrationForConnector((ICollectionConnectorListProvider) e
          .getTreePath().getLastPathComponent());
    }

    private void checkListenerRegistrationForConnector(
        ICollectionConnectorListProvider nodeConnector) {
      for (ICollectionConnector childNodeConnector : nodeConnector
          .getCollectionConnectors()) {
        childNodeConnector
            .addSelectionChangeListener(connectorsSelectionListener);
      }
    }

  }
}
