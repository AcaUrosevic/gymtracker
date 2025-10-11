package rs.ac.fon.gymtracker.service;

import rs.ac.fon.gymtracker.domain.Member;

import java.util.List;

public interface MemberService extends BaseCrudService<Member, Long> {
    Member changePackage(Long memberId, Long packageId);
    List<Member> searchByName(String query);
}
