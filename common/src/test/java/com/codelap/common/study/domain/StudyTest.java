package com.codelap.common.study.domain;

import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.OffsetDateTime;

import static com.codelap.common.study.domain.Study.MIN_MEMBERS_SIZE;
import static com.codelap.common.study.domain.Study.create;
import static com.codelap.common.study.domain.StudyDifficulty.HARD;
import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static com.codelap.common.study.domain.StudyStatus.*;
import static com.codelap.common.support.CodeLapExceptionTest.assertThatCodeLapException;
import static com.codelap.common.support.ErrorCode.INVALID_MEMBER_SIZE;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;
import static org.junit.jupiter.params.provider.EnumSource.Mode.INCLUDE;

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
        assertThatCodeLapException(INVALID_MEMBER_SIZE).isThrownBy(() -> create("팀", "설명", MIN_MEMBERS_SIZE - 1, NORMAL, period, needCareer, leader));
    }

    @Test
    void 스터디_생성_실패__난이도가_널() {
        assertThatIllegalArgumentException().isThrownBy(() -> create("팀", "설명", 4, null, period, needCareer, leader));
    }

    @Test
    void 스터디_생성_실패__기간이_널() {
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


    @Test
    void 스터디_리더_변경_성공() {
        UserCareer career = UserCareer.create("직무", 1);

        User user = User.create("changeLeader", 10, career, "abcd", "setUp");
        study.addMember(user);

        study.changeLeader(user);

        assertThat(study.isLeader(user));
    }

    @Test
    void 스터디_리더_변경_실패__소속되지_않은_회원() {
        UserCareer career = UserCareer.create("직무", 1);

        User user = User.create("changeLeader", 10, career, "abcd", "setUp");

        assertThatIllegalArgumentException().isThrownBy(() -> study.changeLeader(user));
    }

    @Test
    void 스터디_리더_변경_실패__똑같은_방장으로_변경() {
        UserCareer career = UserCareer.create("직무", 1);

        assertThatIllegalArgumentException().isThrownBy(() -> study.changeLeader(leader));
    }

    @Test
    void 스터디_리더_변경_실패__회원이_널() {
        assertThatIllegalArgumentException().isThrownBy(() -> study.changeLeader(null));
    }

    @Test
    void 스터디_리더_변경_실패__삭제됨_상태() {
        UserCareer career = UserCareer.create("직무", 1);
        User user = User.create("name", 10, career, "abcd", "setUp");

        study.addMember(user);

        study.setStatus(DELETED);

        assertThatIllegalStateException().isThrownBy(() -> study.changeLeader(user));
    }

    @Test
    void 스터디_진행_성공() {
        Study study = create("팀", "설명", 4, NORMAL, period, needCareer, leader);

        study.proceed();

        assertThat(study.getStatus()).isEqualTo(IN_PROGRESS);
    }

    @ParameterizedTest
    @EnumSource(value = StudyStatus.class, names = {"OPENED"}, mode = EXCLUDE)
    void 스터디_진행_실패__상태가_오픈이_아님(StudyStatus status) {
        Study study = create("팀", "설명", 4, NORMAL, period, needCareer, leader);

        study.setStatus(status);

        assertThatIllegalStateException().isThrownBy(() -> study.proceed());
    }

    @ParameterizedTest
    @EnumSource(value = StudyStatus.class, names = {"CLOSED", "IN_PROGRESS"}, mode = INCLUDE)
    void 스터디_오픈_성공(StudyStatus status) {
        Study study = create("팀", "설명", 4, NORMAL, period, needCareer, leader);

        study.setStatus(status);

        study.open(period);

        assertThat(study.getStatus()).isEqualTo(OPENED);
    }

    @Test
    void 스터디_오픈_실패__기간이_널() {
        Study study = create("팀", "설명", 4, NORMAL, period, needCareer, leader);

        assertThatIllegalArgumentException().isThrownBy(() -> study.open(null));
    }

    @ParameterizedTest
    @EnumSource(value = StudyStatus.class, names = {"CLOSED", "IN_PROGRESS"}, mode = EXCLUDE)
    void 스터디_오픈_실패__상태가_닫힘이나_진행중이_아님(StudyStatus status) {
        Study study = create("팀", "설명", 4, NORMAL, period, needCareer, leader);

        study.setStatus(status);

        assertThatIllegalStateException().isThrownBy(() -> study.open(period));
    }

    @Test
    void 스터디_멤버_추방_성공() {
        UserCareer career = UserCareer.create("직무", 1);
        User member = User.create("name", 10, career, "abcd", "member");

        study.addMember(member);

        study.removeMember(member);

        assertThat(study.getMembers()).doesNotContain(member);
    }

    @Test
    void 스터디_멤버_추방_실패__강퇴할_대상이_멤버가_아님() {
        UserCareer career = UserCareer.create("직무", 1);
        User fakeMember = User.create("name", 10, career, "abcd", "fakeMember");

        assertThatIllegalArgumentException().isThrownBy(() -> study.removeMember(fakeMember));
    }

    @Test
    void 스터디_멤버_추방_실패__강퇴할_대상이_리더() {
        assertThatIllegalArgumentException().isThrownBy(() -> study.removeMember(leader));
    }

    @Test
    void 스터디_멤버_추방_실패__스터디가_삭제된_상태() {
        UserCareer career = UserCareer.create("직무", 1);
        User member = User.create("name", 10, career, "abcd", "member");

        study.addMember(member);

        study.setStatus(DELETED);

        assertThatIllegalStateException().isThrownBy(() -> study.removeMember(member));
    }

    @ParameterizedTest
    @EnumSource(value = StudyStatus.class, names = {"OPENED", "IN_PROGRESS"}, mode = INCLUDE)
    void 스터디_닫기_성공(StudyStatus status) {
        study.setStatus(status);

        study.close();

        assertThat(study.getStatus()).isEqualTo(CLOSED);
    }

    @ParameterizedTest
    @EnumSource(value = StudyStatus.class, names = {"OPENED", "IN_PROGRESS"}, mode = EXCLUDE)
    void 스터디_닫기_실패__스터디가_오픈이나_진행중이_아님(StudyStatus status) {
        study.setStatus(status);

        assertThatIllegalStateException().isThrownBy(() -> study.close());
    }

    @Test
    void 스터디_나가기_성공() {
        UserCareer career = UserCareer.create("직무", 1);
        User member = User.create("name", 10, career, "abcd", "member");

        study.addMember(member);

        study.leave(member);

        assertThat(study.getMembers()).doesNotContain(member);
    }

    @Test
    void 스터디_나가기_실패__회원이_리더() {
        UserCareer career = UserCareer.create("직무", 1);
        User member = User.create("name", 10, career, "abcd", "member");

        study.addMember(member);

        assertThatIllegalArgumentException().isThrownBy(() -> study.leave(leader));
    }

    @Test
    void 스터디_삭제_성공() {
        study.delete();

        assertThat(study.getStatus()).isEqualTo(DELETED);
    }

    @Test
    void 스터디_삭제_실패__리더가_아닌_멤버가_있을때() {
        UserCareer career = UserCareer.create("직무", 1);
        User member = User.create("name", 10, career, "abcd", "member");

        study.addMember(member);

        assertThatIllegalStateException().isThrownBy(() -> study.delete());
    }

    @ParameterizedTest
    @EnumSource(value = StudyStatus.class, names = {"DELETED"}, mode = INCLUDE)
    void 스터디_삭제_실패__스터디가_삭제된_상태(StudyStatus status) {
        study.setStatus(status);

        assertThatIllegalStateException().isThrownBy(() -> study.delete());
    }
}
