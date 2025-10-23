package rs.ac.fon.gymtracker.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import rs.ac.fon.gymtracker.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {
    @EntityGraph(attributePaths = {"servicePackage"})
    @NonNull
    List<Member> findAll();

    @EntityGraph(attributePaths = {"servicePackage"})
    @NonNull
    Optional<Member> findById(@NonNull Long id);

    @EntityGraph(attributePaths = {"servicePackage"})
    @NonNull
    List<Member> findAll(@Nullable Specification<Member> spec);
}
