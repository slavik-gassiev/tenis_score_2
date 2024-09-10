package com.slava.servlet;

import com.slava.dto.MatchDto;
import com.slava.dto.MatchTypeDto;
import com.slava.service.NewMatchService;
import com.slava.service.OngoingMatchService;
import com.slava.service.ValidationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "NewMatchServlet", value = "/new")
public class NewMatchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/new-match.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        NewMatchService newMatchService = new NewMatchService();
        ValidationService validationService = new ValidationService();
        OngoingMatchService ongoingMatchService = OngoingMatchService.getInstance();

        String player1 = req.getParameter("player1");
        String player2 = req.getParameter("player2");
        String matchType = req.getParameter("type");

        validationService.checkParameterForNewGame(player1, player2, matchType);
        MatchTypeDto matchTypeDto = null;
        if (matchType == "short") {
             matchTypeDto = MatchTypeDto.SHORT_GAME;
        }
        else {
            matchTypeDto = MatchTypeDto.LONG_GAME;
        }

        Optional<MatchDto> matchDto = newMatchService.initMatch(player1, player2, matchTypeDto);
        String uuid = ongoingMatchService.addMatchToTrack(matchDto);
        String redirect = String.format("/tenis_score_war_exploded/ongoing?uuid=%s", uuid);
        resp.sendRedirect(redirect);
    }


}
