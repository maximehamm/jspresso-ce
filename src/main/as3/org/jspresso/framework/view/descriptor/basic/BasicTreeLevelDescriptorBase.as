/**
 * Generated by Gas3 v1.1.0 (Granite Data Services).
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERRIDDEN EACH TIME YOU USE
 * THE GENERATOR. CHANGE INSTEAD THE INHERITED CLASS (BasicTreeLevelDescriptor.as).
 */

package org.jspresso.framework.view.descriptor.basic {

    import flash.utils.IDataInput;
    import flash.utils.IDataOutput;
    import flash.utils.IExternalizable;
    import org.jspresso.framework.view.descriptor.IListViewDescriptor;
    import org.jspresso.framework.view.descriptor.ITreeLevelDescriptor;

    [Bindable]
    public class BasicTreeLevelDescriptorBase implements IExternalizable, ITreeLevelDescriptor {

        private var _nodeGroupDescriptor:IListViewDescriptor;

        public function set nodeGroupDescriptor(value:IListViewDescriptor):void {
            _nodeGroupDescriptor = value;
        }
        public function get nodeGroupDescriptor():IListViewDescriptor {
            return _nodeGroupDescriptor;
        }

        public function readExternal(input:IDataInput):void {
            _nodeGroupDescriptor = input.readObject() as IListViewDescriptor;
        }

        public function writeExternal(output:IDataOutput):void {
            output.writeObject(_nodeGroupDescriptor);
        }
    }
}