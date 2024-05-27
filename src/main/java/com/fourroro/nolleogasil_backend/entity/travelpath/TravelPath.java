package com.fourroro.nolleogasil_backend.entity.travelpath;
import com.fourroro.nolleogasil_backend.dto.travelpath.TravelPathDto;
import com.fourroro.nolleogasil_backend.entity.users.Users;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name="Travelpath")
@Builder
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC) //기본 생성자
@AllArgsConstructor //전체 변수 생성하는 생성자
@SequenceGenerator(
        name = "SEQ_TRAVELPATH_TRAVELPATH_ID",  //시퀀스 제너레이터 이름
        sequenceName = "SEQ_TRAVELPATH_TRAVELPATH_ID",  //시퀀스 이름
        initialValue = 1,  //시작값
        allocationSize = 1  //증가값
)
public class TravelPath {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TRAVELPATH_TRAVELPATH_ID")
    private Long travelpathId;            //PK
    private String arrival;               //도착지
    private String startDate;             //출발 날짜
    private String endDate;		          //오는 날짜

    @ManyToOne
    @JoinColumn(name = "usersId")
    private Users users;    		      //사용자 entity

    @OneToOne(mappedBy = "travelPath", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Keyword keyword;              // TravelPath와의 OneToOne 관계

    @OneToOne(mappedBy = "travelPath", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Recommendation recommendation; // TravelPath와의 OneToOne 관계

    public static TravelPath changeToEntity(TravelPathDto dto, Users users) {
        return TravelPath.builder()
                .arrival(dto.getArrival())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .users(users)
                .build();

    }

}
