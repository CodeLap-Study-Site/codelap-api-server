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
import static com.codelap.common.study.domain.StudyDifficulty.HARD;
import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static com.codelap.common.study.domain.StudyStatus.DELETED;
import static com.codelap.common.study.domain.StudyStatus.OPENED;
import static org.assertj.core.api.Assertions.*;

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

        study = create("팀", "설명", 4, NORMAL, period, needCareer, leader);
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

    @Test
    void 스터디_수정_성공() {
        StudyPeriod updatePeriod = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer updateNeedCareer = StudyNeedCareer.create("직무", 1);

        study.update("updateName", "updateInfo", 5, HARD, updatePeriod, updateNeedCareer);

        assertThat(study.getName()).isEqualTo("updateName");
        assertThat(study.getInfo()).isEqualTo("updateInfo");
        assertThat(study.getMaxMembersSize()).isEqualTo(5);
        assertThat(study.getDifficulty()).isEqualTo(HARD);
        assertThat(study.getPeriod()).isSameAs(updatePeriod);
        assertThat(study.getNeedCareer()).isSameAs(updateNeedCareer);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_수정_실패__이름이_공백_혹은_널(String name) {
        StudyPeriod updatePeriod = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer updateNeedCareer = StudyNeedCareer.create("직무", 1);

        assertThatIllegalArgumentException().isThrownBy(() -> study.update(name, "설명", 4, NORMAL, updatePeriod, updateNeedCareer));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_수정_실패__설명이_공백_혹은_널(String info) {
        StudyPeriod updatePeriod = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer updateNeedCareer = StudyNeedCareer.create("직무", 1);

        assertThatIllegalArgumentException().isThrownBy(() -> study.update("팀", info, 4, NORMAL, updatePeriod, updateNeedCareer));
    }

    @Test
    void 스터디_수정_실패__최대회원수가_최소값_보다_작음() {
        StudyPeriod updatePeriod = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer updateNeedCareer = StudyNeedCareer.create("직무", 1);

        assertThatIllegalArgumentException().isThrownBy(() -> study.update("팀", "설명", MIN_MEMBERS_SIZE - 1, NORMAL, updatePeriod, updateNeedCareer));
    }

    @Test
    void 스터디_수정_실패__난이도가_널() {
        StudyPeriod updatePeriod = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer updateNeedCareer = StudyNeedCareer.create("직무", 1);

        assertThatIllegalArgumentException().isThrownBy(() -> study.update("팀", "설명", 4, null, updatePeriod, updateNeedCareer));
    }

    @Test
    void 스터디_수정_실패__경력이_널() {
        StudyNeedCareer updateNeedCareer = StudyNeedCareer.create("직무", 1);

        assertThatIllegalArgumentException().isThrownBy(() -> study.update("팀", "설명", 4, NORMAL, null, updateNeedCareer));
    }

    @Test
    void 스터디_수정_실패__직무가_널() {
        StudyPeriod updatePeriod = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));

        assertThatIllegalArgumentException().isThrownBy(() -> study.update("팀", "설명", 4, NORMAL, updatePeriod, null));
    }

    @Test
    void 스터디_수정_실패__삭제됨_상태() {
        study.setStatus(DELETED);

        StudyPeriod updatePeriod = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer updateNeedCareer = StudyNeedCareer.create("직무", 1);

        assertThatIllegalStateException().isThrownBy(() -> study.update("updateName", "updateInfo", 5, HARD, updatePeriod, updateNeedCareer));
    }

    @Test
    void 스터디_참여_성공() {
        UserCareer career = UserCareer.create("직무", 1);
        User user = User.create("name", 10, career, "abcd", "setUp");
        study.addMember(user);

        assertThat(study.getNeedCareer().getOccupation()).isEqualTo(user.getCareer().getOccupation());
        assertThat(study.getStatus()).isEqualTo(OPENED);
    }

    @Test
    void 스터디_참여_실패__최대정원_최대값_보다_큼() {
        int maxMembersSize = 4;

        Study study = create("팀", "설명", maxMembersSize, NORMAL, period, needCareer, leader);

        UserCareer career = UserCareer.create("직무", 1);

        User user1 = User.create("name", 10, career, "abcd", "user1");
        User user2 = User.create("name", 20, career, "abcd", "user2");
        User user3 = User.create("name", 30, career, "abcd", "user3");
        User user4 = User.create("name", 40, career, "abcd", "user4");

        study.addMember(user1);
        study.addMember(user2);
        study.addMember(user3);

        assertThatIllegalArgumentException().isThrownBy(() -> study.addMember(user4));
    }

    @Test
    void 스터디_참여_실패__참여자_중복() {
        UserCareer career = UserCareer.create("직무", 1);
        User user = User.create("name", 10, career, "abcd", "user1");

        study.addMember(user);

        assertThatIllegalArgumentException().isThrownBy(() -> study.addMember(user));
    }

    @Test
    void 스터디_참여_실패__삭제됨_상태() {
        study.setStatus(DELETED);

        UserCareer career = UserCareer.create("직무", 1);
        User user = User.create("name", 10, career, "abcd", "setUp");

        assertThatIllegalStateException().isThrownBy(() -> study.addMember(user));
    }

}
