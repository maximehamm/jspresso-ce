/**
 * Generated by Gas3 v1.1.0 (Granite Data Services).
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERRIDDEN EACH TIME YOU USE
 * THE GENERATOR. CHANGE INSTEAD THE INHERITED CLASS (AbstractCardViewDescriptor.as).
 */

package org.jspresso.framework.view.descriptor.basic {

    import flash.utils.IDataInput;
    import flash.utils.IDataOutput;
    import org.granite.collections.IMap;
    import org.jspresso.framework.view.descriptor.ICardViewDescriptor;

    [Bindable]
    public class AbstractCardViewDescriptorBase extends BasicViewDescriptor implements ICardViewDescriptor {

        private var _cardViewDescriptors:IMap;

        public function get cardViewDescriptors():IMap {
            return _cardViewDescriptors;
        }

        override public function readExternal(input:IDataInput):void {
            super.readExternal(input);
            _cardViewDescriptors = input.readObject() as IMap;
        }

        override public function writeExternal(output:IDataOutput):void {
            super.writeExternal(output);
            output.writeObject(_cardViewDescriptors);
        }
    }
}