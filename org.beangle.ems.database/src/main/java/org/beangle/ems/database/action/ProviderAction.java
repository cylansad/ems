/* Copyright c 2005-2012.
 * Licensed under GNU  LESSER General Public License, Version 3.
 * http://www.gnu.org/licenses
 */
package org.beangle.ems.database.action;

import org.beangle.ems.web.action.SecurityActionSupport;

public class ProviderAction extends SecurityActionSupport {

  @Override
  protected String getShortName() {
    return "provider";
  }

  @Override
  protected String getEntityName() {
    return "org.beangle.webapp.database.model.DatasourceProviderBean";
  }

}
