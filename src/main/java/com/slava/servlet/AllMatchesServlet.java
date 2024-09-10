package com.slava.servlet;

import com.slava.dto.TableDto;
import com.slava.service.AllMatchesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AllMatchesServlet", value = "/all")
public class AllMatchesServlet extends HttpServlet {
    AllMatchesService allMatchesService = new AllMatchesService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<TableDto> matches = allMatchesService.getAllMatches();
        req.setAttribute("matches", matches);
        req.getRequestDispatcher("/all-matches.jsp").forward(req, resp);
    }
}
