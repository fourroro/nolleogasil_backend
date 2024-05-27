package com.fourroro.nolleogasil_backend.controller.travelpath;

import com.fourroro.nolleogasil_backend.dto.travelpath.*;
import com.fourroro.nolleogasil_backend.dto.users.UsersDto;
import com.fourroro.nolleogasil_backend.entity.travelpath.Recommendation;
import com.fourroro.nolleogasil_backend.entity.travelpath.TravelInfo;
import com.fourroro.nolleogasil_backend.entity.travelpath.TravelPath;
import com.fourroro.nolleogasil_backend.service.travelpath.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class TravelPathController {

    private final TravelPathService travelPathService;
    private final KeywordService keywordService;
    private final RecommendationService recommendationService;
    private final TravelDateService travelDateService;
    private final TravelInfoService travelInfoService;


    //Form에서 받은 user가 선택한 데이터를 dto형태로 session에 저장
    @PostMapping("/form")
    public ResponseEntity<String> dataFromForm(@RequestBody ConditionDto conditionDto, HttpSession session) {
        
        session.setAttribute("conditionDto", conditionDto);
        return ResponseEntity.ok("/travelPath/result"); // 리다이렉션 대신 응답 데이터에 리다이렉션할 주소를 담아서 전달
    }


    //Result.js로 데이터 전송
    @GetMapping("/toResult")
    public ResponseEntity<ConditionDto> dataToResult(HttpSession session) {
        
        ConditionDto conditionDto = (ConditionDto) session.getAttribute("conditionDto");

        if (conditionDto != null) {
            return ResponseEntity.status(HttpStatus.OK).body(conditionDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    //user가 여행경로 정보 저장 시 Travelpath, Keyword, Recommendation 등 연관 관계 형성한 table에 insert
    @PostMapping("/travelPath/insert")
    public ResponseEntity insertTravelPathData(@RequestBody TravelDetailDto travelDetailDto, HttpSession session){
        
        if(travelDetailDto.checkNullField()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Some fields are null");
        }

        UsersDto users = (UsersDto) session.getAttribute("users");
        Long usersId = users.getUsersId();


        TravelPathDto travelPathDto = TravelPathDto.builder()
                .arrival(travelDetailDto.getDestination())
                .startDate(travelDetailDto.getStartDate())
                .endDate(travelDetailDto.getEndDate())
                .usersId(usersId)
                .build();

        TravelPath savedTravelPath = travelPathService.insertTravelPath(travelPathDto);

        KeywordDto keywordDto = KeywordDto.builder()
                .party(travelDetailDto.getPartyItems())
                .place(travelDetailDto.getPlaceItems())
                .concept(travelDetailDto.getConceptItems())
                .food(travelDetailDto.getFoodItems())
                .travelpathId(savedTravelPath.getTravelpathId())
                .build();

        RecommendationDto recommendationDto = RecommendationDto.builder()
                .travelpathId(savedTravelPath.getTravelpathId())
                .build();

        keywordService.insertKeyword(savedTravelPath, keywordDto);
        Recommendation savedRecommendation = recommendationService.insertRecommendation(savedTravelPath, recommendationDto);

        for(String date : travelDetailDto.getResultDto().getDates()) {
            TravelDateDto travelDateDto = TravelDateDto.builder()
                    .dates(date)
                    .recommendationId(savedRecommendation.getRecommendationId())
                    .build();

            travelDateService.insertTravelDate(savedRecommendation, travelDateDto);
        }

        for(String info : travelDetailDto.getResultDto().getInfos()) {
            TravelInfoDto travelInfoDto = TravelInfoDto.builder()
                    .infos(info)
                    .recommendationId(savedRecommendation.getRecommendationId())
                    .build();

            travelInfoService.insertTravelInfo(savedRecommendation, travelInfoDto);
        }
        return ResponseEntity.ok("/travelPathList");
    }


    //최신순으로 여행경로 정보 조회
    @GetMapping("/getTravelPathList")
    public ResponseEntity<List<Map<String, Object>>> getTravelPathList(@RequestParam(name="sortBy") String sortBy, HttpSession session) {

        UsersDto users = (UsersDto) session.getAttribute("users");
        Long usersId = users.getUsersId();

        List<TravelPath> travelPathList = new ArrayList<>();
        if(sortBy.equals("최신순")){
            travelPathList = travelPathService.getTravelPathListDesc(usersId);
        }
        else if(sortBy.equals("오래된순")){
            travelPathList = travelPathService.getTravelPathListAsc(usersId);
        }
        else {
            travelPathList = travelPathService.getTravelPathListByArrival(usersId);
        }
        //List<TravelPath> travelPathList = travelPathService.getTravelPathList(usersId);
        List<Map<String, Object>> resultList = new ArrayList<>();

        for(TravelPath travelPath : travelPathList) {
            Map<String, Object> travelPathMap = new HashMap<>();
            travelPathMap.put("travelPathDto", TravelPathDto.changeToDto(travelPath));
            travelPathMap.put("keywordDto", KeywordDto.changeToDto(travelPath.getKeyword()));
            resultList.add(travelPathMap);
        }

        if (!resultList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(resultList);
        } else {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }


    //travelpathId에 해당하는 TravelPath 정보를 session에 저장
    @PostMapping("/showDetail")
    public ResponseEntity showDetailByTravelPathId(@RequestBody Long  travelpathId, HttpSession session) {

        TravelPath travelPath = travelPathService.getTravelPathById(travelpathId);

        Long recommendationId = travelPath.getRecommendation().getRecommendationId();
        List<String> travelDates = recommendationService.getTravelDateList(recommendationId);
        List<String> travelInfos = recommendationService.getTravelInfoList(recommendationId);

        ResultDto resultDto = new ResultDto(travelDates, travelInfos);
        TravelDetailDto travelDetailDto = new TravelDetailDto(
                travelPath.getArrival(),
                travelPath.getStartDate(),
                travelPath.getEndDate(),
                travelPath.getKeyword().getParty(),
                travelPath.getKeyword().getPlace(),
                travelPath.getKeyword().getConcept(),
                travelPath.getKeyword().getFood(),
                resultDto
        );

        session.setAttribute("travelDetailDto", travelDetailDto);
        session.setAttribute("recommendationId", recommendationId);
        return ResponseEntity.ok("/TravelDetail");
    }


    //session에 저장된 TravelPath 정보 전달
    @GetMapping("/getDetail")
    public ResponseEntity<TravelDetailDto> getTravelDetail(HttpSession session){
        TravelDetailDto travelDetailDto = (TravelDetailDto) session.getAttribute("travelDetailDto");
        if( travelDetailDto != null){
            return ResponseEntity.status(HttpStatus.OK).body(travelDetailDto);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    //user가 정보 수정 시 새로운 내용 저장
    @PostMapping("/travelPath/update")
    public ResponseEntity<String> updateTravelPathData(@RequestBody ResultDto resultDto, HttpSession session){

        Long recommendationId = (Long) session.getAttribute("recommendationId");
        Optional<Recommendation> recommendation = recommendationService.getRecommendation(recommendationId);

        //조회 후 변경
        List<TravelInfo> previousInfos = travelInfoService.getTravelInfoById(recommendationId);
        List<String> newInfos = resultDto.getInfos();
        if(previousInfos.size() != newInfos.size()){
            throw new IllegalArgumentException("이전 infos와 새로운 infos의 크기가 다릅니다.");
        }

        List<TravelInfoDto> newTravelInfoDtos = new ArrayList<>();
        for(int i = 0; i < previousInfos.size(); i++){
            TravelInfo travelInfo = previousInfos.get(i);
            TravelInfoDto travelInfoDto = TravelInfoDto.changeToDto(travelInfo);
            travelInfoDto.setInfos(newInfos.get(i));
            newTravelInfoDtos.add(travelInfoDto);
        }

        travelInfoService.updateTravelInfo(newTravelInfoDtos, recommendation.get());
        TravelDetailDto travelDetailDto = (TravelDetailDto) session.getAttribute("travelDetailDto");
        travelDetailDto.getResultDto().setInfos(newInfos);
        session.setAttribute("travelDetailDto", travelDetailDto);

        return ResponseEntity.status(HttpStatus.OK).build();
    }


    //user가 저장한 여행경로 정보 개수 조회
    @GetMapping("/getCount")
    public ResponseEntity<Long> countTravelPath (HttpSession session){
        UsersDto users = (UsersDto) session.getAttribute("users");
        Long usersId = users.getUsersId();
        Long count = travelPathService.countTravelPath(usersId);

        return ResponseEntity.status(HttpStatus.OK).body(count);
    }


    //여행경로 정보 삭제
    @DeleteMapping("/travelPath/delete/{travelpathId}")
    public ResponseEntity deleteTravelPath(@PathVariable Long travelpathId){
        travelPathService.deleteTravelPathById(travelpathId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}