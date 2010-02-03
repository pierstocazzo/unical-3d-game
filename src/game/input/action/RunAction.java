/**
 * Copyright (C) 2010 Salvatore Loria, Andrea Martire, Giuseppe Leone
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package game.input.action;

import game.input.GameInputHandler;

import com.jme.input.action.InputActionEvent;
import com.jme.input.action.KeyInputAction;

/**
 * Class RunAction
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class RunAction extends KeyInputAction {

    private GameInputHandler handler;

    public RunAction(GameInputHandler handler) {
        this.handler = handler;
    }

    public void performAction(InputActionEvent event) {
        if( event.getTriggerPressed() ) {
        	handler.setRunning(true);
        } else {
        	handler.setRunning(false);
        }
    }
}
