package com.codelap.common.studyViews.domain;

import com.codelap.common.study.domain.Study;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.codelap.common.support.Preconditions.require;
import static java.util.Objects.nonNull;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uq_ip_address", columnNames = "ipAddress"),
})
public class StudyView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Study study;

    private String ipAddress;

    private StudyView(Study study, String ipAddress) {
        this.study = study;
        this.ipAddress = ipAddress;
    }

    public static StudyView create(Study study, String ipAddress) {
        require(nonNull(study));
        require(isNotBlank(ipAddress));

        return new StudyView(study, ipAddress);
    }
}
