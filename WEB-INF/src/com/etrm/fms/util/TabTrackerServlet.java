package com.etrm.fms.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//Coded By          : Harsh Maheta
//Code Reviewed by  :
//CR Date           : 04/02/2025
//Status            : Developing

@WebServlet("/TabTrackerServlet")
public class TabTrackerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static String db_src_file_name = "TabTrackerServlet.java";

    private String getSessionKey() {
        String ctx = getServletContext().getContextPath();
        return "activeTabs_" + (ctx == null || ctx.isEmpty() ? "ROOT" : ctx);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String function_nm = "doPost()";

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        String tabId = request.getParameter("tabId");

        if (action == null || action.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try {                                                            // Line 44 fix
                response.getWriter().write("{\"error\":\"Missing action\",\"tabCount\":0}");
            } catch (IOException e) {
                new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
            }
            return;
        }

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_OK);
            try {                                                            // Line 51 fix
                response.getWriter().write("{\"tabCount\":0}");
            } catch (IOException e) {
                new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
            }
            return;
        }

        String sessionKey = getSessionKey();

        @SuppressWarnings("unchecked")
        ConcurrentHashMap<String, Long> activeTabs =
                (ConcurrentHashMap<String, Long>) session.getAttribute(sessionKey);

        if (activeTabs == null) {
            synchronized (session) {
                activeTabs = (ConcurrentHashMap<String, Long>) session.getAttribute(sessionKey);
                if (activeTabs == null) {
                    activeTabs = new ConcurrentHashMap<>();
                    session.setAttribute(sessionKey, activeTabs);
                }
            }
        }

        try {
            switch (action) {
                case "open":
                    if (tabId == null || tabId.isEmpty()) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("{\"error\":\"Missing tabId\",\"tabCount\":0}");
                        return;
                    }
                    activeTabs.put(tabId, System.currentTimeMillis());
                    break;

                case "close":
                    if (tabId == null || tabId.isEmpty()) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("{\"error\":\"Missing tabId\",\"tabCount\":0}");
                        return;
                    }
                    activeTabs.remove(tabId);
                    break;

                case "heartbeat":
                    if (tabId != null && !tabId.isEmpty()) {
                        activeTabs.put(tabId, System.currentTimeMillis());
                    }
                    break;

                case "getCount":
                    break;

                default:
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\":\"Unknown action\",\"tabCount\":0}");
                    return;
            }

            // Clean up stale tabs
            long staleThreshold = System.currentTimeMillis() - (2 * 60 * 1000);
            Iterator<Map.Entry<String, Long>> it = activeTabs.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Long> entry = it.next();
                if (entry.getValue() < staleThreshold) {
                    it.remove();
                }
            }

            int tabCount = activeTabs.size();
            response.getWriter().write("{\"tabCount\":" + tabCount + "}");

        } catch (IOException e) {                                            // Line 122 fix
            new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().write("{\"error\":\"Internal server error\",\"tabCount\":0}");
            } catch (IOException ioEx) {
                new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, ioEx);
            }
        } catch (Exception e) {
            new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().write("{\"error\":\"Internal server error\",\"tabCount\":0}");
            } catch (IOException ioEx) {
                new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, ioEx);
            }
        }
    }
}