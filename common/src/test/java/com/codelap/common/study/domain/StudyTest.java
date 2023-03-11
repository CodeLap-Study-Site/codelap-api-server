package com.codelap.common.study.domain;

import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.OffsetDateTime;

import static com.codelap.common.study.domain.Study.MIN_MEMBERS_SIZE;
import static com.codelap.common.study.domain.Study.create;
import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static com.codelap.common.study.domain.StudyStatus.OPENED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class StudyTest {

    private User leader;
    private Study study;

    private StudyPeriod period;
    private StudyNeedCareer needCareer;

    @BeforeEach
    void setUp() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = User.create("name", 10, career, "abcd", "setUp");

        period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        needCareer = StudyNeedCareer.create("직무", 1);
    }

    @Test
    void 스터디_생성_성공() {
        Study study = create("팀", "설명", 4, NORMAL, period, needCareer, leader);

        assertThat(study.getName()).isEqualTo("팀");
        assertThat(study.getInfo()).isEqualTo("설명");
        assertThat(study.getMaxMembersSize()).isEqualTo(4);
        assertThat(study.getDifficulty()).isEqualTo(NORMAL);
        assertThat(study.getPeriod()).isSameAs(period);
        assertThat(study.getNeedCareer()).isSameAs(needCareer);
        assertThat(study.getLeader()).isSameAs(leader);
        assertThat(study.getMembers()).containsExactly(leader);
        assertThat(study.getStatus()).isEqualTo(OPENED);
        assertThat(study.getCreatedAt()).isNotNull();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_생성_실패__이름이_공백_혹은_널(String name) {
        assertThatIllegalArgumentException().isThrownBy(() -> create(name, "설명", 4, NORMAL, period, needCareer, leader));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_생성_실패__설명이_공백_혹은_널(String info) {
        assertThatIllegalArgumentException().isThrownBy(() -> create("팀", info, 4, NORMAL, period, needCareer, leader));
    }

    @Test
    void 스터디_생성_실패__최대회원수가_최소값_보다_작음() {
        assertThatIllegalArgumentException().isThrownBy(() -> create("팀", "설명", MIN_MEMBERS_SIZE - 1, NORMAL, period, needCareer, leader));
    }

    @Test
    void 스터디_생성_실패__난이도가_널() {
        assertThatIllegalArgumentException().isThrownBy(() -> create("팀", "설명", 4, null, period, needCareer, leader));
    }

    @Test
    void 스터디_생성_실패__경력이_널() {
        assertThatIllegalArgumentException().isThrownBy(() -> create("팀", "설명", 4, NORMAL, null, needCareer, leader));
    }

    @Test
    void 스터디_생성_실패__직무가_널() {
        assertThatIllegalArgumentException().isThrownBy(() -> create("팀", "설명", 4, NORMAL, period, null, leader));
    }

    @Test
    void 스터디_생성_실패__팀장이_널() {
        assertThatIllegalArgumentException().isThrownBy(() -> create("팀", "설명", 4, NORMAL, period, needCareer, null));
    }
}