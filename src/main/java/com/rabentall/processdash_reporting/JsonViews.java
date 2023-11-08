package com.rabentall.processdash_reporting;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import net.sourceforge.processdash.api.PDashContext;

import com.google.gson.Gson;

public class JsonViews extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // perform a series of queries to load data
        PDashContext ctx = (PDashContext) req.getAttribute(PDashContext.REQUEST_ATTR);

        String pathInfo = req.getPathInfo();

        resp.setContentType("application/json");
        resp.setHeader("Content-Disposition","inline");

        Gson gson = new Gson();

        DataLoader loader = new DataLoader(gson, ctx);

        resp.getOutputStream().print(loader.get(pathInfo));
    }

}

class DataLoader{

  Gson gson_;

  Map<String,  DashData> dashData = new HashMap<String, DashData>();

  Set<String> values = dashData.keySet();

  DataLoader(Gson gson, PDashContext ctx){
    gson_ = gson;
    dashData.put("/processPhases", new ProcessPhases(ctx));
    dashData.put("/taskList",      new TaskList(ctx));
    dashData.put("/wbsElementList", new WbsElementList(ctx));
    dashData.put("/customColumns", new CustomColumns(ctx));
  }
  
  String get(String pathInfo){

    if(pathInfo != null || values.contains(pathInfo)){      
      DashData t = dashData.get(pathInfo);
      t.load();
      return gson_.toJson(t);
    } else {
      return gson_.toJson(values);   
    }
  }
}





