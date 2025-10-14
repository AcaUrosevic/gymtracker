package rs.ac.fon.gymtracker.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import rs.ac.fon.gymtracker.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @EntityGraph(attributePaths = {"servicePackage"})
    @NonNull
    List<Member> findAll();

    @EntityGraph(attributePaths = {"servicePackage"})
    @NonNull
    Optional<Member> findById(@NonNull Long id);

    @EntityGraph(attributePaths = {"servicePackage"})
    Optional<Member> findByEmail(String email);

    @EntityGraph(attributePaths = {"servicePackage"})
    List<Member> findByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCase(String ln, String fn);
}
