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

public class JsonViews extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

      try{
        // perform a series of queries to load data
        PDashContext ctx = (PDashContext) req.getAttribute(PDashContext.REQUEST_ATTR);

        String pathInfo = req.getPathInfo();

        resp.setContentType("application/json");
        resp.setHeader("Content-Disposition","inline");

        Gson gson = new Gson();
        Lookups lookups = new Lookups(ctx);
        lookups.load();

        DataLoader loader = new DataLoader(gson, lookups);

        //API doesn't use trailing slashes for resource requests.
        //Identify these and return 404 error.
        if(pathInfo != null && pathInfo.endsWith("/")){
          resp.sendError(404);
        } else {
          resp.getOutputStream().print(loader.get(ctx, pathInfo));
        }
      } catch (Exception ex){
        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal error state. Check server logs for more details.");
        throw new ServletException(ex);
      }
    }
}

class DataLoader{

  Gson gson_;

  Map<String,  DashDataList> dashData = new HashMap<String, DashDataList>();

  Set<String> values = dashData.keySet();

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

  }

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
