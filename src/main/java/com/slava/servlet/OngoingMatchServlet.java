package com.slava.servlet;

import com.slava.dto.MatchDto;
import com.slava.dto.PlayerDto;
import com.slava.dto.TableDto;
import com.slava.dto.WinnerDto;
import com.slava.service.MatchScoreCalculationService;
import com.slava.service.NewMatchService;
import com.slava.service.OngoingMatchService;
import com.slava.service.ValidationService;
import com.slava.util.MapperUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Optional;

@WebServlet(name = "OngoingMatchServlet", value = "/ongoing")
public class OngoingMatchServlet extends HttpServlet {
    OngoingMatchService ongoingMatchService = OngoingMatchService.getInstance();
    NewMatchService newMatchService = new NewMatchService();
    MatchScoreCalculationService matchScoreCalculationService = new MatchScoreCalculationService();
    ValidationService validationService = new ValidationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uuid = req.getParameter("uuid");
        validationService.isUuidCorrect(uuid);
        MatchDto matchDto = ongoingMatchService.getMatch(uuid);
//        если матч закончился
        if (ongoingMatchService.isMatchFinished(uuid)) {
            WinnerDto winnerDto = MapperUtil.mapToWinnerDto(matchDto);
            newMatchService.addMatchToDB(winnerDto);
            ongoingMatchService.deleteMatchFromTrack(uuid);
            req.setAttribute("winner", winnerDto);
            req.getRequestDispatcher("/match-result.jsp").forward(req, resp);
        }
//        если матч не закончился

        TableDto tableDto = MapperUtil.mapToTableDto(matchDto);
        req.setAttribute("match", tableDto);
        req.setAttribute("uuid", uuid);
        req.getRequestDispatcher("/match-score.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uuid = req.getParameter("uuid");
        String point = req.getParameter("point_winner");
        PlayerDto playerDto = ongoingMatchService.getPointWinner(point, uuid);
        MatchDto calculatedMatch = matchScoreCalculationService.toGoal(uuid, playerDto);
        ongoingMatchService.refreshMatch(uuid, calculatedMatch);
        String redirect = String.format("/tenis_score/ongoing?uuid=%s", uuid);
        resp.sendRedirect(redirect);
    }
}
