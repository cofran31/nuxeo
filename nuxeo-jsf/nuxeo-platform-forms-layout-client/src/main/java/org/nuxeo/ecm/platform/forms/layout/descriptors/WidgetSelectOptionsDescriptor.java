/*
 * (C) Copyright 2010 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Anahide Tchertchian
 */
package org.nuxeo.ecm.platform.forms.layout.descriptors;

import java.io.Serializable;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;
import org.nuxeo.ecm.platform.forms.layout.api.WidgetSelectOptions;

/**
 * @author Anahide Tchertchian
 * @since 5.4.2
 */
@XObject("options")
public class WidgetSelectOptionsDescriptor implements WidgetSelectOptions {

    private static final long serialVersionUID = 1L;

    @XNode("@value")
    protected String value;

    @XNode("@var")
    protected String var;

    @XNode("@itemLabel")
    protected String itemLabel;

    @XNode("@itemValue")
    protected String itemValue;

    @XNode("@itemDisabled")
    protected String itemDisabled;

    @XNode("@itemRendered")
    protected String itemRendered;

    @XNode("@ordering")
    protected String ordering;

    @XNode("@caseSensitive")
    protected Boolean caseSensitive;

    public Serializable getValue() {
        return value;
    }

    public String getVar() {
        return var;
    }

    public String getItemLabel() {
        return itemLabel;
    }

    public String getItemValue() {
        return itemValue;
    }

    public Serializable getItemDisabled() {
        return itemDisabled;
    }

    public Serializable getItemRendered() {
        return itemRendered;
    }

    public String getOrdering() {
        return ordering;
    }

    public Boolean getCaseSensitive() {
        return caseSensitive;
    }

    @Override
    public String getTagConfigId() {
        StringBuilder builder = new StringBuilder();
        builder.append(value).append(";");
        builder.append(var).append(";");
        builder.append(itemLabel).append(";");
        builder.append(itemValue).append(";");
        builder.append(itemDisabled).append(";");
        builder.append(itemRendered).append(";");
        builder.append(ordering).append(";");
        builder.append(caseSensitive).append(";");

        Integer intValue = builder.toString().hashCode();
        return intValue.toString();
    }

}