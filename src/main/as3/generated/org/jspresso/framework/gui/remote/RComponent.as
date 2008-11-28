/**
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 */


package org.jspresso.framework.gui.remote {

    import org.jspresso.framework.state.remote.RemoteValueState;
    import org.jspresso.framework.util.remote.RemotePeer;

    [RemoteClass(alias="org.jspresso.framework.gui.remote.RComponent")]
    public class RComponent extends RemotePeer {

        private var _actionLists:Array;
        private var _background:String;
        private var _borderType:String;
        private var _font:String;
        private var _foreground:String;
        private var _icon:RIcon;
        private var _label:String;
        private var _state:RemoteValueState;
        private var _tooltip:String;

        public function set actionLists(value:Array):void {
            _actionLists = value;
        }
        public function get actionLists():Array {
            return _actionLists;
        }

        public function set background(value:String):void {
            _background = value;
        }
        public function get background():String {
            return _background;
        }

        public function set borderType(value:String):void {
            _borderType = value;
        }
        public function get borderType():String {
            return _borderType;
        }

        public function set font(value:String):void {
            _font = value;
        }
        public function get font():String {
            return _font;
        }

        public function set foreground(value:String):void {
            _foreground = value;
        }
        public function get foreground():String {
            return _foreground;
        }

        public function set icon(value:RIcon):void {
            _icon = value;
        }
        public function get icon():RIcon {
            return _icon;
        }

        public function set label(value:String):void {
            _label = value;
        }
        public function get label():String {
            return _label;
        }

        public function set state(value:RemoteValueState):void {
            _state = value;
        }
        public function get state():RemoteValueState {
            return _state;
        }

        public function set tooltip(value:String):void {
            _tooltip = value;
        }
        public function get tooltip():String {
            return _tooltip;
        }
    }
}