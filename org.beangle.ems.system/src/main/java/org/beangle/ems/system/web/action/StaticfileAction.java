/* Copyright c 2005-2012.
 * Licensed under GNU  LESSER General Public License, Version 3.
 * http://www.gnu.org/licenses
 */
package org.beangle.ems.system.web.action;

import java.net.URL;
import java.util.List;

import org.beangle.commons.collection.CollectUtils;
import org.beangle.commons.lang.Strings;
import org.beangle.commons.web.io.StreamDownloader;
import org.beangle.ems.io.StaticFileLoader;
import org.beangle.struts2.action.ActionSupport;

/**
 * 静态资源下载
 * 
 * @author chaostone
 */
public class StaticfileAction extends ActionSupport {

  private List<StaticFileLoader> loaders = CollectUtils.newArrayList();

  private StreamDownloader streamDownloader;

  public String index() throws Exception {
    String name = get("file");
    String displayName = get("display");
    URL url = null;
    if (Strings.isNotEmpty(name)) {
      for (StaticFileLoader loader : loaders) {
        url = loader.getFile(name);
        if (null != url) {
          break;
        }
      }
      if (null != url) {
        streamDownloader.download(getRequest(), getResponse(), url, displayName);
      }
    }
    if (null == url) {
      return "nofound";
    } else {
      return null;
    }
  }

  public List<StaticFileLoader> getLoaders() {
    return loaders;
  }

  public void setLoaders(List<StaticFileLoader> loaders) {
    this.loaders = loaders;
  }

  public void setStreamDownloader(StreamDownloader streamDownloader) {
    this.streamDownloader = streamDownloader;
  }

}
