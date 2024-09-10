package com.slava.service;

import com.slava.dao.MatchDAO;
import com.slava.dao.interfaces.IMatchDAO;
import com.slava.dto.MatchDto;
import com.slava.dto.TableDto;
import com.slava.util.MapperUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AllMatchesService {

    IMatchDAO matchDAO = new MatchDAO();

    public List<TableDto> getAllMatches() {
        List<TableDto> matches = null;
        try {
            List<MatchDto> matchesFromDB = matchDAO.getAll();
            matches = matchesFromDB.stream()
                    .map(matchDto -> MapperUtil.mapToTableDto(matchDto))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Не можем загрузить все матчи из Hibernate" + e);
        }
      return matches;
    }
}
