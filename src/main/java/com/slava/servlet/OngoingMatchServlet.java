package com.slava.servlet;

import com.slava.dto.MatchDto;
import com.slava.dto.TableDto;
import com.slava.service.OngoingMatchService;
import com.slava.service.ValidationService;
import com.slava.util.MapperUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Optional;

public class OngoingMatchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OngoingMatchService ongoingMatchService = OngoingMatchService.getInstance();
        ValidationService validationService = new ValidationService();
        String uuid = req.getParameter("uuid");


        validationService.isUuidCorrect(ongoingMatchService, uuid);
        MatchDto matchDto = ongoingMatchService.getMatch(uuid);
        TableDto tableDto = MapperUtil.mapToTableDto(matchDto);
        req.setAttribute("match", matchDto);
        req.getRequestDispatcher("/match-score.jsp").forward(req, resp);

    }


}
