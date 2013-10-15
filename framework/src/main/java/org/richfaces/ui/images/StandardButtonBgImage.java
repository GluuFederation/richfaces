/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.ui.images;

import javax.faces.context.FacesContext;

import org.richfaces.skin.Skin;
import org.richfaces.skin.SkinFactory;

public class StandardButtonBgImage extends BaseControlBackgroundImage {
    @Override
    protected void initializeProperties(FacesContext context, Skin skin) {
        super.initializeProperties(context, skin);

        Integer heightParameter = skin.getIntegerParameter(context, Skin.BUTTON_SIZE_FONT);
        if (heightParameter == null) {
            Skin defaultSkin = SkinFactory.getInstance(context).getDefaultSkin(context);
            heightParameter = defaultSkin.getIntegerParameter(context, Skin.BUTTON_SIZE_FONT);
        }

        setHeight((int) (1.7 * heightParameter));
        setBaseColorParam(Skin.ADDITIONAL_BACKGROUND_COLOR);
        setGradientColorParam(Skin.TRIM_COLOR);
        setHeight(9);
    }
}
