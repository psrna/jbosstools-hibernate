/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.hibernate.xml.model.impl;

import org.jboss.tools.common.model.impl.OrderedByEntityChildren;
import org.jboss.tools.common.model.impl.OrderedObjectImpl;
import org.jboss.tools.common.model.impl.RegularChildren;

public class HibernateIdImpl extends OrderedObjectImpl {
    private static final long serialVersionUID = 377569900063569848L;
	
	protected RegularChildren createChildren() {
		return new IdOrderedByEntityChildren();
	}
    
	public String name() {
		String nm = "" + getAttributeValue("name"); //$NON-NLS-1$ //$NON-NLS-2$
		if(nm.length() == 0) nm = "<" + getModelEntity().getXMLSubPath() + ">"; //$NON-NLS-1$ //$NON-NLS-2$
		return nm;
	}
	
	public String getPathPart() {
		return "[id]"; //$NON-NLS-1$
	}

}

class IdOrderedByEntityChildren extends OrderedByEntityChildren {
	protected int getEntityIndex(String s) {
		if("Hibernate3KeyManyToOne".equals(s)) //$NON-NLS-1$
			s = "Hibernate3KeyProperty"; //$NON-NLS-1$
		return super.getEntityIndex(s);
	}

}
