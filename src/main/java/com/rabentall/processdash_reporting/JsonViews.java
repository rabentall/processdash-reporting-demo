package com.rabentall.processdash_reporting;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;

import net.sourceforge.processdash.api.PDashContext;

import com.google.gson.Gson;

/**
 * \brief Top-level class to load json data from in-memory data warehouse.
 */
public class JsonViews extends HttpServlet {

    /**
     * \brief Implementation called from GET request to API.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

      try{
        PDashContext ctx = (PDashContext) req.getAttribute(PDashContext.REQUEST_ATTR);

        String pathInfo = req.getPathInfo();

        resp.setContentType("application/json");
        resp.setHeader("Content-Disposition","inline");

        //Load the set of lookups from in-memory database.
        Lookups lookups = new Lookups(ctx);
        lookups.load();

        Gson gson = new Gson();
        DataLoader loader = new DataLoader(gson, lookups);

        //API doesn't use trailing slashes for resource requests.
        //Identify these and return 404 error.
        if(pathInfo != null && pathInfo.endsWith("/")){
          resp.sendError(404);
        } else {
          //Return the json serialised data associated with pathInfo parameter.
          resp.getOutputStream().print(loader.get(ctx, pathInfo));
        }
      } catch (Exception ex){
        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal error state. Check server logs for more details.");
        throw new ServletException(ex);
      }
    }
}

/**
 * \brief Handles data loading for individual DashData sources.
 */
class DataLoader{

  /**
   * \brief Gson serialisation library.
   */
  Gson gson_;

  /**
   * \brief Map of DashData elements, keyed on pathInfo parameter.
   */
  Map<String,  DashDataList> dashData = new HashMap<String, DashDataList>();

  /**
   * \brief Returns the keys associated with the map. This provides the set of all possible valid URL
   * values that the jsonViews API knows about.
   */
  Set<String> values = dashData.keySet();

  /**
   * \brief Populates dashData map with individual DashData instances.
   */
  DataLoader(Gson gson, Lookups lookups){
    gson_ = gson;
    dashData.put("/customColumns",     new CustomColumns());
    dashData.put("/defects",           new Defects());
    dashData.put("/dependencies",      new Dependencies(lookups));
    dashData.put("/milestones",        new Milestones(lookups));
    dashData.put("/notes",             new Notes());
    dashData.put("/processPhases",     new ProcessPhases());
    dashData.put("/sizeMetrics",       new SizeMetrics());
    dashData.put("/tasks",             new Tasks());
    dashData.put("/timeLog",           new TimeLog());
    dashData.put("/wbsElements",       new WbsElements());
    dashData.put("/dashboardSettings", new DashboardSettings());
    dashData.put("/planItems",         new PlanItems(lookups));

  }

  /**
   * \brief Returns the DashData element associated with the pathInfo parameter,
   * or the set of allowed pathInfo values if none is presented.
   * @param ctx The dashboard context.
   * @param pathInfo The path info parameter
   * @return Serialized json Data.
   */
  String get(PDashContext ctx, String pathInfo){

    if(pathInfo != null || values.contains(pathInfo)){
      DashDataList t = dashData.get(pathInfo);
      t.load(ctx);
      return gson_.toJson(t);
    } else {
      return gson_.toJson(values);
    }
  }
}
