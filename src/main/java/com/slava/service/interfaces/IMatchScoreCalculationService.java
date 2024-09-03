package com.slava.service.interfaces;

public interface IMatchScoreCalculationService <DTO, ID, P>{
    DTO toGoal(ID matchId, P player);

//    Проверка существования матча
//    Проверка то что пользователь играет в матче
//    Обновить состояние игры
//    Если игра закончена, конвертируем MatchStateDto в Match и
//    сохраняем в бд, так же удаляем из OngoingMatchesService
//    Возвратить обнавлённый MatchStateDto;

}
