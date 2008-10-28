/**
 * Generated by Gas3 v1.1.0 (Granite Data Services).
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERRIDDEN EACH TIME YOU USE
 * THE GENERATOR. CHANGE INSTEAD THE INHERITED CLASS (BasicCompositeTreeLevelDescriptor.as).
 */

package org.jspresso.framework.view.descriptor.basic {

    import flash.utils.IDataInput;
    import flash.utils.IDataOutput;
    import mx.collections.ListCollectionView;
    import org.granite.collections.IMap;
    import org.jspresso.framework.view.descriptor.ICompositeTreeLevelDescriptor;

    [Bindable]
    public class BasicCompositeTreeLevelDescriptorBase extends BasicTreeLevelDescriptor implements ICompositeTreeLevelDescriptor {

        private var _childrenDescriptorsMap:IMap;

        public function get childrenDescriptors():ListCollectionView {
            return null;
        }

        override public function readExternal(input:IDataInput):void {
            super.readExternal(input);
            _childrenDescriptorsMap = input.readObject() as IMap;
        }

        override public function writeExternal(output:IDataOutput):void {
            super.writeExternal(output);
            output.writeObject(_childrenDescriptorsMap);
        }
    }
}