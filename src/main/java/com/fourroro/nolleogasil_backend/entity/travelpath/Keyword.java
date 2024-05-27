package com.fourroro.nolleogasil_backend.entity.travelpath;

import com.fourroro.nolleogasil_backend.dto.travelpath.KeywordDto;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name="Keyword")
@Builder
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //기본 생성자
@AllArgsConstructor //전체 변수 생성하는 생성자
@SequenceGenerator(
        name = "SEQ_KEYWORD_KEYWORD_ID",  //시퀀스 제너레이터 이름
        sequenceName = "SEQ_KEYWORD_KEYWORD_ID",  //시퀀스 이름
        initialValue = 1,  //시작값
        allocationSize = 1  //증가값
)
public class Keyword implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_KEYWORD_KEYWORD_ID")
    private Long keywordId;            //PK
    private String party;              //일행
    private String place;              //장소
    private String concept;            //컨셉
    private String food;               //음식

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travelpathId")
    private TravelPath travelPath;     //TravelPath

    public static Keyword changeToEntity(KeywordDto dto, TravelPath travelPath) {
        return Keyword.builder()
                .party(dto.getParty())
                .place(dto.getPlace())
                .concept(dto.getConcept())
                .food(dto.getFood())
                .travelPath(travelPath)
                .build();
    }

}
